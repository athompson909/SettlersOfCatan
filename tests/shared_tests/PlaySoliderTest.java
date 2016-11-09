package shared_tests;

import junit.framework.TestCase;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.ClientModel;
import shared.model.map.VertexObject;
import shared.model.player.Player;
import shared.model.resourcebank.ResourceList;

/**
 * Created by Mitchell on 11/9/2016.
 */
public class PlaySoliderTest extends TestCase {

    ClientModel clientModel = new ClientModel(1);

    private final int PLAYERWHITE = 0;
    private final int PLAYERRED = 1;
    private final int PLAYERORANGE = 2;
    private final int PLAYERBLUE = 3;

    public void testPlayingSoldiers() {
        //Fake players
        clientModel.getPlayers()[PLAYERWHITE] = new Player(CatanColor.BLUE, "bacon", 2, 0);
        clientModel.getPlayers()[PLAYERRED] = new Player(CatanColor.PURPLE, "petey", 5, 0);
        clientModel.getPlayers()[PLAYERORANGE] = new Player(CatanColor.RED, "WHOA", 4, 0);
        clientModel.getPlayers()[PLAYERBLUE] = new Player(CatanColor.GREEN, "HEY", 15, 0);

        VertexObject whiteSettlement1 = new VertexObject(new VertexLocation(new HexLocation(1,-1), VertexDirection.NorthEast));
        whiteSettlement1.setOwner(PLAYERWHITE);
        whiteSettlement1.setPieceType(PieceType.SETTLEMENT);
        clientModel.buildSettlement(whiteSettlement1, true);

        VertexObject whiteSettlement2 = new VertexObject(new VertexLocation(new HexLocation(-1,2), VertexDirection.NorthWest));
        whiteSettlement2.setOwner(PLAYERWHITE);
        whiteSettlement2.setPieceType(PieceType.SETTLEMENT);
        clientModel.buildSettlement(whiteSettlement2, true);

        ResourceList playerWhiteResourceList = clientModel.getPlayers()[PLAYERWHITE].getPlayerResourceList();
        ResourceList playerBlueResourceList = clientModel.getPlayers()[PLAYERBLUE].getPlayerResourceList();
        playerWhiteResourceList.incWoodCardCount(1);

        clientModel.playSoldierCard(PLAYERBLUE, whiteSettlement1.getVertexLocation().getHexLoc(), PLAYERWHITE);

        assert (playerBlueResourceList.getWoodCardCount() == 1);
        assert (playerWhiteResourceList.getCardCount() == 0);



    }
}
