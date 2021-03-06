package DAO_tests;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import server.plugins.data_access.FileGameDAO;
import shared.locations.*;
import shared.model.commandmanager.moves.BuildRoadCommand;
import shared.model.commandmanager.moves.BuildSettlementCommand;
import shared.model.map.VertexObject;

/**
 *
 * Created by Sierra on 12/1/16.
 */
public class FileGameDAOTest extends TestCase {

    private FileGameDAO fileGameDAO = new FileGameDAO();
    String brc1String = "";
    String bsc1String = "";
    String rp1String = "";
    String oT1String = "";
    String aT1String = "";
    private JSONObject brc1JSON;
    private JSONObject bsc1JSON;
    private JSONObject rp1JSON;
    private JSONObject rn1JSON;
    private JSONObject oT1JSON;
    private JSONObject aT1JSON;
    private String testClientModel;
    private String testGameInfo1;
    private String testGameInfo2;

    Gson gsonTranslator = new Gson();

    @Test
    public void setUp() throws Exception {
        super.setUp();

        setUpSampleCmds();
        setUpModelString();
        setUpGameInfoStrings();
    }

    private void setUpSampleCmds(){

//            HexLocation brc1HL1 = new HexLocation(-1,1);
//            EdgeLocation brc1EL1 = new EdgeLocation(brc1HL1, EdgeDirection.North);
//        brc1 = new BuildRoadCommand(brc1EL1, 0, true);
        brc1String = "{\n" +
                "    \"roadLocation\": {\n" +
                "      \"direction\": \"N\",\n" +
                "      \"x\": 2,\n" +
                "      \"y\": 1\n" +
                "    },\n" +
                "    \"free\": true,\n" +
                "    \"type\": \"buildRoad\",\n" +
                "    \"playerIndex\": 0\n" +
                "  }";
        brc1JSON = new JSONObject(brc1String);
        //brc1JSON = new JSONObject(gsonTranslator.toJson(brc1));  //should look just like how the server usually wants it


        bsc1String = "  {\n" +
                "    \"vertexLocation\": {\n" +
                "      \"direction\": \"SE\",\n" +
                "      \"x\": 0,\n" +
                "      \"y\": -1\n" +
                "    },\n" +
                "    \"free\": false,\n" +
                "    \"type\": \"buildSettlement\",\n" +
                "    \"playerIndex\": 1\n" +
                "  }";
        bsc1JSON = new JSONObject(bsc1String);

        rp1String = "  {\n" +
                "    \"victimIndex\": 1,\n" +
                "    \"location\": {\n" +
                "      \"x\": 2,\n" +
                "      \"y\": 2\n" +
                "    },\n" +
                "    \"type\": \"robPlayer\",\n" +
                "    \"playerIndex\": 0\n" +
                "  }";
        rp1JSON = new JSONObject(rp1String);

        oT1String = "{\n" +
                "    \"receiver\": 2,\n" +
                "    \"offer\": {\n" +
                "      \"brick\": 0,\n" +
                "      \"wood\": 0,\n" +
                "      \"sheep\": 1,\n" +
                "      \"wheat\": 1,\n" +
                "      \"ore\": 0\n" +
                "    },\n" +
                "    \"type\": \"offerTrade\",\n" +
                "    \"playerIndex\": 0\n" +
                "  }";
        oT1JSON = new JSONObject(oT1String);

        aT1String = "  {\n" +
                "    \"willAccept\": true,\n" +
                "    \"type\": \"acceptTrade\",\n" +
                "    \"playerIndex\": 2\n" +
                "  }";
        aT1JSON = new JSONObject(aT1String);

    }

