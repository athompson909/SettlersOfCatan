package server;

import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.*;
import shared.model.ClientModel;
import shared.model.map.VertexObject;
import shared.model.messagemanager.MessageLine;
import shared.model.messagemanager.MessageList;
import shared.model.messagemanager.MessageManager;
import shared.model.player.Player;
import shared.model.resourcebank.DevCardList;
import shared.model.resourcebank.ResourceBank;
import shared.model.resourcebank.ResourceList;
import shared.model.turntracker.TurnTracker;

import java.util.*;

/**
 *
 *  GamesManager keeps track of all the games that have been created on the server.
 *  The ServerFacade can access any Game it needs from its gameID.
 *
 *  This is a singleton!!
 *
 * Created by adamthompson on 11/4/16.
 */
public class GamesManager {

    /**
     * Hashmap of every game that has been created so far
     */
    private HashMap<Integer, Game> allGames = new HashMap<Integer, Game>();

    /**
     * Parts of the singleton pattern
     */
    private static GamesManager instance = new GamesManager();

    public static GamesManager getInstance() {
        return instance;
    }

    public static int gameID = 0;  //TESTING FOR MOCK GAMES
    /**
     * Private constructor
     */
    private GamesManager() {
//       buildMockGames();

    }


    /**
     * Adding a default game so the gameHub looks better and testing is easier hopefully
     */
    private void buildMockGames(){
        System.out.println(">GAMESMANAGER: building Mock Games");


        GameInfo mockGameInfo1 = new GameInfo();
        mockGameInfo1.setId(0);
        mockGameInfo1.setTitle("Test Game 1");

//fake PlayerInfos
            PlayerInfo mockPlayerInfo1Game1 = new PlayerInfo();
            mockPlayerInfo1Game1.setId(0);   //I just picked these random mock users to be in this game. this one is Adam
            mockPlayerInfo1Game1.setName(UserManager.getInstance().getUser(0).getUserName());
            mockPlayerInfo1Game1.setPlayerIndex(0); //goes from 0-4
            mockPlayerInfo1Game1.setColor(CatanColor.RED);

            PlayerInfo mockPlayerInfo2Game1 = new PlayerInfo();
            mockPlayerInfo2Game1.setId(1);   //this one is Steph
            mockPlayerInfo2Game1.setName(UserManager.getInstance().getUser(1).getUserName());
            mockPlayerInfo2Game1.setPlayerIndex(1); //goes from 0-4
            mockPlayerInfo2Game1.setColor(CatanColor.ORANGE);

            PlayerInfo mockPlayerInfo3Game1 = new PlayerInfo();
            mockPlayerInfo3Game1.setId(2);   //this one is Mitch
            mockPlayerInfo3Game1.setName(UserManager.getInstance().getUser(2).getUserName());
            mockPlayerInfo3Game1.setPlayerIndex(2); //goes from 0-4
            mockPlayerInfo3Game1.setColor(CatanColor.BLUE);

            PlayerInfo mockPlayerInfo4Game1 = new PlayerInfo();
            mockPlayerInfo4Game1.setId(3);   //this one is Sierra
            mockPlayerInfo4Game1.setName(UserManager.getInstance().getUser(3).getUserName());
            mockPlayerInfo4Game1.setPlayerIndex(3); //goes from 0-4
            mockPlayerInfo4Game1.setColor(CatanColor.WHITE);

        List<PlayerInfo> mockGame1PlayerInfos = new ArrayList<>();
        mockGame1PlayerInfos.add(mockPlayerInfo1Game1);
        mockGame1PlayerInfos.add(mockPlayerInfo2Game1);
        mockGame1PlayerInfos.add(mockPlayerInfo3Game1);
        mockGame1PlayerInfos.add(mockPlayerInfo4Game1);

        mockGameInfo1.setPlayers(mockGame1PlayerInfos);


        ClientModel mockGame1ClientModel = buildMockClientModel();

            //tell the ClientModel to add the users to its list of Players too!

        Game mockGame1 = new Game(mockGameInfo1, false, false, false);
        mockGame1.setClientModel(mockGame1ClientModel);
        mockGame1.setGameInfo(mockGameInfo1);

        allGames.put(0, mockGame1); //TESTING since gameID wasn't incrementing for some reason

            System.out.println(">GAMESMANAGER: built " + allGames.size() + " mock games");

    }

