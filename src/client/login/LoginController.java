package client.login;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import client.base.IAction;
import client.misc.IMessageView;
import shared.model.commandmanager.game.LoginCommand;
import shared.model.commandmanager.game.RegisterCommand;

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

		System.out.println("LOGINCONTROLLER: constructor called");
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

		//System.out.println("LOGINCONTROLLER: START called");

	}

	@Override
	public void signIn() {

		String username = getLoginView().getLoginUsername();
		String password = getLoginView().getLoginPassword();
		LoginCommand loginCommand = new LoginCommand(username, password);

		System.out.println("LOGINCONTROLLER: SIGNIN called: un= " + username + ", pw= " + password);

		if(ClientFacade.getInstance().userLogin(loginCommand)) {
			System.out.println("LOGINCONTROLLER: got success from ClientFacade, finishing login");

			//save the username to ClientUser singleton -> local Player data
			ClientUser.getInstance().setName(username);

			getLoginView().closeModal();
			loginAction.execute();  //how do we implement this action from IAction?
		}
		else {
			// todo: notify user to retry ?
			System.out.println("LOGINCONTROLLER: got FAIL from ClientFacade");

			//need to reset action somehow, it just stops here
		}
	}

	@Override
	public void register() {
		
		// TODO: register new user (which, if successful, also logs them in)
		String registerUsername = getLoginView().getRegisterUsername();
		String registerPassword = getLoginView().getRegisterPassword();
		String registerPasswordRepeat = getLoginView().getRegisterPasswordRepeat();

		System.out.println("LOGINCONTROLLER: REG: run= " + registerUsername + ", rpw= " + registerPassword);

		//This originally (?) had a !equals, which didn't make sense to me, so I took it out - Sierra
		if(registerPassword.equals(registerPasswordRepeat)) {
			RegisterCommand registerCommand = new RegisterCommand(registerUsername, registerPassword);

			if(ClientFacade.getInstance().userRegister(registerCommand)) {
				System.out.println("LOGINCONTROLLER: REG: got success from ClientFacade, finishing reg");
				// If register succeeded
				getLoginView().closeModal();
				loginAction.execute();
			}
			else {
				System.out.println("LOGINCONTROLLER: REG: got FAIL from ClientFacade");

				// the server rejected the input
			}
		}
		else {
			System.out.println("LOGINCONTROLLER: REG: passwords don't match");

			// the password and password repeat don't match
		}
	}

	@Override
	public void update(Observable o, Object arg) {


	}
}

