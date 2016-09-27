package base_tests;

/**
 * Created by adamthompson on 9/27/16.
 */
public class AllTests {


    public static void main(String[] args) {


        String[] testClasses = new String[] {"client_tests.ServerProxyTest"};//add the rest of the junits

        org.junit.runner.JUnitCore.main(testClasses);
    }

}
