package shared.model.dicemanager;

import java.util.Random;

/**
 *
 * DiceManager simulates rolling two independent dice,
 * and is used at the start of every player's turn during normal play.
 *
 * Created by Sierra on 9/18/16.
 */
public class DiceManager {

    /**
     * The first randomly generated value, within the range [1-6], simulating a real dice roll
     */
    private int dice1Roll = 0;

    /**
     * The second randomly generated value, within the range [1-6], simulating a real dice roll
     */
    private int dice2Roll = 0;

    /**
     * RollDice creates two random ints between 1-6 (inclusive) and
     * adds them both together to get a final roll number.
     * This simulates rolling two individual dice and keeps the
     * same probabilities of rolling numbers as those in the real game.
     *
     * @pre the ClientModel's status is "Rolling", and it is your turn (the player using the DiceManager)
     * @post the ClientModel's status is now "Discarding", "Robbing", or "Playing"
     * @return the combined rolled numbers of both "dice"
     */
    public int rollDice(){

        // dice1Roll = rand() from 1-6
        Random rand = new Random();
        dice1Roll = rand.nextInt(6) + 1;
        // dice2Roll = rand() from 1-6
        dice2Roll = rand.nextInt(6) + 1;
        return dice1Roll + dice2Roll;
    }

}
