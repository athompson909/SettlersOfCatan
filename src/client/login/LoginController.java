package client.login;

import client.ClientFacade;
import client.base.Controller;
import client.base.IAction;
import client.misc.IMessageView;
import client.misc.MessageView;
import shared.model.commandmanager.game.LoginCommand;
import shared.model.commandmanager.game.RegisterCommand;

import java.util.Observable;
import java.util.regex.Pattern;


/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;

	private Pattern delimiter = Pattern.compile("([A-z]|[0-9]){1,24}");

	public Pattern getDelimiter() {
		return delimiter;
	}

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

	/**
	 * reject blank strings or strings longer than 24 characters
	 */
	@Override
	public void signIn() {

		String username = getLoginView().getLoginUsername();
		String password = getLoginView().getLoginPassword();
		if (!delimiter.matcher(username).matches() ||
				!delimiter.matcher(password).matches()) {
			showRejectMessage("Error", "username and password must be between 1 and 24 alphanumeric characters");
			return;
		}


		LoginCommand loginCommand = new LoginCommand(username, password);

		if(ClientFacade.getInstance().userLogin(loginCommand)) {
			getLoginView().closeModal();
			loginAction.execute();
		}
		else showRejectMessage("Error", "invalid username or password");
	}

	@Override
	public void register() {

		String registerUsername = getLoginView().getRegisterUsername();
		String registerPassword = getLoginView().getRegisterPassword();
		String registerPasswordRepeat = getLoginView().getRegisterPasswordRepeat();
		if(!delimiter.matcher(registerUsername).matches() ||
				!delimiter.matcher(registerPassword).matches()) {
			showRejectMessage("Error", "username must be between 1 and 24 alphanumeric characters");
			return;
		}

		if(registerPassword.equals(registerPasswordRepeat)) {
			RegisterCommand registerCommand = new RegisterCommand(registerUsername, registerPassword);

			if(ClientFacade.getInstance().userRegister(registerCommand)) {
				// If register succeeded
				getLoginView().closeModal();
				loginAction.execute();
			}
			else showRejectMessage("Server Error", "invalid registration");
		}
		else showRejectMessage("Error", "the values in the two password fields don't match");
	}

	/**
	 * after bad input is rejected a customizable message is displayed
	 * @param title message title
	 * @param message message content
	 */
	private void showRejectMessage(String title, String message) {
		MessageView loginFailedView = (MessageView) messageView;

		loginFailedView.setTitle(title, 220);
		loginFailedView.setMessage(message, 220);
		loginFailedView.setCloseButton("Retry");
		loginFailedView.showModal();

		// clear both panels
		((LoginView) getLoginView()).clearLoginPanel();
		((LoginView) getLoginView()).clearRegisterPanel();
	}

	@Override
	public void update(Observable o, Object arg) {

	}
}

