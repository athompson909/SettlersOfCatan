/**
 * Created by Alise on 9/17/2016.
 */

import java.io.File;

import Client.model.LoggingLevel;
import Client.model.map.HexLocation;
import Client.model.map.VertexObject;
import Client.model.map.EdgeLocation;
import Client.model.resourcebank.Resource;
import Client.model.resourcebank.ResourceList;

public class ClientFacade {
    /**
     *
     * @param username
     * @param password
     */
    public void userLogin(String username, String password){

    }

    public void userRegister(String username, String password){

    }

    public void gamesList(){

    }

    public void gameCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts){

    }

    public void gameJoin(int gameID, String color){

    }

    public void gameSave(int gameID,File fileName){

    }

    public void gameLoad(File fileName){

    }

    public void gameModelVersion(int version){

    }

    public void gameReset(){

    }

    public void getGameCommands(){

    }

    public void executeGameCommands(){

    }

    public void listAI(){

    }

    public void addAI(){

    }

    public void utilChangeLogLevel(LoggingLevel loggingLevel){

    }

    //Move
    public void sendChat(int playerIndex, String content){

    }

    public void rollNumber(int playerIndex, int number){

    }

    public void finishTurn(int playerIndex){

    }

    public void discardCards(int playerIndex, ResourceList discardedCards){

    }

    public void buildRoad(int playerIndex, HexLocation roadLocation, boolean free){

    }

    public void buildSettlement(int playerIndex, VertexObject vertexLocation, boolean free){

    }

    public void buildCity(int playerIndex, VertexObject vertexLocation){

    }

    public void offerTrade(int playerIndex, ResourceList offer, int receiver){

    }

    public void acceptTrade(int playerIndex, boolean willAccept){

    }

    public void maritimeTrade(int playerIndex, int ratio, Resource inputResource, Resource outputResource){

    }

    public void robPlayer(int playerIndex, HexLocation location, int victimIndex){

    }

    public void purchaseDevCard(int playerIndex){

    }

    public void playSoldier(int playerIndex, HexLocation location, int victimIndex){

    }

    public void playYearOfPlenty(int playerIndex, Resource resource1, Resource resource2){

    }

    public void playRoadBuilding(int playerIndex, EdgeLocation spot1, EdgeLocation spot2){

    }

    public void playMonopoly(int playerIndex, Resource resource){

    }

    public void playMonument(int playerIndex){

    }
}