    private ClientModel buildMockClientModel(){
        ClientModel mockClientModel = new ClientModel(0);  //0 = the game number

     //add the players
        mockClientModel.joinGame(CatanColor.RED, UserManager.getInstance().getUser(0));
        mockClientModel.joinGame(CatanColor.ORANGE, UserManager.getInstance().getUser(1));
        mockClientModel.joinGame(CatanColor.BLUE, UserManager.getInstance().getUser(2));
        mockClientModel.joinGame(CatanColor.WHITE, UserManager.getInstance().getUser(3));

        //extras  - why do we need these again?
        mockClientModel.setChat(mockClientModel.getMessageManager().getChat());
        mockClientModel.setLog(mockClientModel.getMessageManager().getLog());

        mockClientModel.sendChat(3, "***YOU ARE IN MOCK GAME 1***");
        mockClientModel.sendChat(3, "Starting the TurnTracker AFTER the 2 setup phases...");


  //set up fake settlements/roads

        //player 1 is red
                VertexLocation mockStlmt1P1VL = new VertexLocation(new HexLocation(0,1), VertexDirection.NorthWest);
            VertexObject mockStlmt1P1VO = new VertexObject(mockStlmt1P1VL);
        mockStlmt1P1VO.setOwner(0);
        mockStlmt1P1VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt1P1VO, true);

            EdgeLocation mockRoadP1EL1 = new EdgeLocation(new HexLocation(0,1), EdgeDirection.North);
                mockClientModel.buildRoad(mockRoadP1EL1, 0, true);

