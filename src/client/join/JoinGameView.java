package client.join;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.Client;
import client.base.*;
import client.data.*;

/**
 * Implementation for the join game view, which lets the user select a game to
 * join
 */
@SuppressWarnings("serial")
public class JoinGameView extends OverlayView implements IJoinGameView
{

	private final int LABEL_TEXT_SIZE = 40;
	private final int PANEL_TEXT_SIZE = 14;
	private final int BUTTON_TEXT_SIZE = 28;
	private final int BORDER_WIDTH = 10;

	private JLabel label;
	private JLabel subLabel;

	private JLabel hash;
	private JLabel name;
	private JLabel currentPlayer;
	private JLabel join;

	private JButton createButton;
	private JButton tempJoinButton;

	private JPanel labelPanel;
	private JPanel gamePanel;
	private JPanel buttonPanel;

	private GameInfo[] games;
	private GameInfo[] initialGamesList;  //give this to setGames();
	private PlayerInfo localPlayer;

	public JoinGameView()
	{
        //TESTING
            //trying here
            //Go get list of games from the server, populate games[]:
            //fetchInitialGamesList();
            //setGames(initialGamesList, localPlayerInfoSoFar);

        //this.initialize();
	}

	//MiniPoller should probably call this function
	public void initialize()
	{
		System.out.println("===JOINGAMEVIEW - INITIALIZE called");
		this.initializeView();
	}

	private void initializeView()
	{
		System.out.println("===JOINGAMEVIEW - INITIALIZEVIEW called");

		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));

		label = new JLabel("Welcome to the game hub");
		Font labelFont = label.getFont();
		labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE);
		label.setFont(labelFont);
		subLabel = new JLabel("Join an existing game, or create your own");
		labelFont = subLabel.getFont();
		labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE * 2 / 3);
		subLabel.setFont(labelFont);

		labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());
		labelPanel.add(label);
		labelPanel.add(subLabel);
		this.add(labelPanel, BorderLayout.NORTH);


		// This is the header layout
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(0, 4));
		hash = new JLabel("#");
		labelFont = new Font(labelFont.getFontName(), Font.BOLD, PANEL_TEXT_SIZE);

		hash.setFont(labelFont);
		name = new JLabel("Name");
		name.setFont(labelFont);
		currentPlayer = new JLabel("Current Players");
		currentPlayer.setFont(labelFont);
		join = new JLabel("Join");
		join.setFont(labelFont);

		gamePanel.add(hash);
		gamePanel.add(name);
		gamePanel.add(currentPlayer);
		gamePanel.add(join);


		System.out.println("\tJOINGAMEVIEW: setting games/players list");

		// This is the looped layout
		if (games != null && games.length > 0)
		{
			labelFont = labelFont.deriveFont(labelFont.getStyle(), PANEL_TEXT_SIZE);
			for (GameInfo game : games)
			{
				JLabel tmp1 = new JLabel(String.valueOf(game.getId()));
				tmp1.setFont(labelFont);
				gamePanel.add(tmp1);
				JLabel tmp2 = new JLabel(game.getTitle());
				tmp2.setFont(labelFont);
				gamePanel.add(tmp2);
				String players = String.valueOf(game.getPlayers().size()) + "/4 : ";
				for (int j = 0; j < game.getPlayers().size(); j++) {
					if (j < game.getPlayers().size() - 1) {
						players = players + game.getPlayers().get(j).getName() + ", ";
					} else {
						players = players + game.getPlayers().get(j).getName();
					}
				}
				JLabel tmp3 = new JLabel(players);
				tmp3.setFont(labelFont);
				gamePanel.add(tmp3);
				JButton joinButton;

                // ***************************************************localPlayer used here
				//TESTING
				//System.out.println(">JGV: testing if game " + game.getTitle() + " has player " + localPlayer.getName());

				if (isInGame(game, localPlayer)) //(game.getPlayers().contains(localPlayer))
				{
						System.out.println("\t>JGV: game " + game.getTitle() + " has you!");
					joinButton = new JButton("Re-Join");
				}
				else if (game.getPlayers().size() >= 4)
				{
						System.out.println("\t>JGV: game " + game.getTitle() + " does NOT have you.");
					joinButton = new JButton("Full");
					joinButton.setEnabled(false);
				}
				else
				{
						System.out.println("\t>JGV: game " + game.getTitle() + " does NOT have you, but there is still room.");
					joinButton = new JButton("Join");
				}
				joinButton.setActionCommand("" + game.getId());
				joinButton.addActionListener(actionListener);
				gamePanel.add(joinButton);
			}
		}

		//Add all the above
		this.add(gamePanel, BorderLayout.CENTER);

		//TAKE THIS OUT
