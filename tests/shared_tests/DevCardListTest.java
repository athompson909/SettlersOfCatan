package shared_tests;
import junit.framework.TestCase;
import shared.definitions.DevCardType;
import shared.model.resourcebank.DevCardList;

/**
 * Created by Mitchell on 9/24/2016.
 */
public class DevCardListTest extends TestCase {
    private DevCardList devCardList = new DevCardList();

    public void testDevCardList() {
        assert (devCardList.getTotalCardCount() == 0);
        devCardList.addDevCard(DevCardType.SOLDIER);
        assert (devCardList.getTotalCardCount() == 1);
        assert (devCardList.getSoldierCardCount() == 1);
        devCardList.removeDevCard(DevCardType.SOLDIER);
        assert (devCardList.getTotalCardCount() == 0);
        assert (devCardList.getSoldierCardCount() == 0);

        devCardList.addDevCard(DevCardType.SOLDIER);
        devCardList.addDevCard(DevCardType.MONUMENT);
        devCardList.addDevCard(DevCardType.MONOPOLY);
        devCardList.addDevCard(DevCardType.YEAR_OF_PLENTY);
        devCardList.addDevCard(DevCardType.ROAD_BUILD);

        assert (devCardList.getTotalCardCount() == 5);
        assert (devCardList.getSoldierCardCount() == 1);
        assert (devCardList.getRoadBuildingCardCount() == 1);
        assert (devCardList.getMonopolyCardCount() == 1);
        assert (devCardList.getMonumentCardCount() == 1);
        assert (devCardList.getYearOfPlentyCardCount() == 1);

        devCardList.removeRandomCard();
        devCardList.removeRandomCard();
        devCardList.removeRandomCard();
        devCardList.removeRandomCard();
        devCardList.removeRandomCard();

        assert (devCardList.getTotalCardCount() == 0);
        assert (devCardList.getSoldierCardCount() == 0);
        assert (devCardList.getRoadBuildingCardCount() == 0);
        assert (devCardList.getMonopolyCardCount() == 0);
        assert (devCardList.getMonumentCardCount() == 0);
        assert (devCardList.getYearOfPlentyCardCount() == 0);
    }
}

