package client;

import org.junit.jupiter.api.*;
import server.Server;
import client.ServerFacade;
import model.AuthData;

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
        //TODO
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
        facade.register("user10", "pass10", "user10@email.com");
        AuthData result = facade.login("user10", "pass10");
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
}