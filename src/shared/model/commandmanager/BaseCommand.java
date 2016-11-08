package shared.model.commandmanager;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 9/15/2016.
 */
public abstract class BaseCommand implements HttpHandler {

    private int userId = 0;

    private int gameId = 0;

    private String loginCookie = "/";

    private String gameCookie = "/";

    private String requestStr;



    /**
     * Handles commands received from the server and returns a response
     * if response is null return an error response (setResponse -> 400)
     * @param exchange
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String query;
        InputStream in = exchange.getRequestBody();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[4096];
            for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                out.write(buf, 0, n);
            }
            query = new String(out.toByteArray());
        }
        finally {
            in.close();
        }

        System.out.println("Query: "+query);
        requestStr = query;

        String responseStr = serverExec(userId, gameId);
        responseStr = "Success";//for testing



        List<String> cookieList = new ArrayList<>(1);
        cookieList.add(loginCookie);
        exchange.getResponseHeaders().put("Set-cookie", cookieList);
        exchange.sendResponseHeaders(200, responseStr.length());

        exchange.getResponseBody().write(responseStr.getBytes());

        exchange.close();

        //Todo
        //translate and set parameters
        //get cookie
        //call serverExec and return JSON to the client
    }

    /**
     * Kicks off server Execution
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    public abstract String serverExec(int userId, int gameId);


    // getters and setters:


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getLoginCookie() {
        return loginCookie;
    }

    public void setLoginCookie(String loginCookie) {
        this.loginCookie = loginCookie;
    }

    public String getGameCookie() {
        return gameCookie;
    }

    public void setGameCookie(String gameCookie) {
        this.gameCookie = gameCookie;
    }

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }
}
