package client_tests;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import shared.definitions.CatanColor;
import shared.definitions.LoggingLevel;
import shared.definitions.ResourceType;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.GameListItem;
import shared.model.JSONTranslator;
import shared.model.commandmanager.BaseCommand;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.VertexObject;
import shared.model.resourcebank.ResourceList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This is the JUnit test class for the JSONTranslator.
 *
 * Created by Sierra on 9/23/16.
 */
public class JSONTranslatorTest extends TestCase {

    private JSONTranslator jsonTranslator = new JSONTranslator();
    private String testResponseModel = null;
    private String testModelString2 = null;
    private String testCommandsListJSON = null;
    private String testGamesListJSON = null;
    private String testGameCreateResponseJSON = null;
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
    private FinishTurnCommand finishTurnCommand;
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


    @Test
    public void setUp() throws Exception {
        super.setUp();

        setUpModelString();
        setUpCommandsListString();
        setUpGamesListString();
        setUpGameCommands();
        setUpMovesCommands();

        testGameCreateResponseJSON = "{" +
                                        "\"title\":" + "\"yoo\"," +
                                        "\"id\":" + 3 + "," +
                                        "\"players\":" +  " [" +
                                            "{}," +
                                            "{}," +
                                            "{}," +
                                            "{}" +
                                        "]" +
                                    " }";
    }

    @Test
    private void setUpGameCommands()
    {
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

        //GAME CREATE CMD SETUP
        //-----------------
        String gCGameName = "bacon";
        Boolean randTiles = true;
        Boolean randNums = true;
        Boolean randPorts = false;
        gameCreateCommand = new GameCreateCommand(gCGameName, randTiles, randNums, randPorts);
        //-----------------

        //GAME JOIN CMD SETUP
        //-----------------
        int gJGameID = 5;
        CatanColor gJColor = CatanColor.BLUE;
        gameJoinCommand = new GameJoinCommand(gJGameID, gJColor);
        //-----------------

        //GAME LOAD CMD SETUP
        //-----------------
        String gLGameName = "bacon";
        gameLoadCommand = new GameLoadCommand(gLGameName);
        //-----------------

        //GAME SAVE CMD SETUP
        //-----------------
        int gSGameID = 10;
        String gSGameName = "BestGame";
        gameSaveCommand = new GameSaveCommand(gSGameID, gSGameName);
        //-----------------

        //LOGIN CMD SETUP
        //-----------------
        String loginUsername = "yoyo";
        String loginPassword = "yoyo";
        loginCommand = new LoginCommand(loginUsername, loginPassword);
        //-----------------

        //REGISTER CMD SETUP
        //-----------------
        String regUsername = "yoyo";
        String regPassword = "yoyo";
        registerCommand = new RegisterCommand(regUsername, regPassword);
        //-----------------

        //SEND CHAT CMD SETUP
        //-----------------
        int chatPIndex = 12;
        String chatContent = "bacon yo";
        sendChatCommand = new SendChatCommand(chatPIndex, chatContent);
        //-----------------

        //CHANGE LOG LEVEL CMD SETUP
        //-----------------
        utilChangeLogLevelCommand = new UtilChangeLogLevelCommand(LoggingLevel.FINE);
        //-----------------


        //FINISH TURN CMD SETUP
        //-----------------
        int fTPIndex = 3;
        finishTurnCommand = new FinishTurnCommand(fTPIndex);
        //-----------------

    }

