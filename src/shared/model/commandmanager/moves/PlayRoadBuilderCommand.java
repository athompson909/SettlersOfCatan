package shared.model.commandmanager.moves;

import shared.locations.EdgeLocation;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class PlayRoadBuilderCommand implements BaseCommand {

    /**
     * Index of player playing roadBuilding card
     */
    int playerIndex;
    /**
     * First roadLocation where player is building road
     */
    EdgeLocation locationONE;
    /**
     * Second roadLocation where player is building road
     */
    EdgeLocation locationTWO;

    /**
     * Creates PlayRoadBuilderCommand object to be sent to client.ClientFacade
     * to be translated into JSON
     *
     * sets data members
     *
     * @param playerIndex
     * @param edgeLocation1
     * @param edgeLocation2
     */
    public PlayRoadBuilderCommand(int playerIndex, EdgeLocation edgeLocation1, EdgeLocation edgeLocation2){
        this.playerIndex = playerIndex;
        locationONE = edgeLocation1;
        locationTWO = edgeLocation2;
    }

    /**
     * Calls all necessary model update methods
     *
     * Calls Map update methods to reflect to new roads of the
     * specified colr
     * Calls Player update methods to reflect decremented road count
     * Calls TurnTracker to recount and reassign longestRoad
     *
     * @param BC
     */
    @Override
    public void serverExec(BaseCommand BC) {

    }
}
