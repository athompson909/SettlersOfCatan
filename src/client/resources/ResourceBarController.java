package client.resources;

import client.ClientUser;
import client.base.Controller;
import client.base.IAction;
import shared.model.ClientModel;
import shared.model.player.Player;
import shared.model.turntracker.TurnTracker;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;


/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController {

	private Map<ResourceBarElement, IAction> elementActions;
	private ClientModel model;
	private boolean turn;
	
	public ResourceBarController(IResourceBarView view) {

		super(view);
		
		elementActions = new HashMap<ResourceBarElement, IAction>();
	}

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView)super.getView();
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is clicked by the user
	 * 
	 * @param element The resource bar element with which the action is associated
	 * @param action The action to be executed
	 */
	public void setElementAction(ResourceBarElement element, IAction action) {

		elementActions.put(element, action);
	}

	@Override
	public void buildRoad() {
		if(model.canPurchaseRoad(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.ROAD);
		}
	}

	@Override
	public void buildSettlement() {
		if(model.canPurchaseSettlement(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.SETTLEMENT);
		}
	}

	@Override
	public void buildCity() {
		if(model.canPurchaseCity(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.CITY);
		}
	}

	@Override
	public void buyCard() {
		if(model.canPurchaseDevCard(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.BUY_CARD);
		}
	}

	@Override
	public void playCard() {
		executeElementAction(ResourceBarElement.PLAY_CARD);
	}
	
	private void executeElementAction(ResourceBarElement element) {
		
		if (elementActions.containsKey(element)) {
			
			IAction action = elementActions.get(element);
			action.execute();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		model = (ClientModel)o;
		//todo add state checking
		TurnTracker tracker = model.getTurnTracker();
		//check if it is this players turn and enable or disable appropriately
		if(tracker.getCurrentTurn() == ClientUser.getInstance().getIndex()){
			//enable things that can be clicked on
			if(model.canPurchaseRoad(ClientUser.getInstance().getIndex())) {
				getView().setElementEnabled(ResourceBarElement.ROAD,true);
			}else{
				getView().setElementEnabled(ResourceBarElement.ROAD,false);
			}
		}else{
			//disable everything
			getView().setElementEnabled(ResourceBarElement.ROAD,false);
			getView().setElementEnabled(ResourceBarElement.SETTLEMENT,false);
			getView().setElementEnabled(ResourceBarElement.CITY,false);
			getView().setElementEnabled(ResourceBarElement.BUY_CARD,false);
			getView().setElementEnabled(ResourceBarElement.PLAY_CARD,false);
		}
		//todo reset numbers
		//Player player = model.getCurrentPlayer();
		//getView().setElementAmount(ResourceBarElement.SOLDIERS, );
	}
}