    @Test
    private void setUpMovesCommands()
    {
        //ACCEPT TRADE CME SETUP
        //-----------------
        int pIndex = 3;
        acceptTradeCommand = new AcceptTradeCommand(pIndex, true);
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
        VertexObject cityVtx = new VertexObject(bCVertexLocation);
        cityVtx.setOwner(bCOwner);
        buildCityCommand = new BuildCityCommand(cityVtx);
        //-----------------

        //BUILD SETTLEMENT CMD SETUP
        //-----------------
        int bSOwner = 3;
        HexLocation bSELHexLoc = new HexLocation(3, 3);
        VertexDirection bSVertexDir = VertexDirection.SouthEast;
        VertexLocation bSVertexLocation = new VertexLocation(bSELHexLoc, bSVertexDir);
        VertexObject stlmtVtx = new VertexObject(bSVertexLocation);
        stlmtVtx.setOwner(bSOwner);
        buildSettlementCommand = new BuildSettlementCommand(stlmtVtx);
        buildSettlementCommand.setFree(false);
        //----------------

        //DISCARD CARDS CMD SETUP
        //-----------------
        int dcPIndex = 3;
        //RL: int woodCardCount, int brickCardCount, int sheepCardCount, int wheatCardCount, int oreCardCount
        ResourceList dCRL = new ResourceList(0,1,0,2,0);
        discardCommand = new DiscardCommand(dcPIndex, dCRL);
        //-----------------

        //MARITIME TRADE CMD SETUP
        //-----------------
        int mtPIndex = 1;
        int mtRatio = 3;
        ResourceType iR = ResourceType.WOOD;
        ResourceType oR = ResourceType.WHEAT;
        maritimeTradeCommand = new MaritimeTradeCommand(mtPIndex, mtRatio, iR, oR);
        //-----------------

        //OFFER TRADE CMD SETUP
        //-----------------
        int otPIndex = 2;
        int otReceiver = 1;
        //RL: int woodCardCount, int brickCardCount, int sheepCardCount, int wheatCardCount, int oreCardCount
        ResourceList oTRL = new ResourceList(0,2,0,0,-3);
        offerTradeCommand = new OfferTradeCommand(otPIndex, oTRL, otReceiver);
        //-----------------

        //PLAY MONOPOLY CMD SETUP
        //-----------------
        int pMpPIndex = 1;
        ResourceType pMpRt = ResourceType.BRICK;
        playMonopolyCommand = new PlayMonopolyCommand(pMpPIndex, pMpRt);
        //-----------------

        //PLAY MONUMENT CMD SETUP
        //-----------------
        int pMmPIndex = 2;
        playMonumentCommand = new PlayMonumentCommand(pMmPIndex);
        //-----------------

        //PLAY ROADBUILDING CMD SETUP
        //-----------------
        int pRbPIndex = 3;
        HexLocation rBLoc1HL = new HexLocation(1,2);
        EdgeDirection rBLoc1ED = EdgeDirection.North;  //I'm not thinking about whether these all make sense together haha
        EdgeLocation rBLoc1 = new EdgeLocation(rBLoc1HL, rBLoc1ED);
        HexLocation rBLoc2HL = new HexLocation(1,1);
        EdgeDirection rBLoc2ED = EdgeDirection.NorthEast;
        EdgeLocation rBLoc2 = new EdgeLocation(rBLoc2HL, rBLoc2ED);
        playRoadBuilderCommand = new PlayRoadBuilderCommand(pRbPIndex, rBLoc1, rBLoc2);
        //-----------------

        //PLAY SOLDIER  CMD SETUP
        //-----------------
        int pSPIndex = 1;
        int pSVIndex = 3;
        HexLocation pSHL = new HexLocation(-1,3);
        playSoldierCommand = new PlaySoldierCommand(pSPIndex,pSHL, pSVIndex);
        //-----------------

        //PLAY YEAROFPLENTY CMD SETUP
        //-----------------
        int pYOPIndex = 0;
        ResourceType yopR1 = ResourceType.ORE;
        ResourceType yopR2 = ResourceType.SHEEP;
        playYearOfPlentyCommand = new PlayYearOfPlentyCommand(pYOPIndex, yopR1, yopR2);
        //-----------------

        //GAME JOIN CMD SETUP
        //-----------------
        int bDCPID = 0;
        purchaseDevCardCommand = new PurchaseDevCardCommand(bDCPID);
        //-----------------

        //ROB PLAYER CMD SETUP
        //-----------------
        int rpPID = 3;
        int rpVID = 2;
        HexLocation rPHL = new HexLocation(-2,2);
        robPlayerCommand = new RobPlayerCommand(rpPID, rPHL, rpVID);
        //-----------------

        //ROLL NUMBER SETUP
        //-----------------
        int rolledNum = 6;
        rollDiceCommand = new RollDiceCommand(rolledNum);
        rollDiceCommand.setPlayerIndex(1);
        //-----------------

    }

