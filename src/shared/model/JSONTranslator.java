package shared.model;

import com.google.gson.*;
import org.json.JSONObject;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;

/**
 * JSONTranslator gets the new model from the server as a huge string, converts it to a JSONObject,
 * uses GSON to break it down into objects, then sends those new objects to the
 * ClientUpdateManager to be distributed to the existing ClientModel objects.
 *
 * There needs to be a function for every BaseCommand to be translated to JSON. JSON>BaseCommandObj isn't necessary,
 * we just need a way for the ClientFacade to change its BaseCommand objs to JSON readable by the server.
 *
 *
 * Created by Sierra on 9/22/16.
 */
public class JSONTranslator {

    /**
     * This is the temporary ClientModel that will be created from the JSON coming back from the server.
     * We will give this object to ClientUpdateManager after it has been fully parsed out.
     */
    private ClientModel newClientModel = new ClientModel();

    /**
     * This Gson object can be reused as many times as you want
     */
    private Gson gsonConverter = new Gson();

    /**
     * Constructor
     */
    public void JSONTranslator(){}


    /**
     * TranslateModel() can take in either a string or a JSON,
     * and uses Gson to convert it to normal objects to send to UpdateManager.
     * But first convert the incoming JSONObject to a String so Gson is happy!
     *
     * @throws JsonSyntaxException - this is Gson's exception
     * @param newModelJSON - this is the huge JSON string coming back directly from the server
     * @return newClientModel - the new ClientModel object created from the server's response JSON
     */
    public ClientModel translateModel(JSONObject newModelJSON) throws JsonSyntaxException {

        String modelJSONString = newModelJSON.toString();

        //this fromJson() takes in a JSON string and the template class, and returns a complete ClientModel object
        newClientModel = gsonConverter.fromJson(modelJSONString, ClientModel.class);

        return newClientModel;
    }


    /**
     *
     * @param num
     * @return
     */
    public JSONObject translateModelVersionNumber(int num)
    {
        return null;
    }


    //COMMAND OBJECT TRANSLATORS ==================================================

    public JSONObject translateAddAICommand(AddAICommand addAICommandObj)
    {
        return null;
    }





}
