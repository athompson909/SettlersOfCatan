package tests.client_tests;

import org.skyscreamer.jsonassert.*;
import org.json.JSONObject;
import shared.definitions.CatanColor;
import shared.locations.*;
import shared.model.ClientModel;
import junit.framework.TestCase;
import com.google.gson.*;
import shared.model.JSONTranslator;
import shared.model.commandmanager.BaseCommand;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.BuildCity;
import shared.model.map.VertexObject;

import java.util.ArrayList;


/**
 * This is the JUnit test for the JSONTranslator class.
 * I'm mainly using it to experiment with/understand Gson.
 * <p>
 * Created by Sierra on 9/23/16.
 */
public class JSONTranslatorTest extends TestCase {

    private JSONTranslator jsonTranslator = new JSONTranslator();
    private String testResponseBody = null;
    private Gson gsonTest = new Gson();
    private AddAICommand addAICommand;
    private ExecuteGameCommandsCommand execGameCmdsCommand;
    private FetchNewModelCommand fetchNewModelCommand;
    private GameCreateCommand gameCreateCommand;
    private GameListCommand gameListCommand;
    private GameLoadCommand gameLoadCommand;
    private GameResetCommand gameResetCommand;
    private GameSaveCommand gameSaveCommand;
    private GetGameCommandsCommand getGameCmdsCommand;
    private ListAICommand listAICommand;
    private LoginCommand loginCommand;
    private RegisterCommand registerCommand;
    private SendChatCommand sendChatCommand;
    private UtilChangeLogLevelCommand utilChangeLogLevelCommand;
    private GameJoinCommand gameJoinCommand;

    private AcceptTradeCommand acceptTradeCommand;
    private BuildCityCommand buildCityCommand;
    private BuildRoadCommand buildRoadCommand;
    private BuildSettlementCommand buildSettlementCommand;
    private DiscardCommand discardCommand;
    private EndTurnCommand endTurnCommand;
    private MaritimeTradeCommand maritimeTradeCommand;
    private OfferTradeCommand offerTradeCommand;
    private PlayMonopolyCommand playMonopolyCommand;
    private PlayMonumentCommand playMonumentCommand;
    private PlayRoadBuilderCommand playRoadBuilderCommand;
    private PlaySoldierCommand playSoldierCommand;
    private PlayYearOfPlentyCommand playYearOfPlentyCommand;
    private PurchaseDevCardCommand purchaseDevCardCommand;
    private RobPlayerCommand robPlayerCommand;
    private RollDiceCommand rollDiceCommand;


    public void setUp() throws Exception {
        super.setUp();

        setUpModelString();

        //ADD AI CMD SETUP
        //-----------------
        String aaiAIType = "LARGEST_ARMY";
        addAICommand = new AddAICommand(aaiAIType);
        //-----------------

        //EXEC GAME CMDS CMD SETUP
        //-----------------
        execGameCmdsCommand = new ExecuteGameCommandsCommand();
        //the server has no request model schema for this one, since it only sends back the model (updated from all the game cmds)
        //-----------------

        //FETCH NEW MODEL CMD SETUP
        //-----------------
        int fnmVer = 14;
        fetchNewModelCommand = new FetchNewModelCommand(fnmVer);
        //the server has no request model schema for this one, since it only requires an int
        //-----------------

        //GAME JOIN CMD SETUP
        //-----------------
        int gJGameID = 5;
        CatanColor gJColor = CatanColor.BLUE;
        gameJoinCommand = new GameJoinCommand(gJGameID, gJColor);
        //-----------------

        //ACCEPT TRADE CME SETUP
        //-----------------
        int pIndex = 3;
        boolean willAcc = true;
        acceptTradeCommand = new AcceptTradeCommand(pIndex, willAcc);
        //-----------------



        //BUILD ROAD CMD SETUP
        //-----------------
        int bRPlayerID = 2;
        EdgeDirection bREdgeDir = EdgeDirection.NorthEast;
        HexLocation brELHexLoc = new HexLocation(1, 1);
        EdgeLocation bREdgeLocation = new EdgeLocation(brELHexLoc, bREdgeDir);
        buildRoadCommand = new BuildRoadCommand(bREdgeLocation, bRPlayerID);
        buildRoadCommand.setFree(true);
        //-----------------

        //BUILD CITY CMD SETUP
        //-----------------
        int bCOwner = 2;
        HexLocation bCELHexLoc = new HexLocation(2, 2);
        VertexDirection bCVertexDir = VertexDirection.NorthEast;
        VertexLocation bCVertexLocation = new VertexLocation(bCELHexLoc, bCVertexDir);
        VertexObject cityVtx = new VertexObject();
        cityVtx.setOwner(bCOwner);
        cityVtx.setVertexLocation(bCVertexLocation);
        buildCityCommand = new BuildCityCommand(cityVtx);
        //-----------------

        //BUILD SETTLEMENT CMD SETUP
        //-----------------
        int bSOwner = 3;
        HexLocation bSELHexLoc = new HexLocation(3, 3);
        VertexDirection bSVertexDir = VertexDirection.SouthEast;
        VertexLocation bSVertexLocation = new VertexLocation(bSELHexLoc, bSVertexDir);
        VertexObject stlmtVtx = new VertexObject();
        stlmtVtx.setOwner(bSOwner);
        stlmtVtx.setVertexLocation(bSVertexLocation);
        buildSettlementCommand = new BuildSettlementCommand(stlmtVtx);
        buildSettlementCommand.setFree(false);
        //----------------

    }

