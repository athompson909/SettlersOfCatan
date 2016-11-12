package shared_tests;

import junit.framework.TestCase;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.definitions.ResourceType;
import shared.locations.*;
import shared.model.map.EdgeValue;
import shared.model.map.Map;
import shared.model.map.Robber;
import shared.model.map.VertexObject;
import shared.model.messagemanager.MessageLine;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.ClientModel;
import shared.definitions.PortType;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by Stephanie on 9/29/16.
 */

public class TradeTest extends TestCase {
    private ClientModel model = new ClientModel(1);
   // private Map map = new Map(false, false, false); //Don't randomize
    //private Player player = new Player(CatanColor.BLUE, "Bob", 37, 1);
    //private Player player = new Player(CatanColor.BLUE, "Bob", "bob37", 1);
    //private VertexLocation vertLoc = new VertexLocation(new HexLocation(-2,0), VertexDirection.NorthWest);

    // Creates all the pieces of a fake client model object, following the JSONTranslator's test
    // ClientModel JSON as closely as possible.
    public void setUpTestClientModel(){
        model = new ClientModel(0);
        model.setWinner(-1);
        model.setChanged(false);

        DevCardList testDevCardList = new DevCardList(14, 5, 2, 2, 2);
        ResourceList testResCardsList = new ResourceList(3, 3, 3, 3, 3);
        ResourceBank testResBank = new ResourceBank();
        testResBank.setResourceList(testResCardsList);
        testResBank.setDevCardList(testDevCardList);

        MessageManager testMessageMgr = new MessageManager();
        MessageLine testChatMsg1 = new MessageLine("CHAT YO", "baconc");
        MessageLine testChatMsg2 = new MessageLine("YOUR MOM", "baconz");
        MessageList testChatMList = new MessageList();
        testChatMList.insertMessageLine(testChatMsg1);
        testChatMList.insertMessageLine(testChatMsg2);
        MessageLine testLogMsg1 = new MessageLine("LOG YO", "baconl");
        MessageLine testLogMsg2 = new MessageLine("WHADDUP", "bacony");
        MessageList testGameLogMList = new MessageList();
        testGameLogMList.insertMessageLine(testLogMsg1);
        testGameLogMList.insertMessageLine(testLogMsg2);
        testMessageMgr.setChat(testChatMList);
        testMessageMgr.setLog(testGameLogMList);

        TurnTracker testTT = new TurnTracker();
        testTT.setStatus("FirstRound");
        testTT.setCurrentTurn(0);
        testTT.setLargestArmyHolder(-1);
        testTT.setLongestRoadHolder(-1);


        Map testMap = new Map(false, true, false);
        Robber testRobber = new Robber(testMap);
        testMap.setRobber(testRobber);

        //fake settlements
        HexLocation testStlmtHL1 = new HexLocation(0, 3);
        VertexDirection testStlmtVD1 = VertexDirection.NorthEast;
        VertexLocation testStlmtVL1 = new VertexLocation(testStlmtHL1, testStlmtVD1);
        VertexObject testStlmtVO1 = new VertexObject(testStlmtVL1);
        testStlmtVO1.setPieceType(PieceType.SETTLEMENT);
        testStlmtVO1.setOwner(2);

        HexLocation testStlmtHL2 = new HexLocation(-1, 0);
        VertexDirection testStlmtVD2 = VertexDirection.SouthEast;
        VertexLocation testStlmtVL2 = new VertexLocation(testStlmtHL2, testStlmtVD2);
        VertexObject testStlmtVO2 = new VertexObject(testStlmtVL2);
        testStlmtVO2.setPieceType(PieceType.SETTLEMENT);
        testStlmtVO2.setOwner(3);

        //fake cities
        HexLocation testCityHL1 = new HexLocation(2, -2);
        VertexDirection testCityVD1 = VertexDirection.NorthWest;
        VertexLocation testCityVL1 = new VertexLocation(testCityHL1, testCityVD1);
        VertexObject testCityVO1 = new VertexObject(testCityVL1);
        testCityVO1.setPieceType(PieceType.CITY);
        testCityVO1.setOwner(4);

        HexLocation testCityHL2 = new HexLocation(-1, -1);
        VertexDirection testCityVD2 = VertexDirection.NorthEast;
        VertexLocation testCityVL2 = new VertexLocation(testCityHL2, testCityVD2);
        VertexObject testCityVO2 = new VertexObject(testCityVL2);
        testCityVO2.setPieceType(PieceType.CITY);
        testCityVO2.setOwner(1);
        //
        HashMap<VertexLocation, VertexObject> testMapVLs = new HashMap<>();
        testMapVLs.put(testStlmtVL1, testStlmtVO1);
        testMapVLs.put(testStlmtVL2, testStlmtVO2);
        //


        //fake roads
        HexLocation testRoadHL1 = new HexLocation(0, -1);
        EdgeDirection testRoadED1 = EdgeDirection.SouthEast;
        EdgeLocation testRoadEL1 = new EdgeLocation(testRoadHL1, testRoadED1);
        EdgeValue testRoadEV1 = new EdgeValue(testRoadEL1);
        testRoadEV1.setOwner(1);

        HexLocation testRoadHL2 = new HexLocation(2, -2);
        EdgeDirection testRoadED2 = EdgeDirection.NorthWest;
        EdgeLocation testRoadEL2 = new EdgeLocation(testRoadHL2, testRoadED2);
        EdgeValue testRoadEV2 = new EdgeValue(testRoadEL2);
        testRoadEV2.setOwner(4);
        //
        HashMap<EdgeLocation, EdgeValue> testMapEVs = new HashMap<>();
        testMapEVs.put(testRoadEL1, testRoadEV1);
        testMapEVs.put(testRoadEL2, testRoadEV2);
        //

        testMap.setVertexObjects(testMapVLs);
        testMap.setEdgeValues(testMapEVs);


        //fake players
        Player testPlayer1 = new Player(CatanColor.BLUE, "bacon", 2, 0);
        Player testPlayer2 = new Player(CatanColor.PURPLE, "petey", 5, 0);
        Player testPlayer3 = new Player(CatanColor.RED, "WHOA", 4, 0);
        Player testPlayer4 = new Player(CatanColor.GREEN, "HEY", 15, 0);
        Player[] testPlayersArr = new Player[4];
        testPlayersArr[0] = testPlayer1;
        testPlayersArr[1] = testPlayer2;
        testPlayersArr[2] = testPlayer3;
        testPlayersArr[3] = testPlayer4;

        //fake trade offer
        /*
        ResourceList testTORL = new ResourceList(0,0,0,-2,1);
        TradeOffer testTO = new TradeOffer();
            testTO.setSenderIndex(8);
            testTO.setReceiverIndex(4);
            testTO.setTradeOfferList(testTORL);
            testClientModel.setTradeOffer(testTO);
            */


        //combine it all into a ClientModel object
        model.setResourceBank(testResBank);
        model.setMessageManager(testMessageMgr);
        model.setTurnTracker(testTT);
        model.setMap(testMap);
        model.setPlayers(testPlayersArr);

    }

