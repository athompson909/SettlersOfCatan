package server.main;

import server.Server;

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
     * the argument reading functionality is that same as in ServerMain
     *
     * @param args - command line arguments
     *             args[0] = host number
     *             args[1] = port number
     */
    public static void main(String[] args) {

        String hostNumber = "localhost";
        String portNumber = "8081";

        //if they specified a host/port number
        if (args.length == 1) {
            //error
            System.out.println("ERROR: invalid host/port args");
            System.exit(0);
        }
        else if (args.length == 2){
            //good to pass the host/port nums to the ClientFacade
            hostNumber = args[0];
            portNumber = args[1];

            System.out.println("\t\t args[0] =" + args[0] + ", args[1] =" + args[1]);
        }


        Server server = new Server(hostNumber, Integer.parseInt(portNumber));
        System.out.println("starting server.run()");
        server.run();
    }
}
