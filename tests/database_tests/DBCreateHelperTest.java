package database_tests;

import junit.framework.TestCase;

/**
 * Created by adamthompson on 11/30/16.
 */
public class DBCreateHelperTest extends TestCase {

    public void testCreateDatabase() {

        DBCreateHelper.createNewDatabase("catan.db");
    }
}