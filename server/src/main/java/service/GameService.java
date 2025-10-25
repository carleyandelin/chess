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

    public ListGamesResult listGames(ListGamesRequest request) throws ServiceException {
        try {
            // validate auth token
            AuthData auth = dataAccess.getAuth(request.authToken());
            if (auth == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }
            // get all games
            GameData[] games = dataAccess.listGames();
            GameSummary[] gameSummaries = new GameSummary[games.length];
            for (int i = 0; i < games.length; i++) {
                GameData game = games[i];
                gameSummaries[i] = new GameSummary(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            }
            return new ListGamesResult(gameSummaries);
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

    public record CreateGameRequest(String authToken, String gameName) {}
    public record CreateGameResult(int gameID) {}

    public record ListGamesRequest(String authToken) {}
    public record ListGamesResult(GameSummary[] games) {}
    public record GameSummary(int gameID, String whiteUsername, String blackUsername, String gameName) {}

}
