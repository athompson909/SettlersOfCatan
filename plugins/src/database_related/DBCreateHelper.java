package database_related;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by adamthompson on 11/30/16.
 */
public class DBCreateHelper {




    /**
     * Connect to a sample database
     *
     * This doesn't work... talk to the TAs
     *
     * @param fileName the database file name
     */
    public static void createNewDatabase(String fileName) {

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            // I think this should technically be catching a ClassNotFoundException but it won't let me catch that
            System.out.println(e.getMessage());
        }
    }

}