//		tempJoinButton = new JButton("Temporary Join Button");
//		tempJoinButton.addActionListener(actionListener);
//		Font buttonFont = tempJoinButton.getFont();
//		buttonFont = buttonFont.deriveFont(buttonFont.getStyle(), BUTTON_TEXT_SIZE);
//		tempJoinButton.setFont(buttonFont);
		///////

		createButton = new JButton("Create Game");
		createButton.addActionListener(actionListener);
		Font buttonFont = createButton.getFont();
		buttonFont = buttonFont.deriveFont(buttonFont.getStyle(), BUTTON_TEXT_SIZE);
		createButton.setFont(buttonFont);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(createButton);
//		buttonPanel.add(tempJoinButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * IsInGame checks whether a given PlayerInfo is contained in the Players list of the given GameInfo object.
	 * This is used to determine whether to show the button "Join", "Re-join", or "Full".
	 * This may really be only used to check for localPlayer, but I'm making it generic just in case.
	 *
	 * 	NOTE: this fix depends on our server NOT allowing duplicate player names OR playerIDs.
	 * 	I'm pretty sure we had this in mind already, but just in case!
	 *
	 * @param thisGame - the game to check if Player exists in it
	 * @param player - the PlayerInfo to check for inside thisGame. Right now it's only ever localPlayer
	 * @return - true if Player IS inside thisGame's list of Players, false otherwise
	 */
	private boolean isInGame(GameInfo thisGame, PlayerInfo player){

		//check each PlayerInfo within the selected GameInfo item
		for (PlayerInfo currPlayer : thisGame.getPlayers()){
			//if it matches the player's name/playerID, return true (stop looping)
			if (currPlayer.getName().equals(player.getName()) && currPlayer.getId() == player.getId())
			{
				return true;
			}
		}

		return false;
	}




	@Override
	public IJoinGameController getController()
	{
		return (IJoinGameController) super.getController();
	}

	//DEPRECATED
	public GameInfo[] getInitialGamesList() {
		return initialGamesList;
	}

	//DEPRECATED
	public void setInitialGamesList(GameInfo[] initialGamesList) {
		this.initialGamesList = initialGamesList;
	}

	@Override
	public void setGames(GameInfo[] games, PlayerInfo localPlayer)
	{
			System.out.println(">JOINGAMEVIEW: SETGAMES called:");
		this.games = games;
		this.localPlayer = localPlayer;

		this.removeAll();
        this.initializeView();
	}

	
	private ActionListener actionListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == createButton) {
				getController().startCreateNewGame();
			}
			else //if they pushed JOIN on an existing game
			{
				try
				{
					int gameId = Integer.parseInt(e.getActionCommand());
					GameInfo game = null;
					for (GameInfo g : games)
					{
						if (g.getId() == gameId)
						{
							game = g;
								System.out.println("They pushed JOIN game " + g.getTitle());
							Client.getInstance().setCurrentJoinGameCandidate(g.getId());

							break;
						}
					}

					//"game" is the game they just clicked to join
					getController().startJoinGame(game);

				}
				catch (NumberFormatException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	};
}

