package client.discard;

import client.ClientFacade;
import client.ClientUser;
import shared.definitions.*;
import client.base.*;
import client.misc.*;
import shared.model.commandmanager.moves.DiscardCommand;
import shared.model.resourcebank.ResourceList;

import java.util.Observable;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController {
	
	private ResourceList discardList = new ResourceList();
//Todo create checking for max amounts and make sure they don't go below zero
//Todo check if they have enough things selected so we can allow them to click the discard button
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
	}

	@Override
	public void discard() {
		DiscardCommand command = new DiscardCommand(ClientUser.getInstance().getIndex(), discardList);
		ClientFacade.getInstance().discardCards(command);
		//todo open waiting modal?
		getDiscardView().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
	//todo check if the player needs to discard or wait for others to discard
	}

}

