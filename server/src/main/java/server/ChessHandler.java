package server;

import com.google.gson.Gson;
import service.GameService;
import service.ServiceException;
import service.UserService;

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

}
