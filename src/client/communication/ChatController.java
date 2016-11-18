package client.communication;

import client.Client;
import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import shared.definitions.CatanColor;
import shared.definitions.State;
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

	private boolean disabled = true;

	public ChatController(IChatView view) {
		
		super(view);
		ChatView chatView = (ChatView)getView();
		chatView.sendEnabled(false);
		System.out.println("CHATCONTROLLER constructor called");
	}

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	/**
	 * gives a chat command to the client facade which will be for the server proxy
	 * @param message taken from the view
	 */
	@Override
	public void sendMessage(String message) {
		SendChatCommand command = new SendChatCommand(ClientUser.getInstance().getIndex(), message);
		//Client.getInstance().setUpdateOverride(true);
		ClientFacade.getInstance().sendChat(command);
	}

	/**
	 * sets the message list every time update(...) is called
	 * @param o the ClientModel
	 * @param arg not used
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(!(Client.getInstance().getClientModel().getTurnTracker().getStatus().equals("FirstRound"))
				&& !(Client.getInstance().getClientModel().getTurnTracker().getStatus().equals("SecondRound"))) {
	//	if(disabled && !(Client.getInstance().getGameState() == State.WAITING)) {
				//&& !(Client.getInstance().getGameState() == State.SECONDROUND)) {
			ChatView chatView = (ChatView)getView();
			chatView.sendEnabled(true);
		}

		System.out.println("CHATCONTROLLER UPDATE called");

		ClientModel model = (ClientModel) o;

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

