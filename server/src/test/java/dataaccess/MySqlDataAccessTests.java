package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.UserData;


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
    public void testMethod_Positive() {
        // implement
    }

    @Test
    public void testMethod_Negative() {
        // implement
    }
     */

    @Test
    public void clear() {
        // implement
    }

    @Test
    public void insertUser_Positive() {
        // implement
    }

    @Test
    public void insertUser_Negative() {
        // implement
    }

    @Test
    public void getUser_Positive() {
        // Tests successful get u
    }

    @Test
    public void getUser_Negative() {
        // implement
    }

    @Test
    public void insertGame_Positive() {
        // implement
    }

    @Test
    public void insertGame_Negative() {
        // implement
    }

    @Test
    public void getGame_Positive() {
        // implement
    }

    @Test
    public void getGame_Negative() {
        // implement
    }

    @Test
    public void listGames_Positive() {
        // implement
    }

    @Test
    public void listGames_Negative() {
        // implement
    }

    @Test
    public void updateGame_Positive() {
        // implement
    }

    @Test
    public void updateGame_Negative() {
        // implement
    }

    @Test
    public void insertAuth_Positive() {
        // implement
    }

    @Test
    public void insertAuth_Negative() {
        // implement
    }

    @Test
    public void getAuth_Positive() {
        // implement
    }

    @Test
    public void getAuth_Negative() {
        // implement
    }

    @Test
    public void deleteAuth_Positive() {
        // implement
    }

    @Test
    public void deleteAuth_Negative() {
        // implement
    }
}