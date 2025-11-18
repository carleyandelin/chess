package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.UserData;
import model.GameData;
import chess.ChessGame;
import model.AuthData;


public class MySqlDataAccessTests {
    private MySqlDataAccess dataAccess;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize database and clear it
        DatabaseManager.createDatabase();
        dataAccess = new MySqlDataAccess();
        dataAccess.createTables();
        dataAccess.clear(); // Start with clean database
    }

    // Basic code for testing each method below
    /*
    @Test
    public void testMethod_Positive() throws Exception {
        // implement
    }

    @Test
    public void testMethod_Negative() throws Exception {
        // implement
    }
     */

    @Test
    public void clear() throws Exception {
        // insert data
        UserData user = new UserData("user1", "hashpass", "user1@example.com");
        dataAccess.insertUser(user);
        AuthData auth = new AuthData("authToken1", "user1");
        dataAccess.insertAuth(auth);
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(0, "whiteTester", "blackTester", "testClear", chessGame);
        int gameID = dataAccess.insertGame(game);
        // check data exits
        assertNotNull(dataAccess.getUser("user1"), "User should exist before clear");
        assertNotNull(dataAccess.getAuth("authToken1"), "Auth should exist before clear");
        assertNotNull(dataAccess.getGame(gameID), "Game should exist before clear");

        dataAccess.clear();

        // check data is gone
        assertNull(dataAccess.getUser("user1"), "User should be gone after clear");
        assertNull(dataAccess.getAuth("authToken1"), "Auth should be gone after clear");
        assertNull(dataAccess.getGame(gameID), "Game should be gone after clear");
    }

    @Test
    public void insertUser_Positive() throws Exception {
        // implement
    }

    @Test
    public void insertUser_Negative() throws Exception {
        // implement
    }

    @Test
    public void getUser_Positive() throws Exception {
        // implement
    }

    @Test
    public void getUser_Negative() throws Exception {
        // implement
    }

    @Test
    public void insertGame_Positive() throws Exception {
        // implement
    }

    @Test
    public void insertGame_Negative() throws Exception {
        // implement
    }

    @Test
    public void getGame_Positive() throws Exception {
        // implement
    }

    @Test
    public void getGame_Negative() throws Exception {
        // implement
    }

    @Test
    public void listGames_Positive() throws Exception {
        // implement
    }

    @Test
    public void listGames_Negative() throws Exception {
        // ensures empty list, not null
        GameData[] games = dataAccess.listGames();
        assertNotNull(games, "games array should not be null");
        assertEquals(0, games.length, "Should return 0 games");
    }

    @Test
    public void updateGame_Positive() throws Exception {
        // implement
    }

    @Test
    public void updateGame_Negative() throws Exception {
        // implement
    }

    @Test
    public void insertAuth_Positive() throws Exception {
        // implement
    }

    @Test
    public void insertAuth_Negative() throws Exception {
        // implement
    }

    @Test
    public void getAuth_Positive() throws Exception {
        // implement
    }

    @Test
    public void getAuth_Negative() throws Exception {
        // implement
    }

    @Test
    public void deleteAuth_Positive() throws Exception {
        // implement
    }

    @Test
    public void deleteAuth_Negative() throws Exception {
        // implement
    }
}