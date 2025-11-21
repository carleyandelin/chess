package ui;

import java.util.Scanner;

public class PreloginUI {
    private final Scanner scanner;
    // pass in ServerFacade here for real requests.
    // private final ServerFacade facade;

    public PreloginUI(/*ServerFacade facade*/) {
        this.scanner = new Scanner(System.in);
        // this.facade = facade;
    }

    public void run() {
        printWelcome();
        boolean running = true;
        while (running) {
            printPrompt();
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "help" -> printHelp();
                case "register" -> register();
                case "login" -> login();
                case "quit" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Unknown command, type 'help' for available commands.");
            }
        }
    }

    private void printWelcome() {
        System.out.println("Welcome to Chess! (Prelogin)");
    }

    private void printPrompt() {
        System.out.print("[PRELOGIN] Enter a command (help, register, login, quit): ");
    }

    private void printHelp() {
        System.out.println("""
                Prelogin commands:
                  help      - Show this help message
                  register  - Register a new account
                  login     - Login to your account
                  quit      - Exit the program
                """);
    }

    private void register() {
        System.out.println("=== Register ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        // Example: Replace with call to facade
        // AuthData auth = facade.register(username, password, email);
        System.out.println("Registered and logged in as " + username + " (stub - connect to server in real code).");
        // Transition to postlogin UI in project code
    }

    // Simulate login. (Replace with real facade calls.)
    private void login() {
        System.out.println("=== Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Example: Replace with call to facade
        // AuthData auth = facade.login(username, password);
        System.out.println("Logged in as " + username + " (stub - connect to server in real code).");
        // Transition to postlogin UI in project code
    }
}