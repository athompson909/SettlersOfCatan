package shared.model.commandmanager;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;

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
    public void handle(HttpExchange exchange) {

        try {

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
            } finally {
                in.close();
            }

            setRequest(query);
            System.out.println("[HTTP " + ((query.length() > 0) ? "POST, request = " + query + "]" : "GET]"));

            String response = serverExec();

            // setting the json for swagger
            ArrayList<String> mimetypes = new ArrayList<>();
            mimetypes.add("application/json");
            exchange.getResponseHeaders().put("Content­type", mimetypes);

            /*
            todo: ask the TAs why I can't put anything in the response body
             */
            if(response == null)
                setErrorResponse(exchange, "http error: bad request");
            else
                setSuccessfulResponse(exchange, response);

            System.out.println("Server Response: " + ((response == null) ? "null" : response));

            exchange.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR IN BaseCommand.handle... IOException thrown");
        }
    }

    private void setSuccessfulResponse(HttpExchange exchange, String str) throws IOException {
        exchange.sendResponseHeaders(200, str.length());
        exchange.getResponseBody().write(str.getBytes());
        exchange.getResponseBody().close();
    }

    private void setErrorResponse(HttpExchange exchange, String str) throws IOException {
        exchange.sendResponseHeaders(400, str.length());
        exchange.getResponseBody().write(str.getBytes());
        exchange.getResponseBody().close();
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

    private transient int gameId;

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
