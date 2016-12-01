package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

/**
 * Created by adamthompson on 11/29/16.
 */
public class FileGameDAO implements IGameDAO {

    private static String baseGamesFilePath = "./json_files/games/";  //i have no idea if the ./ will get it to the right folder
    private static String baseUsersFilePath = "./json_files/users/";

    /**
     * Overwrites the clientModel inside the file corresponding to gameJSON's gameID.
     * @param gameJSON JSON with the game info.
     */
    @Override
    public void writeGame(JSONArray gameJSON) {

        //open a writeStream and overwrite the contents of the file with gameJSON
        //gameJSON should be an array with 1 items: #0 is the clientModel as JSON, #1 is the gameInfo


    }

    /**
     * Adds a new game.
     * @param gameJSON JSON with the new game info.
     */
    @Override
    public void writeNewGame(JSONArray gameJSON) {

        //use the gameID to build a filename like: game#.json
        //open a writeStream and add the contents of the file with gameJSON
        //gameJSON should be an array with 1 items: #0 is the clientModel as JSON, #1 is the gameInfo


    }

    /**
     * Adds a new command.
     * @param commandJSON The type of command.
     */
    @Override
    public void writeCommand(JSONObject commandJSON) {

    }


    /**
     * Reads all the games created.
     *
     * @return a JSONArray with all the created games. Each entry in the JSONArray is another JSONArray with 2 entries:
     *              one for the game's clientModel, and one for the game's gameInfo.
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

        //try reading the first file:
        JSONArray readGameResultJSONArr = readGame(currGameFilePath);
        // the result of that first file read is saved to the collector JSONArr in the first loop iteration IF IT WORKED:
        while (readGameResultJSONArr != null){

            //if readGame returns a JSONArray, add it as an entry to allGamesJSONArr.



            //try the next file:
            currGameID++;
            currGameFileName = "game" + currGameID + ".json";
        }

        //if the collector JSONArr is size 0 here, no files were read, not even the first one.
        if (allGamesJSONArray.length() == 0){
            System.out.println(">FILEGAMEDAO; readAllGames: looks like no game files were read... ");
        }


        return allGamesJSONArray;
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
                //add currline just read to the collector string. we'll convert it into a JSONObject after it's all read.
                allJSON += scanner.nextLine();
            }

                System.out.println(">>FILEGAMEDAO: readGame(): file read good! got allJSON= " + allJSON);

            gameJSONArr = new JSONArray(allJSON);

        }
        catch(FileNotFoundException fnf) {
                System.out.println(">FILEGAMEDAO: readGame(): Unable to open file " + gameFilePath);
           // fnf.printStackTrace();
           // return null;
        }
        catch(IOException io) {
                System.out.println(">FILEGAMEDAO: readGame(): Error while reading file " + gameFilePath);
            io.printStackTrace();
          //  return null;
        }


        //if null is returned here, then the file read-in didn't work.
        // So, either that file must not exist or it has an error.
        //if it doesn't exist, then we reached the end of the files list.

        return gameJSONArr;

    }


}
