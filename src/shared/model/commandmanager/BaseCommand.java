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

        System.out.println("Query: "+query);
        setRequest(query);

        String response = serverExec();
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());

        exchange.close();
    }


    public int getUserId() {


        Headers headers = httpExchange.getRequestHeaders();
        String undecodedCookie = headers.get("Cookie").get(0);
        String rawCookie = URLDecoder.decode(undecodedCookie);
        String cookie = rawCookie.substring(11, rawCookie.length());
        JSONObject cookieJSON = new JSONObject(cookie);
        return cookieJSON.getInt("playerID");
    }

    public int getGameId() {
        return 0;
    }

    /**
     * Kicks off server Execution
     */
    public abstract String serverExec();



    // getters and setters:

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
}
