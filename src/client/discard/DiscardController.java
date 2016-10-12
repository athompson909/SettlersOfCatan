package client.discard;

import client.ClientFacade;
import client.ClientUser;
import shared.definitions.*;
import client.base.*;
import client.misc.*;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.DiscardCommand;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.Observable;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController {

	private ResourceList discardList = new ResourceList();
    private ResourceList resources;
    private int amount = 0;
    private int total = 6;
	private IWaitView waitView;
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		this.waitView = waitView;
	}

	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}

	@Override
	public void increaseAmount(ResourceType resource) {
        updateAmount(1);

		if(resource.toString().equals("WOOD")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, true, true);
			discardList.incWoodCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWoodCardCount());
            if(discardList.getWoodCardCount() == resources.getWoodCardCount()){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, false, true);
            }
		}else if(resource.toString().equals("BRICK")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, true, true);
			discardList.incBrickCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getBrickCardCount());
            if(discardList.getBrickCardCount() == resources.getBrickCardCount()){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, false, true);
            }
		}else if(resource.toString().equals("SHEEP")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, true);
			discardList.incSheepCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getSheepCardCount());
            if(discardList.getSheepCardCount() == resources.getSheepCardCount()){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, false, true);
            }
		}else if(resource.toString().equals("WHEAT")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, true);
			discardList.incWheatCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWheatCardCount());
            if(discardList.getWheatCardCount() == resources.getWheatCardCount()){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, false, true);
            }
		}else if(resource.toString().equals("ORE")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, true, true);
			discardList.incOreCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getOreCardCount());
            if(discardList.getOreCardCount() == resources.getOreCardCount()){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, false, true);
            }
		}
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
        updateAmount(-1);
        //I am assuming if they ever decrease it could not be at the amount needed
        getDiscardView().setDiscardButtonEnabled(false);

		if(resource.toString().equals("WOOD")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, true, true);
			discardList.decWoodCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWoodCardCount());
            if(discardList.getWoodCardCount() == 0){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, true, false);
            }
		}else if(resource.toString().equals("BRICK")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, true, true);
			discardList.decBrickCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getBrickCardCount());
            if(discardList.getBrickCardCount() == 0){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, true, false);
            }
		}else if(resource.toString().equals("SHEEP")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, true);
			discardList.decSheepCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getSheepCardCount());
            if(discardList.getSheepCardCount() == 0){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, false);
            }
		}else if(resource.toString().equals("WHEAT")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, true);
			discardList.decWheatCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWheatCardCount());
            if(discardList.getWheatCardCount() == 0){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, false);
            }
		}else if(resource.toString().equals("ORE")){
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, true, true);
			discardList.decOreCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getOreCardCount());
            if(discardList.getOreCardCount() == 0){
                getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, true, false);
            }
		}
	}

	@Override
	public void discard() {
		DiscardCommand command = new DiscardCommand(ClientUser.getInstance().getIndex(), discardList);
		ClientFacade.getInstance().discardCards(command);
		//todo open waiting modal? do I want to use the state pattern here
        //getWaitView().setMessage("Please wait while others finish discarding");
		getDiscardView().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
	//todo check if the player needs to discard or wait for others to discard
        ClientModel model = (ClientModel)o;

        //save current players resource list
        Player player = model.getCurrentPlayer();
        resources = player.getPlayerResourceList();
        total = resources.getCardCount()/2;//purposely using integer division

    //check if this player needs to discard or wait for others to discard
        TurnTracker tracker = model.getTurnTracker();
        if(tracker.getStatus().equals("Discarding")){
            //I need to discard
            if(resources.getCardCount() > 7){
                setDiscardModalValues();
            }else {//others are discarding
                getWaitView().showModal();
                //todo how do I close this modal when it is no longer discarding?
                //idea - close the modal if not equal to "Discarding"
            }
        }
	}

	private void setDiscardModalValues(){
        //set Max discard values
        getDiscardView().setResourceMaxAmount(ResourceType.WOOD, resources.getWoodCardCount());
        getDiscardView().setResourceMaxAmount(ResourceType.BRICK, resources.getBrickCardCount());
        getDiscardView().setResourceMaxAmount(ResourceType.SHEEP, resources.getSheepCardCount());
        getDiscardView().setResourceMaxAmount(ResourceType.WHEAT, resources.getWheatCardCount());
        getDiscardView().setResourceMaxAmount(ResourceType.ORE, resources.getOreCardCount());

        //set appropriate up arrows
        if(resources.getWoodCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, true, false);
        }
        if(resources.getBrickCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, true, false);
        }
        if(resources.getSheepCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, false);
        }
        if(resources.getWheatCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, false);
        }
        if(resources.getOreCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, true, false);
        }

        //set disabled button
        amount = 0;
        getDiscardView().setStateMessage("Discard: " + amount + "/" + total);
        getDiscardView().setDiscardButtonEnabled(true);

    }

    public void updateAmount(int num){
        amount += num;
        getDiscardView().setStateMessage("Discard: " +amount + "/" + total);
        if(amount == total) {//needed discard amount reached
            //enable button
            getDiscardView().setDiscardButtonEnabled(true);
        }
    }

}

