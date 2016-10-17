package client.domestic;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.definitions.*;
import client.base.*;
import client.misc.*;
import shared.model.ClientModel;
import shared.model.TradeOffer;
import shared.model.commandmanager.moves.AcceptTradeCommand;
import shared.model.commandmanager.moves.OfferTradeCommand;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceList;

import java.util.List;
import java.util.Observable;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController {
//Todo - allow trade when send, receive and player selected
	//todo - allow increase on send only if you have the resource
	//todo - allow decrease only if above zero
	
	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	/**this tracks the resources they want to send/receive*/
	private ResourceList tradeList;
	private int receiver;
	private int wood = 0;
	private int brick = 0;
	private int sheep = 0;
	private int wheat = 0;
	private int ore = 0;

	/**
	 * DomesticTradeController constructor
	 * 
	 * @param tradeView Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
									IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);
		
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
	}
	
	public IDomesticTradeView getTradeView() {
		
		return (IDomesticTradeView)super.getView();
	}

	public IDomesticTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	public IWaitView getWaitOverlay() {
		return waitOverlay;
	}

	public void setWaitOverlay(IWaitView waitView) {
		this.waitOverlay = waitView;
	}

	public IAcceptTradeOverlay getAcceptOverlay() {
		return acceptOverlay;
	}

	public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
		this.acceptOverlay = acceptOverlay;
	}

	@Override
	public void startTrade() {
		getTradeOverlay().setPlayerSelectionEnabled(Client.getInstance().getState().toEnum()==State.PLAYING);

		//if it is this players turn enable trade otherwise set message
		boolean myTurn = (Client.getInstance().getState().toEnum()==State.PLAYING);
		getTradeOverlay().setResourceSelectionEnabled(myTurn);
		if(myTurn) {
			getTradeOverlay().setStateMessage("Set the trade you want to make");
		}else{
			getTradeOverlay().setStateMessage("Not Your Turn");
		}

		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, false, false);
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, false, false);
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, false, false);
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, false, false);
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, false, false);

		//set player info
		GameInfo gameInfo = ClientUser.getInstance().getCurrentAddedGameInfo();
		List<PlayerInfo> playerInfoList = gameInfo.getPlayers();
		PlayerInfo[] playerInfo = new PlayerInfo[3];
		int index = 0;
		for(int i = 0; i < playerInfoList.size(); i++){
			//don't add currentPlayer
			if(ClientUser.getInstance().getIndex() != i) {
				playerInfo[index] = playerInfoList.get(i);
				index++;
			}
		}
		getTradeOverlay().setPlayers(playerInfo);

		getTradeOverlay().showModal();
		tradeList = new ResourceList();
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		if(resource.toString().equals("WOOD")){
			tradeList.decWoodCardCount(wood);
		}else if(resource.toString().equals("BRICK")){
			tradeList.decBrickCardCount(brick);
		}else if(resource.toString().equals("SHEEP")){
			tradeList.decSheepCardCount(sheep);
		}else if(resource.toString().equals("WHEAT")){
			tradeList.decWheatCardCount(wheat);
		}else if(resource.toString().equals("ORE")){
			tradeList.decOreCardCount(ore);
		}
	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		if(resource.toString().equals("WOOD")){
			tradeList.incWoodCardCount(wood);
		}else if(resource.toString().equals("BRICK")){
			tradeList.incBrickCardCount(brick);
		}else if(resource.toString().equals("SHEEP")){
			tradeList.incSheepCardCount(sheep);
		}else if(resource.toString().equals("WHEAT")){
			tradeList.incWheatCardCount(wheat);
		}else if(resource.toString().equals("ORE")){
			tradeList.incOreCardCount(ore);
		}

		//reset number
		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
	}

	@Override
	public void sendTradeOffer() {
		OfferTradeCommand command = new OfferTradeCommand(ClientUser.getInstance().getIndex(), tradeList, receiver);
		ClientFacade.getInstance().offerTrade(command);
		getTradeOverlay().closeModal();
		getWaitOverlay().showModal();
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {
		receiver = playerIndex;
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		if(resource.toString().equals("WOOD")){
			wood = -1;
		}else if(resource.toString().equals("BRICK")){
			brick = -1;
		}else if(resource.toString().equals("SHEEP")){
			sheep = -1;
		}else if(resource.toString().equals("WHEAT")){
			wheat = -1;
		}else if(resource.toString().equals("ORE")){
			ore = -1;
		}

		//reset number
		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
	}

	@Override
	public void setResourceToSend(ResourceType resource) {

		if(resource.toString().equals("WOOD")){
			wood = 1;
		}else if(resource.toString().equals("BRICK")){
			brick = 1;
		}else if(resource.toString().equals("SHEEP")){
			sheep = 1;
		}else if(resource.toString().equals("WHEAT")){
			wheat = 1;
		}else if(resource.toString().equals("ORE")){
			ore = 1;
		}

		//reset number
		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
	}

	@Override
	public void unsetResource(ResourceType resource) {
		if(resource.toString().equals("WOOD")){
			wood = 0;
		}else if(resource.toString().equals("BRICK")){
			brick = 0;
		}else if(resource.toString().equals("SHEEP")){
			sheep = 0;
		}else if(resource.toString().equals("WHEAT")){
			wheat = 0;
		}else if(resource.toString().equals("ORE")){
			ore = 0;
		}

		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {
		AcceptTradeCommand command = new AcceptTradeCommand(receiver, willAccept);
		ClientFacade.getInstance().acceptTrade(command);
		getAcceptOverlay().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		//check for tradeOffer
		ClientModel model = (ClientModel)o;
		TradeOffer offer = model.getTradeOffer();

		//there is a trade and this user is the receiver
		if(offer.tradeToAccept() && offer.getReceiverIndex()==ClientUser.getInstance().getIndex()){

			getAcceptOverlay().reset();
			ResourceList tradeList = offer.getTradeOfferList();

			if(tradeList.getWoodCardCount() > 0){
				getAcceptOverlay().addGetResource(ResourceType.WOOD, tradeList.getWoodCardCount());
			}else if(tradeList.getWoodCardCount() < 0){
				getAcceptOverlay().addGiveResource(ResourceType.WOOD, -1*tradeList.getWoodCardCount());
			}

			if(tradeList.getBrickCardCount() > 0){
				getAcceptOverlay().addGetResource(ResourceType.BRICK, tradeList.getBrickCardCount());
			}else if(tradeList.getBrickCardCount() < 0){
				getAcceptOverlay().addGiveResource(ResourceType.BRICK, -1*tradeList.getBrickCardCount());
			}

			if(tradeList.getSheepCardCount() > 0){
				getAcceptOverlay().addGetResource(ResourceType.SHEEP, tradeList.getSheepCardCount());
			}else if(tradeList.getSheepCardCount() < 0){
				getAcceptOverlay().addGiveResource(ResourceType.SHEEP, -1*tradeList.getSheepCardCount());
			}

			if(tradeList.getWheatCardCount() > 0){
				getAcceptOverlay().addGetResource(ResourceType.WHEAT, tradeList.getWheatCardCount());
			}else if(tradeList.getWheatCardCount() < 0){
				getAcceptOverlay().addGiveResource(ResourceType.WHEAT, -1*tradeList.getWheatCardCount());
			}

			if(tradeList.getOreCardCount() > 0){
				getAcceptOverlay().addGetResource(ResourceType.ORE, tradeList.getOreCardCount());
			}else if(tradeList.getOreCardCount() < 0){
				getAcceptOverlay().addGiveResource(ResourceType.ORE, -1*tradeList.getOreCardCount());
			}

			//enable accept button if this player has enough resources
			ResourceList resources = model.getCurrentPlayer().getPlayerResourceList();
			getAcceptOverlay().setAcceptEnabled(offer.canPlayerAccept(resources));

			getAcceptOverlay().showModal();
		}
	}
}