    @Test
    public void testMaritime() {
        System.out.println("Testing Maritime Trade");
        setUpTestClientModel();
        Player player = model.getPlayers()[2];

        player.getPlayerResourceList().incBrickCardCount(3);
        player.getPlayerResourceList().incSheepCardCount(2);
        player.getPlayerResourceList().incWheatCardCount(4);
        player.getPlayerResourceList().incWoodCardCount(2);
        player.getPlayerResourceList().incOreCardCount(0);

        //not their turn
        assert(!model.maritimeTrade(2, 2, ResourceType.WOOD, ResourceType.WHEAT));
        model.getTurnTracker().setCurrentTurn(2);
        
        //bad index
        assert (!model.maritimeTrade(-1, 2, ResourceType.ORE, ResourceType.WHEAT));
        assert (!model.maritimeTrade(4, 2, ResourceType.ORE, ResourceType.WHEAT));

        //don't have the resource
        assert (!model.maritimeTrade(2, 4, ResourceType.SHEEP, ResourceType.BRICK));

        //bad ratio
        assert (!model.maritimeTrade(2, 1, ResourceType.WOOD, ResourceType.WHEAT));

        //dont have the port
        assert (!model.maritimeTrade(2, 2, ResourceType.SHEEP, ResourceType.ORE));
        assert (!model.maritimeTrade(2, 3, ResourceType.BRICK, ResourceType.ORE));

        //can't trade same resource
        assert (!model.maritimeTrade(2, 3, ResourceType.BRICK, ResourceType.BRICK));

        //save bank values
        int wood = model.getResourceBank().getResourceList().getWoodCardCount();

        //valid trade
        ResourceList bank = model.getResourceBank().getResourceList();
        int bankWheat = bank.getWheatCardCount();
        int bankOre = bank.getOreCardCount();
        assert (model.maritimeTrade(2, 4, ResourceType.WHEAT, ResourceType.ORE));
        assert (player.getPlayerResourceList().getWheatCardCount() == 0);
        assert (player.getPlayerResourceList().getOreCardCount() == 1);
        assert (bank.getWheatCardCount() == bankWheat + 4);
        assert (bank.getOreCardCount() == bankOre -1);

        int bankWood = bank.getWoodCardCount();
        bankWheat = bank.getWheatCardCount();
        assert (model.maritimeTrade(2, 2, ResourceType.WOOD, ResourceType.WHEAT));
        assert (player.getPlayerResourceList().getWoodCardCount() == 0);
        assert (player.getPlayerResourceList().getWheatCardCount() == 1);
        assert (bank.getWoodCardCount() == bankWood + 2);
        assert (bank.getWheatCardCount() == bankWheat -1);

        //add 3:1 port
        HexLocation testStlmtHL1 = new HexLocation(-2, 3);
        VertexDirection testStlmtVD1 = VertexDirection.NorthEast;
        VertexLocation testStlmtVL1 = new VertexLocation(testStlmtHL1, testStlmtVD1);
        VertexObject testStlmtVO1 = new VertexObject(testStlmtVL1);
        testStlmtVO1.setPieceType(PieceType.SETTLEMENT);
        testStlmtVO1.setOwner(2);

        HashMap<VertexLocation, VertexObject> testMapVLs = new HashMap<>();
        testMapVLs.put(testStlmtVL1, testStlmtVO1);
        model.getMap().setVertexObjects(testMapVLs);
        int bankBrick = bank.getBrickCardCount();
        bankWheat = bank.getWheatCardCount();
        assert (model.maritimeTrade(2, 3, ResourceType.BRICK, ResourceType.WHEAT));
        assert (player.getPlayerResourceList().getBrickCardCount() == 0);
        assert (player.getPlayerResourceList().getWheatCardCount() == 2);
        assert (bank.getBrickCardCount() == bankBrick + 3);
        assert (bank.getWheatCardCount() == bankWheat - 1);

    }

