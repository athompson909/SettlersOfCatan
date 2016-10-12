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
	
	public Catan()
	{
		
		client.base.OverlayView.setWindow(this);
		
		this.setTitle("Settlers of Catan");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//this CatanPanel initializes a LeftPanel, which initializes the Chat/GameHistory components
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

				//instantiating a new Client object
				Client.getInstance(); //Singleton
				
				PlayerWaitingView playerWaitingView = new PlayerWaitingView();
				final PlayerWaitingController playerWaitingController = new PlayerWaitingController(
																									playerWaitingView);
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

				//THIS IS WHERE LOGIN STOPS AND JOINGAME BEGINS - THIS IS WHERE WE SHOULD INITIALIZE THE GAMELIST IN JOINGAMEVIEW
                loginController.setLoginAction(new IAction() {
					@Override
					public void execute()
					{
                        /*
                            Sierra put the JoinGameView's initialize() here so it would happen only AFTER the user has
                            logged in, and the ClientUser singleton has saved their name/id as a PlayerInfo object (localPlayer).
                            This ensures that the JoinGameView's populateGameList functions correctly check whether
                            the localPlayer is actually added to one of the games or not.
                            Before, joinView.initialize() was being called BEFORE the user had actually logged in,
                            so the populateGameList functions were checking for the empty localPlayer info,
                            and incorrectly returned true (that the localPlayer WAS added to a game, when they really weren't).
                         */
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

