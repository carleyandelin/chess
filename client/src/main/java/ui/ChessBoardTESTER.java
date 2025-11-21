package ui;

public class ChessBoardTESTER {
    public static void main(String[] args) {
        char[][] board = ChessBoardUI.startingBoard();
        ChessBoardUI.drawBoard(board, "BLACK");
    }
}