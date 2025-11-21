package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Map;


public class ChessBoardUI {

    /**
     * Draws the chess board given a 2D char array of piece codes (see toCharArray), and color perspective.
     * @param board 8x8 character array: uppercase for white pieces, lowercase for black, '.' or ' ' for empty.
     * @param colorPerspective "WHITE" or "BLACK" (observers: use "WHITE")
     */
    public static void drawBoard(char[][] board, String colorPerspective) {
        boolean whitePerspective = !"BLACK".equalsIgnoreCase(colorPerspective);

        // Define labels as char arrays (flip for black)
        char[] fileLabels = {'a','b','c','d','e','f','g','h'};
        char[] rankLabels = {'1','2','3','4','5','6','7','8'};
        if (!whitePerspective) {
            reverse(fileLabels);
            reverse(rankLabels);
        }

        // Map chars to unicode (adjust as needed for your board)
        Map<Character, String> pieceMap = Map.ofEntries(
                Map.entry('k', EscapeSequences.WHITE_KING),
                Map.entry('q', EscapeSequences.WHITE_QUEEN),
                Map.entry('b', EscapeSequences.WHITE_BISHOP),
                Map.entry('n', EscapeSequences.WHITE_KNIGHT),
                Map.entry('r', EscapeSequences.WHITE_ROOK),
                Map.entry('p', EscapeSequences.WHITE_PAWN),
                Map.entry('K', EscapeSequences.BLACK_KING),
                Map.entry('Q', EscapeSequences.BLACK_QUEEN),
                Map.entry('B', EscapeSequences.BLACK_BISHOP),
                Map.entry('N', EscapeSequences.BLACK_KNIGHT),
                Map.entry('R', EscapeSequences.BLACK_ROOK),
                Map.entry('P', EscapeSequences.BLACK_PAWN),
                Map.entry('.', EscapeSequences.EMPTY),
                Map.entry(' ', EscapeSequences.EMPTY)
        );

        // Print top file label row
        System.out.print("   ");
        for (char file : fileLabels) {
            System.out.print(" " + file + " ");
        }
        System.out.println();

        // Board drawing (rows: ranks for white 8..1, for black 1..8)
        for (int i = 0; i < 8; i++) {
            int rankIdx = whitePerspective ? 7-i : i;
            char rank = rankLabels[whitePerspective ? 7-i : i];
            System.out.print(" " + rank + " ");

            for (int j = 0; j < 8; j++) {
                int fileIdx = whitePerspective ? j : 7-j;
                boolean lightSquare = (rankIdx + fileIdx) % 2 == 0;

                // Set pastel background colors!
                System.out.print(lightSquare ? EscapeSequences.SET_BG_COLOR_DARK_PURPLE : EscapeSequences.SET_BG_COLOR_LIGHT_PINK);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);

                // Draw piece or empty
                char piece = board[rankIdx][fileIdx];
                System.out.print(pieceMap.getOrDefault(piece, EscapeSequences.EMPTY));
                System.out.print(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
            }
            // End of row
            System.out.println(" " + rank);
        }

        // Print bottom file label row
        System.out.print("   ");
        for (char file : fileLabels) {
            System.out.print(" " + file + " ");
        }
        System.out.println();
    }

    // Helper to reverse char arrays
    private static void reverse(char[] arr) {
        for (int i = 0; i < arr.length/2; i++) {
            char tmp = arr[i];
            arr[i] = arr[arr.length-1-i];
            arr[arr.length-1-i] = tmp;
        }
    }

    // Convert your GameData's ChessGame into a char[][]
    public static char[][] toCharArray(ChessGame game) {
        char[][] board = new char[8][8];
        var chessBoard = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // row,col=0 is board position (1,1) = a1, spec says bottom left is (a1)
                // ChessPosition row/col start at 1
                ChessPosition pos = new ChessPosition(row+1, col+1);
                ChessPiece piece = chessBoard.getPiece(pos);
                if (piece == null) {
                    board[7-row][col] = '.'; // spec: a1 bottom left = 7,0
                } else {
                    char letter = piece.getPieceType().toString().charAt(0);
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        board[7-row][col] = Character.toUpperCase(letter);
                    } else {
                        board[7-row][col] = Character.toLowerCase(letter);
                    }
                }
            }
        }
        return board;
    }

    // Example starting position for demo/testing
    public static char[][] startingBoard() {
        return new char[][] {
                {'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {'.','.','.','.','.','.','.','.'},
                {'.','.','.','.','.','.','.','.'},
                {'.','.','.','.','.','.','.','.'},
                {'.','.','.','.','.','.','.','.'},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'}
        };
    }
}