package server;

/**
 * Created by Mitchell on 9/15/2016.
 */
public interface IServerProxy {
    String url = "";

   boolean httpPost(String json);

    boolean httpGet(String json);

    //Non-Move

 /**
  *
  * @param json
  */
    void userLogin(String json);

    void userRegister(String json);

    void gamesList(String json);

    void gameCreate(String json);

    void gameJoin(String json);

    void gameSave(String json);

    void gameLoad(String json);

    void gameModelVersion(String json);

    void gameReset(String json);

    void getGameCommands(String json);

    void executeGameCommands(String json);

    void listAI(String json);

    void addAI(String json);

    void utilChangeLogLevel(String json);

    //Move
    void sendChat(String json);

    void rollNumber(String json);

    void finishTurn(String json);

    void discardCards(String json);

    void buildRoad(String json);

    void buildSettlement(String json);

    void buildCity(String json);

    void offerTrade(String json);

    void acceptTrade(String json);

    void maritimeTrade(String json);

    void robPlayer(String json);

    void purchaseDevCard(String json);

    void playSoldier(String json);

    void playYearOfPlenty(String json);

    void playRoadBuilding(String json);

    void playMonopoly(String json);

    void playMonument(String json);

}
