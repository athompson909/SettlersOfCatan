package client.turntracker;

import client.ClientFacade;
import client.utils.ClientUser;
import shared.definitions.CatanColor;
import client.base.*;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.FinishTurnCommand;
import shared.model.player.Player;
import shared.model.turntracker.TurnTracker;

import java.util.Observable;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController {

	public TurnTrackerController(ITurnTrackerView view) {
		
		super(view);
		
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		FinishTurnCommand command = new FinishTurnCommand(ClientUser.getInstance().getIndex());
		ClientFacade.getInstance().finishTurn(command);
	}
	
	private void initFromModel() {
		//<temp>
		getView().setLocalPlayerColor(CatanColor.RED);
		//</temp>
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientModel model = (ClientModel)o;
		TurnTracker turnTracker = model.getTurnTracker();
		int turn = turnTracker.getCurrentTurn();
		int longestRoad = turnTracker.getLongestRoadHolder();
		int largestArmy = turnTracker.getLargestArmyHolder();
		Player[] players = model.getPlayers();
		for(int i = 0; i < players.length; i++){
			Player player = players[i];
			boolean highlight = (i == turn);
			boolean road = (i == longestRoad);
			boolean army = (i == largestArmy);
			getView().updatePlayer(player.getPlayerIndex(), player.getVictoryPoints(), highlight, army, road);
		}
	}

}

