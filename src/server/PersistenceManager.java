package server;

import client.data.GameInfo;
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
//    private static IPersistenceProvider persistenceProvider;
//
////    public static void setPersistenceProvider(IPersistenceProvider provider){
////        persistenceProvider = provider;
////    }
////
////    public static IPersistenceProvider getPersistenceProvider(){
////        return persistenceProvider;
////    }

    public void writeGame(int gameID){
        String gameInfoJSON = getGameInfoJSON(gameID);
        String modelJSON = getModelJSON(gameID);
        //todo - see if I can do this
       // persistenceProvider.writeGame(gameID, modelJSON, gameInfoJSON);
    }

    public void writeNewGame(String name, int gameID){
        String gameInfoJSON = getGameInfoJSON(gameID);
        String modelJSON = getModelJSON(gameID);
 //       persistenceProvider.writeNewGame(gameID, name, modelJSON, gameInfoJSON);
    }

    public void clearCommands(int gameID){
       // persistenceProvider.clearCommands(gameID);
    }

    public void writeCommand(int gameID, BaseCommand command){
//        String commandJSON = ServerTranslator.getInstance().commandToJSON(command);
//        persistenceProvider.writeCommand(gameID, commandJSON);
    }

    public void writeUser(int userID, String username, String password){
        //persistenceProvider.writeUser(userID, username, password);
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
//        String userJSON = persistenceProvider.readAllUsers();
//        HashMap<Integer, User> allUsers = ServerTranslator.getInstance().userJSONtoHashMap(userJSON);
//        UserManager.getInstance().setAllUsers(allUsers);
    }

    /**
     *
     */
    public void loadAllGames(){
//        String allGames = persistenceProvider.readAllGames();
//        HashMap<Integer, Game> games = ServerTranslator.getInstance().JSONtoGames(allGames);
//        GamesManager.getInstance().setAllGames(games);
    }

    /**
     * Clears all the data.
     */
    public void clearAllData(){}

    public void setPersistenceType(String type) {
          persistenceType = type;
//        try {
//            persistenceProvider = Class.forName(type);
//        }catch (ClassNotFoundException e) {
//            System.out.println("Persistence Provider class name incorrect");
//        }
    }
}
