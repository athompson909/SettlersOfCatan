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
import java.util.TimerTask;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController {
	private IRollResultView resultView;

	private IRollView view;

	private DiceManager dice = new DiceManager();

	private boolean rollModal = false;

	private Timer timer = new Timer();

	private int millisecondsToWait = 5000;

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
		timer.cancel();
		int number = dice.rollDice();

		//send result across to the server
		RollDiceCommand command = new RollDiceCommand(number);
		ClientFacade.getInstance().rollNumber(command);

		resultView.setRollValue(number);
		resultView.showModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientModel model = (ClientModel)o;
		TurnTracker tracker = model.getTurnTracker();

		//See if it is our turn to roll
		if(Client.getInstance().getGameState() == State.ROLLING){
			if(!getRollView().isModalShowing()) {
				getRollView().showModal();
				timer = new Timer();
				timer.schedule(
					new TimerTask(){
						@Override
						public void run() {
							if(getRollView().isModalShowing()) {
								getRollView().closeModal();
								rollDice();
							}
						}
					},
						millisecondsToWait
				);
			}

		}else if(getRollView().isModalShowing()){
			getRollView().closeModal();
		}

	}

	@Override
	public IRollView getView() {
		return view;
	}

	public void setView(IRollView view) {
		this.view = view;
	}
}

