package server_tests;

import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.TradeOffer;
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

/**
 * This is the JUnit test class for the ServerTranslator.
 *
 * Created by Sierra on 11/05/16.
 */
public class ServerTranslatorTest extends TestCase {

    private ClientModel testClientModel;

    @Test
    public void setUp() throws Exception {
        super.setUp();

        setUpTestClientModel();
    }

    // Creates all the pieces of a fake client model object, following the JSONTranslator's test
    // ClientModel JSON as closely as possible.
    public void setUpTestClientModel(){
        testClientModel = new ClientModel(0);
        testClientModel.setWinner(-1);
        testClientModel.setChanged(false);

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
        /*
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
        */

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
        ResourceList testTORL = new ResourceList(0,0,0,-2,1);
        TradeOffer testTO = new TradeOffer();
            testTO.setSenderIndex(8);
            testTO.setReceiverIndex(4);
            testTO.setTradeOfferList(testTORL);


        //combine it all into a ClientModel object
        testClientModel.setResourceBank(testResBank);
        testClientModel.setMessageManager(testMessageMgr);
        testClientModel.setTurnTracker(testTT);
        testClientModel.setMap(testMap);
        testClientModel.setPlayers(testPlayersArr);
        testClientModel.setTradeOffer(testTO);

    }

    /*
    @Test
    public void testAddAICmdTranslation() throws Exception {
        System.out.println(">TESTING ADDAICMD TRANSLATION!");


       // JSONAssert.assertEquals(expectedResult, addAICmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }
    */

}
