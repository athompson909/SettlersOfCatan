package client.devcards;

import client.ClientFacade;
import client.utils.ClientUser;
import shared.definitions.ResourceType;
import client.base.*;
import shared.model.commandmanager.moves.PlayMonopolyCommand;
import shared.model.commandmanager.moves.PlayMonumentCommand;
import shared.model.commandmanager.moves.PlayYearOfPlentyCommand;
import shared.model.commandmanager.moves.PurchaseDevCardCommand;

import java.util.Observable;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	
	/**
	 * DevCardController constructor
	 * 
	 * @param view "Play dev card" view
	 * @param buyCardView "Buy dev card" view
	 * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
	 * @param roadAction Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView, 
								IAction soldierAction, IAction roadAction) {

		super(view);
		
		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView)super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {
		
		getBuyCardView().showModal();
	}

	@Override
	public void cancelBuyCard() {
		
		getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {
		PurchaseDevCardCommand command = new PurchaseDevCardCommand(ClientUser.getInstance().getIndex());
		ClientFacade.getInstance().purchaseDevCard(command);
		getBuyCardView().closeModal();
	}

	@Override
	public void startPlayCard() {
		
		getPlayCardView().showModal();
	}

	@Override
	public void cancelPlayCard() {

		getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		PlayMonopolyCommand command = new PlayMonopolyCommand(ClientUser.getInstance().getIndex(), resource);
		ClientFacade.getInstance().playMonopoly(command);
		//Todo: should I close the modal after I call the facade?
	}

	@Override
	public void playMonumentCard() {
		PlayMonumentCommand command = new PlayMonumentCommand(ClientUser.getInstance().getIndex());
		ClientFacade.getInstance().playMonument(command);
	}

	@Override
	public void playRoadBuildCard() {
		
		roadAction.execute();
	}

	@Override
	public void playSoldierCard() {

		soldierAction.execute();
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		PlayYearOfPlentyCommand command = new PlayYearOfPlentyCommand(ClientUser.getInstance().getIndex(), resource1, resource2);
		ClientFacade.getInstance().playYearOfPlenty(command);
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}

