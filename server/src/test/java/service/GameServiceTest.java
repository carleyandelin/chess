package service;

import dataaccess.MemoryDataAccess;
import dataaccess.DataAccessException;
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

    @Test
    public void createGameSuccess() throws Exception {
        // positive
        AuthData auth = new AuthData("test-token", "alice");
        dataAccess.insertAuth(auth);

        // test createGame
        GameService.CreateGameRequest request = new GameService.CreateGameRequest("test-token", "My Game");
        GameService.CreateGameResult result = gameService.createGame(request);

        assertTrue(result.gameID() > 0);

        // verify
        GameData game = dataAccess.getGame(result.gameID());
        assertNotNull(game);
        assertEquals("My Game", game.gameName());
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    public void createGameBadRequest() throws Exception {
        // negative - empty game name
        AuthData auth = new AuthData("test-token", "alice");
        dataAccess.insertAuth(auth);

        GameService.CreateGameRequest request = new GameService.CreateGameRequest("test-token", "");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            gameService.createGame(request);
        });
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("bad request"));
    }

    @Test
    public void createGameUnauthorized() {
        // negative = invalid auth
        GameService.CreateGameRequest request = new GameService.CreateGameRequest("invalid-token", "My Game");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            gameService.createGame(request);
        });
        assertEquals(401, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    public void joinGameSuccess() throws Exception {
        // positive
        AuthData auth = new AuthData("test-token", "alice");
        dataAccess.insertAuth(auth);

        GameData game = new GameData(1, null, null, "My Game", new ChessGame());
        dataAccess.insertGame(game);

        // test joinGame
        GameService.JoinGameRequest request = new GameService.JoinGameRequest("test-token", "WHITE", 1);
        assertDoesNotThrow(() -> gameService.joinGame(request));

        // verify
        GameData updatedGame = dataAccess.getGame(1);
        assertEquals("alice", updatedGame.whiteUsername());
        assertNull(updatedGame.blackUsername());
    }

    @Test
    public void joinGameBadRequest() throws Exception {
        // negative - invalid gameID
        AuthData auth = new AuthData("test-token", "alice");
        dataAccess.insertAuth(auth);

        GameService.JoinGameRequest request = new GameService.JoinGameRequest("test-token", "WHITE", 999);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            gameService.joinGame(request);
        });
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("bad request"));
    }

    @Test
    public void joinGameAlreadyTaken() throws Exception {
        // negative - color already taken
        AuthData auth = new AuthData("test-token", "alice");
        dataAccess.insertAuth(auth);

        GameData game = new GameData(1, "bob", null, "My Game", new ChessGame());
        dataAccess.insertGame(game);

        GameService.JoinGameRequest request = new GameService.JoinGameRequest("test-token", "WHITE", 1);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            gameService.joinGame(request);
        });
        assertEquals(403, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("already taken"));
    }

    @Test
    public void joinGameUnauthorized() {
        // negative - invalid auth token
        GameService.JoinGameRequest request = new GameService.JoinGameRequest("invalid-token", "WHITE", 1);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            gameService.joinGame(request);
        });
        assertEquals(401, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

}
