package client.model.commandmanager.moves;

import client.model.commandmanager.BaseCommand;
import client.model.map.HexLocation;

/**
 * Created by Alise on 9/18/2016.
 */
public class RobPlayerCommand extends BaseCommand {
    /**
     * int 0-3 of player
     */
    private int playerIndex;

    /**
     * location to move Robber to
     */
    private HexLocation location;

    /**
     * int 0-3 of player to rob; -1 if nobody
     */
    private int victimIndex;

    /**
     * Creates RobPlayerCommand to send to client.ClientFacade. Sets data members
     * @param playerIndex
     * @param location
     * @param victimIndex
     */
    public RobPlayerCommand(int playerIndex, HexLocation location, int victimIndex){

    }

    /**
     * Tells server to move resource from victim to robbing players hand
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
