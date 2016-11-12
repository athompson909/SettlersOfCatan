package server_tests;

import client.data.GameInfo;
import client.data.PlayerInfo;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import server.ServerTranslator;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.JSONTranslator;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the JUnit test class for the ServerTranslator.
 *
 * Created by Sierra on 11/05/16.
 */
public class ServerTranslatorTest extends TestCase {

    private ClientModel testClientModel;
    private GameInfo[] testGamesArr;
    private GameInfo testGameInfo;
    private String testGameCreateResponse;
    private JSONTranslator jsonTranslator = new JSONTranslator(); //JUST FOR TESTING MODEL TRANSLATION

    @Test
    public void setUp() throws Exception {
        super.setUp();

        setUpTestGameList();
        setUpTestClientModel();
        setUpTestGameList();
        setUpTestGameInfo();
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
        //why do we need to do this? idk but they are all used in the program sometime
        testClientModel.setChat(testChatMList);
        testClientModel.setLog(testGameLogMList);

        TurnTracker testTT = new TurnTracker();
            testTT.setStatus("FirstRound");
            testTT.setCurrentTurn(0);
            testTT.setLargestArmyHolder(-1);
            testTT.setLongestRoadHolder(-1);


        Map testMap = new Map(false, true, false);
            Robber testRobber = new Robber(testMap);
            HexLocation testRobberHL = new HexLocation(2,2);
            testRobber.setCurrentHexlocation(testRobberHL);
        testMap.setRobber(testRobber);

        //fake settlements
                    HexLocation testStlmtHL1 = new HexLocation(1, -2);
                    VertexDirection testStlmtVD1 = VertexDirection.SouthWest;
                VertexLocation testStlmtVL1 = new VertexLocation(testStlmtHL1, testStlmtVD1);
            VertexObject testStlmtVO1 = new VertexObject(testStlmtVL1.getNormalizedLocation());  ///TESTING THIS **********
            testStlmtVO1.setPieceType(PieceType.SETTLEMENT);
            testStlmtVO1.setOwner(1);

                    HexLocation testStlmtHL2 = new HexLocation(-1, 0);
                    VertexDirection testStlmtVD2 = VertexDirection.SouthEast;
                VertexLocation testStlmtVL2 = new VertexLocation(testStlmtHL2, testStlmtVD2);
            VertexObject testStlmtVO2 = new VertexObject(testStlmtVL2.getNormalizedLocation());  ///TESTING THIS **********
            testStlmtVO2.setPieceType(PieceType.SETTLEMENT);
            testStlmtVO2.setOwner(3);

        //fake cities
                    HexLocation testCityHL1 = new HexLocation(2, -2);
                    VertexDirection testCityVD1 = VertexDirection.NorthWest;
                VertexLocation testCityVL1 = new VertexLocation(testCityHL1, testCityVD1);
            VertexObject testCityVO1 = new VertexObject(testCityVL1.getNormalizedLocation());  ///TESTING THIS **********
            testCityVO1.setPieceType(PieceType.CITY);
            testCityVO1.setOwner(4);

                    HexLocation testCityHL2 = new HexLocation(-1, -1);
                    VertexDirection testCityVD2 = VertexDirection.NorthEast;
                VertexLocation testCityVL2 = new VertexLocation(testCityHL2, testCityVD2);
            VertexObject testCityVO2 = new VertexObject(testCityVL2.getNormalizedLocation());  ///TESTING THIS **********
            testCityVO2.setPieceType(PieceType.CITY);
            testCityVO2.setOwner(1);
        //
            HashMap<VertexLocation, VertexObject> testMapVLs = new HashMap<>();
            testMapVLs.put(testStlmtVL1.getNormalizedLocation(), testStlmtVO1);    ///TESTING THESE **********
            testMapVLs.put(testStlmtVL2.getNormalizedLocation(), testStlmtVO2);
            testMapVLs.put(testCityVL1.getNormalizedLocation(), testCityVO1);
            testMapVLs.put(testCityVL2.getNormalizedLocation(), testCityVO2);
        //


        //fake roads
                    HexLocation testRoadHL1 = new HexLocation(0, -1);
                    EdgeDirection testRoadED1 = EdgeDirection.SouthEast;
                EdgeLocation testRoadEL1 = new EdgeLocation(testRoadHL1, testRoadED1);
            EdgeValue testRoadEV1 = new EdgeValue(testRoadEL1.getNormalizedLocation());  ///TESTING THIS **********
            testRoadEV1.setOwner(1);

                    HexLocation testRoadHL2 = new HexLocation(2, -2);
                    EdgeDirection testRoadED2 = EdgeDirection.NorthWest;
                EdgeLocation testRoadEL2 = new EdgeLocation(testRoadHL2, testRoadED2);
            EdgeValue testRoadEV2 = new EdgeValue(testRoadEL2.getNormalizedLocation());  ///TESTING THIS **********
            testRoadEV2.setOwner(4);
        //
            HashMap<EdgeLocation, EdgeValue> testMapEVs = new HashMap<>();
            testMapEVs.put(testRoadEL1.getNormalizedLocation(), testRoadEV1);  ///TESTING THESE **********
            testMapEVs.put(testRoadEL2.getNormalizedLocation(), testRoadEV2);
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
        ResourceList testTORL = new ResourceList(0,0,0,-2,1);
        TradeOffer testTO = new TradeOffer();
            testTO.setSenderIndex(8);
            testTO.setReceiverIndex(4);
            testTO.setTradeOfferList(testTORL);
            testClientModel.setTradeOffer(testTO);



        //combine it all into a ClientModel object
        testClientModel.setResourceBank(testResBank);
        testClientModel.setMessageManager(testMessageMgr);
        testClientModel.setTurnTracker(testTT);
        testClientModel.setMap(testMap);
        testClientModel.setPlayers(testPlayersArr);

    }

