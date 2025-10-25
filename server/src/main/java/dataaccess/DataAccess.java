package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

public interface DataAccess {
    void clear() throws DataAccessException;

    // user operations
    void insertUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;

    // game operations
    int insertGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    GameData[] listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;

    // auth operations
    void insertAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}