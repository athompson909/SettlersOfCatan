package server;

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
}
