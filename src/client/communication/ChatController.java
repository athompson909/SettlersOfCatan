package client.communication;

import client.base.*;

import java.util.Observable;


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
		
	}

	@Override
	public void update(Observable o, Object arg) {

		System.out.println("CHATCONTROLLER UPDATE called");

		//set view.setEntries(list of entries from ClientModel) here?

	}

}

