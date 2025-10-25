package dataaccess;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import model.UserData;
import model.GameData;
import model.AuthData;

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
}
