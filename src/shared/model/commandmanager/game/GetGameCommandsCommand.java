package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class GetGameCommandsCommand extends BaseCommand {
    /**
     * Creates GetGameCommandsCommand.
     */
    public GetGameCommandsCommand(){

    }

    /**
     * Tells the server to get all the game commands
     */
    @Override
    public String serverExec() {

        return null;
    }
}
