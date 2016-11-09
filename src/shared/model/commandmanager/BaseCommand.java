package shared.model.commandmanager;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

/**
 * Created by Mitchell on 9/15/2016.
 */
public abstract class BaseCommand implements HttpHandler {

    /**
     * Handles commands received from the server and returns a response
     * if response is null return an error response (setResponse -> 400)
     * @param exchange
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        setHttpExchange(exchange);

        String query;
        InputStream in = httpExchange.getRequestBody();
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

        if(query.length() > 0) System.out.println("query, post: "+query);
        else System.out.println("query, get.");
        setRequest(query);

        String response = serverExec();
        if(response != null) {
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }
        else {
            String error = "Error: failure";
            exchange.sendResponseHeaders(400, error.length());
            exchange.getResponseBody().write(error.getBytes());
        }

        exchange.close();
    }


    public int getUserId() {
        return getCookieJSON().getInt("playerID");
    }

    public String getUsername() {
        return getCookieJSON().getString("name");
    }

    public String getPassword() {
        return getCookieJSON().getString("password");
    }

    public int getGameId() {
        return gameId;
    }

    public abstract JSONObject getCookieJSON();

    public JSONObject getCookieJSONOnlyLogin() {
        Headers headers = httpExchange.getRequestHeaders();
        String undecodedCookie = headers.get("Cookie").get(0);
        String rawCookie = URLDecoder.decode(undecodedCookie);
        String cookie = rawCookie.substring(11, rawCookie.length());
        return new JSONObject(cookie);
    }

    public JSONObject getCookieJSONBoth() {
        Headers headers = httpExchange.getRequestHeaders();
        String undecodedCookie = headers.get("Cookie").get(0);
        String rawCookie = URLDecoder.decode(undecodedCookie);
        String gameIdStr = rawCookie.substring(rawCookie.length()-12, rawCookie.length());
        gameId = Integer.parseInt(gameIdStr.substring(11, gameIdStr.length()));
        String cookie = rawCookie.substring(11, rawCookie.length()-14);
        return new JSONObject(cookie);
    }

    /**
     * Kicks off server Execution
     */
    public abstract String serverExec();



    // getters and setters:

    private String request;

    private HttpExchange httpExchange;

    private int gameId;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public HttpExchange getHttpExchange() {
        return httpExchange;
    }

    public void setHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
