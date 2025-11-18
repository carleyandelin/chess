package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.UserData;
import model.GameData;
import chess.ChessGame;
import chess.ChessBoard;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Set;
import java.util.HashSet;


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
        String plainPassword = "MySecret123";
        UserData user = new UserData("testUser", plainPassword, "test@example.com");

        dataAccess.insertUser(user);

        UserData fetched = dataAccess.getUser("testUser");
        assertNotNull(fetched, "Inserted user should be retrievable");
        assertEquals("testUser", fetched.username());
        assertEquals("test@example.com", fetched.email());

        assertTrue(BCrypt.checkpw(plainPassword, fetched.password()), "Passwords should match using BCrypt");
    }

    @Test
    public void insertUser_Negative() throws Exception {
        // no duplicates allowed
        UserData user1 = new UserData("dupeUser", "pw1", "dupe1@example.com");
        dataAccess.insertUser(user1);
        UserData user2 = new UserData("dupeUser", "pw2", "dupe2@example.com");
        assertThrows(DataAccessException.class, () -> dataAccess.insertUser(user2), "Inserting duplicate username should fail.");

        // can't be null
        assertThrows(DataAccessException.class, () -> dataAccess.insertUser(null), "Inserting null user should fail.");
        UserData nullNameUser = new UserData(null, "pw", "email@example.com");
        assertThrows(DataAccessException.class, () -> dataAccess.insertUser(nullNameUser), "Inserting user with null username should fail.");
    }

    @Test
    public void getUser_Positive() throws Exception {
        UserData testUser = new UserData("dupeUser", "pw1", "dupe1@example.com");
        dataAccess.insertUser(testUser);
        UserData result = dataAccess.getUser("dupeUser");

        assertNotNull(result, "user needs to exist in data structure after insertUser");
        assertEquals("dupeUser", result.username(), "username should match inserted username");
        assertEquals("dupe1@example.com", result.email(), "email should match inserted email");
    }

    @Test
    public void getUser_Negative() throws Exception {
        assertThrows(DataAccessException.class, () -> dataAccess.getUser(null), "Should throw exception for null username");
    }

    @Test
    public void insertGame_Positive() throws Exception {
        GameData testGame = new GameData(0, "user1", "user2", "coolGame", new ChessGame());
        int gameID = dataAccess.insertGame(testGame);
        dataAccess.insertGame(testGame);
        GameData fetched = dataAccess.getGame(gameID);

        assertNotNull(fetched, "Inserted game should be retrievable");
        assertEquals(gameID, fetched.gameID(), "gameID should match inserted gameID");
        assertEquals("user1", fetched.whiteUsername(), "white username should match inserted one");
        assertEquals("user2", fetched.blackUsername(), "black username should match inserted one");
        assertEquals("coolGame", fetched.gameName(), "gameName username should match inserted one");
        assertEquals(new ChessGame(), fetched.game(), "game should match inserted one");
    }

    @Test
    public void insertGame_Negative() throws Exception {
        GameData invalid = new GameData(0, "word", "blurb", "name", null);
        assertThrows(DataAccessException.class, () -> dataAccess.insertGame(invalid),
                "GameData with null game should throw an exception.");
    }

    @Test
    public void getGame_Positive() throws Exception {
        GameData testGame = new GameData(0, "user1", "user2", "coolGame", new ChessGame());
        int gameID = dataAccess.insertGame(testGame);
        dataAccess.insertGame(testGame);
        GameData result = dataAccess.getGame(gameID);

        assertNotNull(result, "game needs to exist in data base");
        assertEquals(gameID, result.gameID());
        assertEquals("user1", result.whiteUsername());
        assertEquals("user2", result.blackUsername());
        assertEquals("coolGame", result.gameName());
        assertEquals(new ChessGame(), result.game());
    }

    @Test
    public void getGame_Negative() throws Exception {
        GameData result = dataAccess.getGame(99999); // unlikely ID
        assertNull(result, "getGame should return null for a non-existent gameID");
    }

    @Test
    public void listGames_Positive() throws Exception {
        GameData game1 = new GameData(0, "whiteA", "blackA", "Fun Game 1", new ChessGame());
        GameData game2 = new GameData(0, "whiteB", "blackB", "Fun Game 2", new ChessGame());
        int gameId1 = dataAccess.insertGame(game1);
        int gameId2 = dataAccess.insertGame(game2);
        GameData[] games = dataAccess.listGames();
        assertNotNull(games, "listGames should not return null");
        assertEquals(2, games.length, "There should be 2 games in the database");
        Set<String> names = new HashSet<>();
        for (GameData g : games) {
            names.add(g.gameName());
        }
        assertTrue(names.contains("Fun Game 1"), "Should contain 'Fun Game 1'");
        assertTrue(names.contains("Fun Game 2"), "Should contain 'Fun Game 2'");
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
        GameData original = new GameData(0, "w", "b", "Game", new ChessGame());
        int gameId = dataAccess.insertGame(original);
        ChessGame updatedGame = new ChessGame();
        GameData updated = new GameData(gameId, "WhitePlayer", "BlackPlayer", "UpdatedName", updatedGame);
        dataAccess.updateGame(updated);
        GameData fetched = dataAccess.getGame(gameId);
        assertNotNull(fetched);
        assertEquals("UpdatedName", fetched.gameName());
        assertEquals("WhitePlayer", fetched.whiteUsername());
        assertEquals("BlackPlayer", fetched.blackUsername());
    }

    @Test
    public void updateGame_Negative() {
        GameData fake = new GameData(99999, "none", "none", "none", new ChessGame());
        assertThrows(DataAccessException.class, () -> dataAccess.updateGame(fake), "Updating nonexistent game should throw");
        assertThrows(DataAccessException.class, () -> dataAccess.updateGame(null), "Null update should throw");
    }

    @Test
    public void insertAuth_Positive() throws Exception {
        AuthData auth = new AuthData("token123", "alice");
        dataAccess.insertAuth(auth);

        AuthData found = dataAccess.getAuth("token123");
        assertNotNull(found);
        assertEquals("token123", found.authToken());
        assertEquals("alice", found.username());
    }

    @Test
    public void insertAuth_Negative() {
        assertThrows(DataAccessException.class, () -> dataAccess.insertAuth(null));
        assertThrows(DataAccessException.class, () -> dataAccess.insertAuth(new AuthData(null, "bob")));
        assertThrows(DataAccessException.class, () -> dataAccess.insertAuth(new AuthData("sometoken", null)));
        AuthData auth = new AuthData("dupeToken", "alice");
        assertDoesNotThrow(() -> dataAccess.insertAuth(auth));
        assertThrows(DataAccessException.class, () -> dataAccess.insertAuth(new AuthData("dupeToken", "bob")));
    }

    @Test
    public void getAuth_Positive() throws Exception {
        AuthData auth = new AuthData("findMe", "bob");
        dataAccess.insertAuth(auth);
        AuthData got = dataAccess.getAuth("findMe");
        assertNotNull(got);
        assertEquals("findMe", got.authToken());
        assertEquals("bob", got.username());
    }

    @Test
    public void getAuth_Negative() throws Exception {
        AuthData got = dataAccess.getAuth("noSuchToken");
        assertNull(got);
        assertThrows(DataAccessException.class, () -> dataAccess.getAuth(null));
    }

    @Test
    public void deleteAuth_Positive() throws Exception {
        AuthData auth = new AuthData("todelete", "user");
        dataAccess.insertAuth(auth);
        assertNotNull(dataAccess.getAuth("todelete"));
        dataAccess.deleteAuth("todelete");
        assertNull(dataAccess.getAuth("todelete"));
    }

    @Test
    public void deleteAuth_Negative() {
        assertDoesNotThrow(() -> dataAccess.deleteAuth("nonexistentToken"));
        assertThrows(DataAccessException.class, () -> dataAccess.deleteAuth(null));
    }
}