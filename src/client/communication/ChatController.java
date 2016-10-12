package client.communication;

import client.ClientFacade;
import client.ClientUser;
import client.base.*;
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
		SendChatCommand command = new SendChatCommand(ClientUser.getInstance().getIndex(), message);
		ClientFacade.getInstance().sendChat(command);
	}

	@Override
	public void update(Observable o, Object arg) {

		System.out.println("CHATCONTROLLER UPDATE called");

		//set view.setEntries(list of entries from ClientModel) here?
		ClientModel model = (ClientModel)o;
		MessageList chat = model.getChat();
		List<MessageLine> lines = chat.getLines();
		//create LogEntry list from MessageList
		List<LogEntry> logEntries= new ArrayList<LogEntry>();
		//create map of players and colors for easy translation between the two
		Map<String, CatanColor> playerMap = new HashMap<>();
		Player[] players = model.getPlayers();
		for(int i = 0; i < 4; i++){
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

