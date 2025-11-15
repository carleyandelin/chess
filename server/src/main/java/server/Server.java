package server;

import io.javalin.*;
import dataaccess.DataAccess;
import dataaccess.DatabaseManager;
import dataaccess.MySqlDataAccess;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // initialize database and tables first
        try {
            DatabaseManager.createDatabase();
            MySqlDataAccess dataAccess = new MySqlDataAccess();
            dataAccess.createTables();
            
            // create services with the MySQL data access
            GameService gameService = new GameService(dataAccess);
            UserService userService = new UserService(dataAccess);
            ChessHandler handler = new ChessHandler(userService, gameService);

            // register endpoints and exception handlers here.
            javalin.delete("/db", handler::clear);
            javalin.post("/user", handler::register);
            javalin.post("/session", handler::login);
            javalin.delete("/session", handler::logout);
            javalin.get("/game", handler::listGames);
            javalin.post("/game", handler::createGame);
            javalin.put("/game", handler::joinGame);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize server", e);
        }

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
