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
            }
            case PieceType.PAWN -> {
                // pawn move logic
            }
            default -> {
                throw new RuntimeException("Unknown piece type");
            }
        }
        return possibleMoves;
    }
}
