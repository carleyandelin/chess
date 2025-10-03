package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     *
     * call ChessPiece.pieceMoves method. That returns a list of all the possible moves
     * go through a for loop of all those moves and if isInCheck returns true remove it from the list
     *
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        // check each move to see if it follows rules of chess
        for (ChessMove move : possibleMoves) {
            // make a temporary move to see if king is in check afterwards
            ChessPiece originalPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() != null) {
                ChessPiece promotedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promotedPiece);
            } else {
                board.addPiece(move.getEndPosition(), piece);
            }
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }
            // undo temp move
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), originalPiece);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     *
     * Gameplan:
     *
     * get the piece and see if its the correct team turn
     * check if the move given is valid by calling the validMoves method
     * actually move the piece
     * if it's a pawn deal with the potential promotion piece
     * switch turns
     *
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // get the piece at the start position and see if it's filled and the right team
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not your turn");
        }
        // check if the move is in the list of valid moves
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }
        // move piece from start position to end position
        board.addPiece(move.getStartPosition(), null);

        // if it's a pawn deal with the promotion piece
        if (move.getPromotionPiece() != null) {
            // Create the promoted piece
            ChessPiece promotedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promotedPiece);
        } else {
            board.addPiece(move.getEndPosition(), piece);
        }
        // change turns
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     *
     *
     * Gameplan in pseudocode:
     *
     * row = King.teamColor.getRow()
     * col = King.teamColor.getColumn()
     * kingSpot = new ChessPosition(row, col)
     *          **create new method getPosition() with parameter piece that loops through the board array and returns
     *          the position the piece is found.
     * list opposingPositions[]
     * for x,y in this.board {
     *     if getpiece != null && getPiece == opposing color {
     *         add x,y to opposingPositions
     *     }
     * }
     * for (piece : opposingPositions) {
     *     places = (ChessPosition.pieceMoves(type)
     *     for (option : places) {
     *         if position == kingSpot {
     *             return true
     *         }
     *     }
     * }
     * return false
     *
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get teamColor King's position
        ChessPosition kingSpot = null;
        for (int row = 1; row <= 8 && kingSpot == null; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingSpot = new ChessPosition(row, col);
                    break;
                }
            }
        }
        // get all current opposing pieces
        var opposingPieces = new ArrayList<ChessPosition>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var piece = this.board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getTeamColor() != teamColor ) {
                    opposingPieces.add(new ChessPosition(i, j));
                }
            }
        }
        // loop through possibleMoves of opposing pieces and see if any can capture the king
        for (ChessPosition position : opposingPieces) {
            ChessPiece piece = board.getPiece(position);
            Collection<ChessMove> possibleMoves = piece.pieceMoves(board, position);
            for (ChessMove move : possibleMoves) {
                if (move.getEndPosition() == kingSpot) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     *
     * Gameplan:
     *
     * for kings position AND every position the King could go {
     *     if !isInCheck {
     *         return false
     *     }
     * }
     * return true
     *
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // check if the team is in check
        if (!isInCheck(teamColor)) {
            return false; // can't be checkmate if not already in check
        }
        // get all pieces of the team
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                // if the piece belongs to the team in check get it's valid moves
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(position);
                    // if any piece has valid moves, it's not checkmate
                    if (validMoves != null && !validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     *
     * Gameplan:
     *
     * for !kings position AND every position the king could go {
     *     if !isInCheck {
     *         return false
     *     }
     * }
     * return true
     *
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // exact same as checkmate, but king is not currently in check
        if (isInCheck(teamColor)) {
            return false; // can't be stalemate if in check
        }
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(position);
                    if (validMoves != null && !validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
