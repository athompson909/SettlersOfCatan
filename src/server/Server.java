package server;

import com.sun.net.httpserver.HttpServer;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;

import java.io.IOException;
import java.net.InetSocketAddress;

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

    private final int MAX_WAITING_CONNECTIONS = 40;

    public Server(String hostNumber, int portNumber) {
        setHostNumber(hostNumber);
        setPortNumber(portNumber);
    }

    /**
     * this is called in the main method (of ServerMain)
     * creates all the contexts and sets the handlers as empty command objects
     */
    public void run() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(portNumber), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        httpServer.setExecutor(null);

        // for swagger page:
        httpServer.createContext("/docs/api/data", new Handlers.JSONAppender(""));
        httpServer.createContext("/docs/api/view", new Handlers.BasicFile(""));
        ///////

        // user contexts:
        httpServer.createContext("/user/login", new LoginCommand());
        httpServer.createContext("/user/register", new RegisterCommand());

        // games (multiple) contexts:
        httpServer.createContext("/games/list", new GameListCommand());
        httpServer.createContext("/games/create", new GameCreateCommand());
        httpServer.createContext("/games/join", new GameJoinCommand());
        httpServer.createContext("/games/save", new GameSaveCommand());
        httpServer.createContext("/games/load", new GameLoadCommand());

        // game contexts:
        httpServer.createContext("/game/model", new FetchNewModelCommand());
        httpServer.createContext("/game/reset", new GameResetCommand());
        httpServer.createContext("/game/commands", new GetGameCommandsCommand());
        httpServer.createContext("/game/addAI", new AddAICommand());
        httpServer.createContext("/game/listAI", new ListAICommand());

        // moves contexts:
        httpServer.createContext("/moves/sendChat", new SendChatCommand());
        httpServer.createContext("/moves/rollNumber", new RollDiceCommand());
        httpServer.createContext("/moves/robPlayer", new RobPlayerCommand());
        httpServer.createContext("/moves/finishTurn", new FinishTurnCommand());
        httpServer.createContext("/moves/buyDevCard", new PurchaseDevCardCommand());
        httpServer.createContext("/moves/Year_of_Plenty", new PlayYearOfPlentyCommand());
        httpServer.createContext("/moves/Road_Building", new PlayRoadBuilderCommand());
        httpServer.createContext("/moves/Soldier", new PlaySoldierCommand());
        httpServer.createContext("/moves/Monopoly", new PlayMonopolyCommand());
        httpServer.createContext("/moves/Monument", new PlayMonumentCommand());
        httpServer.createContext("/moves/buildRoad", new BuildRoadCommand());
        httpServer.createContext("/moves/buildSettlement", new BuildSettlementCommand());
        httpServer.createContext("/moves/buildCity", new BuildCityCommand());
        httpServer.createContext("/moves/offerTrade", new OfferTradeCommand());
        httpServer.createContext("/moves/acceptTrade", new AcceptTradeCommand());
        httpServer.createContext("/moves/maritimeTrade", new MaritimeTradeCommand());
        httpServer.createContext("/moves/discardCards", new DiscardCommand());

        System.out.println("*SERVER: contexts created");

        //try
        UserManager.getInstance();  //make it build the mock users by calling its constructor
        GamesManager.getInstance(); //make it build the mock games by callings its constructor
//load game (will have at least the defaults)
//        PersistenceManager.getInstance().loadAllUsers();
//        PersistenceManager.getInstance().loadAllGames();
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
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
