package client.roll;

import client.ClientFacade;
import client.base.*;
import shared.model.commandmanager.moves.RollDiceCommand;
import shared.model.dicemanager.DiceManager;

import java.util.Observable;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController {

	private IRollResultView resultView;

	private DiceManager dice = new DiceManager();

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

	}

}

