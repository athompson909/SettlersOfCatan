package client.login;

import client.ClientFacade;
import client.base.Controller;
import client.base.IAction;
import client.misc.IMessageView;
import shared.model.commandmanager.game.LoginCommand;

import java.util.Observable;


/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;
	
	/**
	 * LoginController constructor
	 * 
	 * @param view Login view
	 * @param messageView Message view (used to display error messages that occur during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {

		super(view);
		
		this.messageView = messageView;
	}
	
	public ILoginView getLoginView() {
		
		return (ILoginView)super.getView();
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	
	/**
	 * Sets the action to be executed when the user logs in
	 * 
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		
		loginAction = value;
	}
	
	/**
	 * Returns the action to be executed when the user logs in
	 * 
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		
		return loginAction;
	}

	@Override
	public void start() {
		
		getLoginView().showModal();
	}

	@Override
	public void signIn() {

		String username = getLoginView().getLoginUsername();
		String password = getLoginView().getLoginPassword();
		LoginCommand loginCommand = new LoginCommand(username, password);

		if(ClientFacade.getInstance().userLogin(loginCommand)) {
			getLoginView().closeModal();
			loginAction.execute();
		}
		else {
			// todo: notify user to retry ?
		}
	}

	@Override
	public void register() {
		
		// TODO: register new user (which, if successful, also logs them in)
		String registerUsername = getLoginView().getRegisterUsername();
		String registerPassword = getLoginView().getRegisterPassword();
		String registerPasswordRepeat = getLoginView().getRegisterPasswordRepeat();

		if(!registerPassword.equals(registerPasswordRepeat)
			/*&& registerUsername is not in the list of users (on server)*/) {

		}

		
		// If register succeeded
		getLoginView().closeModal();
		loginAction.execute();
	}

	@Override
	public void update(Observable o, Object arg) {

	}
}

