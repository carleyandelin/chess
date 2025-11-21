package client;

import java.net.*;
import java.io.*;
import java.util.*;
import model.AuthData;
import model.GameData;

public class ServerFacade {
    private final String serverURL;
    public ServerFacade(int port) {
        this.serverURL = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        throw new RuntimeException("Not yet implemented");
    }

    public AuthData login(String username, String password) throws Exception {
        throw new RuntimeException("Not yet implemented");
    }

    public void logout(String authToken) throws Exception {
        throw new RuntimeException("Not yet implemented");
    }

    public void createGame(String authToken, String gameName) throws Exception {
        throw new RuntimeException("Not yet implemented");
    }

    public List<GameData> listGames(String authToken) throws Exception {
        throw new RuntimeException("Not yet implemented");
    }

    public void joinGame(String authToken, int gameID, String color) throws Exception {
        throw new RuntimeException("Not yet implemented");
    }

    public void observeGame(String authToken, int gameID) throws Exception {
        throw new RuntimeException("Not yet implemented");
    }
}
