package server;

import io.javalin.*;
import dataaccess.DataAccess;
import service.GameService;
import dataaccess.MemoryDataAccess;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        DataAccess dataAccess = new MemoryDataAccess();
        GameService gameService = new GameService(dataAccess);
        UserService userService = new UserService(dataAccess);
        ChessHandler handler = new ChessHandler(userService, gameService);

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", handler::clear);
        javalin.post("/user", handler::register);
        javalin.post("/session", handler::login);
        javalin.delete("/session", handler::logout);
        javalin.get("/game", handler::listGames);
        javalin.post("/game", handler::createGame);
        javalin.put("/game", handler::joinGame);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}