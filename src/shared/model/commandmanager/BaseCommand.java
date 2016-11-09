package shared.model.commandmanager;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mitchell on 9/15/2016.
 */
public abstract class BaseCommand implements HttpHandler {

    private String request;

    private HttpExchange httpExchange;

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

        System.out.println("Query: "+query);
        setRequest(query);

        String response = serverExec(0, 0);
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());

        exchange.close();
    }

    /**
     * Kicks off server Execution
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    public abstract String serverExec(int userId, int gameId);
    // getters and setters:

}
