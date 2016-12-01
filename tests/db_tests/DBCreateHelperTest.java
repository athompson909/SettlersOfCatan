package db_tests;

import junit.framework.TestCase;
import server.plugins.database_related.DBCreateHelper;


/**
 * Created by adamthompson on 11/30/16.
 */
public class DBCreateHelperTest extends TestCase {

    public void testCreateDatabase() {
        DBCreateHelper.createNewDatabase("catan.db");
    }
}
