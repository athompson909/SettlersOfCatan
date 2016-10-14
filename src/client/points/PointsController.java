package client.points;

import client.ClientUser;
import client.base.*;
import shared.model.ClientModel;

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
		//getPointsView().setPoints(clientModel.getCurrentPlayer().getVictoryPoints());
	}

	@Override
	public void update(Observable o, Object arg) {
		clientModel = (ClientModel) o;
		getPointsView().setPoints(((ClientModel) o).getCurrentPlayer().getVictoryPoints());
	}
	
}