    //YOU PROBABLY NEED TO ADD A "TYPE" FIELD TO EVERY COMMAND OBJECT  *************

    private void setUpModelString() {
        // This is an actual server response body:
        testResponseBody = " {\n" +
                "  \"deck\": {\n" +
                "    \"yearOfPlenty\": 2,\n" +
                "    \"monopoly\": 2,\n" +
                "    \"soldier\": 14,\n" +
                "    \"roadBuilding\": 2,\n" +
                "    \"monument\": 5\n" +
                "  },\n" +
                "  \"map\": {\n" +
                "    \"hexes\": [\n" +
                "      {\n" +
                "        \"resource\": \"sheep\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": -2\n" +
                "        },\n" +
                "        \"number\": 12\n" +
                "      },\n" +
                "      {\n" +
                "        \"location\": {\n" +
                "          \"x\": 1,\n" +
                "          \"y\": -2\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"brick\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 2,\n" +
                "          \"y\": -2\n" +
                "        },\n" +
                "        \"number\": 8\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wood\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -1,\n" +
                "          \"y\": -1\n" +
                "        },\n" +
                "        \"number\": 11\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wheat\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": -1\n" +
                "        },\n" +
                "        \"number\": 6\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"ore\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 1,\n" +
                "          \"y\": -1\n" +
                "        },\n" +
                "        \"number\": 5\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wood\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 2,\n" +
                "          \"y\": -1\n" +
                "        },\n" +
                "        \"number\": 3\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"sheep\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -2,\n" +
                "          \"y\": 0\n" +
                "        },\n" +
                "        \"number\": 10\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"brick\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -1,\n" +
                "          \"y\": 0\n" +
                "        },\n" +
                "        \"number\": 4\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wood\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": 0\n" +
                "        },\n" +
                "        \"number\": 4\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wheat\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 1,\n" +
                "          \"y\": 0\n" +
                "        },\n" +
                "        \"number\": 11\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"sheep\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 2,\n" +
                "          \"y\": 0\n" +
                "        },\n" +
                "        \"number\": 10\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wheat\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -2,\n" +
                "          \"y\": 1\n" +
                "        },\n" +
                "        \"number\": 2\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"brick\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -1,\n" +
                "          \"y\": 1\n" +
                "        },\n" +
                "        \"number\": 5\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"ore\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": 1\n" +
                "        },\n" +
                "        \"number\": 3\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"ore\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 1,\n" +
                "          \"y\": 1\n" +
                "        },\n" +
                "        \"number\": 9\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wheat\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -2,\n" +
                "          \"y\": 2\n" +
                "        },\n" +
                "        \"number\": 8\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"sheep\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -1,\n" +
                "          \"y\": 2\n" +
                "        },\n" +
                "        \"number\": 9\n" +
                "      },\n" +
                "      {\n" +
                "        \"resource\": \"wood\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": 2\n" +
                "        },\n" +
                "        \"number\": 6\n" +
                "      }\n" +
                "    ],\n" +
                "    \"roads\": [],\n" +
                "    \"cities\": [],\n" +
                "    \"settlements\": [],\n" +
                "    \"radius\": 3,\n" +
                "    \"ports\": [\n" +
                "      {\n" +
                "        \"ratio\": 3,\n" +
                "        \"direction\": \"S\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -1,\n" +
                "          \"y\": -2\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 3,\n" +
                "        \"direction\": \"NE\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -3,\n" +
                "          \"y\": 2\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 2,\n" +
                "        \"resource\": \"ore\",\n" +
                "        \"direction\": \"S\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 1,\n" +
                "          \"y\": -3\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 2,\n" +
                "        \"resource\": \"wheat\",\n" +
                "        \"direction\": \"SE\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -3,\n" +
                "          \"y\": 0\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 3,\n" +
                "        \"direction\": \"N\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": 3\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 2,\n" +
                "        \"resource\": \"wood\",\n" +
                "        \"direction\": \"SW\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 3,\n" +
                "          \"y\": -3\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 2,\n" +
                "        \"resource\": \"brick\",\n" +
                "        \"direction\": \"NE\",\n" +
                "        \"location\": {\n" +
                "          \"x\": -2,\n" +
                "          \"y\": 3\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 3,\n" +
                "        \"direction\": \"NW\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 2,\n" +
                "          \"y\": 1\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ratio\": 2,\n" +
                "        \"resource\": \"sheep\",\n" +
                "        \"direction\": \"NW\",\n" +
                "        \"location\": {\n" +
                "          \"x\": 3,\n" +
                "          \"y\": -1\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"robber\": {\n" +
                "      \"x\": 1,\n" +
                "      \"y\": -2\n" +
                "    }\n" +
                "  },\n" +
                "  \"players\": [\n" +
                "    {\n" +
                "      \"resources\": {\n" +
                "        \"brick\": 0,\n" +
                "        \"wood\": 0,\n" +
                "        \"sheep\": 0,\n" +
                "        \"wheat\": 0,\n" +
                "        \"ore\": 0\n" +
                "      },\n" +
                "      \"oldDevCards\": {\n" +
                "        \"yearOfPlenty\": 0,\n" +
                "        \"monopoly\": 0,\n" +
                "        \"soldier\": 0,\n" +
                "        \"roadBuilding\": 0,\n" +
                "        \"monument\": 0\n" +
                "      },\n" +
                "      \"newDevCards\": {\n" +
                "        \"yearOfPlenty\": 0,\n" +
                "        \"monopoly\": 0,\n" +
                "        \"soldier\": 0,\n" +
                "        \"roadBuilding\": 0,\n" +
                "        \"monument\": 0\n" +
                "      },\n" +
                "      \"roads\": 15,\n" +
                "      \"cities\": 4,\n" +
                "      \"settlements\": 5,\n" +
                "      \"soldiers\": 0,\n" +
                "      \"victoryPoints\": 0,\n" +
                "      \"monuments\": 0,\n" +
                "      \"playedDevCard\": false,\n" +
                "      \"discarded\": false,\n" +
                "      \"playerID\": 12,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"bacon\",\n" +
                "      \"color\": \"blue\"\n" +
                "    },\n" +
                "    null,\n" +
                "    null,\n" +
                "    null\n" +
                "  ],\n" +
                "  \"log\": {\n" +
                "    \"lines\": [" +
                "{" +
                "\"message\": \"LOG YO\"," +
                "\"source\": \"baconl\"" +
                "}" +
                "]\n" +
                "  },\n" +
                "  \"chat\": {\n" +
                "    \"lines\": [" +
                "{" +
                "\"message\": \"CHAT YO\"," +
                "\"source\": \"baconc\"" +
                "}" +
                "]\n" +
                "  },\n" +
                "  \"bank\": {\n" +
                "    \"brick\": 24,\n" +
                "    \"wood\": 24,\n" +
                "    \"sheep\": 24,\n" +
                "    \"wheat\": 24,\n" +
                "    \"ore\": 24\n" +
                "  },\n" +
                "  \"turnTracker\": {\n" +
                "    \"status\": \"FirstRound\",\n" +
                "    \"currentTurn\": 0,\n" +
                "    \"longestRoad\": -1,\n" +
                "    \"largestArmy\": -1\n" +
                "  },\n" +
                "  \"winner\": -1,\n" +
                "  \"version\": 0\n" +
                "}";
    }


