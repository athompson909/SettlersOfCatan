package shared.shared_utils;

/**
 * Created by adamthompson on 11/8/16.
 */
public class MockResponses {

    public static final String GAMES_LIST = "[\n"+
            "  {\n"+
            "    \"title\": \"Default Game\",\n"+
            "    \"id\": 0,\n"+
            "    \"players\": [\n"+
            "      {\n"+
            "        \"color\": \"orange\",\n"+
            "        \"name\": \"Sam\",\n"+
            "        \"id\": 0\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"blue\",\n"+
            "        \"name\": \"Brooke\",\n"+
            "        \"id\": 1\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"red\",\n"+
            "        \"name\": \"Pete\",\n"+
            "        \"id\": 10\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"green\",\n"+
            "        \"name\": \"Mark\",\n"+
            "        \"id\": 11\n"+
            "      }\n"+
            "    ]\n"+
            "  },\n"+
            "  {\n"+
            "    \"title\": \"AI Game\",\n"+
            "    \"id\": 1,\n"+
            "    \"players\": [\n"+
            "      {\n"+
            "        \"color\": \"orange\",\n"+
            "        \"name\": \"Pete\",\n"+
            "        \"id\": 10\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"white\",\n"+
            "        \"name\": \"Ken\",\n"+
            "        \"id\": -2\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"blue\",\n"+
            "        \"name\": \"Scott\",\n"+
            "        \"id\": -3\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"purple\",\n"+
            "        \"name\": \"Quinn\",\n"+
            "        \"id\": -4\n"+
            "      }\n"+
            "    ]\n"+
            "  },\n"+
            "  {\n"+
            "    \"title\": \"Empty Game\",\n"+
            "    \"id\": 2,\n"+
            "    \"players\": [\n"+
            "      {\n"+
            "        \"color\": \"orange\",\n"+
            "        \"name\": \"Sam\",\n"+
            "        \"id\": 0\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"blue\",\n"+
            "        \"name\": \"Brooke\",\n"+
            "        \"id\": 1\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"red\",\n"+
            "        \"name\": \"Pete\",\n"+
            "        \"id\": 10\n"+
            "      },\n"+
            "      {\n"+
            "        \"color\": \"green\",\n"+
            "        \"name\": \"Mark\",\n"+
            "        \"id\": 11\n"+
            "      }\n"+
            "    ]\n"+
            "  }\n"+
            "]";

    public final static String JOIN_REQUEST = "{\n" +
            "  \"id\": 0,\n" +
            "  \"color\": \"puce\"\n" +
            "}";

    public final static String GAME_CREATE_REQUEST = "{\n" +
            "  \"randomTiles\": \"false\",\n" +
            "  \"randomNumbers\": \"false\",\n" +
            "  \"randomPorts\": \"false\",\n" +
            "  \"name\": \"adam is cool\"\n" +
            "}";

    public final static String GAME_CREATE_RESPONSE = "{\n" +
            "  \"title\": \"adam is cool\",\n" +
            "  \"id\": 3,\n" +
            "  \"players\": [\n" +
            "    {},\n" +
            "    {},\n" +
            "    {},\n" +
            "    {}\n" +
            "  ]\n" +
            "}";

