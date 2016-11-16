package shared.model.commandmanager.game;

import client.data.GameInfo;
import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.commandmanager.BaseCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameCreateCommand extends BaseCommand {
    /**
     * name of game
     */
    private String name;

    /**
     * true if client wants tiles randomly placed, false if they want the default
     */
    private boolean randomTiles;

    /**
     * true if client wants numbers randomly placed, false if they want the default
     */
    private boolean randomNumbers;

    /**
     * true if client wants ports randomly placed, false if they want the default
     */
    private boolean randomPorts;

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final transient String type = "createGame";

    /**
     * Creates empty GameCreateCommand
     */
    public GameCreateCommand() {
    }

    /**
     * Creates GameCreateCommand to pass to the client.ClientFacade. Sets data members.
     * @param name
     * @param randomTiles
     * @param randomNumbers
     * @param randomPorts
     */
    public GameCreateCommand(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts ){
        this.name = name;
        this.randomTiles = randomTiles;
        this.randomNumbers = randomNumbers;
        this.randomPorts = randomPorts;
    }

    /**
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return getCookieJSONOnlyLogin();
    }


    /**
     * Tells the server to create new game
     */
    @Override
    public String serverExec() {

        System.out.print("GameCreateCommand -> ServerExecute()");
        JSONObject requestJSON = new JSONObject(getRequest());
        name = requestJSON.getString("name");
        randomTiles = requestJSON.getBoolean("randomTiles");
        randomNumbers = requestJSON.getBoolean("randomNumbers");
        randomPorts = requestJSON.getBoolean("randomPorts");

        GameInfo gameInfo = IServerFacade.getInstance().create(getUserId(), this);
        String fullResponseLoginCookieStr = "catan.game="+IServerFacade.getInstance().getGameId()+";Path=/;";
        List<String> cookieList = new ArrayList<>(1);
        cookieList.add(fullResponseLoginCookieStr);
        getHttpExchange().getResponseHeaders().put("Set-cookie", cookieList);
        return ServerTranslator.getInstance().gameInfoToJSON(gameInfo);
    }

    //Getters
    public String getName() {
        return name;
    }

    public boolean isRandomTiles() {
        return randomTiles;
    }

    public boolean isRandomNumbers() {
        return randomNumbers;
    }

    public boolean isRandomPorts() {
        return randomPorts;
    }

    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        return "GameCreateCommand{" +
                "name='" + name + '\'' +
                ", randTiles=" + randomTiles +
                ", randNumbers=" + randomNumbers +
                ", randPorts=" + randomPorts +
                '}';
    }
}