    /**
     * @throws Exception
     */
    public void testModelTranslation() throws Exception {
        System.out.println(">I'M TESTING MODEL TRANSLATION!");
        //System.out.println("Test JSON string: \n");
        //System.out.println(testResponseBody);
        //System.out.println("========================");

        //Gson translating
        ClientModel newClientModel = gsonTest.fromJson(testResponseBody, ClientModel.class);

        System.out.println("\n==============");
        System.out.println("Just deserialized testResponseBody, newClientModel info:");
        System.out.println("\t verNo= " + newClientModel.getVersion());
        System.out.println("\t winner= " + newClientModel.getWinner());
        System.out.println("\t MapRadius= " + newClientModel.getMap().getRadius());
        System.out.println("\t Map#Hexes= " + newClientModel.getMap().getHexes().size());
        System.out.println("\t Map#Ports= " + newClientModel.getMap().getPorts().size());
        System.out.println("\t MapHex1Loc=" + newClientModel.getMap().getHexes().get(1).getLocation().toString());
        System.out.println("\t ChatList= " + newClientModel.getChat().getLines().get(0).getMessage());
        System.out.println("========================");

    }

    // come back to this - I'm going to do the game commands list translators after all the other simple ones are done
    public void testGameCmdsListTranslation() throws Exception{
        String testCmdsList = "[\n" +
                "  {\n" +
                "    \"roadLocation\": {\n" +
                "      \"direction\": \"N\",\n" +
                "      \"x\": 1,\n" +
                "      \"y\": -1\n" +
                "    },\n" +
                "    \"free\": true,\n" +
                "    \"type\": \"buildRoad\",\n" +
                "    \"playerIndex\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"vertexLocation\": {\n" +
                "      \"direction\": \"NW\",\n" +
                "      \"x\": 1,\n" +
                "      \"y\": -1\n" +
                "    },\n" +
                "    \"free\": true,\n" +
                "    \"type\": \"buildSettlement\",\n" +
                "    \"playerIndex\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"finishTurn\",\n" +
                "    \"playerIndex\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"roadLocation\": {\n" +
                "      \"direction\": \"S\",\n" +
                "      \"x\": 0,\n" +
                "      \"y\": 0\n" +
                "    },\n" +
                "    \"free\": true,\n" +
                "    \"type\": \"buildRoad\",\n" +
                "    \"playerIndex\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"vertexLocation\": {\n" +
                "      \"direction\": \"SE\",\n" +
                "      \"x\": 0,\n" +
                "      \"y\": 0\n" +
                "    },\n" +
                "    \"free\": true,\n" +
                "    \"type\": \"buildSettlement\",\n" +
                "    \"playerIndex\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"finishTurn\",\n" +
                "    \"playerIndex\": 1\n" +
                "  }\n" +
                "]" ;

        //The list of exec'd commands will come back as a JSON list of lots of different types of commands.
        //Trying to find a way to hold all the commands that have been executed so far, as Gson deserializes them.
        ArrayList<BaseCommand> listOfGameCmdsExecuted = new ArrayList<BaseCommand>();


    }




//TEST GAME COMMANDS  ===============================

