package shared.model;

import com.google.gson.*;

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
     * this function might need to throw an exception, maybe JsonSyntaxException like Gson.fromJson() does?
     *
     * We need to discuss how the ClientFacade is giving this JsonElement to JSONTranslator in the first place!
     * JUST AN IDEA: ******
     * What if JSONTranslator is a personal data member of ClientUpdateManager, and it gets the new JsonElement
     * response body thing from the ClientUpdateManager? Then it would be easy for CUM to delegateUpdates() using
     * JSONTranslator's finished object.
     *
     * @param newModelJSON - this is the huge JSON string/object coming back directly from the server
     * @return true if it all  worked, false otherwise
     */
    public boolean translateModel(JsonElement newModelJSON) {

        //this Gson object can be reused as many times as you want
        Gson gsonTest = new Gson();
        try {
            newClientModel = gsonTest.fromJson(newModelJSON, ClientModel.class);
        } catch (Exception e) {
            System.out.println("\n >JSONTranslator: translateModel: There was a problem in fromJson()! ********");
            return false;
        }

        System.out.println(">Just deserialized newModelJSON!");

        return true;
    }


    /**
     *
     */
    public void sendToUpdateManager(){

    }

}
