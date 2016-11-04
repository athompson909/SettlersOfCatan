package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.lang.String;

/**
 * The Catan Server
 *
 * sets contexts
 * assigns handlers
 * has a base command object
 *
 * Created by adamthompson on 11/4/16.
 */
public class Server {

    private HttpServer httpServer;

    private String hostNumber;

    private int portNumber;

    private final int MAX_WAITING_CONNECTIONS = 20;

    public Server(String hostNumber, int portNumber) {
        setHostNumber(hostNumber);
        setPortNumber(portNumber);
    }

    /**
     * this is called in the main method (of ServerMain)
     */
    public void run() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(portNumber), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHostNumber() {
        return hostNumber;
    }

    public void setHostNumber(String hostNumber) {
        this.hostNumber = hostNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
