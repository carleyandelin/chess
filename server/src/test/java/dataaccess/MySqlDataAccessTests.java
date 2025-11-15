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
}