package client.join;

import client.base.*;

import java.util.Observable;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {

		getView().showModal();
	}

	@Override
	public void addAI() {

		// TEMPORARY

		//TODO: actually talk to the server to add the player HERE
		//go check the addAI command, it really does add an AI player to the server

		getView().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}

