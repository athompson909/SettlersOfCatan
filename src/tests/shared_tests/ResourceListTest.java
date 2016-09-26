package tests.shared_tests;
import junit.framework.TestCase;
import shared.definitions.ResourceType;
import shared.model.resourcebank.ResourceList;

/**
 * Created by Mitchell on 9/24/2016.
 */
public class ResourceListTest extends TestCase {
    private ResourceList resourceList = new ResourceList();

    public void testResourceList() {
        resourceList.incWoodCardCount(1);
        resourceList.incBrickCardCount(2);
        resourceList.incSheepCardCount(3);
        resourceList.incWheatCardCount(4);
        resourceList.incOreCardCount(5);

        assert(resourceList.getWoodCardCount() == 1);
        assert(resourceList.getBrickCardCount() == 2);
        assert(resourceList.getSheepCardCount() == 3);
        assert(resourceList.getWheatCardCount() == 4);
        assert(resourceList.getOreCardCount() == 5);

        resourceList.removeCardByType(ResourceType.WOOD);
        assert(resourceList.getWoodCardCount() == 0);
    }
}


