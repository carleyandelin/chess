package ui;

import client.ServerFacade;

public class Main {
    public static void main(String[] args) {
        // You can change the greeting if you like!
        System.out.println("""
        ===========================
        |   Welcome To  Chess!    |
        =========================== 
        """);
        ServerFacade facade = new ServerFacade(8080);

        while (true) {
            PreloginUI prelogin = new PreloginUI(facade);
            PreloginUI.AuthDataBundle bundle = prelogin.run();

            if (bundle == null) {
                // User chose to quit in prelogin
                System.out.println("Bye! See you next time!");
                break;
            }

            PostloginUI postlogin = new PostloginUI(facade);
            boolean wantsLogout = postlogin.run(bundle.username(), bundle.authToken());

            if (!wantsLogout) {
                System.out.println("Bye! See you next time!");
                break;
            }
            // If user logs out, loop back to prelogin UI
        }
    }
}