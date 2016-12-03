package db_tests;

import junit.framework.TestCase;
import server.plugins.SQLPlugin;
import server.plugins.database_related.DBCreateHelper;
import shared.shared_utils.MockJSONs;

/**
 * Created by adamthompson on 12/2/16.
 */
public class SQLPluginTest extends TestCase {


    public void testCreateNewDatabase() {
        DBCreateHelper.createNewDatabase();
    }


    public void testWriteNewGame() {

        int gameID = 0;
        String modelJSON = MockJSONs.GAME_MODEL;
        String gameInfoJSON = MockJSONs.GAME_CREATE_RESPONSE;

       // SQLPlugin.getInstance().writeNewGame(gameID, modelJSON, gameInfoJSON);

    }

}
