import chess.*;
import java.util.Collection;

public class TestPawnFix {
    public static void main(String[] args) {
        // Create the test board from pawnPromotionCapture test
        ChessBoard board = new ChessBoard();
        
        // Set up the board: black pawn at (2,2), white knight at (1,1)
        board.addPiece(new ChessPosition(2, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        board.addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        
        // Get the black pawn
        ChessPiece pawn = board.getPiece(new ChessPosition(2, 2));
        
        // Get possible moves
        Collection<ChessMove> moves = pawn.pieceMoves(board, new ChessPosition(2, 2));
        
        System.out.println("Number of moves: " + moves.size());
        for (ChessMove move : moves) {
            System.out.println("Move: " + move.getStartPosition().getRow() + "," + move.getStartPosition().getColumn() + 
                             " -> " + move.getEndPosition().getRow() + "," + move.getEndPosition().getColumn() + 
                             " (promotion: " + move.getPromotionPiece() + ")");
        }
        
        // Expected: 8 moves total
        // 4 moves to (1,1) with different promotions (QUEEN, BISHOP, ROOK, KNIGHT)
        // 4 moves to (1,2) with different promotions (QUEEN, BISHOP, ROOK, KNIGHT)
        System.out.println("Expected: 8 moves (4 to 1,1 and 4 to 1,2)");
    }
}
