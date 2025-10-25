package service;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.AuthData;
import model.GameData;
import chess.ChessGame;

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

    @Test
    public void listGamesSuccess() throws Exception {
        // positive
        AuthData auth = new AuthData("test-token", "alice");
        dataAccess.insertAuth(auth);

        // create some test games
        GameData game1 = new GameData(1, "alice", "bob", "Game1", new ChessGame());
        GameData game2 = new GameData(2, null, null, "Game2", new ChessGame());
        dataAccess.insertGame(game1);
        dataAccess.insertGame(game2);

        // test listGames
        GameService.ListGamesRequest request = new GameService.ListGamesRequest("test-token");
        GameService.ListGamesResult result = gameService.listGames(request);

        assertEquals(2, result.games().length);
        assertEquals("Game1", result.games()[0].gameName());
        assertEquals("Game2", result.games()[1].gameName());
    }

    @Test
    public void listGamesUnauthorized() {
        // negative
        GameService.ListGamesRequest request = new GameService.ListGamesRequest("invalid-token");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            gameService.listGames(request);
        });
        assertEquals(401, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

}
