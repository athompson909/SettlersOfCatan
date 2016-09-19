package Client.model.commandmanager.game;

import Client.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GameResetCommand extends BaseCommand {
    /**
     * Creates GameResetCommand to send to the ClientFacade
     */
    public GameResetCommand(){

    }

    /**
     * Tells server to reset the game
     * @param command
     */
    @Override
    public void serverExec(BaseCommand command){

    }
}
