package client.communication;

import client.Client;
import client.ClientUser;
import client.base.Controller;
import shared.definitions.CatanColor;
import shared.model.ClientModel;
import shared.model.messagemanager.MessageLine;
import shared.model.messagemanager.MessageList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;


/**
 * Game history controller implementation
 * This needs to extend Observer I think?
 */
public class GameHistoryController extends Controller implements IGameHistoryController {

	public GameHistoryController(IGameHistoryView view) {
		
		super(view);
		
		initFromModel();
	}
	
	@Override
	public IGameHistoryView getView() {
		
		return (IGameHistoryView)super.getView();
	}
	
	private void initFromModel() {

		System.out.println("GAMEHISTORYCONTROLLER: INITFROMMODEL  called");

		updateList(Client.getInstance().getClientModel());

	}


	/**
	 * creates a new list of log entries and gives it to the view
	 */
	@Override
	public void update(Observable o, Object arg) {

		System.out.println("GAMEHISTORYCONTROLLER UPDATE called");

		updateList((ClientModel) o);
	}

	private void updateList(ClientModel clientModel) {

		MessageList log = clientModel.getLog();

		List<LogEntry> entries = new ArrayList<LogEntry>();

		Map<String, CatanColor> playerColors = ClientUser.getInstance().getPlayerColors();

		if(playerColors.size() == 4) {//todo: remove this condition when states are fixed
			for (MessageLine line : log.getLines()) {
				entries.add(new LogEntry(playerColors.get(line.getSource()), line.getMessage()));
			}
			getView().setEntries(entries);
		}
	}

}

