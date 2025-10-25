package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

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

    public record ListGamesRequest(String authToken) {}
    public record ListGamesResult(GameSummary[] games) {}
    public record GameSummary(int gameID, String whiteUsername, String blackUsername, String gameName) {}

}
