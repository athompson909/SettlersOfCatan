package server;

import client.data.GameInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.IPersistenceProvider;
import shared.model.ClientModel;
import shared.model.JSONTranslator;
import shared.model.commandmanager.BaseCommand;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by adamthompson on 11/29/16.
 */
public class PersistenceManager {
    //just for translating the list of command objects
    JSONTranslator jsonTranslator = new JSONTranslator();
    JSONObject pluginListObj = null;
//Singleton
    private static PersistenceManager instance = new PersistenceManager();
    public static PersistenceManager getInstance(){
        return instance;
    }
    private PersistenceManager() {
        parseConfig("./config.json");
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

    public void writeCommand(BaseCommand command, int gameID){
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
        System.out.println(">PERSISTENCEMGR: loading all users");

        JSONArray usersJSON = persistenceProvider.readAllUsers();
        HashMap<Integer, User> allUsers = ServerTranslator.getInstance().usersFromJSON(usersJSON);
        UserManager.getInstance().setAllUsers(allUsers);

        System.out.println(">PERSISTENCEMGR: all users loaded!");
    }

    /**
     * Reads all the game files.
     * Saves each Game to the GamesManager.
     * Then calls loadAndExecModelCmds() to bring all the game models up to date.
     */
    public void loadAllGames(){
        System.out.println(">PERSISTENCEMGR: loading all games");

        JSONArray allGames = persistenceProvider.readAllGames();
        HashMap<Integer, Game> games = ServerTranslator.getInstance().gamesFromJSON(allGames);
        GamesManager.getInstance().setAllGames(games);

        //NOW you can execute all the commands!
        loadAndExecModelCommands();
    }

    /**
     * helper fn for loadAllGames()
     * After all the games have been read in and given to the GamesManager, we need to read in all the commands
     * and execute them all on their respective clientModels.
     *
     * loop through each file and read it in just like in readAllGames. Get a JSONArray of commands for each one.
     * Send each games' cmdsListJSON through the JSONTranslator to get its personal list of BaseCommands.
     * Add that list of commands to its respective game's commandManager.
     * Call reExecute on that commandManager.
     * Model should now be up to date!
     */
    public void loadAndExecModelCommands(){
        System.out.println(">PERSISTENCEMGR: loading/executing all extra game commands");
        JSONArray allGameCmds = persistenceProvider.readAllCommands();

        //get/execute one game's worth of commands at a time
        for (int i = 0; i < allGameCmds.length(); i++){  //use i as the curr gameID
            JSONArray currGameCmdsArr = allGameCmds.getJSONArray(i);
            //translate this list so this game's commandManager can use it
            List<BaseCommand> currGameCmdsToExec = jsonTranslator.commandsListFromJSON(currGameCmdsArr);
            Game currGame = GamesManager.getInstance().getGame(i); //by now these better not be null

            currGame.commandManager.setExecutedCommands(currGameCmdsToExec);
            //it would be good if each command's reExec() fn return a boolean whether it worked or not.
            currGame.commandManager.reExecuteCommands(i);

            //now this model should be up to date according to its cmds file.
        }

        //all models have been re-executed
        System.out.println(">PERSISTENCEMGR: all read models are up to date!");
    }

    /**
     * Clears all the data.
     */
    public void clearAllData(){
        System.out.println(">PERSISTENCEMGR: clearing all data");

        persistenceProvider.clearAllData();
    }

    public void parseConfig(String filePath) {
        String allJSON = "";
        JSONObject jsonObj = null;

        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            InputStreamReader isr = new InputStreamReader(bis);
            Scanner scanner = new Scanner(isr);

            while(scanner.hasNextLine()) {
                allJSON += scanner.nextLine();
            }
            scanner.close();


            jsonObj = new JSONObject(allJSON);
            pluginListObj = jsonObj.getJSONObject("types");
        }
        catch(FileNotFoundException fnf) {
            System.out.println("PARSE CONFIG method Unable to find file");
        }

    }

    public void setPersistenceType(String type) {

        System.out.println(">PERSISTENCEMGR: setPersistenceType to " + type);

        JSONObject plugin = pluginListObj.getJSONObject(type);
        Class c = null;
        try {
        //    URL url = new URL("file", "localhost:", plugin.getString("path"));
        //    URL[] myURLArray = {url};
        //    ClassLoader loader = new URLClassLoader(myURLArray);//   loader.loadClass(plugin.getString("name"));

            c = Class.forName(plugin.getString("path"));
            persistenceProvider = (IPersistenceProvider) c.newInstance();
      //  }catch (MalformedURLException e) {
      //      System.out.println("Malformed URL");
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
