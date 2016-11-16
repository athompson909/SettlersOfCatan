package client.devcards;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.base.IAction;
import client.misc.MessageView;
import client.utils.MessageUtils;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.PlayMonopolyCommand;
import shared.model.commandmanager.moves.PlayMonumentCommand;
import shared.model.commandmanager.moves.PlayYearOfPlentyCommand;
import shared.model.commandmanager.moves.PurchaseDevCardCommand;
import shared.model.player.Player;
import shared.model.resourcebank.DevCardList;
import shared.model.turntracker.TurnTracker;

import java.util.Observable;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController {

    private IBuyDevCardView buyCardView;
    private IPlayDevCardView playDevCardView;
    private IAction soldierAction;
    private IAction roadAction;

    private ClientModel clientModel;
    private boolean playedCard = false;

    /**
     * DevCardController constructor
     *
     * @param view          "Play dev card" view
     * @param buyCardView   "Buy dev card" view
     * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
     * @param roadAction    Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
     */
    public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView,
                             IAction soldierAction, IAction roadAction) {

        super(view);

        this.buyCardView = buyCardView;
        this.playDevCardView = view;
        this.soldierAction = soldierAction;
        this.roadAction = roadAction;
    }

    public IBuyDevCardView getBuyCardView() {
        return buyCardView;
    }

    public IPlayDevCardView getPlayDevCardView() {
        return playDevCardView;
    }

    @Override
    public void startBuyCard() {

        buyCardView.showModal();
    }

    @Override
    public void cancelBuyCard() {

        buyCardView.closeModal();
    }

    /**
     * updates playDevCardView
     */
    @Override
    public void buyCard() {
        buyCardView.closeModal();
        //(for testing)
        /*if(true) {*/if (clientModel.canPurchaseDevCard(ClientUser.getInstance().getIndex())) {
            PurchaseDevCardCommand command = new PurchaseDevCardCommand(ClientUser.getInstance().getIndex());
            ClientFacade.getInstance().purchaseDevCard(command);
            Player thisPlayer = clientModel.getClientPlayer();
            setCardAmounts(thisPlayer.getOldDevCardList());
        }
        else {
            MessageUtils.showRejectMessage(new MessageView(), "Not enough resources",
                    "you do not have sufficient resources to buy a development card at this time");
        }
    }

    /**
     * sets the cards to be enabled if their counts >= 1, I don't know if that's the right response)
     * @param devCardList the current player's devCardList
     */
    private void setCardAmounts(DevCardList devCardList) {
        
        //Show sum of old and new cards
        DevCardList newCardList = clientModel.getClientPlayer().getNewDevCardList();
        playDevCardView.setCardAmount(DevCardType.SOLDIER, devCardList.getSoldierCardCount()+ newCardList.getSoldierCardCount());
        playDevCardView.setCardAmount(DevCardType.MONUMENT, devCardList.getMonumentCardCount() + newCardList.getMonumentCardCount());
        playDevCardView.setCardAmount(DevCardType.YEAR_OF_PLENTY, devCardList.getYearOfPlentyCardCount() + newCardList.getYearOfPlentyCardCount());
        playDevCardView.setCardAmount(DevCardType.MONOPOLY, devCardList.getMonopolyCardCount() + newCardList.getMonopolyCardCount());
        playDevCardView.setCardAmount(DevCardType.ROAD_BUILD, devCardList.getRoadBuildingCardCount() + newCardList.getRoadBuildingCardCount());

        //Must be their turn and in playing state to play cards
        TurnTracker turnTracker = clientModel.getTurnTracker();
        if(turnTracker.getStatus().equals("Playing") && turnTracker.getCurrentTurn() == ClientUser.getInstance().getIndex()) {
           //can only play one card per turn except monuments
            if(!clientModel.getClientPlayer().hasPlayedDevCard()) {
                //can only play old cards except for monuments
                playDevCardView.setCardEnabled(DevCardType.SOLDIER, (devCardList.getSoldierCardCount() >= 1));
                playDevCardView.setCardEnabled(DevCardType.YEAR_OF_PLENTY, (devCardList.getYearOfPlentyCardCount() >= 1));
                playDevCardView.setCardEnabled(DevCardType.MONOPOLY, (devCardList.getMonopolyCardCount() >= 1));
                //must have 2 unused roads to build
                boolean enoughRoads = (clientModel.getClientPlayer().getAvailableRoadCount() >= 2);
                playDevCardView.setCardEnabled(DevCardType.ROAD_BUILD, (devCardList.getRoadBuildingCardCount() >= 1 && enoughRoads));
            }else{
                playDevCardView.setCardEnabled(DevCardType.SOLDIER, false);
                playDevCardView.setCardEnabled(DevCardType.YEAR_OF_PLENTY, false);
                playDevCardView.setCardEnabled(DevCardType.MONOPOLY, false);
                playDevCardView.setCardEnabled(DevCardType.ROAD_BUILD, false);
            }
            playDevCardView.setCardEnabled(DevCardType.MONUMENT, (devCardList.getMonumentCardCount() >= 1 || newCardList.getMonumentCardCount() >= 1));
            //clientModel.getClientPlayer().setPlayedDevCard(false);
        }
        else {
            playDevCardView.setCardEnabled(DevCardType.SOLDIER, false);
            playDevCardView.setCardEnabled(DevCardType.MONUMENT, false);
            playDevCardView.setCardEnabled(DevCardType.YEAR_OF_PLENTY, false);
            playDevCardView.setCardEnabled(DevCardType.MONOPOLY, false);
            playDevCardView.setCardEnabled(DevCardType.ROAD_BUILD, false);
        }

        addNewCard(clientModel.getClientPlayer().getNewDevCardList());
    }

    /**
     * todo fix this, the new devcardlist seems do not have anything in it!!
     * @param devCardList
     */
    private void addNewCard(DevCardList devCardList) {
        if(devCardList.getMostRecentAddedCard() != null) {
            switch(devCardList.getMostRecentAddedCard()) {
                case SOLDIER:
                    playDevCardView.setCardAmount(DevCardType.SOLDIER, devCardList.getSoldierCardCount() + 1);
                    devCardList.setMostRecentAddedCard(null);
                    break;
                case MONUMENT:
                    playDevCardView.setCardAmount(DevCardType.MONUMENT, devCardList.getMonumentCardCount() + 1);
                    devCardList.setMostRecentAddedCard(null);
                    break;
                case YEAR_OF_PLENTY:
                    playDevCardView.setCardAmount(DevCardType.YEAR_OF_PLENTY, devCardList.getYearOfPlentyCardCount() + 1);
                    devCardList.setMostRecentAddedCard(null);
                    break;
                case MONOPOLY:
                    playDevCardView.setCardAmount(DevCardType.MONOPOLY, devCardList.getMonopolyCardCount() + 1);
                    devCardList.setMostRecentAddedCard(null);
                    break;
                case ROAD_BUILD:
                    playDevCardView.setCardAmount(DevCardType.ROAD_BUILD, devCardList.getRoadBuildingCardCount() + 1);
                    devCardList.setMostRecentAddedCard(null);
                    break;
            }
        }
    }

    @Override
    public void startPlayCard() {
        setCardAmounts(clientModel.getClientPlayer().getOldDevCardList());
        playDevCardView.showModal();
    }

    @Override
    public void cancelPlayCard() {
        playDevCardView.closeModal();
    }

    @Override
    public void playMonopolyCard(ResourceType resource) {
        if (clientModel.canPlayMonopoly(ClientUser.getInstance().getIndex())) {
            PlayMonopolyCommand command = new PlayMonopolyCommand(ClientUser.getInstance().getIndex(), resource);
            ClientFacade.getInstance().playMonopoly(command);
            setCardAmounts(clientModel.getClientPlayer().getOldDevCardList());
        }
        //Todo: should I close the modal after I call the facade?
    }

    @Override
    public void playMonumentCard() {
        if (clientModel.canPlayMonument(ClientUser.getInstance().getIndex())) {
            PlayMonumentCommand command = new PlayMonumentCommand(ClientUser.getInstance().getIndex());
            ClientFacade.getInstance().playMonument(command);
            setCardAmounts(clientModel.getClientPlayer().getOldDevCardList());
        }
    }

    @Override
    public void playRoadBuildCard() {
//todo - why is this not sending (probably issue like play soldier card was
        //if needed can create function in client or model to update map state here
        roadAction.execute();
        setCardAmounts(clientModel.getClientPlayer().getOldDevCardList());
    }

    @Override
    public void playSoldierCard() {

        soldierAction.execute();
        setCardAmounts(clientModel.getClientPlayer().getOldDevCardList());
    }

    @Override
    public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
        PlayYearOfPlentyCommand command = new PlayYearOfPlentyCommand(ClientUser.getInstance().getIndex(), resource1, resource2);
        ClientFacade.getInstance().playYearOfPlenty(command);
        setCardAmounts(clientModel.getClientPlayer().getOldDevCardList());//will this help?
    }

    @Override
    public void update(Observable o, Object arg) {
        clientModel = (ClientModel) o;
        setCardAmounts(clientModel.getClientPlayer().getOldDevCardList());
    }

}