    public void setUpTestGameList(){

        GameInfo gI1 = new GameInfo();
        gI1.setTitle("BEST GAME");
        gI1.setId(0);
            PlayerInfo pI1G1 = new PlayerInfo();
                pI1G1.setName("You");
                pI1G1.setId(6);
                pI1G1.setPlayerIndex(0);
                pI1G1.setColor(CatanColor.GREEN);
            PlayerInfo pI2G1 = new PlayerInfo();
                pI2G1.setName("Are");
                pI2G1.setId(8);
                pI2G1.setPlayerIndex(1);
                pI2G1.setColor(CatanColor.PURPLE);
            PlayerInfo pI3G1 = new PlayerInfo();
                pI3G1.setName("My");
                pI3G1.setId(3);
                pI3G1.setPlayerIndex(2);
                pI3G1.setColor(CatanColor.RED);
            PlayerInfo pI4G1 = new PlayerInfo();
                pI4G1.setName("Mom");
                pI4G1.setId(10);
                pI4G1.setPlayerIndex(3);
                pI4G1.setColor(CatanColor.YELLOW);

        List<PlayerInfo> pListGI1 = new ArrayList<>();
            pListGI1.add(pI1G1);
            pListGI1.add(pI2G1);
            pListGI1.add(pI3G1);
            pListGI1.add(pI4G1);

        gI1.setPlayers(pListGI1);

//
        GameInfo gI2 = new GameInfo();
        gI2.setTitle("WEIRD GAME");
        gI2.setId(1);
            PlayerInfo pI1G2 = new PlayerInfo();
                pI1G2.setName("WHY");
                pI1G2.setId(2);
                pI1G2.setPlayerIndex(0);
                pI1G2.setColor(CatanColor.BLUE);
            PlayerInfo pI2G2 = new PlayerInfo();
                pI2G2.setName("EAT");
                pI2G2.setId(9);
                pI2G2.setPlayerIndex(1);
                pI2G2.setColor(CatanColor.PUCE);
            PlayerInfo pI3G2 = new PlayerInfo();
                pI3G2.setName("TOAST");
                pI3G2.setId(11);
                pI3G2.setPlayerIndex(2);
                pI3G2.setColor(CatanColor.WHITE);

        List<PlayerInfo> pListGI2 = new ArrayList<>();
            pListGI2.add(pI1G2);
            pListGI2.add(pI2G2);
            pListGI2.add(pI3G2);

        gI2.setPlayers(pListGI2);

//

        GameInfo gI3 = new GameInfo();
        gI3.setId(2);
        gI3.setTitle("WHAT GAME");
            PlayerInfo pI1G3 = new PlayerInfo();
                pI1G3.setName("HEYY");
                pI1G3.setId(7);
                pI1G3.setPlayerIndex(0);
                pI1G3.setColor(CatanColor.BROWN);
        List<PlayerInfo> pListGI3 = new ArrayList<>();
            pListGI3.add(pI1G3);

        gI3.setPlayers(pListGI3);

//
        GameInfo gI4 = new GameInfo();
        gI4.setTitle("NO GAME");
        gI4.setId(3);
            //empty

        /////////////////

     testGamesArr = new GameInfo[4];
        testGamesArr[0] = gI1;
        testGamesArr[1] = gI2;
        testGamesArr[2] = gI3;
        testGamesArr[3] = gI4;
    }

