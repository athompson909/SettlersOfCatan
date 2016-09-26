package shared.model.commandmanager.moves;

import shared.locations.HexLocation;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class RobPlayerCommand implements BaseCommand {
    /**
     * int 0-3 of player
     */
    private int playerIndex;

    /**
     * roadLocation to move Robber to
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
        this.playerIndex = playerIndex;
        this.location = location;
        this.victimIndex = victimIndex;
    }

    /**
     * Tells server to move resource from victim to robbing players hand
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
