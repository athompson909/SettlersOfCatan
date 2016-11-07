package shared.model.commandmanager.game;

import org.json.JSONObject;
import server.IServerFacade;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class RegisterCommand extends BaseCommand {
    /**
     * name user chose
     */
    private String username;

    /**
     * password of new user
     */
    private String password;

    /**
     * Creates empty Command
     */
    public RegisterCommand() {
    }

    /**
     * Creates RegisterCommand to send to client.ClientFacade. Sets data members.
     * @param username
     * @param password
     */
    public RegisterCommand(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     * Tells server to add new user with given name and password
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){
        boolean success = IServerFacade.getInstance().register(userId, gameId, this);
        if(success) {
            return "Success";
        }else {
            return null;
        }
    }

    //Getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