    @Test
    private void setUpModelString() {
        // This is an actual server response body:
        testResponseModel = " {\n" +
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
                "        \"resource\": \"sheep\",\n" +   //regular hex - has resource AND number
                "        \"location\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": -2\n" +
                "        },\n" +
                "        \"number\": 12\n" +
                "      },\n" +
                "      {\n" +
                "        \"location\": {\n" +     // no resource = desert hex
                "          \"x\": 1,\n" +
                "          \"y\": -2\n" +
                "        },\n" +
                "        \"number\": 8\n" +
                "      },\n" +
                "      {\n" +
                "        \"location\": {\n" +   //no resource OR number = ocean/water hex
                "          \"x\": 2,\n" +
                "          \"y\": -2\n" +
                "        }\n" +
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
                "    ],\n" +                    /// ==================================END HEXES
                "    \"roads\": [\n" +
                "       {\n" +
                "           \"owner\": 1,\n" +
                "           \"location\": {\n" +
                "                   \"direction\": \"SE\",\n" +
                "                   \"x\": 0,\n" +
                "                   \"y\": -1\n" +
                "               }\n" +
                "       },\n" +
                "       {\n" +
                "           \"owner\": 4,\n" +
                "           \"location\": {\n" +
                "                   \"direction\": \"NW\",\n" +
                "                   \"x\": 2,\n" +
                "                   \"y\": -2\n" +
                "               }\n" +
                "       }\n" +
                "    ],\n" +             /// ==================================END ROADS
                "    \"cities\": [\n" +
                "       {\n" +
                "        \"owner\": 4,\n" +
                "        \"location\": {\n" +
                "                \"direction\": \"NW\",\n" +
                "                \"x\": 2,\n" +
                "                \"y\": -2\n" +
                "                }\n" +
                "        },\n" +
                "       {\n" +
                "        \"owner\": 1,\n" +
                "        \"location\": {\n" +
                "                \"direction\": \"NE\",\n" +
                "                \"x\": -1,\n" +
                "                \"y\": -1\n" +
                "                }\n" +
                "        }\n" +
                "    ],\n" +              /// ==================================END CITIES
                "    \"settlements\": [\n" +
                "       {\n" +
                "        \"owner\": 1,\n" +
                "        \"location\": {\n" +
                "                \"direction\": \"SW\",\n" +
                "                \"x\": 1,\n" +
                "                \"y\": -2\n" +
                "                }\n" +
                "        },\n" +
                "       {\n" +
                "        \"owner\": 3,\n" +
                "        \"location\": {\n" +
                "                \"direction\": \"SE\",\n" +
                "                \"x\": -1,\n" +
                "                \"y\": 0\n" +
                "                }\n" +
                "        }\n" +
                "    ],\n" +              /// ==================================END SETTLEMENTS
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
                "  },\n" +           /// ==================================END PORTS
                "  \"players\": [\n" +
                "    {\n" +
                "      \"resources\": {\n" +
                "        \"brick\": 3,\n" +
                "        \"wood\": 3,\n" +
                "        \"sheep\": 3,\n" +
                "        \"wheat\": 3,\n" +
                "        \"ore\": 3\n" +
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
                "      \"playerID\": 2,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"bacon\",\n" +
                "      \"color\": \"blue\"\n" +
                "    },\n" +  //--------------------------------------- END PLAYER 1
                "    {\n" +
                "      \"resources\": {\n" +
                "        \"brick\": 3,\n" +
                "        \"wood\": 2,\n" +
                "        \"sheep\": 10,\n" +
                "        \"wheat\": 1,\n" +
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
                "      \"playerID\": 5,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"petey\",\n" +
                "      \"color\": \"purple\"\n" +
                "    },\n" + //--------------------------------------- END PLAYER 2
                "    {\n" +
                "      \"resources\": {\n" +
                "        \"brick\": 2,\n" +
                "        \"wood\": 1,\n" +
                "        \"sheep\": 0,\n" +
                "        \"wheat\": 4,\n" +
                "        \"ore\": 1\n" +
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
                "      \"playerID\": 4,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"WHOA\",\n" +
                "      \"color\": \"red\"\n" +
                "    },\n" + //--------------------------------------- END PLAYER 3
                "    {\n" +
                "      \"resources\": {\n" +
                "        \"brick\": 6,\n" +
                "        \"wood\": 6,\n" +
                "        \"sheep\": 6,\n" +
                "        \"wheat\": 6,\n" +
                "        \"ore\": 6\n" +
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
                "      \"playerID\": 15,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"HEY\",\n" +
                "      \"color\": \"green\"\n" +
                "    },\n" + //--------------------------------------- END PLAYER 4
                "  ],\n" +         //===========================================================
                "    \"tradeOffer\": " + "{\n" +
                "       \"sender\": 8,\n" +
                "       \"receiver\": 4,\n" +
                "       \"offer\":" + "{\n" +
                "           \"brick\": 0,\n" +
                "           \"ore\": 1,\n" +
                "           \"sheep\": 0,\n" +
                "           \"wheat\": -2,\n" +
                "           \"wood\": 0,\n" +
                "            }\n" +
                "      },\n" +
                "  \"log\": {\n" +
                "    \"lines\": [" +
                "       {" +
                "           \"message\": \"LOG YO\"," +
                "           \"source\": \"baconl\"" +
                "       }," +
                "       {" +
                "           \"message\": \"WHADDUP\"," +
                "           \"source\": \"bacony\"" +
                "       }" +
                "]\n" +
                "  },\n" +
                "  \"chat\": {\n" +
                "    \"lines\": [" +
                "       {" +
                "        \"message\": \"CHAT YO\"," +
                "        \"source\": \"baconc\"" +
                "       }," +
                "       {" +
                "           \"message\": \"YOUR MOM\"," +
                "           \"source\": \"baconz\"" +
                "       }" +
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

    @Test
    private void setUpCommandsListString() {
        testCommandsListJSON = "[\n" +
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
    }
    @Test
    private void setUpGamesListString(){
        testGamesListJSON = "[" +
                        "{" +
                        "\"title\":" + "\"Default Game\"," +
                        "\"id\":" + 0 + "," +
                        "\"players\":" + "[" +
                                "{" +
                                    "\"color\":" + "\"orange\"," +
                                    "\"name\":" + "\"Sam\"," +
                                    "\"id\":" + 0 +
                                "}, " +
                                "{" +
                                    "\"color\":" + "\"blue\"," +
                                    "\"name\":" + "\"Brooke\"," +
                                    "\"id\":" + 1 +
                                "}, " +
                                "{" +
                                    "\"color\":" + "\"red\"," +
                                    "\"name\":" + "\"Pete\"," +
                                    "\"id\":" + 10 +
                                "}, " +
                                "{" +
                                    "\"color\":" + "\"green\"," +
                                    "\"name\":" + "\"Mark\"," +
                                    "\"id\":" + 11 +
                                "} " +
                              "]" +
                        "}," +

                        "{" +
                        "\"title\":" + "\"AI Game\"," +
                        "\"id\":" + 1 + "," +
                        "\"players\":" + "[" +
                                    "{" +
                                        "\"color\":" + "\"orange\"," +
                                        "\"name\":" + "\"Pete\"," +
                                        "\"id\":" + 10 +
                                    "}, " +
                                    "{" +
                                        "\"color\":" + "\"yellow\"," +
                                        "\"name\":" + "\"Squall\"," +
                                        "\"id\":" + -2 +
                                    "}, " +
                                    "{" +
                                        "\"color\":" + "\"red\"," +
                                        "\"name\":" + "\"Ken\"," +
                                        "\"id\":" + -3 +
                                    "}, " +
                                    "{" +
                                        "\"color\":" + "\"green\"," +
                                        "\"name\":" + "\"Miguel\"," +
                                        "\"id\":" + -4 +
                                    "} " +
                                "]" +
                        "}," +

                        "{" +
                        "\"title\":" + "\"Best Game\"," +
                        "\"id\":" + 2 + "," +
                        "\"players\":" + "[" +
                                    "{" +
                                        "\"color\":" + "\"blue\"," +
                                        "\"name\":" + "\"yo\"," +
                                        "\"id\":" + 12 +
                                    "}, " +
                                    "{" +
                                        "\"color\":" + "\"orange\"," +
                                        "\"name\":" + "\"Squall\"," +
                                        "\"id\":" + -2 +
                                    "}, " +
                                    "{" +
                                        "\"color\":" + "\"green\"," +
                                        "\"name\":" + "\"Quinn\"," +
                                        "\"id\":" + -3 +
                                    "}, " +
                                    "{" +
                                    "} " +
                            "]" +
                        "}" +

                    "]";

    }

    /**
     * @throws Exception
     */
    @Test
    public void testModelTranslation() throws Exception {
        System.out.println(">I'M TESTING MODEL TRANSLATION from JSON!");

        JSONObject newClientModelJSON = new JSONObject(testResponseModel);

        ClientModel newCM = jsonTranslator.modelFromJSON(newClientModelJSON);





        //serialize it again and compare to the original JSON model string
       // String newClientModelSerializedAgain = gsonTest.toJson(newClientModel);

       // JSONAssert.assertEquals(testResponseModel, newClientModelSerializedAgain, false);

    }


//TEST GAME COMMANDS  ===============================

    //GOOD
    @Test
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

    //GOOD
    @Test
    public void testGameCreateCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMECREATECMD TRANSLATION!");

        JSONObject gameCreateCmdJSONResult = jsonTranslator.gameCreateCmdToJSON(gameCreateCommand);
                // gsonTest.toJson(gameCreateCommand);

        System.out.println("Just serialized gameCreateCmd, JSONstring result= ");
        System.out.println(gameCreateCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"randomTiles\":" + true + ",\n" +
                                "\"randomNumbers\":" + true + ",\n" +
                                "\"randomPorts\":" + false + ",\n" +
                                "\"name\":" + "\"bacon\"\n" +
                                "}";

        JSONAssert.assertEquals(expectedResult, gameCreateCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testGameCreateResponseFromJSON() throws Exception {
        System.out.println(">TESTING GAMECREATERESPONSE TRANSLATION!");

        JSONObject gameCreateResponse = new JSONObject(testGameCreateResponseJSON);
        GameListItem newGameLI = jsonTranslator.gameCreateResponseFromJSON(gameCreateResponse);

        //asserts
        assertEquals("yoo", newGameLI.getTitle());
        assertEquals(3, newGameLI.getGameID());
    }

    //GOOD
    //the GameList command isn't actually a CommandObject, just a URL command.
    //But the server returns a JSONArray of Games after being asked for a list of all games
    //that we ONLY use to display the list of all available games for the user to join.
    //(as far as I know)
    @Test
    public void testGamesListResponseFromJSON() throws Exception{
        System.out.println(">TESTING GAMELISTRESPONSE TRANSLATION!");

        JSONArray testGamesListJSONArr = new JSONArray(testGamesListJSON);

        ArrayList<GameListItem> gLIArrayResult = jsonTranslator.gamesListResponseFromJSON(testGamesListJSONArr);
        System.out.println(">gLIArrayResult size= " + gLIArrayResult.size());

        //asserts
        assertEquals(gLIArrayResult.size(), 3);
        assertEquals(4, gLIArrayResult.get(0).getPlayers().size());
        assertEquals(4, gLIArrayResult.get(1).getPlayers().size());
        assertEquals(4, gLIArrayResult.get(2).getPlayers().size()); //the 4th one is null
    }
    //GOOD
    @Test
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
                        "\"color\": \"blue\"" +
                        " }\n";

        JSONAssert.assertEquals(expectedResult, gameJoinCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testGameLoadCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMELOADCMD TRANSLATION!");

        JSONObject gameLoadCmdJSONResult = jsonTranslator.gameLoadCmdToJSON(gameLoadCommand);
                // gsonTest.toJson(gameLoadCommand);

        System.out.println("Just serialized gameLoadCmd, JSONstring result= ");
        System.out.println(gameLoadCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                    "\"name\":" + "bacon\n" +
                                "}";

        JSONAssert.assertEquals(expectedResult, gameLoadCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testGameSaveCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMESAVECMD TRANSLATION!");

        JSONObject gameSaveCmdJSONResult = jsonTranslator.gameSaveCmdToJSON(gameSaveCommand);
                //gsonTest.toJson(gameSaveCommand);

        System.out.println("Just serialized gameSaveCmd, JSONstring result= ");
        System.out.println(gameSaveCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"id\":" + 10 + ",\n" +
                                "\"name\":" + "BestGame" +
                                "}";

        JSONAssert.assertEquals(expectedResult, gameSaveCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }


    //GOOD
    //the GET game cmds doesn't acutally need to send any JSON to the server,
    // it just uses the URL to ask it to send back a list of all the commands that
    // have happened so far.
    //So the Translator can have a function to parse that big JSONArray response from the server
    //that returns an arraylist of BaseCommand objects that have been executed so far,
    //but the actual getGameCommandsCommandObj doesn't need to be translated.
    @Test
    public void testCmdsListFromJSON() throws Exception{
        System.out.println(">TESTING CMDLIST>from<JSON TRANSLATION!");

        JSONArray testCmdsList = new JSONArray(testCommandsListJSON);

        List<BaseCommand> resultList = jsonTranslator.commandsListFromJSON(testCmdsList);

        assertEquals(6, resultList.size());
        assertEquals(BuildRoadCommand.class, resultList.get(0).getClass());
        assertEquals(BuildSettlementCommand.class, resultList.get(1).getClass());
        assertEquals(FinishTurnCommand.class, resultList.get(2).getClass());
        assertEquals(BuildRoadCommand.class, resultList.get(3).getClass());
        assertEquals(BuildSettlementCommand.class, resultList.get(4).getClass());
        assertEquals(FinishTurnCommand.class, resultList.get(5).getClass());

        //so I don't have to build a complete list of CommandObjects to pass in:
        testCmdsListToJSON(resultList);
    }

    //GOOD
    //this translation is used when posting the list of exec'd commands to the server
    @Test
    public void testCmdsListToJSON(List<BaseCommand> allCmdObjsList) throws Exception {
        System.out.println(">TESTING CMDLIST>to<JSON TRANSLATION!");

        JSONArray cmdsListJSONArrResult = jsonTranslator.commandsListToJSON(allCmdObjsList);
        String cmdsListStringResult = cmdsListJSONArrResult.toString();
        System.out.println(">JSONRESULT: " + cmdsListStringResult);

        JSONAssert.assertEquals(testCommandsListJSON, cmdsListStringResult, false);
    }


    //GOOD
    //this function translates the JSON string array into a list of available AIs (strings)
    //Swagger page says that LARGEST_ARMY is the only supported AI type right now
    //the server executes this command without any JSON in the request body, just the URL
    @Test
    public void testListAICmdTranslation() throws Exception {
        System.out.println(">>TESTING LISTAICMD TRANSLATION!");

        String strExpectedResponse = "["+
                                        "\"LARGEST_ARMY\""+ "," +
                                        "\"YOUR_MOM\""+ "," +
                                        "\"WHADDUP\"" +
                                        "]";

       // System.out.println(">>strExpectedResponse = " + strExpectedResponse);

        JSONArray jsonExpectedResponse = new JSONArray(strExpectedResponse);

        ArrayList<String> allAIsList = jsonTranslator.listAIResponseFromJSON(jsonExpectedResponse);

        //there should really only be one AI available, but we're planning for more
        assertEquals(3, allAIsList.size());
        assertEquals(allAIsList.get(0), "LARGEST_ARMY");
    }

    //GOOD
    @Test
    public void testLoginCmdTranslation() throws Exception {
        System.out.println(">TESTING LOGINCMD TRANSLATION!");

        JSONObject loginCmdJSONResult = jsonTranslator.loginCmdToJSON(loginCommand);
                //gsonTest.toJson(loginCommand);

        System.out.println("Just serialized loginCmd, JSONstring result= ");
        System.out.println(loginCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"username\":" + "\"yoyo\"," +
                                "\"password\":" + "\"yoyo\"" +
                                " }" ;

        JSONAssert.assertEquals(expectedResult, loginCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testRegisterCmdTranslation() throws Exception {
        System.out.println(">TESTING REGISTERCMD TRANSLATION!");

        JSONObject registerCmdJSONResult = jsonTranslator.registerCmdToJSON(registerCommand);
                // gsonTest.toJson(registerCommand);

        System.out.println("Just serialized registerCmd, JSONstring result= ");
        System.out.println(registerCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                            "\"username\":" + "\"yoyo\"," +
                            "\"password\":" + "\"yoyo\"" +
                            " }" ;

        JSONAssert.assertEquals(expectedResult, registerCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testSendChatCmdTranslation() throws Exception {
        System.out.println(">TESTING SENDCHATCMD TRANSLATION!");

        JSONObject sendChatCmdJSONResult = jsonTranslator.sendChatCmdToJSON(sendChatCommand);
                // gsonTest.toJson(sendChatCommand);

        System.out.println("Just serialized sendChatCmd, JSONstring result= ");
        System.out.println(sendChatCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                 "\"type\":" + "\"sendChat\",\n" +
                                 "\"playerIndex\":" + 12 + ",\n" +
                                 "\"content\":" + "\"bacon yo\"" +
                                "}";

        JSONAssert.assertEquals(expectedResult, sendChatCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testUtilChangeLogLevelCmdTranslation() throws Exception {
        System.out.println(">TESTING UTILCHANGELOGLEVELCMD TRANSLATION!");

        JSONObject utilChangeLogLevelCmdJSONResult = jsonTranslator.utilChangeLogLevelCmdToJSON(utilChangeLogLevelCommand);
                //gsonTest.toJson(utilChangeLogLevelCommand);

        System.out.println("Just serialized utilChangeLogLevelCmd, JSONstring result= ");
        System.out.println(utilChangeLogLevelCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"logLevel\":" + "\"FINE\"" +
                                "}";

        JSONAssert.assertEquals(expectedResult, utilChangeLogLevelCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }


//TEST MOVES COMMANDS  ===============================

    //GOOD
    @Test
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
    @Test
    public void testBuildCityCmdTranslation() throws Exception {
        System.out.println(">TESTING BUILDCITYCMD TRANSLATION!");

        JSONObject buildCityCmdJSONResult = jsonTranslator.buildCityCmdToJSON(buildCityCommand);
                // gsonTest.toJson(buildCityCommand);

        System.out.println("Just serialized buildCityCmd, JSONstring result= ");
        System.out.println(buildCityCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                             "  \"type\": \"buildCity\",\n" +
                             "  \"playerIndex\": 2,\n" +
                             "  \"vertexLocation\": {\n" +
                                        "       \"x\": 2,\n" +
                                        "       \"y\": 2,\n" +
                                        "       \"direction\": \"NE\"\n" +
                                "   }\n" +
                                "}";

        //in order for the JSON to look exactly like what the server wants it to, you sometimes have to
        //bring up values from nested object to higher up. Example: here I had to take values from VertexObject's
        // EdgeLocation datamem up to be in VertexObject itself so the JSON string would match what the server wants.
        // I don't know if this will cause any problems later.....

        JSONAssert.assertEquals(expectedResult, buildCityCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
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
    @Test
    public void testBuildSettlementCmdTranslation() throws Exception {
        System.out.println(">TESTING BUILDSTLMTCMD TRANSLATION!");

        JSONObject buildStlmtCmdJSONResult = jsonTranslator.buildSettlementCmdToJSON(buildSettlementCommand);
                //gsonTest.toJson(buildSettlementCommand);

        System.out.println("Just serialized buildStlmtCmd, JSONstring result= ");
        System.out.println(buildStlmtCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"type\": \"buildSettlement\", \n" +
                                "\"playerIndex\": 3,\n" +
                                "\"vertexLocation\":" + " {\n" +
                                        "\"x\": 3,\n" +
                                        "\"y\": 3,\n" +
                                        "\"direction\":" + "SE\n" +
                                                  "},\n" +
                                "\"free\": false\n" +
                                "}";

        JSONAssert.assertEquals(expectedResult, buildStlmtCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testDiscardCmdTranslation() throws Exception {
        System.out.println(">TESTING DISCARDCMD TRANSLATION!");

        JSONObject discardCmdJSONResult = jsonTranslator.discardCmdToJSON(discardCommand);
                //gsonTest.toJson(discardCommand);

        System.out.println("Just serialized discardCmd, JSONstring result= ");
        System.out.println(discardCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"type\":" + "\"discardCards\",\n" +
                                "\"playerIndex\": 3,\n" +
                                "\"discardedCards\":" + "{ \n" +
                                    "\"wood\": 0,\n" +
                                    "\"brick\": 1,\n" +
                                    "\"sheep\": 0,\n" +
                                    "\"wheat\": 2,\n" +
                                    "\"ore\": 0,\n" +
                                     " }\n"+
                                "}";

        JSONAssert.assertEquals(expectedResult, discardCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testFinishTurnCmdTranslation() throws Exception {
        System.out.println(">TESTING FINISHTURNCMD TRANSLATION!");

        String finishTurnCmdJSONResult = gsonTest.toJson(finishTurnCommand);

        System.out.println("Just serialized finishTurnCmd, JSONstring result= ");
        System.out.println(finishTurnCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{" +
                                    "\"type\":" + "\"finishTurn\", " +
                                    "\"playerIndex\":" + 3 +
                                  "}";

        JSONAssert.assertEquals(expectedResult, finishTurnCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testMaritimeTradeCmdTranslation() throws Exception {
        System.out.println(">TESTING MARITIMETRADECMD TRANSLATION!");

        JSONObject maritimeTradeCmdJSONResult = jsonTranslator.maritimeTradeCmdToJSON(maritimeTradeCommand);
                // gsonTest.toJson(maritimeTradeCommand);

        System.out.println("Just serialized maritimeTradeCMD, JSONstring result= ");
        System.out.println(maritimeTradeCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                 "\"type\":" +  "\"maritimeTrade\",\n"+
                                 "\"playerIndex\": 1,\n" +
                                 "\"ratio\": 3,\n" +
                                 "\"inputResource\": wood,\n" +
                                 "\"outputResource\": wheat\n" +
                                 " }";

        JSONAssert.assertEquals(expectedResult, maritimeTradeCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testOfferTradeCmdTranslation() throws Exception {
        System.out.println(">TESTING OFFERTRADECMD TRANSLATION!");

        JSONObject offerTradeCmdJSONResult = jsonTranslator.offerTradeCmdToJSON(offerTradeCommand);
                // gsonTest.toJson(offerTradeCommand);

        System.out.println("Just serialized offerTradeCMD, JSONstring result= ");
        System.out.println(offerTradeCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                    "\"type\": offerTrade,\n" +
                                    "\"playerIndex\": 2," +
                                    "\"offer\":" + "{\n" +
                                        "\"brick\": 2\n," +
                                        "\"ore\": -3\n," +
                                        "\"sheep\": 0\n," +
                                        "\"wheat\": 0\n," +
                                        "\"wood\": 0\n," +
                                            "}," +
                                    "\"receiver\": 1\n" +
                                 "}";

        JSONAssert.assertEquals(expectedResult, offerTradeCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testPlayMonopolyCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYMONOPOLYCMD TRANSLATION!");

        JSONObject playMonopolyCmdJSONResult = jsonTranslator.playMonopolyCmdToJSON(playMonopolyCommand);
                // gsonTest.toJson(playMonopolyCommand);

        System.out.println("Just serialized playMonopolyCmd, JSONstring result= ");
        System.out.println(playMonopolyCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                   "\"type\": Monopoly,\n" +
                                   "\"resource\": brick,\n" +
                                   "\"playerIndex\": 1\n" +
                                 "}";

        JSONAssert.assertEquals(expectedResult, playMonopolyCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testPlayMonumentCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYMONUMENTCMD TRANSLATION!");

        JSONObject playMonumentCmdJSONResult = jsonTranslator.playMonumentCmdToJSON(playMonumentCommand);
                // gsonTest.toJson(playMonumentCommand);

        System.out.println("Just serialized playMonumentCmd, JSONstring result= ");
        System.out.println(playMonumentCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                    "\"type\": Monument,\n" +
                                    "\"playerIndex\": 2" +
                                 "}";

        JSONAssert.assertEquals(expectedResult, playMonumentCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testPlayRoadBuilderCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYROADBUILDERCMD TRANSLATION!");

        JSONObject playRoadBuilderCmdJSONResult = jsonTranslator.playRoadBuilderCmdToJSON(playRoadBuilderCommand);
                // gsonTest.toJson(playRoadBuilderCommand);

        System.out.println("Just serialized playRoadBuilderCmd, JSONstring result= ");
        System.out.println(playRoadBuilderCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"type\": Road_Building,\n " +
                                "\"playerIndex\": 3,\n " +
                                "\"spot1\":" + " {\n" +
                                    "\"x\": 1,\n" +
                                    "\"y\": 2,\n" +
                                    "\"direction\":" + "N\n" +
                                        "},\n" +
                                "\"spot2\":" + "{\n" +
                                    "\"x\": 1,\n" +
                                    "\"y\": 1,\n" +
                                    "\"direction\":" + "NE\n" +
                                        "},\n" +
                                "}";

        JSONAssert.assertEquals(expectedResult, playRoadBuilderCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testPlaySoldierCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYSOLDIERCMD TRANSLATION!");

        JSONObject playSoldierCmdJSONResult = jsonTranslator.playSoldierCmdToJSON(playSoldierCommand);
                //gsonTest.toJson(playSoldierCommand);

        System.out.println("Just serialized playSoldierCmd, JSONstring result= ");
        System.out.println(playSoldierCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                 "\"type\": \"Soldier\"," +
                                 "\"playerIndex\": 1," +
                                 "\"victimIndex\": 3," +
                                 "\"location\":" + " {\n" +
                                    "\"x\": -1,\n" +
                                    "\"y\": 3,\n" +
                                             "}\n" +
                                "}";

        JSONAssert.assertEquals(expectedResult, playSoldierCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testPlayYearOfPlentyCmdTranslation() throws Exception {
        System.out.println(">TESTING PLAYYEAROFPLENTYCMD TRANSLATION!");

        JSONObject playYearOfPlentyCmdJSONResult = jsonTranslator.playYearOfPlentyCmdToJSON(playYearOfPlentyCommand);
                // gsonTest.toJson(playYearOfPlentyCommand);

        System.out.println("Just serialized playYearOfPlentyCmd, JSONstring result= ");
        System.out.println(playYearOfPlentyCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"type\":" + "\"Year_of_Plenty\",\n" +
                                "\"playerIndex\":" + 0 + ",\n" +
                                "\"resource1\":" + "\"ore\",\n" +
                                "\"resource2\":" + "\"sheep\",\n" +
                                "}";

        JSONAssert.assertEquals(expectedResult, playYearOfPlentyCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testPurchaseDevCardCmdTranslation() throws Exception {
        System.out.println(">TESTING PURCHASEDEVDARDCMD TRANSLATION!");

        JSONObject purchaseDevCardCmdJSONResult = jsonTranslator.purchaseDevDardCmdToJSON(purchaseDevCardCommand);
                // gsonTest.toJson(purchaseDevCardCommand);

        System.out.println("Just serialized playYearOfPlentyCmd, JSONstring result= ");
        System.out.println(purchaseDevCardCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"type\":" + "\"buyDevCard\"," +
                                "\"playerIndex\":" + 0 +
                                "}";

        JSONAssert.assertEquals(expectedResult, purchaseDevCardCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testRobPlayerCmdTranslation() throws Exception {
        System.out.println(">TESTING ROBPLAYERCMD TRANSLATION!");

        String robPlayerCmdJSONResult = gsonTest.toJson(robPlayerCommand);

        System.out.println("Just serialized robPlayerCmd, JSONstring result= ");
        System.out.println(robPlayerCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"type\":" + "\"robPlayer\",\n" +
                                "\"playerIndex\":" + 3 + ",\n" +
                                "\"victimIndex\":" + 2 + ",\n" +
                                "\"location\":" + "{\n" +
                                    "\"x\":" + "-2,\n" +
                                    "\"y\":" + "2\n" +
                                            "}\n" +
                                    "}";

        JSONAssert.assertEquals(expectedResult, robPlayerCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    //GOOD
    @Test
    public void testRollDiceCmdTranslation() throws Exception {
        System.out.println(">TESTING ROLLDICECMD TRANSLATION!");

        JSONObject rollDiceCmdJSONResult = jsonTranslator.rollDiceCmdToJSON(rollDiceCommand);
                //gsonTest.toJson(rollDiceCommand);

        System.out.println("Just serialized rollDiceCmd, JSONstring result= ");
        System.out.println(rollDiceCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                                "\"type\":"  + "\"rollNumber\",\n" +
                                "\"playerIndex\":" + 1 + ",\n" +
                                "\"number\":" + 6 + ",\n" +
                                    "}";

        JSONAssert.assertEquals(expectedResult, rollDiceCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
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

    //GameReset doesn't have a HTTP body request, it just uses the URL
    /*
    public void testGameResetCmdTranslation() throws Exception {
        System.out.println(">TESTING GAMERESETCMD TRANSLATION!");

        String gameResetCmdJSONResult = gsonTest.toJson(gameResetCommand);

        System.out.println("Just serialized gameResetCmd, JSONstring result= ");
        System.out.println(gameResetCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, gameResetCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }
    /*


        /*
    public void testGetGameCmdsCmdTranslation() throws Exception {
        //System.out.println(">TESTING GETCGAMECMDSCMD TRANSLATION!");

        String getGameCmdsCmdJSONResult = gsonTest.toJson(getGameCmdsCommand);

        System.out.println("Just serialized getGameCmdsCmd, JSONstring result= ");
        System.out.println(getGameCmdsCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "";  //get this from server

        JSONAssert.assertEquals(expectedResult, getGameCmdsCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }
    */


    //we don't actually need to translate the ExecGameCommands Cmd object.
    //The list of exec'd commands will come back as a JSON list of lots of different types of commands.
    /*
    public void testExecGameCmdsCmdTranslation() throws Exception {
        System.out.println(">TESTING EXECGAMECMDSCMD TRANSLATION!");

        String execGameCmdsCmdJSONResult = gsonTest.toJson(execGameCmdsCommand);

        System.out.println("Just serialized execGameCmdsCmd, JSONArray result= ");
        System.out.println(execGameCmdsCmdJSONResult);
        System.out.println("=================");

        //JSONArray jsonArrayResult = jsonTranslator.execGameCmdsCmdToJSON(List<BaseCommand>);

        // the server returns the model updated to follow what the list of exec'd game commands said.
        // so I need to translate into a JSONArray the list of all command objects that have been executed so far
        // from the CommandManager's list of BaseCommandObjects.

      //  JSONAssert.assertEquals(expectedResult, execGameCmdsCmdJSONResult, JSONCompareMode.NON_EXTENSIBLE);
    }
    */
}