package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class LoginCommand implements BaseCommand {
    /**
     * name the user chose
     */
    private String username;

    /**
     * password the user created
     */
    private String password;

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
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }

    //Getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
