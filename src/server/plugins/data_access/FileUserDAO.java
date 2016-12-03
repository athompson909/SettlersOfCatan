package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by adamthompson on 11/29/16.
 */
public class FileUserDAO implements IUserDAO {

    private static String baseUsersFilePath = "./json_files/users/";


    /**
     * Adds a new user.
     */
    @Override
    public void writeUser(JSONObject userJSON) {

        String filePath = "";


        try {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
           // bw.write(gameJSONArr.toString()); //really big
            bw.close();
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println(">FILEUSERDAO: writeGame(): File not found " + fnf);
            return; //??
        }
        catch(IOException ioe)
        {
            System.out.println(">FILEUSERDAO: writeGame(): Error while writing to GAME file: " + ioe);
            return; //??
        }

    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    @Override
    public JSONArray readAllUsers() {
        return null;
    }
}
