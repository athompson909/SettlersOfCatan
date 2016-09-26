package tests.shared_tests;

import junit.framework.TestCase;
import shared.definitions.CatanColor;

import shared.definitions.DevCardType;
import shared.model.player.Player;
import shared.model.resourcebank.*;

/**
 * Created by Mitchell on 9/24/2016.
 */
public class PlayerTest extends TestCase {
    private Player player = new Player(CatanColor.BLUE, "Bob", "bob37", 1);
    private ResourceBank resourceBank = new ResourceBank();

    public void testPlayer() {
        initializationTest();
        purchasePiecesTest();
        developmentCardTest();
    }

    private void initializationTest() {
        //Initialization Asserts
        assert (player.canPlayDevelopmentCards());
        assert (!player.canPlaySoldierCard());
        assert (!player.canPlayMonumentCard());
        assert (!player.canPlayMonopolyCard());
        assert (!player.canPlayRoadBuildingCard());
        assert (!player.canPlayYearOfPlentyCard());
        assert (!player.canPurchaseDevelopmentCard());
        assert (!player.canPurchaseRoad());
        assert (!player.canPurchaseSettlement());
        assert (!player.canPurchaseCity());
    }

    private void purchasePiecesTest() {
        //Resources for building roads
        player.getPlayerResourceList().incBrickCardCount(2);
        player.getPlayerResourceList().incWoodCardCount(2);
        assert (player.canPurchaseRoad());
        player.purchaseRoad();
        assert (player.canPurchaseRoad());
        player.purchaseRoad();
        assert (!player.canPurchaseRoad());
        assert (player.getRoadCount() == 13);
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (player.getPlayerResourceList().getBrickCardCount() == 0);

        //Resources for building settlements
        player.getPlayerResourceList().incWoodCardCount(2);
        player.getPlayerResourceList().incBrickCardCount(2);
        player.getPlayerResourceList().incWheatCardCount(2);
        player.getPlayerResourceList().incSheepCardCount(2);
        assert (player.canPurchaseSettlement());
        player.purchaseSettlement();
        assert (player.canPurchaseSettlement());
        player.purchaseSettlement();
        assert (!player.canPurchaseRoad());
        assert (player.getSettlementCount() == 3);
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (player.getPlayerResourceList().getBrickCardCount() == 0);
        assert (player.getPlayerResourceList().getWheatCardCount() == 0);
        assert (player.getPlayerResourceList().getSheepCardCount() == 0);

        //Resource for building cities
        player.getPlayerResourceList().incWheatCardCount(4);
        player.getPlayerResourceList().incOreCardCount(6);
        assert (player.canPurchaseCity());
        player.purchaseCity();
        assert (player.canPurchaseCity());
        player.purchaseCity();
        assert (!player.canPurchaseCity());
        assert (player.getCityCount() == 2);
        assert (player.getSettlementCount() == 5); //Because you get settlement pieces back.
        assert (player.getPlayerResourceList().getWheatCardCount() == 0);
        assert (player.getPlayerResourceList().getOreCardCount() == 0);
    }

    private void developmentCardTest(){
        assert (resourceBank.getDevCardList().getTotalCardCount() == 26);
        assert (player.getNewDevCardList().getTotalCardCount() == 0);
        player.getPlayerResourceList().incOreCardCount(2);
        player.getPlayerResourceList().incWheatCardCount(2);
        player.getPlayerResourceList().incSheepCardCount(2);
        assert(player.canPurchaseDevelopmentCard());
        DevCardType devCard1 = resourceBank.removeRandomDevCard();
        player.purchaseDevelopmentCard(devCard1);
        assert(player.canPurchaseDevelopmentCard());
        DevCardType devCard2 = resourceBank.removeRandomDevCard();
        player.purchaseDevelopmentCard(devCard2);
        assert(!player.canPurchaseDevelopmentCard());
        assert (resourceBank.getDevCardList().getTotalCardCount() == 24);
        assert (player.getNewDevCardList().getTotalCardCount() == 2);
    }


}


/**
 * Created by Mitchell on 9/24/2016.
 */




