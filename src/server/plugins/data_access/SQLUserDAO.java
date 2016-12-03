package server.plugins.data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import server.plugins.SQLPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by adamthompson on 11/29/16.
 */
public class SQLUserDAO implements IUserDAO {

    /**
     * Adds a new user.
     */
    @Override
    public void writeUser(JSONObject jsonStr) {
        int userID = jsonStr.getInt("userID");
        String username = jsonStr.getString("username");
        String password = jsonStr.getString("password");

        try {
            Connection conn = SQLPlugin.startTransaction();

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO users (userID, username, password) " +
                    "VALUES (" + userID + ",\'" + username + "\',\'" + password + "\')";
            statement.execute(sql);

            statement.close();
            SQLPlugin.endTransaction(conn, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all of the users registered.
     * @return a String with all the registered users.
     */
    @Override
    public JSONArray readAllUsers() {

        try {
            Connection conn = SQLPlugin.startTransaction();

            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(sql);

            String jsonResult = "[";
            while(resultSet.next()) {
                jsonResult += "{" +
                        "\"userID\":" + resultSet.getInt("userID") +
                        ",\"username\":\"" + resultSet.getString("username") +
                        "\",\"password\":\"" + resultSet.getString("password") + "\"},";
            }
            jsonResult += "]";

            statement.close();
            SQLPlugin.endTransaction(conn, true);

            return new JSONArray(jsonResult);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
