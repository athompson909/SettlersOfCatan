package client.login;

import client.Client;
import client.ClientFacade;
import client.base.Controller;
import client.base.IAction;
import client.misc.IMessageView;
import client.misc.MessageView;
import client.utils.MessageUtils;
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
	private final String loginFailedMsg = "Invalid username or password. Please try again!";
	private final String invalidUsernameMsg = "Username must be 3-7 characters long, and can include letters, numbers," +
			" underscore, or dash.";
	private final String invalidPasswordMsg = "Password must be 5-16 characters long, and can include letters, numbers," +
			" underscore, or dash.";
	private final String unmatchPasswordsMsg = "Passwords don't match. Please try again!";
	private Pattern unDelim = Client.getInstance().getUsernameDelimiter();
	private Pattern pwDelim = Client.getInstance().getPasswordDelimiter();

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
	 * Checks Login username and password fields for validity
	 * If they're both ok, sends LoginCommand to ClientFacade.
	 */
	@Override
	public void signIn() {

		String username = getLoginView().getLoginUsername();
		String password = getLoginView().getLoginPassword();
		if (!unDelim.matcher(username).matches() || !pwDelim.matcher(password).matches()){
			showRejectMessage("Error", loginFailedMsg);
			return;
		}

		LoginCommand loginCommand = new LoginCommand(username, password);

		if(ClientFacade.getInstance().userLogin(loginCommand)) {
			getLoginView().closeModal();
			loginAction.execute();
		}
		else {
			showRejectMessage("Error", loginFailedMsg);
		}
	}

	/**
	 * Checks the Register username and password fields for validity
	 * If they're all ok, sends Register command to ClientFacade.
	 */
	@Override
	public void register() {

		String registerUsername = getLoginView().getRegisterUsername();
		String registerPassword = getLoginView().getRegisterPassword();
		String registerPasswordRepeat = getLoginView().getRegisterPasswordRepeat();

		if(!unDelim.matcher(registerUsername).matches()){
			showRejectMessage("Error", invalidUsernameMsg);
			return;
		}
		else if (!pwDelim.matcher(registerPassword).matches()) {
			showRejectMessage("Error", invalidPasswordMsg);
			return;
		}

		if(registerPassword.equals(registerPasswordRepeat)) {
			RegisterCommand registerCommand = new RegisterCommand(registerUsername, registerPassword);

			if(ClientFacade.getInstance().userRegister(registerCommand)) {
				// If register succeeded
				getLoginView().closeModal();
				loginAction.execute();
			}
			else showRejectMessage("Server Error", "Registration Failed");


		}
		else showRejectMessage("Registration Error", unmatchPasswordsMsg);
	}

	/**
	 * Displays a customizable error message
	 * @param title message title
	 * @param message message content
	 */
	private void showRejectMessage(String title, String message) {
		MessageUtils.showRejectMessage((MessageView) messageView, title, message);

		// clear both panels
		((LoginView) getLoginView()).clearLoginPanel();
		((LoginView) getLoginView()).clearRegisterPanel();
	}

	//UNUSED
	@Override
	public void update(Observable o, Object arg) {}
}

