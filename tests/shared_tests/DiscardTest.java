package shared_tests;

import junit.framework.TestCase;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.map.EdgeValue;
import shared.model.map.Map;
import shared.model.map.Robber;
import shared.model.map.VertexObject;
import shared.model.messagemanager.MessageLine;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.HashMap;

/**
 * Created by Alise on 11/9/2016.
 */
public class DiscardTest extends TestCase {
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
        HexLocation testStlmtHL1 = new HexLocation(1, -2);
        VertexDirection testStlmtVD1 = VertexDirection.SouthWest;
        VertexLocation testStlmtVL1 = new VertexLocation(testStlmtHL1, testStlmtVD1);
        VertexObject testStlmtVO1 = new VertexObject(testStlmtVL1);
        testStlmtVO1.setPieceType(PieceType.SETTLEMENT);
        testStlmtVO1.setOwner(1);

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
    public void testDiscard(){
        System.out.println("Testing Discard");
        setUpTestClientModel();

        ResourceList list = new ResourceList();
        list.incWheatCardCount(4);

        //player and tracker set up
        Player player0 = model.getPlayers()[0];
        player0.getPlayerResourceList().incOreCardCount(4);
        player0.getPlayerResourceList().incWheatCardCount(4);
        Player player1 = model.getPlayers()[1];
        player1.getPlayerResourceList().incBrickCardCount(10);
        player1.setDiscarded(false);
        TurnTracker tracker = model.getTurnTracker();
        tracker.setStatus("Discarding");

        //index incorrect
        assert(!model.discardCards(-1, list));
        assert(!model.discardCards(4, list));

       //hasDiscarded is true
        assert(!model.discardCards(0, list));
        player0.setDiscarded(false);

        //negative number in resourcelist
        list.decOreCardCount(3);
        assert(!model.discardCards(0, list));
        list.incOreCardCount(4);

        //not right amount of cards to discard
        assert(!model.discardCards(0, list));

        //trying to discard cards they don't have
        list.decOreCardCount(1);
        list.decWheatCardCount(1);
        list.incWoodCardCount(1);
        assert(!model.discardCards(0, list));

        //can discard
        list.decWoodCardCount(1);
        list.incOreCardCount(1);
        assert(model.discardCards(0, list));

        //correct results
        assert(player0.hasDiscarded());
        assert(player0.getPlayerResourceList().getOreCardCount() == 3);
        assert(player0.getPlayerResourceList().getWheatCardCount() == 1);
        assert(tracker.getStatus().equals("Discarding"));

        //other player discards - check turn tracker results
        ResourceList list1 = new ResourceList();
        list1.incBrickCardCount(5);
        assert(model.discardCards(1, list1));
        assert(tracker.getStatus() == "Robbing");

    }

}
