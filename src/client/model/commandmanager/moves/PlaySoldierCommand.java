package client.model.commandmanager.moves;

import client.model.commandmanager.BaseCommand;
import client.model.map.HexLocation;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlaySoldierCommand extends BaseCommand {

    /**
     * Index of player placing the robber
     */
    int playerIndex;
    /**
     * Location of hex where robber is being placed
     */
    HexLocation robberLoc;
    /**
     * PlayerIndex of player being robbed
     */
    int victimInd;

    /**
     * Creates a PlaySoldierCommand object to be sent to client.ClientFacade
     * for translating into JSON
     *
     * sets data members
     *
     * @param index
     * @param robberLocation
     * @param victimIndex
     */
    public PlaySoldierCommand(int index, HexLocation robberLocation, int victimIndex){
        playerIndex = index;
        robberLoc = robberLocation;
        victimInd = victimIndex;
    }

    /**
     * Calls all necessary model update functions
     *
     * Calls Map robber update functions to reset location of the robber
     * Calls Player update functions to reflect changes in hands
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
