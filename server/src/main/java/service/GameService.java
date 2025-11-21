package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import chess.ChessGame;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() throws ServiceException {
        try {
            dataAccess.clear();
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    // CHANGED: Now returns array of full GameData (with ChessGame board)
    public ListGamesResult listGames(ListGamesRequest request) throws ServiceException {
        try {
            // validate auth token
            AuthData auth = dataAccess.getAuth(request.authToken());
            if (auth == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }
            // get all games, include full board (GameData)
            GameData[] games = dataAccess.listGames();
            return new ListGamesResult(games);
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ServiceException {
        try {
            AuthData auth = dataAccess.getAuth(request.authToken());
            if (auth == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }
            if (request.gameName() == null || request.gameName().isEmpty()) {
                throw new ServiceException("Error: bad request", 400);
            }
            GameData game = new GameData(0, null, null, request.gameName(), new ChessGame());
            int gameID = dataAccess.insertGame(game);

            return new CreateGameResult(gameID);
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    // (Optional but recommended) When you implement observe, return GameData with board here as well

    // Also consider updating joinGame to fetch and return the updated GameData after update (for handler response)
    public GameData joinGame(JoinGameRequest request) throws ServiceException {
        try {
            AuthData auth = dataAccess.getAuth(request.authToken());
            if (auth == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }
            if (request.gameID() <= 0) {
                throw new ServiceException("Error: bad request", 400);
            }
            GameData game = dataAccess.getGame(request.gameID());
            if (game == null) {
                throw new ServiceException("Error: bad request", 400);
            }

            // If playerColor is null, allow as observer !! this was a huge problem for me
            if (request.playerColor() == null) {
                return game;
            }

            if (!"WHITE".equals(request.playerColor()) && !"BLACK".equals(request.playerColor())) {
                throw new ServiceException("Error: bad request", 400);
            }

            String username = auth.username();
            if ("WHITE".equals(request.playerColor()) && game.whiteUsername() != null) {
                throw new ServiceException("Error: already taken", 403);
            }
            if ("BLACK".equals(request.playerColor()) && game.blackUsername() != null) {
                throw new ServiceException("Error: already taken", 403);
            }
            // update game for joining as player
            String whiteUsername = "WHITE".equals(request.playerColor()) ? username : game.whiteUsername();
            String blackUsername = "BLACK".equals(request.playerColor()) ? username : game.blackUsername();
            GameData updatedGame = new GameData(game.gameID(), whiteUsername, blackUsername,
                    game.gameName(), game.game());
            dataAccess.updateGame(updatedGame);

            return updatedGame; // NEW: Return the updated game data!
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    public record JoinGameRequest(String authToken, String playerColor, int gameID) {}

    public record CreateGameRequest(String authToken, String gameName) {}
    public record CreateGameResult(int gameID) {}

    public record ListGamesRequest(String authToken) {}
    public record ListGamesResult(GameData[] games) {}  // CHANGED: was GameSummary[]
}