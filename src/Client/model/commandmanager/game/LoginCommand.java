package Client.model.commandmanager.game;

import Client.model.commandmanager.BaseCommand;

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
     * Creates LoginCommand to send to the Client.ClientFacade. Sets data members
     * @param username
     * @param password
     */
    LoginCommand(String username, String password){

    }

    /**
     * Tells server to login this user
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
