package client_tests;

import junit.framework.TestCase;
import shared.model.dicemanager.DiceManager;

/**
 * Created by Alise on 9/24/2016.
 */
public class DiceManagerTest extends TestCase {
    private DiceManager manager = new DiceManager();

    public void testRollDice() {
        int number = manager.rollDice();
        assert(number >= 1);
        assert(number <= 12);
        System.out.print(number);
    }
}
