package base_tests;

/**
 * Created by adamthompson on 9/27/16.
 */
public class AllTests {


    public static void main(String[] args) {

        String[] testClasses = new String[] {"client_tests.ServerProxyTest", "client_tests.JSONTranslatorTest",
                "client_tests.ServerPollerTest", "shared_tests.BuildCityManagerTest",
                "shared_tests.BuildRoadManagerTest", "shared_tests.BuildSettlementManagerTest",
                "shared_tests.MapTest", "shared_tests.PlayerTest", "shared_tests.ResourceListTest",
        "shared_tests.RobberTest", "shared_tests.DevCardListTest"};//add the rest of the junits

        org.junit.runner.JUnitCore.main(testClasses);
    }

}
