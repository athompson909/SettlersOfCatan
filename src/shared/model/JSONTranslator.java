package shared.model;

import com.google.gson.*;

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
     * Constructor
     */
    public void JSONTranslator(){}


    /**
     * TranslateModel() can take in either a string or a JSON,
     * and uses Gson to convert it to normal objects to send to UpdateManager.
     * The HTTP Request body comes back as JSON, so I'm keeping the param as Gson's JsonElement type for now
     *
     * this function might need to throw an exception, maybe JsonSyntaxException like Gson.fromJson() does
     *
     * @param newModelJSON - this is the huge JSON string/object coming back directly from the server
     * @return newClientModel - the new ClientModel object created from the server's response JSON
     */
    public ClientModel translateModel(JsonElement newModelJSON) throws Exception {

        //this Gson object can be reused as many times as you want
        Gson gsonTest = new Gson();
        newClientModel = gsonTest.fromJson(newModelJSON, ClientModel.class);

        return newClientModel;
    }

    /**
     *
     * @param num
     * @return
     */
    public JsonElement translateModelVersionNumber(int num)
    {
        return null;
    }


}
