package client.resources;

import client.ClientUser;
import client.base.Controller;
import client.base.IAction;
import shared.model.ClientModel;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceList;
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
	//Todo - check if we need the if statements that are commented out or if we don't because they are disabled
	public ResourceBarController(IResourceBarView view) {

		super(view);
		
		elementActions = new HashMap<ResourceBarElement, IAction>();

		//disable everything
		getView().setElementEnabled(ResourceBarElement.ROAD,false);
		getView().setElementEnabled(ResourceBarElement.SETTLEMENT,false);
		getView().setElementEnabled(ResourceBarElement.CITY,false);
		getView().setElementEnabled(ResourceBarElement.BUY_CARD,false);
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
		//if(model.canPurchaseRoad(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.ROAD);
		//}
	}

	@Override
	public void buildSettlement() {
		//if(model.canPurchaseSettlement(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.SETTLEMENT);
		//}
	}

	@Override
	public void buildCity() {
		//if(model.canPurchaseCity(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.CITY);
		//}
	}

	@Override
	public void buyCard() {
		//if(model.canPurchaseDevCard(ClientUser.getInstance().getIndex())) {
			executeElementAction(ResourceBarElement.BUY_CARD);
		//}
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
			if(model.canPurchaseSettlement(ClientUser.getInstance().getIndex())) {
				getView().setElementEnabled(ResourceBarElement.SETTLEMENT,true);
			}else{
				getView().setElementEnabled(ResourceBarElement.SETTLEMENT,false);
			}
			if(model.canPurchaseCity(ClientUser.getInstance().getIndex())) {
				getView().setElementEnabled(ResourceBarElement.CITY,true);
			}else{
				getView().setElementEnabled(ResourceBarElement.CITY,false);
			}
			if(model.canPurchaseDevCard(ClientUser.getInstance().getIndex())) {
				getView().setElementEnabled(ResourceBarElement.BUY_CARD, true);
			}else {
				getView().setElementEnabled(ResourceBarElement.BUY_CARD, false);
			}
			//Note: we leave PLAY_CARD enabled and then it will disable within the modal itself
		}else{
			//disable everything
			getView().setElementEnabled(ResourceBarElement.ROAD,false);
			getView().setElementEnabled(ResourceBarElement.SETTLEMENT,false);
			getView().setElementEnabled(ResourceBarElement.CITY,false);
			getView().setElementEnabled(ResourceBarElement.BUY_CARD,false);
			//Note: we leave PLAY_CARD enabled and then it will disable within the modal itself
		}
		//update player values
		Player player = model.getCurrentPlayer();
		ResourceList resources = player.getPlayerResourceList();
		getView().setElementAmount(ResourceBarElement.WOOD, resources.getWoodCardCount());
		getView().setElementAmount(ResourceBarElement.BRICK, resources.getBrickCardCount());
		getView().setElementAmount(ResourceBarElement.SHEEP, resources.getSheepCardCount());
		getView().setElementAmount(ResourceBarElement.WHEAT, resources.getWheatCardCount());
		getView().setElementAmount(ResourceBarElement.ORE, resources.getOreCardCount());
		getView().setElementAmount(ResourceBarElement.ROAD, player.getRoadCount());
		getView().setElementAmount(ResourceBarElement.SETTLEMENT, player.getSettlementCount());
		getView().setElementAmount(ResourceBarElement.CITY, player.getCityCount());
		getView().setElementAmount(ResourceBarElement.SOLDIERS, player.getSoldiersPlayed());


//		//TODO: Delete THIS IS TEMPORARY SO WE CAN TEST MAP FUNCTIONS
//		getView().setElementEnabled(ResourceBarElement.ROAD,true);
//		getView().setElementEnabled(ResourceBarElement.SETTLEMENT,true);
//		getView().setElementEnabled(ResourceBarElement.CITY,true);
//		getView().setElementEnabled(ResourceBarElement.BUY_CARD,true);
	}
}

