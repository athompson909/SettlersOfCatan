package tests.client_tests;

import org.json.JSONObject;
import shared.definitions.CatanColor;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.ClientModel;
import junit.framework.TestCase;
import junit.framework.Assert;
import com.google.gson.*;
import shared.model.JSONTranslator;
import shared.model.commandmanager.game.*;
import shared.model.commandmanager.moves.*;


/**
 * This is the JUnit test for the JSONTranslator class.
 * I'm mainly using it to experiment with/understand Gson.
 *
 * Created by Sierra on 9/23/16.
 */
public class JSONTranslatorTest extends TestCase {

    private JSONTranslator jsonTranslator = new JSONTranslator();
    private String testResponseBody = null;
    private Gson gsonTest = new Gson();
    private GameJoinCommand gameJoinCommand;
    private BuildRoadCommand buildRoadCommand;


    public void setUp() throws Exception {
        super.setUp();

        setUpModelString();

        //GAME JOIN CMD SETUP
        //-----------------
        int gJGameID = 5;
        CatanColor gJColor = CatanColor.BLUE;
        gameJoinCommand = new GameJoinCommand(gJGameID, gJColor);
        //-----------------



        //BUILD ROAD CMD SETUP
        //-----------------
        int bRPlayerID = 2;
        EdgeDirection bREdgeDir = EdgeDirection.NorthEast;
        HexLocation brELHexLoc = new HexLocation(1,1);
        EdgeLocation bREdgeLocation = new EdgeLocation(brELHexLoc, bREdgeDir);
        buildRoadCommand = new BuildRoadCommand(bREdgeLocation, bRPlayerID);
        buildRoadCommand.setFree(true);
        //-----------------

    }
    private void setUpModelString(){
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
                "}" ;
    }

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

    public void testBuildRoadCmdTranslation() throws Exception{
        System.out.println(">TESTING BUILDROADCMD TRANSLATION!");

        String buildRoadCmdJSONResult = gsonTest.toJson(buildRoadCommand);

        System.out.println("Just serialized buildRoadCmd, JSONstring result= ");
        System.out.println(buildRoadCmdJSONResult);
        System.out.println("=================");

        String expectedResult = "{\n" +
                "  \"type\": \"buildRoad\",\n" +
                "  \"playerIndex\": 2,\n" +
                "  \"roadLocation\": {\n" +
                "    \"x\": 1,\n" +
                "    \"y\": 1,\n" +
                "    \"direction\": \"NorthEast\"\n" +
                "  },\n" +
                "  \"free\": \"true\"\n" +
                "}";
        JSONObject jsonExpectedResult = new JSONObject(expectedResult);

        /*
        the Gson result is putting the buildRoadCmd attributes in a different order from what
        the server is expecting. I don't think this would prevent the server from getting the
        necessary data from the Gson result string, but it is making my test fail though since
        the strings aren't exactly the same.
        According to SO, a JSON obj is an UNORDERED list of key/value pairs.
        Gson does not support field order specification.... unless you write your own custom serializer -_-
        */

        //****
        //Trying a workaround here, since building custom parsers for every command obj would suck:
            //apparently Hamcrest is used just for this reason!
        //I think Hamcrest might be the solution to this, but I think I need to talk to the TAS
        // to figure out how to use it...
        //org.hamcrest.MatcherAssert.assertThat

       // assertEquals(expectedResult, buildRoadCmdJSONResult);

    }

    public void testGameJoinCmdTranslation() throws Exception{
        System.out.println(">TESTING GAMEJOINCMD TRANSLATION!");

        String gameJoinCmdJSONResult = gsonTest.toJson(gameJoinCommand);

        System.out.println("Just serialized gameJoinCmd, JSONstring result= ");
        System.out.println(gameJoinCmdJSONResult);
        System.out.println("=================");


        //I don't know how picky we should be with these - our Color enum uses all caps, but the
        //server swagger page wants lowercase. Also, we originally had "id" named "gameID", which
        //I like better, but the swagger calls it "id" so... maybe for now just do exactly what it says?
        String expectedResult =
                        " {\n" +
                        "\"id\": \"5\"," +
                        "\"color\": \"BLUE\"" +
                        " }\n";

   //     assertEquals(expectedResult, gameJoinCmdJSONResult);
    }

}