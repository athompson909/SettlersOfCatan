package client.utils;

/**
 *
 * Created by Alise on 10/8/2016.
 */
public class ClientUser {
    private int index = -1;
    private String name = "";

    private static ClientUser instance = new ClientUser();
    public static ClientUser getInstance() {
        return instance;
    }
    private ClientUser(){}
    public void setName(String username){
        name = username;
    }
    public void setIndex(int playerIndex){
        index = playerIndex;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
