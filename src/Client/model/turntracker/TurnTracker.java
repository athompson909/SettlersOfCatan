package Client.model.turntracker;

/**
 * TurnTracker keeps track of game flow, phases of gameplay, player turns, and certain stats
 * related to player performance, i.e. the holder of the Longest Road and Largest Army distinctions.
 *
 */
public class TurnTracker {

    /**
     * Represents the player whose turn it is
     */
    private int currentTurn;

    /**
     * Represents which phase the game is currently in: Rolling, Robbing,
     * Discarding, Trading, Building, GameSetup
     */
    private String status;

    /**
     * Represents the PlayerID of player with the current longest road
     */
    private int longestRoadHolder;

    /**
     * Represents the PlayerID of player with the current largest army
     */
    private int largestArmyHolder;

    /**
     * Determines if any player needs to discard cards on a 7
     *
     * Retrieves the card count of each player and sets the game
     * to 'Discard Phase' until the appropriate players have discarded
     */
    private void countCardsInHand() {

    }

    /**
     * Compares lengths of all roads in play to find the longest
     *
     * Assigns the extra two points and the Longest Road badge
     * to the player who currently has the longest road
     *
     * @return PlayerID of player with the longest road currently in play
     */
    private int countLongestRoad() {
        return -1;
    }

    /**
     * Compares army sizes of all players to find the largest
     *
     * Assigns the extra two points and the Largest Army badge
     * to the player who currently has the largst army
     *
     * @return PlayerID of player with the largest army currently in play
     */
    private int calcLargestArmy() {
        return -1;
    }

    /**
     * Updates game when the status changes
     *
     * Updates currentTurn to represent the current player and
     * updates status to represent the current status
     */
    private void onStatusChange() {

    }
}
