package shared.model.turntracker;

import client.Client;
import client.ClientUser;
import com.google.gson.annotations.SerializedName;
import shared.definitions.State;

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
    @SerializedName("longestRoad")
    private int longestRoadHolder;

    /**
     * Represents the PlayerID of player with the current largest army
     */
    @SerializedName("largestArmy")
    private int largestArmyHolder;

    /**
     * Constructor
     */
    public TurnTracker(){

    }



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

    public void updateTurnTracker(TurnTracker newTurnTracker) {
        setCurrentTurn(newTurnTracker.getCurrentTurn());
        setStatus(newTurnTracker.getStatus());
        setLongestRoadHolder(newTurnTracker.getLongestRoadHolder());
        setLargestArmyHolder(newTurnTracker.getLargestArmyHolder());
        State state = updateState(newTurnTracker);
        Client.getInstance().setGameState(state);
    }

     public State updateState(TurnTracker tracker){
        String status = tracker.getStatus();
        boolean myTurn = (tracker.getCurrentTurn() == ClientUser.getInstance().getIndex());
        if(status.equals("Rolling")){
            if(myTurn){
                return State.ROLLING;
            }else{
                return State.WAITING;
            }
        }else if(status.equals("Playing")){
            if(myTurn){
                return State.PLAYING;
            }else{
                return State.WAITING;
            }
        }else if(status.equals("Robbing")){
            if(myTurn){
                return State.ROBBING;
            }else{
                return State.WAITING;
            }
        }else if(status.equals("Discarding")){
            //todo
            //did they already discard?
            //do they need to discard?
            return State.DISCARDING;
        }else if(status.equals("FirstRound")){
            //todo -figure out what needs to be different about first and secondRound states
            if(myTurn){
                return State.FIRSTROUND;
            }else{
                return State.WAITING;
            }
        }else if(status.equals("SecondRound")){
            if(myTurn){
                return State.SECONDROUND;
            }else{
                return State.WAITING;
            }
        }
        return null;
    }




    //GETTERS
    public int getCurrentTurn() {return currentTurn;}
    public String getStatus() {return status;}
    public int getLongestRoadHolder() {return longestRoadHolder;}
    public int getLargestArmyHolder() {return largestArmyHolder;}

    //SETTERS
    public void setCurrentTurn(int newCurrentTurn) {currentTurn = newCurrentTurn;}
    public void setStatus(String newStatus) {status = newStatus;}
    public void setLongestRoadHolder(int newLRH) {longestRoadHolder = newLRH;}
    public void setLargestArmyHolder(int newLAH) {largestArmyHolder = newLAH;}


    @Override
    public String toString() {
        return "TurnTracker{" +
                "currentTurn=" + currentTurn +
                ", status='" + status + '\'' +
                ", longestRoadHolder=" + longestRoadHolder +
                ", largestArmyHolder=" + largestArmyHolder +
                '}';
    }
}
