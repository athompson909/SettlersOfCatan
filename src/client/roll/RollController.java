package client.roll;

import client.ClientFacade;
import client.ClientUser;
import client.base.*;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.RollDiceCommand;
import shared.model.dicemanager.DiceManager;
import shared.model.turntracker.TurnTracker;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController {
//Todo check if roll modal opens
//todo count down on roll time
	private IRollResultView resultView;

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
		resultView.setRollValue(number);
		getResultView().showModal();

		//send result accross to the server
		RollDiceCommand command = new RollDiceCommand(number);
		ClientFacade.getInstance().rollNumber(command);
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientModel model = (ClientModel)o;
		TurnTracker tracker = model.getTurnTracker();

		//See if it is our turn to roll
		/*
		if(tracker.getStatus().equals("Rolling") && tracker.getCurrentTurn()== ClientUser.getInstance().getIndex()){
			if(!rollModal) {
				System.out.println("Roll Modal open");
				//todo figure out rolling automatically and message setting
				//getRollView().setMessage("Rolling automatically in... 5 seconds");
				getRollView().showModal();
				timer.schedule(new SetMessage(),0, 5000);
				//todo figure out how to get the modal to not flash
				//rollModal = true;
			}

		}
		*/
	}
	class SetMessage extends TimerTask{
		private int seconds;
		public SetMessage(){
			seconds = 5;
		}
		@Override
		public void run() {
			//getRollView().setMessage("Rolling automatically in... " + seconds + "seconds");
			seconds--;
			if(seconds == 0){
				rollDice();
			}
		}
	}
}

