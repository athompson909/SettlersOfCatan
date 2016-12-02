package server;

import client.data.GameInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.IPersistenceProvider;
import shared.model.ClientModel;
import shared.model.commandmanager.BaseCommand;

import java.util.HashMap;

/**
 * Created by adamthompson on 11/29/16.
 */
public class PersistenceManager {
    String persistenceType;
//Singleton
    private static PersistenceManager instance = new PersistenceManager();
    public static PersistenceManager getInstance(){
        return instance;
    }
    private PersistenceManager() {

    }
//Persistence provider
    private IPersistenceProvider persistenceProvider;


    public void writeGame(int gameID){
        String gameInfoJSON = getGameInfoJSON(gameID);
        String modelJSON = getModelJSON(gameID);
        persistenceProvider.writeGame(gameID, modelJSON, gameInfoJSON);
    }

    //We probably don't really need this function -Sierra
    public void writeNewGame(String name, int gameID){
        String gameInfoJSON = getGameInfoJSON(gameID);
        String modelJSON = getModelJSON(gameID);
        persistenceProvider.writeNewGame(gameID, modelJSON, gameInfoJSON);
    }

    public void clearCommands(int gameID){
        persistenceProvider.clearCommands(gameID);
    }

    public void writeCommand(int gameID, BaseCommand command){
        String commandJSON = ServerTranslator.getInstance().commandObjectToJSON(command);
        JSONObject commandJSONobject = new JSONObject(commandJSON);
        persistenceProvider.writeCommand(commandJSONobject, gameID);
    }

    public void writeUser(User user){
        JSONObject userJSON = ServerTranslator.getInstance().userToJSON(user);
        persistenceProvider.writeUser(userJSON);
    }

    private String getGameInfoJSON(int gameID){
        Game game = GamesManager.getInstance().getGame(gameID);
        GameInfo info = game.getGameInfo();
        return ServerTranslator.getInstance().gameInfoToJSON(info);
    }

    private String getModelJSON(int gameID){
        Game game = GamesManager.getInstance().getGame(gameID);
        ClientModel model = game.getClientModel();
        return ServerTranslator.getInstance().modelToJSON(model);
    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    public void loadAllUsers(){
        JSONArray userJSON = persistenceProvider.readAllUsers();
        HashMap<Integer, User> allUsers = ServerTranslator.getInstance().userJSONtoHashMap(userJSON);
        UserManager.getInstance().setAllUsers(allUsers);
    }

    /**
     *
     */
    public void loadAllGames(){
        JSONArray allGames = persistenceProvider.readAllGames();
        HashMap<Integer, Game> games = ServerTranslator.getInstance().gamesFromJSON(allGames);
        GamesManager.getInstance().setAllGames(games);
    }

    /**
     * Clears all the data.
     */
    public void clearAllData(){}

    public void setPersistenceType(String type) {
          persistenceType = type;
        Class c = null;
        try {
            c = Class.forName(type);
            persistenceProvider = (IPersistenceProvider)c.newInstance();
        }catch (ClassNotFoundException e) {
            System.out.println("Persistence Provider class name incorrect");
        }catch (InstantiationException e) {
            System.out.println("Instantiation Exception");
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            System.out.println("Illegal Access Exception");
            e.printStackTrace();
        }
    }
}
