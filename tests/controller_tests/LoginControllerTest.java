package controller_tests;

import client.Client;
import junit.framework.TestCase;

import java.util.regex.Pattern;

/**
 * Created by adamthompson on 10/5/16.
 *
 * our client must be running
 */
public class LoginControllerTest extends TestCase {

    public void testRegex() {
        Pattern delimiter = Client.getInstance().getUsernameDelimiter();

        String test1 = "a1a1a1a1a1";//10 chars
        String test2 = "aaaaaaa{";
        String test3 = "aaaaaaaaaaaaaaaaaaaaaaaaa";//25 chars

        assertEquals(true, delimiter.matcher(test1).matches());
        assertEquals(false, delimiter.matcher(test2).matches());
        assertEquals(false, delimiter.matcher(test3).matches());
    }
}
