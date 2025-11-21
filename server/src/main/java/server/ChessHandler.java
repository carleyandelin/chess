package server;

import com.google.gson.Gson;
import service.GameService;
import service.ServiceException;
import service.UserService;
import com.google.gson.JsonObject;

public class ChessHandler {
    private final GameService gameService;
    private final UserService userService;
    private final Gson gson;

    public ChessHandler(UserService userService, GameService gameService) {
        this.gameService = gameService;
        this.userService = userService;
        this.gson = new Gson();
    }

    public void clear(io.javalin.http.Context context) {
        try {
            gameService.clear();
            context.status(200);
            context.result("{}");
        } catch (ServiceException e) {
            context.status(e.getStatusCode());
            context.result(createErrorResponse(e.getMessage()));
        }
    }

    private String createErrorResponse(String message) {
        return "{\"message\":\"" + message + "\"}";
    }

    public void register(io.javalin.http.Context context) {
        try {
            UserService.RegisterRequest request = gson.fromJson(context.body(), UserService.RegisterRequest.class);
            UserService.RegisterResult result = userService.register(request);
            context.status(200);
            context.result(gson.toJson(result));
        } catch (ServiceException e) {
            context.status(e.getStatusCode());
            context.result(createErrorResponse(e.getMessage()));
        }
    }

    public void login(io.javalin.http.Context context) {
        try {
            UserService.LoginRequest request = gson.fromJson(context.body(), UserService.LoginRequest.class);
            UserService.LoginResult result = userService.login(request);
            context.status(200);
            context.result(gson.toJson(result));
        } catch (ServiceException e) {
            context.status(e.getStatusCode());
            context.result(createErrorResponse(e.getMessage()));
        }
    }

    public void logout(io.javalin.http.Context context) {
        try {
            String authToken = context.header("authorization");
            UserService.LogoutRequest request = new UserService.LogoutRequest(authToken);
            userService.logout(request);
            context.status(200);
            context.result("{}");
        } catch (ServiceException e) {
            context.status(e.getStatusCode());
            context.result(createErrorResponse(e.getMessage()));
        }
    }

    public void listGames(io.javalin.http.Context context) {
        try {
            String authToken = context.header("authorization");
            GameService.ListGamesRequest request = new GameService.ListGamesRequest(authToken);
            GameService.ListGamesResult result = gameService.listGames(request);
            context.status(200);
            context.result(gson.toJson(result));
        } catch (ServiceException e) {
            context.status(e.getStatusCode());
            context.result(createErrorResponse(e.getMessage()));
        }
    }

    public void createGame(io.javalin.http.Context context) {
        try {
            String authToken = context.header("authorization");
            JsonObject requestBody = gson.fromJson(context.body(), JsonObject.class);

            if (!requestBody.has("gameName") || requestBody.get("gameName").isJsonNull()) {
                context.status(400);
                context.result(createErrorResponse("Error: bad request"));
                return;
            }
            String gameName = requestBody.get("gameName").getAsString();
            GameService.CreateGameRequest request = new GameService.CreateGameRequest(authToken, gameName);
            GameService.CreateGameResult result = gameService.createGame(request);
            context.status(200);
            context.result(gson.toJson(result));
        } catch (ServiceException e) {
            context.status(e.getStatusCode());
            context.result(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            // Handle any other exceptions (like JSON parsing errors)
            context.status(400);
            context.result(createErrorResponse("Error: bad request"));
        }
    }

    public void joinGame(io.javalin.http.Context context) {
        try {
            String authToken = context.header("authorization");
            JsonObject requestBody = gson.fromJson(context.body(), JsonObject.class);

            if (!requestBody.has("playerColor") || !requestBody.has("gameID") || requestBody.get("gameID").isJsonNull()) {
                context.status(400);
                context.result(createErrorResponse("Error: bad request"));
                return;
            }

            String playerColor = null;
            if (!requestBody.get("playerColor").isJsonNull()) {
                playerColor = requestBody.get("playerColor").getAsString();
            }

            int gameID = requestBody.get("gameID").getAsInt();

            GameService.JoinGameRequest request = new GameService.JoinGameRequest(authToken, playerColor, gameID);
            gameService.joinGame(request);
            context.status(200);
            context.result("{}");
        } catch (ServiceException e) {
            context.status(e.getStatusCode());
            context.result(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
            context.result(createErrorResponse("Error: bad request"));
        }
    }

}
