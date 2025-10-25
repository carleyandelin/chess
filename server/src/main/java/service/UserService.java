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

    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}

}
