package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var possibleMoves = new java.util.ArrayList<ChessMove>();
        switch (board.getPiece(myPosition).getPieceType()) {
            case PieceType.KING -> {
                // king move logic
                int[] rowMoves = {-1, -1, -1, 0, 0, 1, 1, 1};
                int[] colMoves = {-1, 0, 1, -1, 1, -1, 0, 1};
                for (int i = 0; i < 8; i++) {
                    int newRow = myPosition.getRow() + rowMoves[i];
                    int newCol = myPosition.getColumn() + colMoves[i];
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        var newPos = new ChessPosition(newRow, newCol);
                        var targetPiece = board.getPiece(newPos);
                        if (targetPiece == null || targetPiece.getTeamColor() != this.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        }
                    }
                 }
            }
            case PieceType.QUEEN -> {
                // queen move logic
                int [] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
                int [] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int r = rowDirections[d];
                    int c = colDirections[d];
                    int newRow = myPosition.getRow();
                    int newCol = myPosition.getColumn();
                    while (true) {
                        newRow += r;
                        newCol += c;
                        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break;
                        var newPos = new ChessPosition(newRow, newCol);
                        var targetPiece = board.getPiece(newPos);
                        if (targetPiece == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (targetPiece.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                            break; // Stop if blocked
                        }
                    }
                }
            }
            case PieceType.BISHOP -> {
                // bishop move logic
                int [] rowDirections = {-1, 1, -1, 1};
                int [] colDirections = {-1, -1, 1, 1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int r = rowDirections[d];
                    int c = colDirections[d];
                    int newRow = myPosition.getRow();
                    int newCol = myPosition.getColumn();
                    while (true) {
                        newRow += r;
                        newCol += c;
                        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break;
                        var newPos = new ChessPosition(newRow, newCol);
                        var targetPiece = board.getPiece(newPos);
                        if (targetPiece == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (targetPiece.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                            break; // Stop if blocked
                        }
                    }
                }
            }
            case PieceType.KNIGHT -> {
                // knight move logic
                int[] rowMoves = {1, 2, 2, 1, -1, -2, -2, -1};
                int[] colMoves = {2, 1, -1, -2, -2, -1, 1, 2};
                for (int i = 0; i < rowMoves.length; i++) {
                    int newRow = myPosition.getRow() + rowMoves[i];
                    int newCol = myPosition.getColumn() + colMoves[i];
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        var newPos = new ChessPosition(newRow, newCol);
                        var targetPiece = board.getPiece(newPos);
                        if (targetPiece == null || targetPiece.getTeamColor() != this.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        }
                    }
                }
            }
            case PieceType.ROOK -> {
                // rook move logic
                int [] rowDirections = {-1, 0, 1, 0};
                int [] colDirections = {0, 1, 0, -1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int r = rowDirections[d];
                    int c = colDirections[d];
                    int newRow = myPosition.getRow();
                    int newCol = myPosition.getColumn();
                    while (true) {
                        newRow += r;
                        newCol += c;
                        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break;
                        var newPos = new ChessPosition(newRow, newCol);
                        var targetPiece = board.getPiece(newPos);
                        if (targetPiece == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (targetPiece.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                            break; // Stop if blocked
                        }
                    }
                }
            }
            case PieceType.PAWN -> {
                // white pawn move logic
                if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    // if initial, can move 1 or 2 spaces forward
                    if (myPosition.getRow() == 2) {
                        var newPos1 = new ChessPosition(3, myPosition.getColumn());
                        var targetPiece1 = board.getPiece(newPos1);
                        if (targetPiece1 == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos1, null));
                        }
                        var newPos2 = new ChessPosition(4, myPosition.getColumn());
                        var targetPiece2 = board.getPiece(newPos2);
                        if (targetPiece2 == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos2, null));
                        }
                    }
                    // else if not initial, can move 1 space forward
                    else if ((3 <= myPosition.getRow()) && (myPosition.getRow() < 7)) {
                        var newPos1 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                        var targetPiece1 = board.getPiece(newPos1);
                        if (targetPiece1 == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos1, null));
                        }
                    }
                    // else if can capture diagonally, can move diagonally
                    var newPos1 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                    var targetPiece1 = board.getPiece(newPos1);
                    var newPos2 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                    var targetPiece2 = board.getPiece(newPos2);
                    if (targetPiece1 != null) {
                        possibleMoves.add(new ChessMove(myPosition, newPos1, null));
                    }
                    if (targetPiece2 != null) {
                        possibleMoves.add(new ChessMove(myPosition, newPos2, null));
                    }
                    // else if moving to the last row, can promote to any piece except king
                    if (myPosition.getRow() == 7) {
                        var newPos = new ChessPosition(8, myPosition.getColumn());
                        var targetPiece = board.getPiece(newPos);
                        if (targetPiece == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, ChessPiece.PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, newPos, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, newPos, ChessPiece.PieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(myPosition, newPos, ChessPiece.PieceType.ROOK));
                        }
                    }
                }
            }
            default -> {
                throw new RuntimeException("Unknown piece type");
            }
        }
        return possibleMoves;
    }
}
