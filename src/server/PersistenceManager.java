package server;

import shared.model.commandmanager.BaseCommand;

/**
 * Created by adamthompson on 11/29/16.
 */
public class PersistenceManager {
    String persistenceType;

    private static PersistenceManager instance = new PersistenceManager();
    public static PersistenceManager getInstance(){
        return instance;
    }
    private PersistenceManager() {

    }

    public void writeGame(int gameID){

    }

    public void clearCommands(int gameID){

    }

    public void writeCommand(int gameID, BaseCommand command){

    }

    public void setPersistenceType(String type) {
        persistenceType = type;
    }
}
