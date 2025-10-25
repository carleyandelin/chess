package service;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;
    private MemoryDataAccess dataAccess;

    @BeforeEach
    public void setUp() {
        dataAccess = new MemoryDataAccess();
        gameService = new GameService(dataAccess);
    }

    @Test
    public void clearSuccess() throws Exception {
        gameService.clear();
    }
}