        VertexLocation mockStlmt2P1VL = new VertexLocation(new HexLocation(1,0), VertexDirection.NorthWest);
            VertexObject mockStlmt2P1VO = new VertexObject(mockStlmt2P1VL);
        mockStlmt2P1VO.setOwner(0);
        mockStlmt2P1VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt2P1VO, true);

            EdgeLocation mockRoadP1EL2 = new EdgeLocation(new HexLocation(1,0), EdgeDirection.North);
                mockClientModel.buildRoad(mockRoadP1EL2, 0, true);

        //player 2 is orange
                VertexLocation mockStlmt1P2VL = new VertexLocation(new HexLocation(0,2), VertexDirection.NorthEast);
            VertexObject mockStlmt1P2VO = new VertexObject(mockStlmt1P2VL);
        mockStlmt1P2VO.setOwner(1);
        mockStlmt1P2VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt1P2VO, true);

            EdgeLocation mockRoadP2EL1 = new EdgeLocation(new HexLocation(0,2), EdgeDirection.North);
                mockClientModel.buildRoad(mockRoadP2EL1, 1, true);

        VertexLocation mockStlmt2P2VL = new VertexLocation(new HexLocation(2,1), VertexDirection.NorthWest);
            VertexObject mockStlmt2P2VO = new VertexObject(mockStlmt2P2VL);
        mockStlmt2P2VO.setOwner(1);
        mockStlmt2P2VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt2P2VO, true);

            EdgeLocation mockRoadP2EL2 = new EdgeLocation(new HexLocation(1,1), EdgeDirection.NorthEast);
                mockClientModel.buildRoad(mockRoadP2EL2, 1, true);

        //player 3 is blue
                VertexLocation mockStlmt1P3VL = new VertexLocation(new HexLocation(-2,2), VertexDirection.NorthWest);
            VertexObject mockStlmt1P3VO = new VertexObject(mockStlmt1P3VL);
        mockStlmt1P3VO.setOwner(2);
        mockStlmt1P3VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt1P3VO, true);

            EdgeLocation mockRoadP3EL1 = new EdgeLocation(new HexLocation(-3,2), EdgeDirection.NorthEast);
                mockClientModel.buildRoad(mockRoadP3EL1, 2, true);

        VertexLocation mockStlmt2P3VL = new VertexLocation(new HexLocation(-1,0), VertexDirection.NorthWest);
            VertexObject mockStlmt2P3VO = new VertexObject(mockStlmt2P3VL);
        mockStlmt2P3VO.setOwner(2);
        mockStlmt2P3VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt2P3VO, true);

            EdgeLocation mockRoadP3EL2 = new EdgeLocation(new HexLocation(-1,0), EdgeDirection.North);
                mockClientModel.buildRoad(mockRoadP3EL2, 2, true);

        //player 4 is white
                VertexLocation mockStlmt1P4VL = new VertexLocation(new HexLocation(1,-1), VertexDirection.NorthEast);
            VertexObject mockStlmt1P4VO = new VertexObject(mockStlmt1P4VL);
        mockStlmt1P4VO.setOwner(3);
        mockStlmt1P4VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt1P4VO, true);

            EdgeLocation mockRoadP4EL1 = new EdgeLocation(new HexLocation(1,-1), EdgeDirection.NorthEast);
                mockClientModel.buildRoad(mockRoadP4EL1, 3, true);

        VertexLocation mockStlmt2P4VL = new VertexLocation(new HexLocation(-1,2), VertexDirection.NorthWest);
            VertexObject mockStlmt2P4VO = new VertexObject(mockStlmt2P4VL);
        mockStlmt2P4VO.setOwner(3);
        mockStlmt2P4VO.setPieceType(PieceType.SETTLEMENT);
            mockClientModel.buildSettlement(mockStlmt2P4VO, true);

            EdgeLocation mockRoadP4EL2 = new EdgeLocation(new HexLocation(-2,2), EdgeDirection.NorthEast);
                mockClientModel.buildRoad(mockRoadP4EL2, 3, true);


     //TurnTracker
        TurnTracker mockTurnTracker = new TurnTracker();
            mockTurnTracker.setStatus("Rolling"); //TEST
            mockTurnTracker.setCurrentTurn(3); //start where the last player just finished their last settlement
        mockClientModel.setTurnTracker(mockTurnTracker);

        return mockClientModel;
    }


    /**
     * Adds a newly created Game to the list of all games
     * and maps it to its gameID
     *
     * @param newGame - the new Game to add to the list of all games
     */
    public int addGame(Game newGame){
        allGames.put(gameID, newGame);
        gameID++;
        return gameID-1;
    }

    /**
     * Returns the desired game object to the serverFacade
     * @param gameID of desired game object
     * @return the game object
     */
    public Game getGame(int gameID) {
        if(isValidGame(gameID)) {

            //check here that no player has NULL color, because the Views need it after this

//            Game currGame = allGames.get(gameID);
//            GameInfo currGameInfo = currGame.getGameInfo();
//
//            for (PlayerInfo currPI : currGameInfo.getPlayers()){
//
//                //if any of the players in currGame has a NULL color right now, we need to default them to a color
//                //that hasn't been taken by another user yet.
//                if (currPI.getColor() == CatanColor.NULL || currPI.getColor() == null)
//                {
//                    //default them to whatever color is available
//                    for (CatanColor currColor : CatanColor.values()) {
//
//                        //check if any of the players in currGame are using currColor
//                        if (isColorAvailable(currColor, currGameInfo.getPlayers())){
//                            //no one has this color yet, so let the NULL slacker have that one as a default
//                            currPI.setColor(currColor);
//                                System.out.println(">GAMESMGR: user " + currPI.getName() + " defaulted to color " + currColor);
//                            break; //I really don't think there's any way more than one player could have NULL as a color
//                        }
//                    }
//                }
//
//            }



            return allGames.get(gameID);
        }
        return null;
    }
//
//    /**
//     * helper function for checking whether a NULL color user can have the given color as a default
//     * @param colorToCheck
//     * @param playerInfosList
//     * @return
//     */
//    private boolean isColorAvailable(CatanColor colorToCheck, List<PlayerInfo> playerInfosList){
//
//        for (int p = 0; p < playerInfosList.size(); p++){
//            if (playerInfosList.get(p).getColor() == colorToCheck){
//                //it's taken already, not available for defaulting
//                return false;
//            }
//        }
//
//        return true;
//    }




    /**
     * Returns true if gameID is valid
     */
    public boolean isValidGame(int gameID) {
        if(allGames.containsKey(gameID)) {
            return true;
        }
        return false;
    }

    public HashMap<Integer, Game> getAllGames() {return allGames;}

    public void setAllGames(HashMap<Integer, Game> games){

        //games could be size 0 if no game files are found
        allGames = games;
    }
}
