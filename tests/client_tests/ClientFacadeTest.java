package client_tests;

import client.ClientFacade;
import client.IServerProxy;
import client.MockProxy;
import client.ServerProxy;
import com.google.gson.Gson;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.LoggingLevel;
import shared.definitions.ResourceType;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.GameListItem;
import shared.model.GameListPlayerItem;
import shared.model.JSONTranslator;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;
import shared.model.map.VertexObject;
import shared.model.resourcebank.ResourceList;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Alise on 9/29/2016.
 */
public class ClientFacadeTest extends TestCase {
    private ClientModel model = new ClientModel(0);
    private IServerProxy mockProxy = new MockProxy();
    private ClientFacade mockClientFacade = new ClientFacade(mockProxy, model);
    private IServerProxy proxy = new ServerProxy();
    private ClientFacade clientFacade = new ClientFacade(proxy, model);

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

//Setup for testing
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

    //Test Functions
    @Test
    public void userLogin() throws Exception {
        //correct login
        LoginCommand command = new LoginCommand("Sam", "sam");
        assert(clientFacade.userLogin(command));

        //incorrect password/username combination
        LoginCommand command2 = new LoginCommand("Sam", "password");
        assert(!clientFacade.userLogin(command2));
    }

    @Test
    public void userRegister() throws Exception {
        //correct registration
        LoginCommand command = new LoginCommand("Harry", "potter");
        assert(clientFacade.userLogin(command));

        //duplicate registration
        assert(!clientFacade.userLogin(command));
    }

    @Test
    public void gamesList() throws Exception {
        List games = mockClientFacade.gamesList();
        GameListPlayerItem playerItem1 = new GameListPlayerItem(CatanColor.ORANGE, "Sam", 0);
        GameListPlayerItem playerItem2 = new GameListPlayerItem(CatanColor.BLUE, "Brooke", 1);
        GameListPlayerItem playerItem3 = new GameListPlayerItem(CatanColor.RED, "Pete", 10);
        GameListPlayerItem playerItem4 = new GameListPlayerItem(CatanColor.GREEN, "Mark", 11);
        ArrayList<GameListPlayerItem> players = new ArrayList<GameListPlayerItem>();
        players.add(playerItem1);
        players.add(playerItem2);
        players.add(playerItem3);
        players.add(playerItem4);
        GameListItem game1 = new GameListItem("Default Game", 0, players);
        //Todo create other game list items and compare to result; or maybe I should get the result and just check the answers
    }

    @Test
    public void gameCreate() throws Exception {

    }

    @Test
    public void gameJoin() throws Exception {

    }

    @Test
    public void gameSave() throws Exception {

    }

    @Test
    public void gameLoad() throws Exception {

    }

    @Test
    public void gameModelVersion() throws Exception {

    }

    @Test
    public void gameReset() throws Exception {

    }

    @Test
    public void getGameCommands() throws Exception {

    }

    @Test
    public void executeGameCommands() throws Exception {

    }

    @Test
    public void listAI() throws Exception {

    }

    @Test
    public void addAI() throws Exception {

    }

    @Test
    public void utilChangeLogLevel() throws Exception {

    }

    @Test
    public void sendChat() throws Exception {

    }

    @Test
    public void rollNumber() throws Exception {

    }

    @Test
    public void finishTurn() throws Exception {

    }

    @Test
    public void discardCards() throws Exception {

    }

    @Test
    public void buildRoad() throws Exception {

    }

    @Test
    public void buildSettlement() throws Exception {

    }

    @Test
    public void buildCity() throws Exception {

    }

    @Test
    public void offerTrade() throws Exception {

    }

    @Test
    public void acceptTrade() throws Exception {

    }

    @Test
    public void maritimeTrade() throws Exception {

    }

    @Test
    public void robPlayer() throws Exception {

    }

    @Test
    public void purchaseDevCard() throws Exception {

    }

    @Test
    public void playSoldier() throws Exception {

    }

    @Test
    public void playYearOfPlenty() throws Exception {

    }

    @Test
    public void playRoadBuilding() throws Exception {

    }

    @Test
    public void playMonopoly() throws Exception {

    }

    @Test
    public void playMonument() throws Exception {

    }

}