package shared_tests;

import junit.framework.TestCase;
import shared.definitions.CatanColor;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
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
        purchaseRoadTest();
        purchaseSettlementTest();
        purchaseCityTest();
        purchaseDevelopmentCardTest();

        playSoldierTest();
        playMonumentTest();
        playRoadBuildingTest();
        playMonopolyTest();
        playYearOfPlentyTest();
    }

    //Initialization Asserts
    private void initializationTest() {
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

    //Resources for building settlements
    private void purchaseSettlementTest(){
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
    }

    //Resources for building roads
    private void purchaseRoadTest(){
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
    }

    //Resource for building cities
    private void purchaseCityTest(){
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

    private void purchaseDevelopmentCardTest(){
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

    private void playSoldierTest() {
        assert (player.getSoldiersPlayed() == 0);
        assert (!player.canPlaySoldierCard());
        player.getOldDevCardList().addDevCard(DevCardType.SOLDIER);
        assert (player.canPlaySoldierCard());
        player.playSoldierCard();
        assert (player.getSoldiersPlayed() == 1);
        assert (!player.canPlaySoldierCard());
        player.setPlayedDevCard(false); //Reset for next test case
    }

    private void playMonumentTest() {
        assert (player.getMonuments() == 0);
        assert (!player.canPlayMonumentCard());
        player.getOldDevCardList().addDevCard(DevCardType.MONUMENT);
        assert (player.canPlayMonumentCard());
        player.playMonumentCard();
        assert (player.getMonuments() == 1);
        assert (!player.canPlayMonumentCard());
        player.setPlayedDevCard(false); //Reset for next test case
    }

    private void playRoadBuildingTest() {
        assert (player.getRoadCount() == 13); //Should still == 13 from previous BuildRoadTest.
        assert (!player.canPlayRoadBuildingCard());
        player.getOldDevCardList().addDevCard(DevCardType.ROAD_BUILD);
        assert (player.canPlayRoadBuildingCard());
        player.playRoadBuildingCard(2);
        assert (!player.canPlayRoadBuildingCard());
        assert (player.getRoadCount() == 11);
        player.setPlayedDevCard(false); //Reset for next test case
    }

    private void playMonopolyTest() {
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (!player.canPlayMonopolyCard());
        player.getOldDevCardList().addDevCard(DevCardType.MONOPOLY);
        assert (player.canPlayMonopolyCard());
        player.playMonopolyCard(ResourceType.WOOD, 5);
        assert (player.getPlayerResourceList().getWoodCardCount() == 5);
        assert (!player.canPlayMonopolyCard());
        player.setPlayedDevCard(false); //Reset for next test case
    }

    private void playYearOfPlentyTest() {
        assert (player.getPlayerResourceList().getWoodCardCount() == 5);
        assert (player.getPlayerResourceList().getBrickCardCount() == 0);
        assert (!player.canPlayYearOfPlentyCard());
        player.getOldDevCardList().addDevCard(DevCardType.YEAR_OF_PLENTY);
        assert (player.canPlayYearOfPlentyCard());
        player.playYearOfPlentyCard(ResourceType.WOOD, ResourceType.BRICK);
        assert (!player.canPlayYearOfPlentyCard());
        assert (player.getPlayerResourceList().getWoodCardCount() == 6);
        assert (player.getPlayerResourceList().getBrickCardCount() == 1);
        player.setPlayedDevCard(false); //Reset for next test case
    }

}



