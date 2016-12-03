package db_tests;

import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.SQLPlugin;
import server.plugins.database_related.DBCreateHelper;
import shared.shared_utils.MockJSONs;

/**
 * Created by adamthompson on 12/2/16.
 */
public class SQLPluginTest extends TestCase {


    SQLPlugin sqlPlugin = new SQLPlugin();

    public void testCreateNewDatabase() {
        DBCreateHelper.createNewDatabase();

        int gameID = 0;
        String modelJSON = MockJSONs.GAME_MODEL;
        String gameInfoJSON = MockJSONs.GAME_CREATE_RESPONSE;

        sqlPlugin.writeNewGame(gameID, modelJSON, gameInfoJSON);
    }


    public void testWriteNewGame() {

        int gameID = 1;
        String modelJSON = MockJSONs.GAME_MODEL;
        String gameInfoJSON = MockJSONs.GAME_CREATE_RESPONSE;

        sqlPlugin.writeNewGame(gameID, modelJSON, gameInfoJSON);

    }

    public void testWriteGame() {

//        sqlPlugin.writeGame(0, "update1", "gameInfoUpdate1");
        int gameID = 0;
        String modelJSON = MockJSONs.GAME_MODEL;
        String gameInfoJSON = MockJSONs.GAME_CREATE_RESPONSE;

        SQLPlugin.writeNewGame(gameID, modelJSON, gameInfoJSON);

    }


    public void testReadAllGames() {
        sqlPlugin.writeGame(0, "model1", "gameInfo1");
        sqlPlugin.writeGame(1, "model2", "gameInfo2");

        JSONArray jsonArray = sqlPlugin.readAllGames();
        System.out.println(jsonArray.toString());
    }


    public void testWriteUser() {
        String json = "{\"userID\":0,\"username\":\"adam\",\"password\":\"adam\"}";
        sqlPlugin.writeUser(new JSONObject(json));

        JSONArray jsonArray = sqlPlugin.readAllUsers();
        System.out.println(jsonArray.toString());

    }
}
