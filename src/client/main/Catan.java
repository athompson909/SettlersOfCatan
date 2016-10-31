package client.main;

import client.Client;
import client.base.IAction;
import client.catan.CatanPanel;
import client.join.*;
import client.login.LoginController;
import client.login.LoginView;
import client.misc.MessageView;

import javax.swing.*;

/**
 * Main entry point for the Catan program
 */
@SuppressWarnings("serial")
public class Catan extends JFrame
{
	
	private CatanPanel catanPanel;
	//these are changed if they specify a host/port nubmer in the command line
	public static String hostNumber = "localhost";
	public static String portNumber = "8081";
	
	public Catan()
	{
		client.base.OverlayView.setWindow(this);
		
		this.setTitle("Settlers of Catan");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		catanPanel = new CatanPanel();

		this.setContentPane(catanPanel);
		
		display();
	}
	
	private void display()
	{
		pack();
		setVisible(true);
	}
	
	//
	// Main
	//
	
	public static void main(final String[] args)
	{
		System.out.println("CATAN - MAIN called");

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				new Catan();

				//if they specified a host/port number
				if (args.length == 1) {
				  //error
					System.out.println("ERROR: invalid host/port args");
					System.exit(0);
				}
				else if (args.length == 2){
					//good to pass the host/port nums to the ClientFacade
					hostNumber = args[0];
					portNumber = args[1];

					System.out.println("\t\t args[0] =" + args[0] + ", args[1] =" + args[1]);
				}

				//instantiating a new Client object
				Client.getInstance(); //Singleton
				Client.getInstance().setServerHostPort(hostNumber, portNumber);

				PlayerWaitingView playerWaitingView = new PlayerWaitingView();
				final PlayerWaitingController playerWaitingController = new PlayerWaitingController(playerWaitingView);
				playerWaitingView.setController(playerWaitingController);
				
				JoinGameView joinView = new JoinGameView();
				NewGameView newGameView = new NewGameView();
				SelectColorView selectColorView = new SelectColorView();
				MessageView joinMessageView = new MessageView();
				final JoinGameController joinController = new JoinGameController(
																				 joinView,
																				 newGameView,
																				 selectColorView,
																				 joinMessageView);
				joinController.setJoinAction(new IAction() {
					@Override
					public void execute()
					{
						playerWaitingController.start();
					}
				});
				joinView.setController(joinController);
				newGameView.setController(joinController);
				selectColorView.setController(joinController);
				joinMessageView.setController(joinController);
				
				LoginView loginView = new LoginView();
				MessageView loginMessageView = new MessageView();
				LoginController loginController = new LoginController(
																	  loginView,
																	  loginMessageView);

                loginController.setLoginAction(new IAction() {
					@Override
					public void execute()
					{
                        joinView.initialize();
						joinController.start();
					}
				});
				loginView.setController(loginController);
				loginView.setController(loginController);

				loginController.start();
			}
		});
	}
	
}

