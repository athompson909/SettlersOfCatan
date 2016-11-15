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
import shared.model.turntracker.TurnTrackerState;

import java.util.Observable;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController {

	private CatanColor localPlayerColor;
	private boolean initialized = false;
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
			localPlayerColor = model.getClientPlayer().getColor();
			getView().setLocalPlayerColor(localPlayerColor);
		}

		Player[] players = model.getPlayers();
		if(players[3] != null) {//all players are ready

			//initialize players
			if(!initialized) {
				initialized = true;
				for (int i = 0; i < players.length; i++) {
					if(players[i].getName() != null) {
						Player player = players[i];
						getView().initializePlayer(player.getPlayerIndex(), player.getName(), player.getColor());
						// todo: revise... I'm setting this to only work if players[i] != null
						if (i == ClientUser.getInstance().getIndex()) {
							getView().setLocalPlayerColor(players[i].getColor());
						}
					}
				}
			}else {
				TurnTrackerView view = (TurnTrackerView)getView();
				//TODO: This line below was somehow preventing the other controllers from getting called (as if it was crashing the program)
				//view.updateColors(players);
			}

			TurnTracker turnTracker = model.getTurnTracker();
			int turn = turnTracker.getCurrentTurn();

			//update game state
			TurnTrackerState state = Client.getInstance().getState();
			state.updateStateButton(getView());

			//update player info
			int longestRoad = turnTracker.getLongestRoadHolder();
			int largestArmy = turnTracker.getLargestArmyHolder();
			for (int i = 0; i < players.length; i++) {
				Player player = players[i];
				if (player != null) {
					boolean highlight = (i == turn);
					boolean road = (i == longestRoad);
					boolean army = (i == largestArmy);
					//TODO: This line below was somehow preventing the other controllers from getting called (as if it was crashing the program)
					//getView().updatePlayer(player.getPlayerIndex(), player.getVictoryPoints(), highlight, army, road);
				}
			}
		}
	}

}

