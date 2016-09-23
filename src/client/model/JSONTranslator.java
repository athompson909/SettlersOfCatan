package client.model;

import com.google.gson.*;
import org.json.*;

/**
 * JSONTranslator gets the new model from the server as a huge string, converts it to a JSONObject,
 * uses GSON to break it down into objects, then sends those new objects to the
 * ClientUpdateManager to be distributed to the existing ClientModel objects.
 *
 *
 * ***If I understand right, each of the BaseCommandObjs have the ability to translate their own unique
 * server requests/commands to JSON before they are sent to the server. If that's not how we want to do it
 * I can totally create translating functions here though! :)
 *
 * Created by Sierra on 9/22/16.
 */
public class JSONTranslator {

    /**
     * Constructor
     */
    public void JSONTranslator(){

    }


    /**
     * TranslateModel() can take in either a string or a JSONObject, I can't remember which we decided,
     * and uses Gson to convert it to normal objects to send to UpdateManager.
     *
     * @param newModel - this is the huge JSON string/object coming back directly from the server
     * @return true if it all  worked, false otherwise
     */
    public boolean translateModel(JSONObject newModel){


        // Gson functionality:
        //Gson gsonTest = new Gson();
        //ClientModel cmTarget = gsonTest.fromJson(testResponseBody, ClientModel.class);

        return true;
    }


    /**
     *
     */
    public void sendToUpdateManager(){

    }

}
