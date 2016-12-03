package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

/**
 * Created by adamthompson on 11/29/16.
 */
public class FileUserDAO implements IUserDAO {

    //there's only one file to hold all the users. so we only ever need to make one file for the whole server: users.json
    private static String usersFilePath = "./json_files/users/users.json";


    /**
     * Adds a new user.
     */
    @Override
    public void writeUser(JSONObject userJSON) {

        try {
            FileWriter fw = new FileWriter(usersFilePath, true); //this path should always be the same. append to it if it exists
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(userJSON.toString());
            bw.newLine();  //probably helpful for reading them back in later
            bw.close();

                System.out.println(">FILEUSERDAO: writeUser(): done writing user to file " + usersFilePath);

        }
        catch(FileNotFoundException fnf)
        {
            System.out.println(">FILEUSERDAO: writeUser(): File not found " + fnf);
        }
        catch(IOException ioe)
        {
            System.out.println(">FILEUSERDAO: writeUser(): Error while writing to USER file: " + ioe);
        }
    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    @Override
    public JSONArray readAllUsers() {

        //the path to the users file will never change!

        JSONArray allUsers = new JSONArray();

        try {
            System.out.println(">>FILEUSERDAO: readAllUsers(): attempting to read file " + usersFilePath);

            FileInputStream fis = new FileInputStream(usersFilePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            InputStreamReader isr = new InputStreamReader(bis);
            Scanner scanner = new Scanner(isr);

            String currUser = "";

            while (scanner.hasNextLine()){
                currUser = scanner.nextLine(); //this is going to get one User at a time.
                //make a JSONObject out of it and add it to the JSONArray of all user
                JSONObject currUserJSON = new JSONObject(currUser);
                allUsers.put(currUserJSON);
            }

            System.out.println(">>FILEUSERDAO: readAllUsers(): file read ok! allUsers has " + allUsers.length() + " cmds");

            scanner.close();

        }
        catch (FileNotFoundException fnf){
            System.out.println(">FILEUSERDAO: readAllUsers(): Unable to open file " + usersFilePath);
        }

        return allUsers;
    }
}
