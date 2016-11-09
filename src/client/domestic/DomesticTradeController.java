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
import shared.model.turntracker.TurnTracker;

import java.util.List;
import java.util.Observable;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController {

	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	/**this tracks the resources they want to send/receive*/
	private ResourceList tradeList;
	private ResourceList playerResources;
	private int receiver = -1;
	private int wood = 0;
	private int brick = 0;
	private int sheep = 0;
	private int wheat = 0;
	private int ore = 0;
	private boolean setUp = false;

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
		if(!setUp) {
			setUp = true;

			//set other player info
			GameInfo gameInfo = ClientUser.getInstance().getCurrentAddedGameInfo();
			List<PlayerInfo> playerInfoList = gameInfo.getPlayers();
			PlayerInfo[] playerInfo = new PlayerInfo[3];
			int index = 0;
			for (int i = 0; i < playerInfoList.size(); i++) {
				//don't add currentPlayer
				if (ClientUser.getInstance().getIndex() != i) {
					playerInfoList.get(i).setPlayerIndex(i);
					playerInfo[index] = playerInfoList.get(i);
					index++;
				}
			}
			getTradeOverlay().setPlayers(playerInfo);
		}

		getTradeOverlay().reset();
		receiver = -1;
		getTradeOverlay().setPlayerSelectionEnabled(Client.getInstance().getState().toEnum()==State.PLAYING);

		//if it is this players turn enable trade otherwise set message
		TurnTracker tracker = Client.getInstance().getClientModel().getTurnTracker();
		boolean myTurn = (ClientUser.getInstance().getIndex() == tracker.getCurrentTurn());
		getTradeOverlay().setResourceSelectionEnabled(myTurn);
		if(myTurn) {
			getTradeOverlay().setStateMessage("Set the trade you want to make");
			playerResources = Client.getInstance().getClientModel().getCurrentPlayer().getPlayerResourceList();
		}else{
			getTradeOverlay().setStateMessage("Not Your Turn");
			getTradeOverlay().setTradeEnabled(false);
		}

		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, false, false);
		getTradeOverlay().setResourceAmount(ResourceType.WOOD, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, false, false);
		getTradeOverlay().setResourceAmount(ResourceType.BRICK, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, false, false);
		getTradeOverlay().setResourceAmount(ResourceType.SHEEP, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, false, false);
		getTradeOverlay().setResourceAmount(ResourceType.WHEAT, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, false, false);
		getTradeOverlay().setResourceAmount(ResourceType.ORE, "0");

		getTradeOverlay().showModal();
		if(myTurn) {
			tradeList = new ResourceList();
			updateOptions();
		}
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
		updateOptions();
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
		updateOptions();
	}

	@Override
	public void sendTradeOffer() {
        getTradeOverlay().closeModal();
        getWaitOverlay().showModal();
		OfferTradeCommand command = new OfferTradeCommand(ClientUser.getInstance().getIndex(), tradeList, receiver);
		ClientFacade.getInstance().offerTrade(command);
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {
		receiver = playerIndex;
		updateOptions();
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		if(resource.toString().equals("WOOD")){
			wood = -1;
			tradeList.loseAllCardsOfType(ResourceType.WOOD);
		}else if(resource.toString().equals("BRICK")){
			brick = -1;
			tradeList.loseAllCardsOfType(ResourceType.BRICK);
		}else if(resource.toString().equals("SHEEP")){
			sheep = -1;
			tradeList.loseAllCardsOfType(ResourceType.SHEEP);
		}else if(resource.toString().equals("WHEAT")){
			wheat = -1;
			tradeList.loseAllCardsOfType(ResourceType.WHEAT);
		}else if(resource.toString().equals("ORE")){
			ore = -1;
			tradeList.loseAllCardsOfType(ResourceType.ORE);
		}

		//reset number
		getTradeOverlay().setResourceAmount(resource, "0");
		updateOptions();
	}

	@Override
	public void setResourceToSend(ResourceType resource) {

		if(resource.toString().equals("WOOD") && playerResources.getWoodCardCount() > 0){
			wood = 1;
			tradeList.loseAllCardsOfType(ResourceType.WOOD);
		}else if(resource.toString().equals("BRICK")){
			brick = 1;
			tradeList.loseAllCardsOfType(ResourceType.BRICK);
		}else if(resource.toString().equals("SHEEP")){
			sheep = 1;
			tradeList.loseAllCardsOfType(ResourceType.SHEEP);
		}else if(resource.toString().equals("WHEAT")){
			wheat = 1;
			tradeList.loseAllCardsOfType(ResourceType.WHEAT);
		}else if(resource.toString().equals("ORE")){
			ore = 1;
			tradeList.loseAllCardsOfType(ResourceType.ORE);
		}

		//reset number
		getTradeOverlay().setResourceAmount(resource, "0");
		updateOptions();
	}

	@Override
	public void unsetResource(ResourceType resource) {
		if(resource.toString().equals("WOOD")){
			wood = 0;
			tradeList.loseAllCardsOfType(ResourceType.WOOD);
		}else if(resource.toString().equals("BRICK")){
			brick = 0;
			tradeList.loseAllCardsOfType(ResourceType.BRICK);
		}else if(resource.toString().equals("SHEEP")){
			sheep = 0;
			tradeList.loseAllCardsOfType(ResourceType.SHEEP);
		}else if(resource.toString().equals("WHEAT")){
			wheat = 0;
			tradeList.loseAllCardsOfType(ResourceType.WHEAT);
		}else if(resource.toString().equals("ORE")){
			ore = 0;
			tradeList.loseAllCardsOfType(ResourceType.ORE);
		}

		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
		updateOptions();
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {
		AcceptTradeCommand command = new AcceptTradeCommand(ClientUser.getInstance().getIndex(), willAccept);
		ClientFacade.getInstance().acceptTrade(command);
		getAcceptOverlay().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		//check for tradeOffer
		ClientModel model = (ClientModel)o;
		TradeOffer offer = model.getTradeOffer();

		//there is a trade and this user is the receiver
		if(offer != null && offer.tradeToAccept()){
			if (offer.getReceiverIndex()==ClientUser.getInstance().getIndex()) {//need to accept/reject

				getAcceptOverlay().reset();
				ResourceList tradeList = offer.getTradeOfferList();

				if (tradeList.getWoodCardCount() > 0) {
					getAcceptOverlay().addGetResource(ResourceType.WOOD, tradeList.getWoodCardCount());
				} else if (tradeList.getWoodCardCount() < 0) {
					getAcceptOverlay().addGiveResource(ResourceType.WOOD, -1 * tradeList.getWoodCardCount());
				}

				if (tradeList.getBrickCardCount() > 0) {
					getAcceptOverlay().addGetResource(ResourceType.BRICK, tradeList.getBrickCardCount());
				} else if (tradeList.getBrickCardCount() < 0) {
					getAcceptOverlay().addGiveResource(ResourceType.BRICK, -1 * tradeList.getBrickCardCount());
				}

				if (tradeList.getSheepCardCount() > 0) {
					getAcceptOverlay().addGetResource(ResourceType.SHEEP, tradeList.getSheepCardCount());
				} else if (tradeList.getSheepCardCount() < 0) {
					getAcceptOverlay().addGiveResource(ResourceType.SHEEP, -1 * tradeList.getSheepCardCount());
				}

				if (tradeList.getWheatCardCount() > 0) {
					getAcceptOverlay().addGetResource(ResourceType.WHEAT, tradeList.getWheatCardCount());
				} else if (tradeList.getWheatCardCount() < 0) {
					getAcceptOverlay().addGiveResource(ResourceType.WHEAT, -1 * tradeList.getWheatCardCount());
				}

				if (tradeList.getOreCardCount() > 0) {
					getAcceptOverlay().addGetResource(ResourceType.ORE, tradeList.getOreCardCount());
				} else if (tradeList.getOreCardCount() < 0) {
					getAcceptOverlay().addGiveResource(ResourceType.ORE, -1 * tradeList.getOreCardCount());
				}

				//enable accept button if this player has enough resources
				ResourceList resources = model.getCurrentPlayer().getPlayerResourceList();
				getAcceptOverlay().setAcceptEnabled(offer.canPlayerAccept(resources));

				getAcceptOverlay().showModal();
			}else if(offer.getSenderIndex() == ClientUser.getInstance().getIndex()){//I am waiting
                //show waiting overlay if not showing
                if(!getWaitOverlay().isModalShowing()){
                    getWaitOverlay().showModal();
                }
            }
		}else if(getWaitOverlay().isModalShowing()){
			getWaitOverlay().closeModal();
		}
	}

	public void updateOptions(){

		//update arrows
			//allow increase on send only if you have enough of the resource
			//allow decrease only if above zero
		//Wood
		boolean woodUp = false;
		boolean woodDown = false;
		if(wood == 1 && wood*tradeList.getWoodCardCount() < playerResources.getWoodCardCount() ){
			woodUp = true;
		}else if(wood == -1){
			woodUp = true;
		}
		if(wood != 0 && wood*tradeList.getWoodCardCount() > 0){
			woodDown = true;
		}
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, woodUp, woodDown);

		//Brick
		boolean brickUp = false;
		boolean brickDown = false;
		if(brick == 1 && brick*tradeList.getBrickCardCount() < playerResources.getBrickCardCount() ){
			brickUp = true;
		}else if(brick == -1){
			brickUp = true;
		}
		if(brick != 0 && brick*tradeList.getBrickCardCount() > 0){
			brickDown = true;
		}
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, brickUp, brickDown);

		//Sheep
		boolean sheepUp = false;
		boolean sheepDown = false;
		if(sheep == 1 && sheep*tradeList.getSheepCardCount() < playerResources.getSheepCardCount() ){
			sheepUp = true;
		}else if(sheep == -1){
			sheepUp = true;
		}
		if(sheep != 0 && sheep*tradeList.getSheepCardCount() > 0){
			sheepDown = true;
		}
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sheepUp, sheepDown);

		//Wheat
		boolean wheatUp = false;
		boolean wheatDown = false;
		if(wheat == 1 && wheat*tradeList.getWheatCardCount() < playerResources.getWheatCardCount() ){
			wheatUp = true;
		}else if(wheat == -1){
			wheatUp = true;
		}
		if(wheat != 0 && wheat*tradeList.getWheatCardCount() > 0){
			wheatDown = true;
		}
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, wheatUp, wheatDown);

		//Ore
		boolean oreUp = false;
		boolean oreDown = false;
		if(ore == 1 && ore*tradeList.getOreCardCount() < playerResources.getOreCardCount() ){
			oreUp = true;
		}else if(ore == -1){
			oreUp = true;
		}
		if(ore != 0 && ore*tradeList.getOreCardCount() > 0){
			oreDown = true;
		}
		getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, oreUp, oreDown);

		//update trade button
		boolean sendingSomething = (wood == 1 && tradeList.getWoodCardCount() > 0
				|| brick == 1 && tradeList.getBrickCardCount() > 0
				|| sheep == 1 && tradeList.getSheepCardCount() > 0
				|| wheat == 1 && tradeList.getWheatCardCount() > 0
				|| ore == 1 && tradeList.getOreCardCount() > 0);
		boolean receivingSomething = (wood == -1 && tradeList.getWoodCardCount() < 0
				|| brick == -1 && tradeList.getBrickCardCount() < 0
				|| sheep == -1 && tradeList.getSheepCardCount() < 0
				|| wheat == -1 && tradeList.getWheatCardCount() < 0
				|| ore == -1 && tradeList.getOreCardCount() < 0);
		if(!sendingSomething || !receivingSomething){
			getTradeOverlay().setStateMessage("Set the trade you want to make");
			getTradeOverlay().setTradeEnabled(false);
		}else if(receiver == -1){
			getTradeOverlay().setStateMessage("Select a player");
			getTradeOverlay().setTradeEnabled(false);
		}
		else {
			getTradeOverlay().setStateMessage("Trade!");
			getTradeOverlay().setTradeEnabled(true);
		}
	}
}

