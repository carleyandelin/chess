package dataaccess;

import java.sql.SQLException;
import model.UserData;
import model.GameData;
import model.AuthData;

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
        throw new DataAccessException("Not implemented");
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        throw new DataAccessException("Not implemented");
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
