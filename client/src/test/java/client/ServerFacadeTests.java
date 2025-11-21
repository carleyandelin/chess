package client;

import org.junit.jupiter.api.*;
import server.Server;
import client.ServerFacade;
import model.AuthData;
import model.GameData;

import java.util.List;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearServer() throws Exception {
        facade.clear();
    }

    @Test
    void registerPositive() throws Exception {
        AuthData result = facade.register("user1", "pass", "u1@email.com");
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("user1", result.username());
    }

    @Test
    void registerNegative() throws Exception {    // duplicate user
        facade.register("user2", "pass", "u2@email.com");
        Assertions.assertThrows(Exception.class, () -> {
            facade.register("user2", "anotherpass", "other@email.com");
        });
    }

    @Test
    void loginPositive() throws Exception {
        facade.register("user10", "pass10", "user10@email.com"); // Register first
        AuthData result = facade.login("user10", "pass10");      // Then login
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("user10", result.username());
    }

    @Test
    void loginNegative() throws Exception {    // wrong password
        facade.register("user11", "pass11", "user11@email.com");
        Assertions.assertThrows(Exception.class, () -> {
            facade.login("user11", "notTheRightPassword");
        });
    }

    @Test
    void loginNegative_noSuchUser() {
        Assertions.assertThrows(Exception.class, () -> {
            facade.login("nonexistent", "nopass");
        });
    }

    @Test
    void logoutPositive() throws Exception {
        AuthData data = facade.register("logoutUser", "logoutPass", "logout@email.com");
        facade.logout(data.authToken());
        // Attempt to create game (should fail or require login) if you want further assertion
    }

    @Test
    void logoutNegative_invalidToken() {
        Assertions.assertThrows(Exception.class, () -> {
            facade.logout("bogus-token");
        });
    }

    @Test
    void createGamePositive() throws Exception {
        AuthData user = facade.register("gameUser", "pw", "guser@email.com");
        facade.createGame(user.authToken(), "my game");
        // You may want to check listGames here to verify a new game exists
    }

    @Test
    void createGameNegative_unauthorized() {
        Assertions.assertThrows(Exception.class, () -> {
            facade.createGame("invalid-token", "should fail");
        });
    }

    @Test
    void listGamesPositive() throws Exception {
        AuthData user = facade.register("lg_positive", "pw", "lgpos@email.com");
        facade.createGame(user.authToken(), "fun game");
        List<GameData> games = facade.listGames(user.authToken());
        boolean found = games.stream()
                .anyMatch(g -> g.gameName().equals("fun game"));
        Assertions.assertTrue(found, "Newly created game should be listed.");
    }

    @Test
    void listGamesNegative_invalidToken() {
        Assertions.assertThrows(Exception.class, () -> {
            facade.listGames("bad-token");
        });
    }

    @Test
    void joinGamePositive() throws Exception {
        AuthData user = facade.register("joinuser", "pw", "join@email.com");
        facade.createGame(user.authToken(), "joinable game");
        List<GameData> games = facade.listGames(user.authToken());
        int gameId = games.stream()
                .filter(g -> g.gameName().equals("joinable game"))
                .findFirst()
                .orElseThrow()
                .gameID();
        // trying to join as white
        facade.joinGame(user.authToken(), gameId, "WHITE");
    }

    @Test
    void joinGameNegative_invalidToken() throws Exception {
        AuthData user = facade.register("joinuser2", "pw", "join2@email.com");
        facade.createGame(user.authToken(), "another game");
        List<GameData> games = facade.listGames(user.authToken());
        int gameId = games.get(0).gameID();
        Assertions.assertThrows(Exception.class, () -> {
            facade.joinGame("bogus-token", gameId, "WHITE");
        });
    }
}