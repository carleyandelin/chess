package server;

import io.javalin.*;
import dataaccess.DataAccess;
import service.GameService;
import dataaccess.MemoryDataAccess;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        DataAccess dataAccess = new MemoryDataAccess();
        GameService gameService = new GameService(dataAccess);
        ChessHandler handler = new ChessHandler(gameService);

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", handler::clear);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
