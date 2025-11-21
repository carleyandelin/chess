package client;

import java.net.*;
import java.io.*;
import java.util.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import com.google.gson.Gson;

public class ServerFacade {
    private final String serverURL;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this.serverURL = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        URL url = new URL(serverURL + "/user");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        UserData req = new UserData(username, password, email);
        String json = gson.toJson(req);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        int status = conn.getResponseCode();
        Reader reader;
        if (status == 200) {
            reader = new InputStreamReader(conn.getInputStream());
            AuthData result = gson.fromJson(reader, AuthData.class);
            conn.disconnect();
            return result;
        } else {
            reader = new InputStreamReader(conn.getErrorStream());
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1024];
            int read;
            while ((read = reader.read(buf)) > 0) {
                sb.append(buf, 0, read);
            }
            conn.disconnect();
            throw new Exception("Registration failed: " + sb.toString());
        }
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
