package server;

import com.google.gson.Gson;
import service.GameService;
import service.ServiceException;

public class ChessHandler {
    private final GameService gameService;
    private final Gson gson;

    public ChessHandler(GameService gameService) {
        this.gameService = gameService;
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
}
