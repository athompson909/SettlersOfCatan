package client.points;

import client.ClientUser;
import client.base.*;
import shared.model.ClientModel;
import shared.model.player.Player;

import java.util.Observable;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController {

	private IGameFinishedView finishedView;

	private ClientModel clientModel;
	
	/**
	 * PointsController constructor
	 * 
	 * @param view Points view
	 * @param finishedView Game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		
		super(view);
		
		setFinishedView(finishedView);
		
		initFromModel();
	}
	
	public IPointsView getPointsView() {
		
		return (IPointsView)super.getView();
	}
	
	public IGameFinishedView getFinishedView() {
		return finishedView;
	}
	public void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	private void initFromModel() {
		getPointsView().setPoints(0);
	}

	@Override
	public void update(Observable o, Object arg) {
		clientModel = (ClientModel)o;
		getPointsView().setPoints(clientModel.getClientPlayer().getVictoryPoints());

		//check for winner
		int winnerID = clientModel.getWinner();
		Player[] players = clientModel.getPlayers();
		if(players[3] != null){
			int winner = -1;
			for(int i = 0; i < players.length; i++){
				if(players[i].getPlayerID()== winnerID){
					winner = i;
					break;
				}
			}
			if(winner >= 0){
				boolean winnerIsMe = (winner == ClientUser.getInstance().getIndex());
				getFinishedView().setWinner(clientModel.getPlayers()[winner].getName(), winnerIsMe);
				getFinishedView().showModal();
			}
		}
	}
	
}

