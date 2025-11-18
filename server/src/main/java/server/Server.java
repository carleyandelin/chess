package server;

import io.javalin.*;
import dataaccess.DataAccess;
import dataaccess.DatabaseManager;
import dataaccess.MySqlDataAccess;
import service.GameService;
import dataaccess.MemoryDataAccess;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Initialize database and tables (ready for when I switch to MySQL)
        try {
            DatabaseManager.createDatabase();
            MySqlDataAccess mysqlDataAccess = new MySqlDataAccess();
            mysqlDataAccess.createTables();
        } catch (Exception e) {
            // If database initialization fails, log it but continue with MemoryDataAccess
            System.err.println("Warning: Could not initialize database: " + e.getMessage());
        }

        // Use MemoryDataAccess for now (so tests pass)
        // TODO: Switch line below from MemoryDataAccess to MySqlDataAccess once all methods are implemented
        DataAccess dataAccess = new MySqlDataAccess();
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