    private void setUpModelString() {
        testClientModel = " {\n" +
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
                "    ],\n" +      /// ==================================END PORTS
                "    \"robber\": {\n" +
                "      \"x\": 1,\n" +
                "      \"y\": -2\n" +
                "    }\n" +
                "  },\n" +    /// ====================================================END MAP
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
                "      \"soldiers\": 60,\n" +
                "      \"victoryPoints\": 0,\n" +
                "      \"monuments\": 0,\n" +
                "      \"playedDevCard\": false,\n" +
                "      \"discarded\": true,\n" +
                "      \"playerID\": 0,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"YOLO\",\n" +
                "      \"color\": \"orange\"\n" +
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
                "      \"soldiers\": 40,\n" +
                "      \"victoryPoints\": 0,\n" +
                "      \"monuments\": 0,\n" +
                "      \"playedDevCard\": false,\n" +
                "      \"discarded\": false,\n" +
                "      \"playerID\": 1,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"Poop\",\n" +
                "      \"color\": \"brown\"\n" +
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
                "      \"soldiers\": 22,\n" +
                "      \"victoryPoints\": 0,\n" +
                "      \"monuments\": 0,\n" +
                "      \"playedDevCard\": false,\n" +
                "      \"discarded\": false,\n" +
                "      \"playerID\": 10,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"petey\",\n" +
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
                "      \"soldiers\": 14,\n" +
                "      \"victoryPoints\": 0,\n" +
                "      \"monuments\": 0,\n" +
                "      \"playedDevCard\": false,\n" +
                "      \"discarded\": false,\n" +
                "      \"playerID\": 11,\n" +
                "      \"playerIndex\": 0,\n" +
                "      \"name\": \"WHAT\",\n" +
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

    private void setUpGameInfoStrings(){
        testGameInfo1 = "{" +
                "\"title\":" + "\"Pizza Game\"," +
                "\"id\":" + 0 + "," +
                "\"players\":" + "[" +
                        "{" +
                        "\"color\":" + "\"orange\"," +
                        "\"name\":" + "\"Sam\"," +
                        "\"id\":" + 0 +
                        "}, " +
                        "{" +
                        "\"color\":" + "\"brown\"," +
                        "\"name\":" + "\"Poop\"," +
                        "\"id\":" + 1 +
                        "}, " +
                        "{" +
                        "\"color\":" + "\"red\"," +
                        "\"name\":" + "\"Petey\"," +
                        "\"id\":" + 10 +
                        "}, " +
                        "{" +
                        "\"color\":" + "\"green\"," +
                        "\"name\":" + "\"WHAT\"," +
                        "\"id\":" + 11 +
                        "} " +
                    "]" +
                "}";

        testGameInfo2 = "{" +
                    "\"title\":" + "\"YOUR MOM's Game\"," +
                    "\"id\":" + 0 + "," +
                    "\"players\":" + "[" +
                        "{" +
                        "\"color\":" + "\"blue\"," +
                        "\"name\":" + "\"Sam\"," +
                        "\"id\":" + 0 +
                        "}, " +
                        "{" +
                        "\"color\":" + "\"puce\"," +
                        "\"name\":" + "\"Poop\"," +
                        "\"id\":" + 1 +
                        "}, " +
                        "{" +
                        "\"color\":" + "\"green\"," +
                        "\"name\":" + "\"Petey\"," +
                        "\"id\":" + 10 +
                        "}, " +
                        "{" +
                        "\"color\":" + "\"white\"," +
                        "\"name\":" + "\"WHAT\"," +
                        "\"id\":" + 11 +
                        "} " +
                    "]" +
                "}";
    }

    @Test
    public void testWriteCommand() throws Exception {
        System.out.println(">TESTING WRITECOMMAND!");

        //this fn should:
        //create a new file called cmds3.json inside the /json_files/games folder
        //write sample Commands to it (in JSON)
        //be able to append new Command JSON bits to the same file without overwriting old content

        fileGameDAO.writeCommand(brc1JSON, 1); //# is the test gameID
        fileGameDAO.writeCommand(bsc1JSON, 1);
        fileGameDAO.writeCommand(rp1JSON, 1);
        fileGameDAO.writeCommand(aT1JSON, 1);
        fileGameDAO.writeCommand(oT1JSON, 1);

        //assert file exists? or just read it in again via readCommands
    }

    @Test
    //run testWriteCmds before you run this one!
    //this just reads ONE FILE of commands!
    public void testReadCommands() throws Exception {
        System.out.println(">TESTING READCMDS!");

        JSONArray readCmdsResult = fileGameDAO.readGameCommands("./json_files/games/cmds3.json");


        assertTrue(readCmdsResult.length() == 5);  //should have 5 commands
    }

    @Test
    //run testWriteCommands before you run this one!
    public void testReadAllCommands() throws Exception {
        System.out.println(">TESTING READALLCMDS!");

        JSONArray allCmdsReadResult = fileGameDAO.readAllCommands();


        assertTrue(allCmdsReadResult.length() == 2); //should be 2 games' worth of commands

        //return allCmdsReadResult; //so ServerTranslator.gamesFromJSON() can test too
    }



    @Test
    public void testWriteGame() throws Exception {
        System.out.println(">TESTING WRITEGAME!");

        //this fn should:
        //create a new file called game0.json inside the /json_files/games folder if it doesn't exist
        //write sample game to it (in a JSONArray s.t. spot0 = clientModel, spot1 = GameInfo), overwriting old content

        fileGameDAO.writeGame(1, testClientModel, testGameInfo1);
        fileGameDAO.writeGame(0, testClientModel, testGameInfo2);

    }


    @Test
    //run testWriteGame before you run this one!
    public void testReadGame() throws Exception {
        System.out.println(">TESTING READGAME!");

        //this fn should:
        //locate the file inside the /json_files/games folder using the given path
        //read in the huge JSONArray, and just return it as a big JSONArray for now.

        JSONObject bigGuy = fileGameDAO.readGame("./json_files/games/game0.json");

        assertTrue(bigGuy.length() == 2);  //should have 2 parts: the clientModel and the gameInfo
    }

    @Test
    //run testWriteGame before you run this one!
    public JSONArray testReadAllGames() throws Exception {
        System.out.println(">TESTING READALLGAMES!");

        JSONArray allGamesReadResult = fileGameDAO.readAllGames();


        assertTrue(allGamesReadResult.length() == 2);

       return allGamesReadResult; //so ServerTranslator.gamesFromJSON() can test too
    }


}
