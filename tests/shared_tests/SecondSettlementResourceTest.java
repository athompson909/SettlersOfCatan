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
 * Created by Mitchell on 11/11/2016.
 */
public class SecondSettlementResourceTest extends TestCase {

        ClientModel clientModel = new ClientModel(1);

        private final int PLAYERWHITE = 0;
        private final int PLAYERRED = 1;
        private final int PLAYERORANGE = 2;
        private final int PLAYERBLUE = 3;

        public void testPlacingSecondSettlement() {
            clientModel.getTurnTracker().setStatus("SecondRoundState");

            //Fake players
            clientModel.getPlayers()[PLAYERWHITE] = new Player(CatanColor.BLUE, "bacon", 2, 0);
            clientModel.getPlayers()[PLAYERRED] = new Player(CatanColor.PURPLE, "petey", 5, 0);
            clientModel.getPlayers()[PLAYERORANGE] = new Player(CatanColor.RED, "WHOA", 4, 0);
            clientModel.getPlayers()[PLAYERBLUE] = new Player(CatanColor.GREEN, "HEY", 15, 0);

            VertexObject whiteSettlement1 = new VertexObject(new VertexLocation(new HexLocation(1,-1), VertexDirection.NorthEast));
            whiteSettlement1.setOwner(PLAYERWHITE);
            whiteSettlement1.setPieceType(PieceType.SETTLEMENT);
            clientModel.buildSettlement(whiteSettlement1, true);

            ResourceList playerWhiteResources = clientModel.getPlayers()[PLAYERWHITE].getPlayerResourceList();

          //  assert (playerWhiteResources.getOreCardCount() == 1);
//            assert (playerWhiteResources.getWoodCardCount() == 1);
  //          assert (playerWhiteResources.getBrickCardCount() == 1);

            VertexObject desertWaterSettlement = new VertexObject(new VertexLocation(new HexLocation(0,-2), VertexDirection.NorthWest));
            desertWaterSettlement.setOwner(PLAYERORANGE);
            desertWaterSettlement.setPieceType(PieceType.SETTLEMENT);
            clientModel.buildSettlement(desertWaterSettlement, true);

            clientModel.getTurnTracker().setStatus("SecondRoundState");

            ResourceList playerOrangeResources = clientModel.getPlayers()[PLAYERORANGE].getPlayerResourceList();
            assert (playerOrangeResources.getCardCount() == 0);

        }



}