    public void setUpTestGameInfo(){

        testGameInfo = new GameInfo();
        testGameInfo.setId(3);
        testGameInfo.setTitle("yoo");
//        PlayerInfo tempPI = new PlayerInfo();
//            tempPI.setColor(CatanColor.ORANGE);
//            tempPI.setId(0);
//            tempPI.setName("Sam");
        ArrayList<PlayerInfo> tempPIList = new ArrayList<>();
//        tempPIList.add(tempPI);
        testGameInfo.setPlayers(tempPIList);

        testGameCreateResponse =  "{" +
                "\"title\":" + "\"yoo\"," +
                "\"id\":" + 3 + "," +
                "\"players\":" +  "[" +
                "{" +
//                    "\"color\": \"orange\"," +
//                    "\"name\": \"Sam\"," +
//                    "\"id\": 0\"" +
                "}," +
                "{}," +
                "{}," +
                "{}" +
                "]" +
                "}";

    }

    @Test
    public void testModelToJSON() throws Exception {
        System.out.println(">TESTING MODELTOJSON TRANSLATION!");

            String testModelString = ServerTranslator.getInstance().modelToJSON(testClientModel);
            JSONObject testModelJSON = new JSONObject(testModelString);

        //put it through the JSONTranslator's modelFromJSON function to see if it comes out the same
            ClientModel resultClientModel = jsonTranslator.modelFromJSON(testModelJSON);

        //compare testClientModel to resultClientModel:
        assertTrue(testClientModel.equals(resultClientModel));
    }

    //FOR THE LIST OF ALL GAMES
    @Test
    public void testGamesListToJSON() throws Exception {
        System.out.println(">TESTING GAMESLISTTOJSON TRANSLATION!");

        String gameListResult = ServerTranslator.getInstance().gamesListToJSON(testGamesArr);



        //TEST - pretend this is coming from the server
        JSONArray gLJSONArray = new JSONArray(gameListResult);

        //put it back through the JSONTranslator to see if it comes out the same:
        GameInfo[] resultGamesArr = jsonTranslator.gamesListResponseFromJSON(gLJSONArray);

        //assertions
        assertTrue(testGamesArr.length == resultGamesArr.length);

        for (int g = 0; g < testGamesArr.length; g++){
            GameInfo testGameInfo = testGamesArr[g];
            GameInfo resultGameInfo = resultGamesArr[g];

            assertTrue(testGameInfo.equals(resultGameInfo));
        }
    }

    //FOR THE GAME CREATE RESPONSE
    @Test
    public void testGameCreateRespToJSON() throws Exception {
        System.out.println(">TESTING GAMELISTTOJSON TRANSLATION!");

        //see if the size of testGameInfo's players list is 4 empty spots!
        String resultGL= ServerTranslator.getInstance().gameInfoToJSON(testGameInfo);
        //JSONObject resultJSON = new JSONObject(resultGL);

        JSONAssert.assertEquals(testGameCreateResponse, resultGL, false);
    }

}
