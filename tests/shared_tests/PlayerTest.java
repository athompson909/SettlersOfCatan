package shared_tests;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.player.Player;
import shared.model.resourcebank.*;

/**
 * Created by Mitchell on 9/24/2016.
 */
public class PlayerTest extends TestCase {
    private Player player = new Player(CatanColor.BLUE, "Bob", 37, 1);
    //private Player player = new Player(CatanColor.BLUE, "Bob", "bob37", 1);
    private ResourceBank resourceBank = new ResourceBank();

    @Before
    public void init(){
        player =  new Player(CatanColor.BLUE, "Bob", 37, 1);
    }
//     public void init(){
//        player =  new Player(CatanColor.BLUE, "Bob", "bob37", 1);
//    }

    //Initialization Asserts
    @Test
    public void testInitialization() {
        System.out.println("testInitialization");
        assert (!player.canPlayDevelopmentCards());
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

    //Resources for building settlements
    @Test
    public void testPurchaseSettlement(){
        System.out.println("testPurchaseSettlement");
        player.getPlayerResourceList().incWoodCardCount(2);
        player.getPlayerResourceList().incBrickCardCount(2);
        player.getPlayerResourceList().incWheatCardCount(2);
        player.getPlayerResourceList().incSheepCardCount(2);
        assert (player.canPurchaseSettlement());
        player.purchaseSettlement(false);
        assert (player.canPurchaseSettlement());
        player.purchaseSettlement(false);
        assert (!player.canPurchaseRoad());
        assert (player.getSettlementCount() == 3);
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (player.getPlayerResourceList().getBrickCardCount() == 0);
        assert (player.getPlayerResourceList().getWheatCardCount() == 0);
        assert (player.getPlayerResourceList().getSheepCardCount() == 0);
    }

    //Resources for building roads
    @Test
    public void testPurchaseRoad(){
        System.out.println("testPurchaseRoad");
        player.getPlayerResourceList().incBrickCardCount(2);
        player.getPlayerResourceList().incWoodCardCount(2);
        assert (player.canPurchaseRoad());
        player.purchaseRoad(false);
        assert (player.canPurchaseRoad());
        player.purchaseRoad(false);
        assert (!player.canPurchaseRoad());
        assert (player.getAvailableRoadCount() == 13);
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (player.getPlayerResourceList().getBrickCardCount() == 0);
    }

    //Resource for building cities
    @Test
    public void testPurchaseCity(){
        System.out.println("testPurchaseCity");
        testPurchaseSettlement(); //First must purchase settlements, then..
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

    @Test
    public void testPurchaseDevelopmentCard(){
        System.out.println("testPurchaseDevelopmentCard");
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

    @Test
    public void testPlaySoldier() {
        System.out.println("testPlaySoldier");
        assert (player.getSoldiersPlayed() == 0);
        assert (!player.canPlaySoldierCard());
        player.getOldDevCardList().addDevCard(DevCardType.SOLDIER);
        assert (player.canPlaySoldierCard());
        player.playSoldierCard();
        assert (player.getSoldiersPlayed() == 1);
        assert (!player.canPlaySoldierCard());
        player.setPlayedDevCard(false); //Reset for next test case
    }

    @Test
    public void testPlayMonument() {
        System.out.println("testPlayMonument");
        assert (player.getMonuments() == 0);
        assert (!player.canPlayMonumentCard());
        player.getOldDevCardList().addDevCard(DevCardType.MONUMENT);
        assert (player.canPlayMonumentCard());
        player.playMonumentCard();
        assert (player.getMonuments() == 1);
        assert (!player.canPlayMonumentCard());
        player.setPlayedDevCard(false); //Reset for next test case
    }

    @Test
    public void testPlayRoadBuilding() {
        System.out.println("testPlayRoadBuilding");
        assert (player.getAvailableRoadCount() == 15);
        assert (!player.canPlayRoadBuildingCard());
        player.getOldDevCardList().addDevCard(DevCardType.ROAD_BUILD);
        assert (player.canPlayRoadBuildingCard());
        player.playRoadBuildingCard();
        assert (!player.canPlayRoadBuildingCard());
        //assert (player.getAvailableRoadCount() == 13);
        player.setPlayedDevCard(false); //Reset for next test case
    }

    @Test
    public void testPlayMonopoly() {
        System.out.println("testPlayMonopoly");
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (!player.canPlayMonopolyCard());
        player.getOldDevCardList().addDevCard(DevCardType.MONOPOLY);
        assert (player.canPlayMonopolyCard());
        player.playMonopolyCard(ResourceType.WOOD, 5);
        assert (player.getPlayerResourceList().getWoodCardCount() == 5);
        assert (!player.canPlayMonopolyCard());
        player.setPlayedDevCard(false); //Reset for next test case
    }

    @Test
    public void testPlayYearOfPlenty() {
        System.out.println("testPlayYearOfPlenty");
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (player.getPlayerResourceList().getBrickCardCount() == 0);
        assert (!player.canPlayYearOfPlentyCard());
        player.getOldDevCardList().addDevCard(DevCardType.YEAR_OF_PLENTY);
        assert (player.canPlayYearOfPlentyCard());
        player.playYearOfPlentyCard(ResourceType.WOOD, ResourceType.BRICK);
        assert (!player.canPlayYearOfPlentyCard());
        assert (player.getPlayerResourceList().getWoodCardCount() == 1);
        assert (player.getPlayerResourceList().getBrickCardCount() == 1);
        player.setPlayedDevCard(false); //Reset for next test case
    }

}



