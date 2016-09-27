package shared.model.commandmanager.game;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by Alise on 9/18/2016.
 */
public class AddAICommand implements BaseCommand {


    /**
     * Creates AICommand object to send to client.ClientFacade
     */
    public AddAICommand(String aiType) {
        setAIType(aiType);
    }

    /**
     * The server swagger page asks for the command type to be included in each of the JSON
     * translations of the commands.
     */
    private final String type = "addAI";


    private String AIType;
    /**
     * Tells server to add AI to game
     */
    @Override
    public void serverExec(BaseCommand command){

    }


    public String getAIType() {
        return AIType;
    }

    public void setAIType(String AIType) {
        this.AIType = AIType;
    }

    public String getType() {
        return type;
    }
}
