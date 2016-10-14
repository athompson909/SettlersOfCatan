package client.turntracker;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
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

	private CatanColor localPlayerColor;
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
		getView().setLocalPlayerColor(CatanColor.WHITE);
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientModel model = (ClientModel)o;
		if(localPlayerColor == null){
			localPlayerColor = model.getCurrentPlayer().getColor();
			getView().setLocalPlayerColor(localPlayerColor);
		}
		//TODO: Feel free to uncomment this if you're editing it. Now that update is getting called, it crashes here.
		TurnTracker turnTracker = model.getTurnTracker();
		int turn = turnTracker.getCurrentTurn();

		//update game state
		IState state = Client.getInstance().updateGameState();
		state.updateStateButton(getView());
		/*
		//update player info
		int longestRoad = turnTracker.getLongestRoadHolder();
		int largestArmy = turnTracker.getLargestArmyHolder();
		Player[] players = model.getPlayers();
		for(int i = 0; i < players.length; i++){
			Player player = players[i];
			boolean highlight = (i == turn);
			boolean road = (i == longestRoad);
			boolean army = (i == largestArmy);
			getView().updatePlayer(player.getPlayerIndex(), player.getVictoryPoints(), highlight, army, road);
		}*/
	}

}