    @Test
    public void testDomesticTrade() {
        setUpTestClientModel();

        //setUp player resource lists
        Player player1 = model.getPlayers()[0];
        player1.getPlayerResourceList().incWheatCardCount(2);
        player1.getPlayerResourceList().incBrickCardCount(3);

        Player player2 = model.getPlayers()[1];
        player2.getPlayerResourceList().incOreCardCount(3);
        player2.getPlayerResourceList().incSheepCardCount(1);

        Player player3 = model.getPlayers()[2];
        player3.getPlayerResourceList().incWheatCardCount(2);
        player3.getPlayerResourceList().incBrickCardCount(3);

        Player player4 = model.getPlayers()[3];
        player4.getPlayerResourceList().incOreCardCount(3);
        player4.getPlayerResourceList().incSheepCardCount(1);

        ResourceList offerList = new ResourceList();
        offerList.incWheatCardCount(1);
        offerList.incBrickCardCount(4);
        offerList.decOreCardCount(4);

        System.out.println("Testing Domestic Trade - Offer");

        //cant trade because doesn't have enough brick
        assert(!model.offerTrade(0, offerList, 1));

        offerList.decBrickCardCount(1);

        //can't trade because sender == receiver
        assert(!model.offerTrade(0, offerList, 0));
        //can't trade because receiver index invalid
        assert(!model.offerTrade(0, offerList, 4));

        //can trade
        assert(model.offerTrade(0, offerList, 1));

        //cant trade because not player 3's turn
        assert(!model.offerTrade(2, offerList, 3));


        System.out.println("Testing Domestic Trade - Accept");

        //can't accept because not enough ore
        assert(!model.acceptTrade(1, true));

        offerList.incOreCardCount(1);
        //can't accept because not the receiver
        assert(!model.acceptTrade(4, true));
        //can accept
        assert(model.acceptTrade(1, true));
    }

}
