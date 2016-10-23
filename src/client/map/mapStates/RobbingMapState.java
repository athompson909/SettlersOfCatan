package client.map.mapStates;

import client.Client;
import client.ClientFacade;
import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.commandmanager.moves.PlaySoldierCommand;
import shared.model.commandmanager.moves.RobPlayerCommand;
import shared.model.map.Map;

/**
 * Used when you roll a 7 or play a soldier card
 * Created by Alise on 10/8/2016.
 */
public class RobbingMapState extends MapState {
    private HexLocation robberHex;

    public RobbingMapState(MapController mapController) {
        super(mapController);
    }

    @Override
    public void initFromModel(Map updatedMap) {
        super.initFromModel(updatedMap);

        //TODO: Only do this if it is your turn?
        CatanColor color = mapController.clientModel.getCurrentPlayer().getColor();
        mapController.getView().startDrop(PieceType.ROBBER, color, true);
    }
@Override
    public void placeRobber(HexLocation hexLoc) {
        System.out.println("MAP: PLACEROBBER");
        robberHex = hexLoc;

        RobPlayerInfo[] victims = mapController.clientModel.calculateRobPlayerInfo(hexLoc);

        if(victims.length > 0){
            mapController.getRobView().setPlayers(victims);
            mapController.getRobView().showModal(); //This shows the counters for how many cards possible players have.
        } else {
            //Don't rob anyone, so send a command with -1.
            int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
            RobPlayerCommand robPlayerCommand = new RobPlayerCommand(currentPlayerId, robberHex, -1);
            ClientFacade.getInstance().robPlayer(robPlayerCommand);
        }
    }

@Override
    public void robPlayer(RobPlayerInfo victim) {
        System.out.println("MAP: ROB PLAYER!");

        //Why does the ROb PLayer command need a hex and victim? Where do I get the hex from?
        int currentPlayerId = mapController.clientModel.getCurrentPlayer().getPlayerIndex();
        RobPlayerCommand robPlayerCommand = new RobPlayerCommand(currentPlayerId, robberHex, victim.getPlayerIndex());
        ClientFacade.getInstance().robPlayer(robPlayerCommand);

    }
}
