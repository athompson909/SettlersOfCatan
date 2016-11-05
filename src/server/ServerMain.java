package server;

/**
 * the main class for the server
 *
 *
 * Created by adamthompson on 11/4/16.
 */
public class ServerMain {


    /**
     * the main method for the server
     *
     * @param args - command line arguments
     *             args[0] = host number
     *             args[1] = port number
     */
    private static void main(String[] args) {

        String hostNumber = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Server server = new Server(hostNumber, portNumber);
        server.run();
    }
}