    //GOOD
    public void testAddAICmdTranslation() throws Exception {
        System.out.println(">TESTING ADDAICMD TRANSLATION!");

        JSONObject addAICmdJSONResult = jsonTranslator.addAICmdToJSON(addAICommand);
                // gsonTest.toJson(addAICommand);

        System.out.print("Just serialized addAICmd, JSONstring result= ");
        System.out.println(addAICmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                  "  \"AIType\": \"LARGEST_ARMY\"\n" +
                                "}";

        JSONAssert.assertEquals(expectedResult, addAICmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testExecGameCmdsCmdTranslation() throws Exception {
        System.out.println(">TESTING EXECGAMECMDSCMD TRANSLATION!");

        String execGameCmdsCmdJSONResult = gsonTest.toJson(execGameCmdsCommand);

        System.out.println("Just serialized execGameCmdsCmd, JSONstring result= ");
        System.out.println(execGameCmdsCmdJSONResult);
        System.out.println("=================");

        // the server returns the model updated to follow what the list of exec'd game commands said.
        // so I need to translate a list of all command objects that have been executed so far, all into JSON?

     //   JSONAssert.assertEquals(expectedResult, execGameCmdsCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //FetchNewModel is executed on the server side just by the URL - no HTTP request body required
    /*
    public void testFetchNewModelCmdTranslation() throws Exception {
        System.out.println(">TESTING FETCHNEWMODELCMD TRANSLATION!");

        String fetchNewModelCmdJSONResult = gsonTest.toJson(fetchNewModelCommand);

        System.out.println("Just serialized fetchNewModelCmd, JSONstring result= ");
        System.out.println(fetchNewModelCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, fetchNewModelCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }
    */

    public void testGameCreateCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMECREATECMD TRANSLATION!");

        String gameCreateCmdJSONResult = gsonTest.toJson(gameCreateCommand);

        System.out.println("Just serialized gameCreateCmd, JSONstring result= ");
        System.out.println(gameCreateCmdJSONResult);
        System.out.println("=================");
    }

    //GOOD
    public void testGameJoinCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMEJOINCMD TRANSLATION!");

        JSONObject gameJoinCmdJSONResult = jsonTranslator.gameJoinCmdToJSON(gameJoinCommand);
                // gsonTest.toJson(gameJoinCommand);

        System.out.print("Just serialized gameJoinCmd, JSONstring result= ");
        System.out.println(gameJoinCmdJSONResult);
        System.out.println("=================");

        String expectedResult =
                        " {\n" +
                        "\"id\": 5," +
                        "\"color\": \"BLUE\"" +
                        " }\n";

        JSONAssert.assertEquals(expectedResult, gameJoinCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);

    }

    //the list of game commands exec'd needs to be translated from JSON to multiple kinds of Command objs
    /*
    public void testGameListCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMELISTCMD TRANSLATION!");

        String gameListCmdJSONResult = gsonTest.toJson(gameListCommand);

        System.out.println("Just serialized gameListCmd, JSONstring result= ");
        System.out.println(gameListCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, gameListCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }
    */

    public void testGameLoadCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMELOADCMD TRANSLATION!");

        String gameLoadCmdJSONResult = gsonTest.toJson(gameLoadCommand);

        System.out.println("Just serialized gameLoadCmd, JSONstring result= ");
        System.out.println(gameLoadCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, gameLoadCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testGameResetCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMERESETCMD TRANSLATION!");

        String gameResetCmdJSONResult = gsonTest.toJson(gameResetCommand);

        System.out.println("Just serialized gameResetCmd, JSONstring result= ");
        System.out.println(gameResetCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, gameResetCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testGameSaveCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMESAVECMD TRANSLATION!");

        String gameSaveCmdJSONResult = gsonTest.toJson(gameSaveCommand);

        System.out.println("Just serialized gameSaveCmd, JSONstring result= ");
        System.out.println(gameSaveCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, gameSaveCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testGetGameCmdsCmdTranslation() throws Exception {
        System.out.println(">TESTING GETCGAMECMDSCMD TRANSLATION!");

        String getGameCmdsCmdJSONResult = gsonTest.toJson(getGameCmdsCommand);

        System.out.println("Just serialized getGameCmdsCmd, JSONstring result= ");
        System.out.println(getGameCmdsCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, getGameCmdsCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testListAICmdTranslation() throws Exception {
        System.out.println(">TESTING LISTAICMD TRANSLATION!");

        String listAICmdJSONResult = gsonTest.toJson(listAICommand);

        System.out.println("Just serialized listAICmd, JSONstring result= ");
        System.out.println(listAICmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, listAICmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testLoginCmdTranslation() throws Exception {
        System.out.println(">TESTING LOGINCMD TRANSLATION!");

        String loginCmdJSONResult = gsonTest.toJson(loginCommand);

        System.out.println("Just serialized loginCmd, JSONstring result= ");
        System.out.println(loginCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, loginCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testRegisterCmdTranslation() throws Exception {
        System.out.println(">TESTING REGISTERCMD TRANSLATION!");

        String registerCmdJSONResult = gsonTest.toJson(registerCommand);

        System.out.println("Just serialized registerCmd, JSONstring result= ");
        System.out.println(registerCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, registerCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testSendChatCmdTranslation() throws Exception {
        System.out.println(">TESTING SENDCHATCMD TRANSLATION!");

        String sendChatCmdJSONResult = gsonTest.toJson(sendChatCommand);

        System.out.println("Just serialized sendChatCmd, JSONstring result= ");
        System.out.println(sendChatCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, sendChatCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testUtilChangeLogLevelCmdTranslation() throws Exception {
        System.out.println(">TESTING UTILCHANGELOGLEVELCMD TRANSLATION!");

        String utilChangeLogLevelCmdJSONResult = gsonTest.toJson(utilChangeLogLevelCommand);

        System.out.println("Just serialized utilChangeLogLevelCmd, JSONstring result= ");
        System.out.println(utilChangeLogLevelCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, utilChangeLogLevelCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }


//TEST MOVES COMMANDS  ===============================

    //GOOD
    public void testAcceptTradeCmdTranslation() throws Exception {
        System.out.println(">TESTING ACCEPTTRADECMD TRANSLATION!");

        JSONObject acceptTradeCmdJSONResult = jsonTranslator.acceptTradeCmdToJSON(acceptTradeCommand);
                // gsonTest.toJson(acceptTradeCommand);

        System.out.println("Just serialized acceptTradeCmd, JSONstring result= ");
        System.out.println(acceptTradeCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{ " +
                                 "\"type\": \"acceptTrade\"," +
                                 "\"playerIndex\": 3," +
                                 "\"willAccept\": true" +
                                 "}" ;

        JSONAssert.assertEquals(expectedResult, acceptTradeCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    public void testBuildCityCmdTranslation() throws Exception {
        System.out.println(">TESTING BUILDCITYCMD TRANSLATION!");

        JSONObject buildCityCmdJSONResult = jsonTranslator.buildCityCmdToJSON(buildCityCommand);
                // gsonTest.toJson(buildCityCommand);

        System.out.println("Just serialized buildCityCmd, JSONstring result= ");
        System.out.println(buildCityCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                             "  \"type\": \"buildCity\",\n" +
                             "  \"playerIndex\": 3,\n" +
                             "  \"vertexLocation\": {\n" +
                                        "       \"x\": 3,\n" +
                                        "       \"y\": 3,\n" +
                                        "       \"direction\": \"SE\"\n" +
                                "   }\n" +
                                "}";

        //in order for the JSON to look exactly like what the server wants it to, you sometimes have to
        //bring up values from nested object to higher up. Example: here I had to take values from VertexObject's
        // EdgeLocation datamem up to be in VertexObject itself so the JSON string would match what the server wants.
        // I don't know if this will cause any problems later.....

        JSONAssert.assertEquals(expectedResult, buildCityCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    public void testBuildRoadCmdTranslation() throws Exception {
        System.out.println(">TESTING BUILDROADCMD TRANSLATION!");

        JSONObject buildRoadCmdJSONResult = jsonTranslator.buildRoadCmdToJSON(buildRoadCommand);
                // gsonTest.toJson(buildRoadCommand);

        System.out.print("Just serialized buildRoadCmd, JSONstring result= ");
        System.out.println(buildRoadCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                "  \"type\": \"buildRoad\",\n" +
                "  \"playerIndex\": 2,\n" +
                "  \"roadLocation\": {\n" +
                "    \"x\": 1,\n" +
                "    \"y\": 1,\n" +
                "    \"direction\": \"NE\"\n" +
                "  },\n" +
                "  \"free\": true\n" +
                "}";
      //  System.out.println(">Expected string result= " + expectedResult);

        //***************
        //use @SerializedName("diryo")  to tell Gson what to set as the key name for that value! yay
        //use transient to tell Gson to skip serializing a field! yay
        //***************

        JSONAssert.assertEquals(expectedResult, buildRoadCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    public void testBuildSettlementCmdTranslation() throws Exception {
        System.out.println(">TESTING BUILDSTLMTCMD TRANSLATION!");

        JSONObject buildStlmtCmdJSONResult = jsonTranslator.buildSettlementCmdToJSON(buildSettlementCommand);
                //gsonTest.toJson(buildSettlementCommand);

        System.out.println("Just serialized buildStlmtCmd, JSONstring result= ");
        System.out.println(buildStlmtCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{" +
                                "\"type\": \"buildSettlement\", " +
                                "\"playerIndex\": 3," +
                                "\"vertexLocation\":" + " { " +
                                        "\"x\": 3," +
                                        "\"y\": 3," +
                                        "\"direction\":" + "SE" +
                                                  "}," +
                                "\"free\": false" +
                                "}";

        JSONAssert.assertEquals(expectedResult, buildStlmtCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testDiscardCmdTranslation() throws Exception {
        System.out.println(">TESTING DISCARDCMD TRANSLATION!");

        String discardCmdJSONResult = gsonTest.toJson(discardCommand);

        System.out.println("Just serialized discardCmd, JSONstring result= ");
        System.out.println(discardCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, discardCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testEndTurnCmdTranslation() throws Exception {
        System.out.println(">TESTING ENDTURNCMD TRANSLATION!");

        String endTurnCmdJSONResult = gsonTest.toJson(endTurnCommand);

        System.out.println("Just serialized endTurnCmd, JSONstring result= ");
        System.out.println(endTurnCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, endTurnCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testMaritimeTradeCmdTranslation() throws Exception {
        System.out.println(">TESTING MARITIMETRADECMD TRANSLATION!");

        String maritimeTradeCmdJSONResult = gsonTest.toJson(maritimeTradeCommand);

        System.out.println("Just serialized maritimeTradeCMD, JSONstring result= ");
        System.out.println(maritimeTradeCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, maritimeTradeCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testOfferTradeCmdTranslation() throws Exception {
        System.out.println(">TESTING OFFERTRADECMD TRANSLATION!");

        String offerTradeCmdJSONResult = gsonTest.toJson(offerTradeCommand);

        System.out.println("Just serialized offerTradeCMD, JSONstring result= ");
        System.out.println(offerTradeCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, offerTradeCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testPlayMonopolyCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYMONOPOLYCMD TRANSLATION!");

        String playMonopolyCmdJSONResult = gsonTest.toJson(playMonopolyCommand);

        System.out.println("Just serialized playMonopolyCmd, JSONstring result= ");
        System.out.println(playMonopolyCmdJSONResult);
        System.out.println("=================");


        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, playMonopolyCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testPlayMonumentCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYMONUMENTCMD TRANSLATION!");

        String playMonumentCmdJSONResult = gsonTest.toJson(playMonumentCommand);

        System.out.println("Just serialized playMonumentCmd, JSONstring result= ");
        System.out.println(playMonumentCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, playMonumentCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testPlayRoadBuilderCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYROADBUILDERCMD TRANSLATION!");

        String playRoadBuilderCmdJSONResult = gsonTest.toJson(playRoadBuilderCommand);

        System.out.println("Just serialized playRoadBuilderCmd, JSONstring result= ");
        System.out.println(playRoadBuilderCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, playRoadBuilderCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testPlaySoldierCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYSOLDIERCMD TRANSLATION!");

        String playSoldierCmdJSONResult = gsonTest.toJson(playSoldierCommand);

        System.out.println("Just serialized playSoldierCmd, JSONstring result= ");
        System.out.println(playSoldierCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, playSoldierCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testPlayYearOfPlentyCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYYEAROFPLENTYCMD TRANSLATION!");

        String playYearOfPlentyCmdJSONResult = gsonTest.toJson(playYearOfPlentyCommand);

        System.out.println("Just serialized playYearOfPlentyCmd, JSONstring result= ");
        System.out.println(playYearOfPlentyCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, playYearOfPlentyCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testPurchaseDevCardCmdTranslation() throws Exception {
        System.out.println(">TESTING PURCHASEDEVDARDCMD TRANSLATION!");

        String purchaseDevCardCmdJSONResult = gsonTest.toJson(purchaseDevCardCommand);

        System.out.println("Just serialized playYearOfPlentyCmd, JSONstring result= ");
        System.out.println(purchaseDevCardCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, purchaseDevCardCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testRobPlayerCmdTranslation() throws Exception {
        System.out.println(">TESTING ROBPLAYERCMD TRANSLATION!");

        String robPlayerCmdJSONResult = gsonTest.toJson(robPlayerCommand);

        System.out.println("Just serialized robPlayerCmd, JSONstring result= ");
        System.out.println(robPlayerCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, robPlayerCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    public void testRollDiceCmdTranslation() throws Exception {
        System.out.println(">TESTING ROLLDICECMD TRANSLATION!");

        String rollDiceCmdJSONResult = gsonTest.toJson(rollDiceCommand);

        System.out.println("Just serialized rollDiceCmd, JSONstring result= ");
        System.out.println(rollDiceCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, rollDiceCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

}