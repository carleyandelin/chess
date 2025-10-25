package dataaccess;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import model.UserData;
import model.GameData;
import model.AuthData;
import java.util.Collection;

public class MemoryDataAccess implements DataAccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();
    private final AtomicInteger nextGameID = new AtomicInteger(1);

    @Override
    public void clear() throws DataAccessException{
        users.clear();
        games.clear();
        auths.clear();
        nextGameID.set(1);
    }

    // user operations
    @Override
    public void insertUser(UserData user) throws DataAccessException {
        if (user == null || user.username() == null) {
            throw new DataAccessException("User data cannot be null");
        }
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User already exists");
        }
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException ("Username cannot be null");
        }
        return users.get(username);
    }

    // game operations
    @Override
    public int insertGame(GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("Game data cannot be null");
        }
        int gameID = nextGameID.getAndIncrement();
        GameData newGame = new GameData(gameID, game.whiteUsername(), game.blackUsername(),
                game.gameName(), game.game());
        games.put(gameID, newGame);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        Collection<GameData> gameCollection = games.values();
        return gameCollection.toArray(new GameData[0]);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("Game data cannot be null");
        }
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Game not found");
        }
        games.put(game.gameID(), game);
    }

    // auth operations
    @Override
    public void insertAuth(AuthData auth) throws DataAccessException {
        if (auth == null || auth.authToken() == null) {
            throw new DataAccessException("Auth data cannot be null");
        }
        auths.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("Auth token cannot be null");
        }
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("Auth token cannot be null");
        }
        auths.remove(authToken);
    }
}
