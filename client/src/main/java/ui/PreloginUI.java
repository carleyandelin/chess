package ui;

import client.ServerFacade;

import java.util.Scanner;

public class PreloginUI {
    private final Scanner scanner;
    private final ServerFacade facade;

    public PreloginUI(ServerFacade facade) {
        this.scanner = new Scanner(System.in);
        this.facade = facade;
    }

    /**
     * @return null if user quits, or an AuthDataBundle upon successful login/register
     */
    public AuthDataBundle run() {
        printWelcome();

        while (true) {
            printPrompt();
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "help" -> printHelp();
                case "register" -> {
                    AuthDataBundle auth = register();
                    if (auth != null) return auth;
                }
                case "login" -> {
                    AuthDataBundle auth = login();
                    if (auth != null) return auth;
                }
                case "quit" -> {
                    System.out.println("👋 Goodbye! Thanks for playing Chess.");
                    return null;
                }
                default -> {
                    System.out.printf("Unknown command: '%s'. Type 'help' for available commands.%n", command);
                }
            }
        }
    }

    private void printWelcome() {
        System.out.println("=======================================");
        System.out.println("   Welcome to Chess! (Pre-login menu)  ");
        System.out.println("=======================================");
    }

    private void printPrompt() {
        System.out.print("[PRELOGIN] Command (help, register, login, quit): ");
    }

    private void printHelp() {
        System.out.println("""
                === Prelogin Commands ===
                help      - Show this help message
                register  - Register a new account
                login     - Login to your account
                quit      - Exit the program
                """);
    }

    private AuthDataBundle register() {
        System.out.println("--- Register a New Account ---");
        String username = promptForNonEmpty("Username: ");
        String password = promptForNonEmpty("Password: ");
        String email    = promptForNonEmpty("Email: ");
        try {
            var auth = facade.register(username, password, email);
            System.out.printf("✅ Registered and logged in as '%s'!%n", username);
            return new AuthDataBundle(auth.username(), auth.authToken());
        } catch (Exception e) {
            System.out.printf("❌ Registration failed: %s%n", extractFriendlyError(e));
            retryOrBack("register");
            return null;
        }
    }

    private AuthDataBundle login() {
        System.out.println("--- Login ---");
        String username = promptForNonEmpty("Username: ");
        String password = promptForNonEmpty("Password: ");
        try {
            var auth = facade.login(username, password);
            System.out.printf("✅ Logged in as '%s'!%n", username);
            return new AuthDataBundle(auth.username(), auth.authToken());
        } catch (Exception e) {
            System.out.printf("❌ Login failed: %s%n", extractFriendlyError(e));
            retryOrBack("login");
            return null;
        }
    }

    private String promptForNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String in = scanner.nextLine().trim();
            if (!in.isEmpty()) return in;
            System.out.println("Cannot be empty, please try again.");
        }
    }

    private void retryOrBack(String operation) {
        while (true) {
            System.out.printf("Would you like to try %s again? (y/n): ", operation);
            String in = scanner.nextLine().trim().toLowerCase();
            if ("n".equals(in)) break;
            if ("y".equals(in)) {
                if ("register".equals(operation)) { register(); }
                else if ("login".equals(operation)) { login(); }
                break;
            }
            System.out.println("Please type 'y' or 'n'.");
        }
    }

    private static String extractFriendlyError(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.isBlank())
            return "Unknown error (check server connection).";
        int idx = msg.indexOf("\"message\":\"");
        if (idx != -1) {
            int startIdx = idx + "\"message\":\"".length();
            int endIdx = msg.indexOf("\"", startIdx);
            if (endIdx != -1) {
                return msg.substring(startIdx, endIdx);
            }
        }
        // hide raw output if json doesn't match one above
        if (msg.startsWith("{") && msg.endsWith("}")) {
            return "An error occurred (details hidden).";
        }
        return msg;
    }

    /** Bundle for easy passing */
    public record AuthDataBundle(String username, String authToken) {}
}