package shared_tests;

import junit.framework.TestCase;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.ClientModel;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceList;

/**
 * Created by Mitchell on 11/8/2016.
 */
public class DiceCardResultsTest extends TestCase{

    ClientModel clientModel = new ClientModel(1);

    private final int PLAYERWHITE = 0;
    private final int PLAYERRED = 1;
    private final int PLAYERORANGE = 2;
    private final int PLAYERBLUE = 3;

    public void testRollSix() {

        //fake players
        clientModel.getPlayers()[PLAYERWHITE] = new Player(CatanColor.BLUE, "bacon", 2, 0);
        clientModel.getPlayers()[PLAYERRED] = new Player(CatanColor.PURPLE, "petey", 5, 0);
        clientModel.getPlayers()[PLAYERORANGE] = new Player(CatanColor.RED, "WHOA", 4, 0);
        clientModel.getPlayers()[PLAYERBLUE] = new Player(CatanColor.GREEN, "HEY", 15, 0);



        /*clientModel.buildSettlement(PLAYERWHITE, new VertexLocation(new HexLocation(1,-1), VertexDirection.NorthEast));
        clientModel.canPlaceSettlement(PLAYERWHITE, new VertexLocation(new HexLocation(-1,2), VertexDirection.NorthWest));

        clientModel.canPlaceSettlement(PLAYERRED, new VertexLocation(new HexLocation(0,1), VertexDirection.NorthWest));
        clientModel.canPlaceSettlement(PLAYERRED, new VertexLocation(new HexLocation(1,0), VertexDirection.NorthWest));

        clientModel.canPlaceSettlement(PLAYERORANGE, new VertexLocation(new HexLocation(0,2), VertexDirection.NorthEast));
        clientModel.canPlaceSettlement(PLAYERORANGE, new VertexLocation(new HexLocation(2,1), VertexDirection.NorthWest));

        clientModel.canPlaceSettlement(PLAYERBLUE, new VertexLocation(new HexLocation(-2,2), VertexDirection.NorthWest));
        clientModel.canPlaceSettlement(PLAYERBLUE, new VertexLocation(new HexLocation(-1,0), VertexDirection.NorthWest));

        clientModel.receiveResourcesFromDiceRoll(6);

        ResourceList playerWhiteResources = clientModel.getPlayers()[PLAYERWHITE].getPlayerResourceList();

        assert (playerWhiteResources.getOreCardCount() == 1);
        assert (playerWhiteResources.getWoodCardCount() == 1);
*/

    }


}




