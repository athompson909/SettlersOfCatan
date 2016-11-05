package shared.model.commandmanager.game;

import server.IServerFacade;
import server.ServerTranslator;
import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class LoginCommand extends BaseCommand {
    /**
     * name the user chose
     */
    private String username;

    /**
     * password the user created
     */
    private String password;

    /**
     * Creates empty Command
     */
    public LoginCommand() {
    }

    /**
     * Creates LoginCommand to send to the client.ClientFacade. Sets data members
     * @param username
     * @param password
     */
    public LoginCommand(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     * Tells server to login this user
     * @param userId - the ID of the user
     * @param gameId - the ID of the game
     */
    @Override
    public String serverExec(int userId, int gameId){

        boolean response = IServerFacade.getInstance().login(username, password);
        return ServerTranslator.getInstance().booleanToString(response);
    }

    //Getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