    public final static String GAME_MODEL = "{\n" +
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
            "        \"location\": {\n" +
            "          \"x\": 0,\n" +
            "          \"y\": -2\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"brick\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 1,\n" +
            "          \"y\": -2\n" +
            "        },\n" +
            "        \"number\": 4\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"wood\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 2,\n" +
            "          \"y\": -2\n" +
            "        },\n" +
            "        \"number\": 11\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"brick\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -1,\n" +
            "          \"y\": -1\n" +
            "        },\n" +
            "        \"number\": 8\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"wood\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 0,\n" +
            "          \"y\": -1\n" +
            "        },\n" +
            "        \"number\": 3\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"ore\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 1,\n" +
            "          \"y\": -1\n" +
            "        },\n" +
            "        \"number\": 9\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"sheep\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 2,\n" +
            "          \"y\": -1\n" +
            "        },\n" +
            "        \"number\": 12\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"ore\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -2,\n" +
            "          \"y\": 0\n" +
            "        },\n" +
            "        \"number\": 5\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"sheep\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -1,\n" +
            "          \"y\": 0\n" +
            "        },\n" +
            "        \"number\": 10\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"wheat\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 0\n" +
            "        },\n" +
            "        \"number\": 11\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"brick\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 1,\n" +
            "          \"y\": 0\n" +
            "        },\n" +
            "        \"number\": 5\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"wheat\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 2,\n" +
            "          \"y\": 0\n" +
            "        },\n" +
            "        \"number\": 6\n" +
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
            "        \"resource\": \"sheep\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -1,\n" +
            "          \"y\": 1\n" +
            "        },\n" +
            "        \"number\": 9\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"wood\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 1\n" +
            "        },\n" +
            "        \"number\": 4\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"sheep\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 1,\n" +
            "          \"y\": 1\n" +
            "        },\n" +
            "        \"number\": 10\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"wood\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -2,\n" +
            "          \"y\": 2\n" +
            "        },\n" +
            "        \"number\": 6\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"ore\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -1,\n" +
            "          \"y\": 2\n" +
            "        },\n" +
            "        \"number\": 3\n" +
            "      },\n" +
            "      {\n" +
            "        \"resource\": \"wheat\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 2\n" +
            "        },\n" +
            "        \"number\": 8\n" +
            "      }\n" +
            "    ],\n" +
            "    \"roads\": [\n" +
            "      {\n" +
            "        \"owner\": 1,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"S\",\n" +
            "          \"x\": -1,\n" +
            "          \"y\": -1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 3,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": -1,\n" +
            "          \"y\": 1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 3,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": 2,\n" +
            "          \"y\": -2\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 2,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"S\",\n" +
            "          \"x\": 1,\n" +
            "          \"y\": -1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 0,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"S\",\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 2,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"S\",\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 0\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 1,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": -2,\n" +
            "          \"y\": 1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 0,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": 2,\n" +
            "          \"y\": 0\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"cities\": [],\n" +
            "    \"settlements\": [\n" +
            "      {\n" +
            "        \"owner\": 3,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": -1,\n" +
            "          \"y\": 1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 3,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SE\",\n" +
            "          \"x\": 1,\n" +
            "          \"y\": -2\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 2,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 0\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 2,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": 1,\n" +
            "          \"y\": -1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 1,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": -2,\n" +
            "          \"y\": 1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 0,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SE\",\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 1,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": -1,\n" +
            "          \"y\": -1\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"owner\": 0,\n" +
            "        \"location\": {\n" +
            "          \"direction\": \"SW\",\n" +
            "          \"x\": 2,\n" +
            "          \"y\": 0\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"radius\": 3,\n" +
            "    \"ports\": [\n" +
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
            "        \"ratio\": 3,\n" +
            "        \"direction\": \"SW\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 3,\n" +
            "          \"y\": -3\n" +
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
            "        \"resource\": \"brick\",\n" +
            "        \"direction\": \"NE\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -2,\n" +
            "          \"y\": 3\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"ratio\": 2,\n" +
            "        \"resource\": \"wheat\",\n" +
            "        \"direction\": \"S\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -1,\n" +
            "          \"y\": -2\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"ratio\": 2,\n" +
            "        \"resource\": \"wood\",\n" +
            "        \"direction\": \"NE\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -3,\n" +
            "          \"y\": 2\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"ratio\": 3,\n" +
            "        \"direction\": \"SE\",\n" +
            "        \"location\": {\n" +
            "          \"x\": -3,\n" +
            "          \"y\": 0\n" +
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
            "      },\n" +
            "      {\n" +
            "        \"ratio\": 3,\n" +
            "        \"direction\": \"N\",\n" +
            "        \"location\": {\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 3\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"robber\": {\n" +
            "      \"x\": 0,\n" +
            "      \"y\": -2\n" +
            "    }\n" +
            "  },\n" +
            "  \"players\": [\n" +
            "    {\n" +
            "      \"resources\": {\n" +
            "        \"brick\": 0,\n" +
            "        \"wood\": 1,\n" +
            "        \"sheep\": 1,\n" +
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
            "      \"roads\": 13,\n" +
            "      \"cities\": 4,\n" +
            "      \"settlements\": 3,\n" +
            "      \"soldiers\": 0,\n" +
            "      \"victoryPoints\": 2,\n" +
            "      \"monuments\": 0,\n" +
            "      \"playedDevCard\": false,\n" +
            "      \"discarded\": false,\n" +
            "      \"playerID\": 0,\n" +
            "      \"playerIndex\": 0,\n" +
            "      \"name\": \"Sam\",\n" +
            "      \"color\": \"puce\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"resources\": {\n" +
            "        \"brick\": 1,\n" +
            "        \"wood\": 0,\n" +
            "        \"sheep\": 1,\n" +
            "        \"wheat\": 0,\n" +
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
            "      \"roads\": 13,\n" +
            "      \"cities\": 4,\n" +
            "      \"settlements\": 3,\n" +
            "      \"soldiers\": 0,\n" +
            "      \"victoryPoints\": 2,\n" +
            "      \"monuments\": 0,\n" +
            "      \"playedDevCard\": false,\n" +
            "      \"discarded\": false,\n" +
            "      \"playerID\": 1,\n" +
            "      \"playerIndex\": 1,\n" +
            "      \"name\": \"Brooke\",\n" +
            "      \"color\": \"blue\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"resources\": {\n" +
            "        \"brick\": 0,\n" +
            "        \"wood\": 1,\n" +
            "        \"sheep\": 1,\n" +
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
            "      \"roads\": 13,\n" +
            "      \"cities\": 4,\n" +
            "      \"settlements\": 3,\n" +
            "      \"soldiers\": 0,\n" +
            "      \"victoryPoints\": 2,\n" +
            "      \"monuments\": 0,\n" +
            "      \"playedDevCard\": false,\n" +
            "      \"discarded\": false,\n" +
            "      \"playerID\": 10,\n" +
            "      \"playerIndex\": 2,\n" +
            "      \"name\": \"Pete\",\n" +
            "      \"color\": \"red\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"resources\": {\n" +
            "        \"brick\": 0,\n" +
            "        \"wood\": 1,\n" +
            "        \"sheep\": 1,\n" +
            "        \"wheat\": 0,\n" +
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
            "      \"roads\": 13,\n" +
            "      \"cities\": 4,\n" +
            "      \"settlements\": 3,\n" +
            "      \"soldiers\": 0,\n" +
            "      \"victoryPoints\": 2,\n" +
            "      \"monuments\": 0,\n" +
            "      \"playedDevCard\": false,\n" +
            "      \"discarded\": false,\n" +
            "      \"playerID\": 11,\n" +
            "      \"playerIndex\": 3,\n" +
            "      \"name\": \"Mark\",\n" +
            "      \"color\": \"green\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"log\": {\n" +
            "    \"lines\": [\n" +
            "      {\n" +
            "        \"source\": \"Sam\",\n" +
            "        \"message\": \"Sam built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Sam\",\n" +
            "        \"message\": \"Sam built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Sam\",\n" +
            "        \"message\": \"Sam's turn just ended\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Brooke\",\n" +
            "        \"message\": \"Brooke built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Brooke\",\n" +
            "        \"message\": \"Brooke built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Brooke\",\n" +
            "        \"message\": \"Brooke's turn just ended\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Pete\",\n" +
            "        \"message\": \"Pete built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Pete\",\n" +
            "        \"message\": \"Pete built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Pete\",\n" +
            "        \"message\": \"Pete's turn just ended\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Mark\",\n" +
            "        \"message\": \"Mark built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Mark\",\n" +
            "        \"message\": \"Mark built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Mark\",\n" +
            "        \"message\": \"Mark's turn just ended\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Mark\",\n" +
            "        \"message\": \"Mark built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Mark\",\n" +
            "        \"message\": \"Mark built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Mark\",\n" +
            "        \"message\": \"Mark's turn just ended\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Pete\",\n" +
            "        \"message\": \"Pete built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Pete\",\n" +
            "        \"message\": \"Pete built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Pete\",\n" +
            "        \"message\": \"Pete's turn just ended\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Brooke\",\n" +
            "        \"message\": \"Brooke built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Brooke\",\n" +
            "        \"message\": \"Brooke built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Brooke\",\n" +
            "        \"message\": \"Brooke's turn just ended\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Sam\",\n" +
            "        \"message\": \"Sam built a road\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Sam\",\n" +
            "        \"message\": \"Sam built a settlement\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"source\": \"Sam\",\n" +
            "        \"message\": \"Sam's turn just ended\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"chat\": {\n" +
            "    \"lines\": []\n" +
            "  },\n" +
            "  \"bank\": {\n" +
            "    \"brick\": 23,\n" +
            "    \"wood\": 21,\n" +
            "    \"sheep\": 20,\n" +
            "    \"wheat\": 22,\n" +
            "    \"ore\": 22\n" +
            "  },\n" +
            "  \"turnTracker\": {\n" +
            "    \"status\": \"Rolling\",\n" +
            "    \"currentTurn\": 0,\n" +
            "    \"longestRoad\": -1,\n" +
            "    \"largestArmy\": -1\n" +
            "  },\n" +
            "  \"winner\": -1,\n" +
            "  \"version\": 0\n" +
            "}";

    public static final String ADD_AI = "{\n" +
            "  \"AIType\": \"LONGEST_ROAD\"\n" +
            "}";

    public static final String SEND_CHAT_REQUEST_JSON = "{\n" +
            "  \"type\": \"sendChat\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"content\": \"hello\"\n" +
            "}";

    public static final String ROLL_NUMBER_REQUEST_JSON = "{\n" +
            "  \"type\": \"rollNumber\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"number\": 8\n" +
            "}";

    public static final String ROB_PLAYER_REQUEST_JSON = "{\n" +
            "  \"type\": \"robPlayer\",\n" +
            "  \"playerIndex\": 0,\n" +
            "  \"victimIndex\": 1,\n" +
            "  \"location\": {\n" +
            "    \"x\": \"0\",\n" +
            "    \"y\": \"0\"\n" +
            "  }\n" +
            "}";

    public static final String FINISH_TURN_REQUEST_JSON = "{\n"+
            "  \"type\": \"finishTurn\",\n"+
            "  \"playerIndex\": 0\n"+
            "}";

}
