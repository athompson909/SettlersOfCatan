package client.communication;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import shared.definitions.CatanColor;
import shared.model.ClientModel;
import shared.model.commandmanager.game.SendChatCommand;
import shared.model.messagemanager.MessageLine;
import shared.model.messagemanager.MessageList;
import shared.model.player.Player;

import java.util.*;


/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController {

	public ChatController(IChatView view) {
		
		super(view);

		System.out.println("CHATCONTROLLER constructor called");
	}

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	@Override
	public void sendMessage(String message) {
		/*NOTE: even though the json command has the format {..."playerIndex":X...} that does not mean we want the
		playerIndex, we actually want the playerId... this is really annoying but just remember that playerIndex is
		playerId//todo:revise
		 */
		SendChatCommand command = new SendChatCommand(ClientUser.getInstance().getIndex(), message);
		//Client.getInstance().setUpdateOverride(true);
		ClientFacade.getInstance().sendChat(command);
	}

	@Override
	public void update(Observable o, Object arg) {

		System.out.println("CHATCONTROLLER UPDATE called");

		//set view.setEntries(list of entries from ClientModel) here?
		ClientModel model = (ClientModel) o;
		//TODO: I commented this out for now because it was causing a few errors now that update is actually getting called. Feel free to uncomment it if you're working on it! - Mitch
		//I'm working on this, -Adam
		MessageList chat = model.getChat();
		List<MessageLine> lines = chat.getLines();
		//create LogEntry list from MessageList
		List<LogEntry> logEntries= new ArrayList<LogEntry>();
		//create map of players and colors for easy translation between the two
		Map<String, CatanColor> playerMap = new HashMap<>();
		Player[] players = model.getPlayers();

		//there may be less then 4 players in the game -
		// if so, model.getPlayers() still returns an array of size 4, but it will have null entries
		for(int i = 0; i < 4; i++){
			if(players[i] == null) break;//takes care of when there are less than 4 players currently in the game
			//that conditional however is only going to be necessary when a player is joining a game and there is only currently one player
			//todo: find a better solution for above
			playerMap.put(players[i].getName(), players[i].getColor());
		}
		for(MessageLine l: lines){
			//create a log entry for each message line
			LogEntry entry = new LogEntry(playerMap.get(l.getSource()), l.getMessage());
			logEntries.add(entry);
		}

		getView().setEntries(logEntries);
	}

}

