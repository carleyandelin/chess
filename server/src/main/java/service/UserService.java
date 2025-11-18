package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest request) throws ServiceException {
        try {
            // validate
            if (request.username() == null || request.password() == null || request.email() == null) {
                throw new ServiceException("Error: bad request", 400);
            }
            if (request.username().isEmpty() || request.password().isEmpty() || request.email.isEmpty()) {
                throw new ServiceException("Error: bad request", 400);
            }
            // does user already exist?
            if (dataAccess.getUser(request.username()) != null) {
                throw new ServiceException("Error: already taken", 403);
            }
            // create user
            UserData user = new UserData(request.username(), request.password(), request.email());
            dataAccess.insertUser(user);
            // create auth token
            String authToken = generateToken();
            AuthData auth = new AuthData(authToken, request.username());
            dataAccess.insertAuth(auth);

            return new RegisterResult(request.username(), authToken);
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LoginResult login(LoginRequest request) throws ServiceException {
        try {
            // validate
            if (request.username() == null || request.password() == null) {
                throw new ServiceException("Error: bad request", 400);
            }
            if (request.username().isEmpty() || request.password().isEmpty()) {
                throw new ServiceException("Error: bad request", 400);
            }
            // get user
            UserData user = dataAccess.getUser(request.username());
            if (user == null || !org.mindrot.jbcrypt.BCrypt.checkpw(request.password(), user.password())) {
                throw new ServiceException("Error: unauthorized", 401);
            }
            // create auth token
            String authToken = generateToken();
            AuthData auth = new AuthData(authToken, request.username());
            dataAccess.insertAuth(auth);

            return new LoginResult(request.username(), authToken);
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    public void logout(LogoutRequest request) throws ServiceException {
        try {
            // validate auth token
            AuthData auth = dataAccess.getAuth(request.authToken());
            if (auth == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }
            // delete auth token
            dataAccess.deleteAuth(request.authToken());
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    public record LogoutRequest(String authToken) {}

    public record LoginRequest(String username, String password) {}
    public record LoginResult(String username, String authToken) {}

    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}

}
