package client.roll;

import client.Client;
import client.ClientFacade;
import client.base.Controller;
import shared.definitions.State;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.RollDiceCommand;
import shared.model.dicemanager.DiceManager;
import shared.model.turntracker.TurnTracker;

import java.util.Observable;
import java.util.Timer;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController {
//Todo check if roll modal opens
//todo count down on roll time

	private IRollResultView resultView;

	private IRollView view;

	private DiceManager dice = new DiceManager();

	private boolean rollModal = false;

	private Timer timer = new Timer();

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {

		super(view);

		setView(view);
		setResultView(resultView);
	}
	
	public IRollResultView getResultView() {
		return resultView;
	}

	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView)getView();
	}
	
	@Override
	public void rollDice() {
		//roll dice and display number
		int number = dice.rollDice();

		//send result across to the server
		RollDiceCommand command = new RollDiceCommand(number);
		ClientFacade.getInstance().rollNumber(command);

//		getView().closeModal();
		resultView.setRollValue(number);
		resultView.showModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientModel model = (ClientModel)o;
		TurnTracker tracker = model.getTurnTracker();
		
		//See if it is our turn to roll
		if(Client.getInstance().getGameState() == State.ROLLING){
		//if(tracker.getStatus().equals("Rolling") && tracker.getCurrentTurn()== ClientUser.getInstance().getIndex()){
			if(!rollModal) {
				System.out.println("Roll Modal open");
				//todo figure out rolling automatically and message setting
				//getRollView().setMessage("Rolling automatically in... 5 seconds");
				getRollView().showModal();
				//timer.schedule(new SetMessage(),0, 5000);
				//todo figure out how to get the modal to not flash
				//rollModal = true;
			}

		}

	}


	// maybe get rid of all this:
	@Override
	public IRollView getView() {
		return view;
	}

	public void setView(IRollView view) {
		this.view = view;
	}
}

