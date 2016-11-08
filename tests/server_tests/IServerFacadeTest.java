package server_tests;

import junit.framework.TestCase;
import server.IServerFacade;

/**
 * Created by adamthompson on 11/5/16.
 */
public class IServerFacadeTest extends TestCase {

    public void testListAI() {

        String[] response = IServerFacade.getInstance().listAI(0);
    }
}
