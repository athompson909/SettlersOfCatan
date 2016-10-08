package client.communication;

import client.base.Controller;
import shared.definitions.CatanColor;
import shared.model.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Game history controller implementation
 * This needs to extend Observer I think?
 */
public class GameHistoryController extends Controller implements IGameHistoryController {

	//This should be connected to the real ClientModel, but NOT when this class' constructor is called.
	//Maybe when the model is first pulled from the server, we can have a function that gives GHC a ref to the CModel?
	ClientModel theModel;

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

		//<temp>
		
		List<LogEntry> entries = new ArrayList<LogEntry>();
		entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(CatanColor.ORANGE, "This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));
		/*entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(CatanColor.ORANGE, "This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));
		entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(CatanColor.ORANGE, "This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));
		entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(CatanColor.ORANGE, "This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));
		*/
		getView().setEntries(entries);
	
		//</temp>
	}


	//This will be called by notifyObservers() when the ClientModel gets updated?
	//I think this is extending Observable class even though the GHC class declaration doesn't say that it does...
	@Override
	public void update(Observable o, Object arg) {

		System.out.println("GAMEHISTORYCONTROLLER UPDATE called");

		//call setEntries with the GameLog MessageList coming in as part of the Object arg field (which is the new Model I think)
	}
	
}

