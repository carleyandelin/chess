package ui;

import client.ServerFacade;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostloginUI {
    private final Scanner scanner;
    private final ServerFacade facade;
    private List<GameData> lastGameList = new ArrayList<>();

    public PostloginUI(ServerFacade facade) {
        this.scanner = new Scanner(System.in);
        this.facade = facade;
    }

    /**
     * @return true if user logs out, false if user chooses to quit app entirely
     */
    public boolean run(String username, String authToken) {
        System.out.printf("%n👑 Welcome, %s!%n", username);
        printHelp();
        while (true) {
            printPrompt();
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "help" -> printHelp();
                case "logout" -> {
                    logout(authToken);
                    return true;
                }
                case "create game" -> createGame(authToken);
                case "list games" -> listGames(authToken);
                case "play game" -> playGame(username, authToken);
                case "observe game" -> observeGame(authToken);
                case "quit" -> {
                    System.out.println("👋 Goodbye! Thanks for playing Chess.");
                    return false;
                }
                default -> {
                    System.out.printf("Unknown command: '%s'. Type 'help' for options.%n", command);
                }
            }
        }
    }



    private void printPrompt() {
        System.out.print("[POSTLOGIN] Command (help, create game, list games, play game, observe game, logout, quit): ");
    }

    private void printHelp() {
        System.out.println("""
                === Postlogin Commands ===
                help         - Show this help
                create game  - Create a new chess game
                list games   - View list of all games
                play game    - Play a game as white or black
                observe game - Watch a game (observer mode)
                logout       - Logout and return to main menu
                quit         - Exit the program
                """);
    }

    private void logout(String authToken) {
        try {
            facade.logout(authToken);
            System.out.println("✅ You have been logged out.");
        } catch (Exception e) {
            System.out.printf("❌ Logout failed: %s%n", extractFriendlyError(e));
        }
    }

    private void createGame(String authToken) {
        System.out.print("Enter a name for the new game: ");
        String gameName = scanner.nextLine().trim();
        if (gameName.isEmpty()) {
            System.out.println("Game name cannot be empty.");
            return;
        }
        try {
            facade.createGame(authToken, gameName);
            System.out.printf("✅ Game '%s' created! Use 'list games' to find your games.%n", gameName);
        } catch (Exception e) {
            System.out.printf("❌ Failed to create game: %s%n", extractFriendlyError(e));
        }
    }

    private void listGames(String authToken) {
        try {
            lastGameList = facade.listGames(authToken);
            if (lastGameList.isEmpty()) {
                System.out.println("No games found. Create a game first!");
                return;
            }
            System.out.println("== Games ==");
            int i = 1;
            for (GameData g : lastGameList) {
                System.out.printf("%2d. %-18s | white: %-10s | black: %-10s%n", i++, g.gameName(),
                        safeName(g.whiteUsername()), safeName(g.blackUsername()));
            }
        } catch (Exception e) {
            System.out.printf("❌ Couldn't list games: %s%n", extractFriendlyError(e));
        }
    }

    private String safeName(String username) {
        return username == null || username.isBlank() ? "[open]" : username;
    }

    private void playGame(String username, String authToken) {
        if (lastGameList.isEmpty()) {
            System.out.println("Run 'list games' first to see available games.");
            return;
        }
        int idx = promptForGameNumber(lastGameList.size(), "play");
        if (idx < 0) return;
        GameData game = lastGameList.get(idx - 1);

        String color = promptColor(game);
        if (color == null) return;

        try {
            facade.joinGame(authToken, game.gameID(), color);
            System.out.printf("✅ Joined game '%s' as %s.%n", game.gameName(), color.toLowerCase());
            printBoard(game, color);
            playGameHelp();
            // TODO: play game interaction commands (move, resign etc)
        } catch (Exception e) {
            System.out.printf("❌ Join failed: %s%n", extractFriendlyError(e));
        }
    }

    private void observeGame(String authToken) {
        if (lastGameList.isEmpty()) {
            System.out.println("Run 'list games' first to see available games.");
            return;
        }
        int idx = promptForGameNumber(lastGameList.size(), "observe");
        if (idx < 0) return;
        GameData game = lastGameList.get(idx - 1);

        try {
            facade.observeGame(authToken, game.gameID());
            System.out.printf("👀 Observing game '%s' (shown from white's perspective).%n", game.gameName());
            printBoard(game, "WHITE");
            observeGameHelp();
            // TODO: observe-mode interaction (refresh board, etc)
        } catch (Exception e) {
            System.out.printf("❌ Observe failed: %s%n", extractFriendlyError(e));
        }
    }

    private int promptForGameNumber(int max, String context) {
        while (true) {
            System.out.printf("Which game number would you like to %s (1-%d, or 'cancel'): ", context, max);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("back")) {
                System.out.println("Cancelled.");
                return -1;
            }
            try {
                int num = Integer.parseInt(input);
                if (num >= 1 && num <= max) return num;
                System.out.printf("Please enter a number between 1 and %d.%n", max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private String promptColor(GameData g) {
        while (true) {
            System.out.print("Play as 'white' or 'black' (or type 'cancel'): ");
            String color = scanner.nextLine().trim().toLowerCase();
            if ("cancel".equals(color) || "back".equals(color)) {
                System.out.println("Cancelled.");
                return null;
            }
            if ("white".equals(color)) {
                if (g.whiteUsername() == null || g.whiteUsername().isBlank()) {
                    return "WHITE";
                } else {
                    System.out.println("Sorry, white is already taken.");
                }
            } else if ("black".equals(color)) {
                if (g.blackUsername() == null || g.blackUsername().isBlank()) {
                    return "BLACK";
                } else {
                    System.out.println("Sorry, black is already taken.");
                }
            } else {
                System.out.println("Please type 'white', 'black', or 'cancel'.");
            }
        }
    }

    private void printBoard(GameData game, String perspective) {
        try {
            char[][] board = ChessBoardUI.toCharArray(game.game());
            ChessBoardUI.drawBoard(board, perspective);
        } catch (Exception e) {
            System.out.println("Could not display chess board: " + e.getMessage());
        }
    }

    private void playGameHelp() {
        System.out.println("""
                [NOW PLAYING]
                Use your game controls (move, resign, help, etc).
                (Commands to interact with the game board can go here.)
                """);
    }

    private void observeGameHelp() {
        System.out.println("""
                [NOW OBSERVING]
                Use 'refresh' to see the updated board, or 'back' to return to the menu.
                """);
    }

    private static String extractFriendlyError(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.isBlank())
            return "Unknown error (check server connection).";
        return msg;
    }
}