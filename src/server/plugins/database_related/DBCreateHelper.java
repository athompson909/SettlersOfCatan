package server.plugins.database_related;

import java.sql.*;

//import org.sqlite.JDBC
/**
 * Created by adamthompson on 11/30/16.
 */
public class DBCreateHelper {


    private static String dbName = "catan.db";

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        DBCreateHelper.dbName = dbName;
    }

    /**
     * Connect to a sample database
     *
     * This doesn't work... talk to the TAs
     *
     */
    public static void createNewDatabase() {

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + dbName;

            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");

                    createTables(conn);
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            // I think this should technically be catching a ClassNotFoundException but it won't let me catch that
            System.out.println(e.getMessage());
        }
    }

    public static void createTables(Connection conn) throws SQLException {

        Statement statement = conn.createStatement();
        statement.setQueryTimeout(30);//30 second query timeout

        statement.execute("drop table if exists games");
        statement.execute(CREATE_TABLE_GAMES_STATEMENT);

        statement.execute("drop table if exists users");
        statement.execute(CREATE_TABLE_USERS_STATEMENT);

        statement.execute("drop table if exists commands");
        statement.execute(CREATE_TABLE_COMMANDS_STATEMENT);

    }

    private static final String CREATE_TABLE_GAMES_STATEMENT = "CREATE TABLE games\n" +
            "(gameID INTEGER DEFAULT 0 NOT NULL,\n" +
            "model TEXT NOT NULL,\n" +
            "gameInfo TEXT NOT NULL,\n" +
            "PRIMARY KEY (gameID)) ";

    private static final String CREATE_TABLE_USERS_STATEMENT = "CREATE TABLE users\n"+
            "(userID INTEGER NOT NULL,\n"+
            "username VARCHAR(25) NOT NULL,\n"+
            "password VARCHAR(25) NOT NULL,\n"+
            "PRIMARY KEY (userID))";

    public static final String CREATE_TABLE_COMMANDS_STATEMENT = "CREATE TABLE commands\n" +
            "(gameID INTEGER NOT NULL,\n" +
            "command TEXT NOT NULL)";

}
