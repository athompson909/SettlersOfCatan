package server_tests;

import junit.framework.TestCase;
import org.junit.Test;
import server.PersistenceManager;

/**
 * Created by macbook on 12/5/16.
 */
public class PersistenceManagerTest extends TestCase {

    PersistenceManager persistenceMgr;  //it's a singleton

    @Test
    public void setUp() throws Exception {
        super.setUp();

    }


    @Test
    public void testLoadAllGames() throws Exception {
        //this should make it read all the games files, translate/save them to the GamesManager,
        //then read all the cmds files and execute all cmds on their respective Game.
        //so after this finishes, all game models should be saved and up to date.

        persistenceMgr = persistenceMgr.getInstance();
        persistenceMgr.setPersistenceType("text");

        persistenceMgr.loadAllGames();

    }

}
