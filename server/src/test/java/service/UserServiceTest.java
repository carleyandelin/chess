package service;

import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private MemoryDataAccess dataAccess;

    @BeforeEach
    public void setUp() {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
    }

    // tests for register
    @Test
    public void registerSuccess() throws Exception {
        // positive test
        UserService.RegisterRequest request = new UserService.RegisterRequest("alice", "password", "alice@email.com");
        UserService.RegisterResult result = userService.register(request);

        assertEquals("alice", result.username());
        assertNotNull(result.authToken());

        UserData user = dataAccess.getUser("alice");
        assertNotNull(user);
        assertEquals("alice", user.username());
        assertEquals("password", user.password());
        assertEquals("alice@email.com", user.email());
    }

    @Test
    public void registerBadRequest() {
        // negative test
        UserService.RegisterRequest request = new UserService.RegisterRequest(null, "password", "alice@email.com");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.register(request);
        });
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("bad request"));
    }

    @Test
    public void registerAlreadyTaken() throws Exception {
        // negative test for username already taken
        UserService.RegisterRequest request1 = new UserService.RegisterRequest("alice", "password", "alice@email.com");
        userService.register(request1);

        UserService.RegisterRequest request2 = new UserService.RegisterRequest("alice", "password2", "alice2@email.com");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.register(request2);
        });
        assertEquals(403, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("already taken"));
    }

    // login tests
    @Test
    public void loginSuccess() throws Exception {
        // positive test
        UserService.RegisterRequest registerRequest = new UserService.RegisterRequest("alice", "password", "alice@email.com");
        userService.register(registerRequest);

        UserService.LoginRequest request = new UserService.LoginRequest("alice", "password");
        UserService.LoginResult result = userService.login(request);

        assertEquals("alice", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void loginBadRequest() {
        // negative test
        UserService.LoginRequest request = new UserService.LoginRequest(null, "password");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.login(request);
        });
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("bad request"));
    }

    @Test
    public void loginUnauthorized() {
        // negative test with wrong password
        UserService.LoginRequest request = new UserService.LoginRequest("alice", "wrongpassword");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.login(request);
        });
        assertEquals(401, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}