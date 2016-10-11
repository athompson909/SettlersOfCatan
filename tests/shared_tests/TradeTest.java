package shared_tests;

import junit.framework.TestCase;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.ClientModel;
import shared.locations.VertexLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.definitions.PortType;

import java.util.Set;
import java.util.HashSet;

/**
 * Created by Stephanie on 9/29/16.
 */

public class TradeTest extends TestCase {
    private ClientModel model = new ClientModel(1);
    private Map map = new Map(false, false, false); //Don't randomize
    private Player player = new Player(CatanColor.BLUE, "Bob", "bob37", 1);;
    private VertexLocation vertLoc = new VertexLocation(new HexLocation(-2,0), VertexDirection.NorthWest);

  /*  @Test
    public void testMaritime() {
        System.out.println("Testing Maritime Trade");
        map.getVertexObjects().get(vertLoc).setOwner(player.getPlayerIndex());
        Set<PortType> ports = new HashSet<>();
        ports.add(map.getPortVertexLocations().get(vertLoc).getResource());
        player.getPlayerResourceList().incBrickCardCount(3);
        player.getPlayerResourceList().incSheepCardCount(2);
        player.getPlayerResourceList().incWheatCardCount(2);
        player.getPlayerResourceList().incWoodCardCount(2);
        player.getPlayerResourceList().incOreCardCount(2);
        assert (player.canMaritimeTrade(ports));

    }

    @Test
    public void testDomestic() {
        System.out.println("Testing Domestic Trade");
        player.getPlayerResourceList().incWheatCardCount(2);
        player.getPlayerResourceList().incBrickCardCount(3);
        assert (player.canDomesticTrade());
    }
*/
}
