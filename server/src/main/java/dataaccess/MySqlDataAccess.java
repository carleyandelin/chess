package dataaccess;

import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import model.UserData;
import model.GameData;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;
import chess.ChessGame;
import java.util.ArrayList;
import java.util.List;

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
        if (game == null || game.game() == null || game.gameName() == null) {
            throw new DataAccessException("game data cannot be null");
        }
        Gson gson = new Gson();
        String gameJson = gson.toJson(game.game());
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                preparedStatement.setString(4, gameJson);

                preparedStatement.executeUpdate();

                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int gameID = generatedKeys.getInt(1);
                        return gameID;
                    } else {
                        throw new DataAccessException("couldn't get generated game ID");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to insert game", ex);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, gameID);

                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        // get data from the row if exists
                        int dbGameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String gameJson = rs.getString("game");

                        Gson gson = new Gson();
                        ChessGame chessGame = gson.fromJson(gameJson, ChessGame.class);

                        return new GameData(dbGameID, whiteUsername, blackUsername, gameName, chessGame);
                    } else {
                        // no game found
                        return null;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get game", ex);
        }
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
        List<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(sql);
            var rs = preparedStatement.executeQuery()) {
            Gson gson = new Gson();
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String gameJson = rs.getString("game");
                ChessGame chessGame = gson.fromJson(gameJson, ChessGame.class);
                games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to list games", ex);
        }
        return games.toArray(new GameData[0]);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("game data cannot be null");
        }
        String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        Gson gson = new Gson();
        String gameJson = gson.toJson(game.game());
        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());
            preparedStatement.setString(4, gameJson);
            preparedStatement.setInt(5, game.gameID());
            int changedRows = preparedStatement.executeUpdate();
            if (changedRows == 0) {
                throw new DataAccessException("no game found with given gameID");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to update game data", ex);
        }
    }

    @Override
    public void insertAuth(AuthData auth) throws DataAccessException {
        if (auth == null || auth.authToken() == null || auth.username() == null) {
            throw new DataAccessException("auth data cannot be null");
        }
        String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, auth.authToken());
            preparedStatement.setString(2, auth.username());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to insert auth data", ex);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("authToken cannot be null");
        }
        String sql = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("authToken"), rs.getString("username"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error retrieving auth", ex);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("auth token cannot be null");
        }
        String sql = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to delete auth token", ex);
        }
    }

}
