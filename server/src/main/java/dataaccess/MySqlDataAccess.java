package dataaccess;

import java.sql.SQLException;
import model.UserData;
import model.GameData;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

public class MySqlDataAccess implements DataAccess {

    public void createTables() throws DataAccessException {
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) PRIMARY KEY,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
                )
                """;
        String createGamesTable = """
                CREATE TABLE IF NOT EXISTS games (
                gameID INT AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL
                )
                """;
        String createAuthTable = """
                CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(255) PRIMARY KEY,
                username VARCHAR(255) NOT NULL
                )
                """;

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createUsersTable)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(createGamesTable)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(createAuthTable)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create tables", ex);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String truncateUsers = "TRUNCATE TABLE users";
        String truncateGames = "TRUNCATE TABLE games";
        String truncateAuth = "TRUNCATE TABLE auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(truncateUsers)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(truncateGames)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(truncateAuth)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear database", ex);
        }
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        if (user == null || user.username() == null) {
            throw new DataAccessException("User data cannot be null");
        }
        if (getUser(user.username()) != null) {
            throw new DataAccessException("User already exists");
        }
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.email());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to insert user", ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("username cannot be null");
        }
        String sql = "SELECT username, password, email FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        // get data from table if row exists
                        String dbUsername = rs.getString("username");
                        String dbPassword = rs.getString("password"); // this password is the hashed one
                        String dbEmail = rs.getString("email");
                        return new UserData(dbUsername, dbPassword, dbEmail);
                    } else {
                        // no user found
                        return null;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get user", ex);
        }
    }

    @Override
    public int insertGame(GameData game) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    @Override
    public void insertAuth(AuthData auth) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

}
