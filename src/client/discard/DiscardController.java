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
    private int amount;
    private int total;
	private IWaitView waitView;
    private boolean discarded = false;
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		this.waitView = waitView;

        //set arrows to false
        reset();
	}

	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}

	@Override
	public void increaseAmount(ResourceType resource) {

		if(resource.toString().equals("WOOD")){
			discardList.incWoodCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWoodCardCount());
		}else if(resource.toString().equals("BRICK")){
			discardList.incBrickCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getBrickCardCount());
		}else if(resource.toString().equals("SHEEP")){
			discardList.incSheepCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getSheepCardCount());
		}else if(resource.toString().equals("WHEAT")){
			discardList.incWheatCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWheatCardCount());
		}else if(resource.toString().equals("ORE")){
			discardList.incOreCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getOreCardCount());
		}
        updateButton();
		setArrows();
	}

	@Override
	public void decreaseAmount(ResourceType resource) {

		if(resource.toString().equals("WOOD")){
			discardList.decWoodCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWoodCardCount());
		}else if(resource.toString().equals("BRICK")){
			discardList.decBrickCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getBrickCardCount());
		}else if(resource.toString().equals("SHEEP")){
			discardList.decSheepCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getSheepCardCount());
		}else if(resource.toString().equals("WHEAT")){
			discardList.decWheatCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getWheatCardCount());
		}else if(resource.toString().equals("ORE")){
			discardList.decOreCardCount(1);
			getDiscardView().setResourceDiscardAmount( resource, discardList.getOreCardCount());
		}
        updateButton();
		setArrows();
	}

	@Override
	public void discard() {
        getDiscardView().closeModal();
		DiscardCommand command = new DiscardCommand(ClientUser.getInstance().getIndex(), discardList);
		ClientFacade.getInstance().discardCards(command);
        discarded = true;
        //set values to prep for next time
        reset();
	}

	@Override
	public void update(Observable o, Object arg) {
        ClientModel model = (ClientModel)o;

        //save current players resource list
        Player player = model.getCurrentPlayer();
        resources = player.getPlayerResourceList();
        total = resources.getCardCount()/2;//purposely using integer division

    //check if this player needs to discard or wait for others to discard
        TurnTracker tracker = model.getTurnTracker();

        if (tracker.getStatus() != null){
            if(tracker.getStatus().equals("Discarding")){

                //I need to discard
                if(resources.getCardCount() > 7 && !model.getCurrentPlayer().hasDiscarded() && !discarded){
                        System.out.println("open Discard Modal");
                        setDiscardModalValues();
                        getDiscardView().showModal();
                }else if(!getWaitView().isModalShowing()){//others are discarding
                    getWaitView().showModal();
                }
            }else if (getWaitView().isModalShowing()) {
                getWaitView().closeModal();  //TEST CMDLINE
                discarded = false;
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

        //set button
        updateButton();

        setArrows();
    }

    public void updateButton(){
        amount = discardList.getCardCount();
        getDiscardView().setStateMessage("Discard: " +amount + "/" + total);
        if(amount == total) {//needed discard amount reached
            //enable button
            getDiscardView().setDiscardButtonEnabled(true);
        }else {
            getDiscardView().setDiscardButtonEnabled(false);
        }
    }

    public void setArrows(){
        //allow increases only if more need to be discarded
        boolean up = !(amount == total);

        //allow decreases only if the count is above zero
        boolean woodDown = (discardList.getWoodCardCount() > 0);
        boolean brickDown = (discardList.getBrickCardCount() > 0);
        boolean sheepDown = (discardList.getSheepCardCount() > 0);
        boolean wheatDown = (discardList.getWheatCardCount() > 0);
        boolean oreDown = (discardList.getOreCardCount() > 0);

		//allow decreases only if the count is above zero
		boolean woodUp = (discardList.getWoodCardCount() < resources.getWoodCardCount());
		boolean brickUp = (discardList.getBrickCardCount() < resources.getBrickCardCount());
		boolean sheepUp = (discardList.getSheepCardCount() < resources.getSheepCardCount());
		boolean wheatUp = (discardList.getWheatCardCount() < resources.getWheatCardCount());
		boolean oreUp = (discardList.getOreCardCount() < resources.getOreCardCount());

        //set appropriate arrows
        if(resources.getWoodCardCount() > 0 ) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, (up&&woodUp), woodDown);
        }
        if(resources.getBrickCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, (up&&brickUp), brickDown);
        }
        if(resources.getSheepCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, (up&&sheepUp), sheepDown);
        }
        if(resources.getWheatCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, (up&&wheatUp), wheatDown);
        }
        if(resources.getOreCardCount() > 0) {
            getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, (up&&oreUp), oreDown);
        }
    }

    public void reset() {
        //set arrows to false
        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, false, false);
        getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, false, false);
        getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, false, false);
        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, false, false);
        getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, false, false);

        discardList = new ResourceList();
        getDiscardView().setResourceDiscardAmount(ResourceType.WOOD, 0);
        getDiscardView().setResourceDiscardAmount(ResourceType.BRICK, 0);
        getDiscardView().setResourceDiscardAmount(ResourceType.SHEEP, 0);
        getDiscardView().setResourceDiscardAmount(ResourceType.WHEAT, 0);
        getDiscardView().setResourceDiscardAmount(ResourceType.ORE, 0);
    }

}

