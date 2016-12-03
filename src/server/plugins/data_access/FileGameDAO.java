package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

/**
 *
 *
 * Created by adamthompson on 11/29/16.
 */
public class FileGameDAO implements IGameDAO {

    private static String baseGamesFilePath = "./json_files/games/";


    /**
     * Constructor
     */
    public FileGameDAO(){}

    /**
     * Overwrites the clientModel inside the file corresponding to gameJSON's gameID.
     * Called when the server needs to save the ClientModel checkpoint!
     * THIS OVERWRITES THE EXISTING FILE CONTENT
     *
     *
     * @param
     */
    @Override
    public void writeGame(int gameID, String modelJSON, String gameInfoJSON) {

        //make a JSONArray using modelJSON and gameInfoJSON
        JSONArray gameJSONArr = new JSONArray();
        JSONObject tempModelJSON = new JSONObject(modelJSON);
        JSONObject tempGIJSON = new JSONObject(gameInfoJSON);
        gameJSONArr.put(tempModelJSON);
        gameJSONArr.put(tempGIJSON);
        //now gameJSONArr should have the modelJSON in spot 0, and the gameInfo in spot 1

        //open a writeStream and overwrite the contents of the file with gameJSONArr
        String fileName = "game" + gameID + ".json";
        String filePath = baseGamesFilePath + fileName;

        try {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gameJSONArr.toString()); //really big
            bw.close();
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println(">FILEGAMEDAO: writeGame(): File not found " + fnf);
            return; //??
        }
        catch(IOException ioe)
        {
            System.out.println(">FILEGAMEDAO: writeGame(): Error while writing to GAME file: " + ioe);
            return; //??
        }
    }

    /**
     * Adds a new game. we may be able to combine this function with WriteGame
     * @param
     */
    @Override
    public void writeNewGame(int newGameID, String modelJSON, String gameInfoJSON) {

        //make a JSONArray using modelJSON and gameInfoJSON
        JSONArray newGameJSONArr = new JSONArray();
            JSONObject tempModelJSON = new JSONObject(modelJSON);
            JSONObject tempGIJSON = new JSONObject(gameInfoJSON);
        newGameJSONArr.put(tempModelJSON);
        newGameJSONArr.put(tempGIJSON);
        //now newGameJSONArr should have the modelJSON in spot 0, and the gameInfo in spot 1

        //use the gameID to build a filename like: game#.json
        //make a new file with that filename
        //open a fileOutputStream and add the contents of the file with gameJSON
        //newGameJSONArr should be an array with 1 items: #0 is the clientModel as JSON, #1 is the gameInfo

        //get gameID and make a filename with that
        String newGameFileName = "game" + newGameID + ".json";
        String newGameFilePath = baseGamesFilePath + newGameFileName;

        try {
            FileWriter fw = new FileWriter(newGameFilePath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(newGameJSONArr.toString()); //really big
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println(">FILEGAMEDAO: writeNewGame(): File not found/couldn't create GAME file " + fnf);
            return; //??
        }
        catch(IOException ioe)
        {
            System.out.println(">FILEGAMEDAO: writeNewGame(): Error while writing to GAME file: " + ioe);
            return; //??
        }

    }


    /**
     * Reads all the games created by scanning the default file location for game files: /json_files/games
     *
     *  we should also get the list of commands and add it as a 3rd entry to the JSONArray.
     *  but then execute them all in sequence after adding to the gamesManager.
     *
     * @return a JSONArray with all the created games. Each entry in that JSONArray is another JSONArray with 3 entries:
     *              one for the game's clientModel, one for the game's gameInfo, and one for the game's list of commands.
     */
    @Override
    public JSONArray readAllGames() {

        JSONArray allGamesJSONArray = new JSONArray();

        // To read all the games, we have to read every file in the /json_files/games folder.
        //so we'll need to get every file in the games folder and read it into a JSONObject individually.
        // Then put each of those JSONObjects in the collector JSONArray, which is what we return after all files are read.

        //for loop through all game filenames and read in each one. when one doesn't open, you've read all the files.
        int currGameID = 0;
        String currGameFileName = "game0.json"; //start at 0
        String currGameFilePath = baseGamesFilePath + currGameFileName;
        String currCmdsFileName = "";
        String currCmdsFilePath = "";

        //try reading the first GAME file:
        JSONArray gameEntryJSONArr = readGame(currGameFilePath);
        // the result of that first file read is saved to the collector JSONArr in the first loop iteration IF IT WORKED:
        while (gameEntryJSONArr != null){  //if the first read isn't 0, there is at least 1 game file in there.

            //if readGame returns a JSONArray (not null), we found a game file for currGameID.

            //if you found a gameFile, you should also be able to get the corresponding cmdsFile.
            // Get the contents of that cmdsFile to add to this game entry:
            currCmdsFileName = "cmds" + currGameID + ".json";
            currCmdsFilePath = baseGamesFilePath + currCmdsFileName;
            JSONArray readCmdsResultJSONArr = readGameCommands(currCmdsFilePath);
            //add readCmdsResultJSONArr as a part of the gameEntryJSONArr. it's the last part we need to represent a complete game

            gameEntryJSONArr.put(readCmdsResultJSONArr); //gameEntryJSONArr should now have 3 entries

            //add completed game entry to the list of all games
            allGamesJSONArray.put(gameEntryJSONArr);

            //now try the next file - there's no way the user can ever delete a game from the Client side, or the server side.
            //so the games should never be out of order.
            currGameID++;
            currGameFileName = "game" + currGameID + ".json";
            currGameFilePath = baseGamesFilePath + currGameFileName; //this should be overwritten with the new filename
            //let readGame() try reading this new filename:
            gameEntryJSONArr = readGame(currGameFilePath);
        }

        //if the collector JSONArr is size 0 here, no files were read, not even the first one.
        if (allGamesJSONArray.length() == 0){
            System.out.println(">FILEGAMEDAO; readAllGames: looks like no game files were read... ");
        }

        return allGamesJSONArray; //whoever gets this result should plan for it to be size 0
    }

    /**
     * Given a file path, reads a single game#.json file which contains a JSONArray
     *  holding the game's ClientModel JSON and a GameInfo about that game.
     *  Returns the whole file read in as a JSONArray.
     *
     *  Called repeatedly by readAllGames() while looping through all available game files.
     *
     * @param gameFilePath path to the file to be read in
     * @return JSONArray representing the ClientModel and GameInfo of a game.
     */
    public JSONArray readGame(String gameFilePath){

        String allJSON = "";

        JSONArray gameJSONArr = null;

        try {
                System.out.println(">>FILEGAMEDAO: readGame(): attempting to read file " + gameFilePath);

            FileInputStream fis = new FileInputStream(gameFilePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            InputStreamReader isr = new InputStreamReader(bis);
            Scanner scanner = new Scanner(isr);

            while (scanner.hasNextLine()){
                allJSON += scanner.nextLine(); //this just gets is in one line
            }

                System.out.println(">>FILEGAMEDAO: readGame(): file read ok!");

            scanner.close();

            gameJSONArr = new JSONArray(allJSON); //this should have 2 entries, one of the clientModel and one of the gameInfo

        }
        catch(FileNotFoundException fnf) {
                System.out.println(">FILEGAMEDAO: readGame(): Unable to open file " + gameFilePath);
           // fnf.printStackTrace();
           // return null;
        }
//        catch(IOException io) {
//                System.out.println(">FILEGAMEDAO: readGame(): Error while reading file " + gameFilePath);
//            io.printStackTrace();
//          //  return null;
//        }


        //if null is returned here, then the file read-in didn't work.
        // So, either that file must not exist or it has an error.
        //if it doesn't exist, then we reached the end of the files list.

        return gameJSONArr;

    }



    /**
     * Adds a new command to the corresponding cmds#.
     * @param commandJSON The type of command.
     * @param
     */
    @Override
    public void writeCommand(JSONObject commandJSON, int gameID) {

        //int gameID = commandJSON.getInt("gameId");  //because we're not 100% sure that the command obj will have the gameID.
        String cmdJSONString = commandJSON.toString();
        String newCmdsFileName = "cmds" + gameID + ".json";
        String newCmdsFilePath = baseGamesFilePath + newCmdsFileName;
        File cmdsFile = new File(newCmdsFilePath);

        try {
            FileWriter fw = new FileWriter(cmdsFile, true);  //true = append if it exists already, create new file otherwise
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cmdJSONString);
            bw.newLine(); //try and see if this helps with reading the commands back in from the file separately
            bw.close();
            System.out.println(">FILEGAMEDAO: writeCmd(): done writing command to file " + newCmdsFilePath);
        }
        catch(IOException ioe)
        {
            System.out.println(">FILEGAMEDAO: writeCmd(): Error while writing to CMD file: " + ioe);
            //return;
        }
    }


    /**
     * When the server is restarting from the file plugin and importing all the games,
     * it needs to be able to read in and execute all the commands saved in the game files' corresponding cmds files
     * to bring the game models back up to speed.
     *
     * So, this function might be called by the PersistenceManager after the Games have been saved to the GamesManager
     * and are ok to be modified by calling commands on them.
     *
     * I don't think we should execute the commands until the Game is safely put away inside the GamesManager.
     *
     * Currently the commands are being appended to the file without being enclosed in a JSONArray.
     * So when we read them in, we need to somehow break them into individual commands and put() them in a JSONArray so
     * they can be executed more easily.
     * either that or every time we add a new command to the file, read the whole thing back in as a JSONarray,
     * add the new command to it, and rewrite the whole thing. That's pretty dumb but it could definitely work.
     *
     *
     * I made it so it adds a newline after each written command! so that could help with reading them back in individually.
     *
     *
     * @return
     */
    public JSONArray readGameCommands(String cmdsFilePath){

        JSONArray allCommands = new JSONArray();

        String currCmd = "";

        try {
            System.out.println(">>FILEGAMEDAO: readGameCmds(): attempting to read file " + cmdsFilePath);

            FileInputStream fis = new FileInputStream(cmdsFilePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            InputStreamReader isr = new InputStreamReader(bis);
            Scanner scanner = new Scanner(isr);

            while (scanner.hasNextLine()){
                currCmd = scanner.nextLine(); //this is going to get one command at a time.
                //make a JSONobject out of it and add it to the JSONArray of all commands
                JSONObject currCmdJSON = new JSONObject(currCmd);
                allCommands.put(currCmdJSON);
            }

                System.out.println(">>FILEGAMEDAO: readGameCmds(): file read ok! allCmds has " + allCommands.length() + " cmds");

            scanner.close();

        }
        catch (FileNotFoundException fnf){
            System.out.println(">FILEGAMEDAO: readGameCmds(): Unable to open file " + cmdsFilePath);
        }

        //allCommands is now going to be added to the gameEntry as its third part
        return allCommands;
    }

}
