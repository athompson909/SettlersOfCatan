package client.turntracker;

import java.awt.*;

import javax.swing.*;

import shared.definitions.*;
import client.base.*;
import client.catan.*;
import client.utils.ImageUtils;
import shared.model.player.Player;


/**
 * Implementation for the turn tracker view, which displays whose turn it is, and what state the game is in
 */
@SuppressWarnings("serial")
public class TurnTrackerView extends PanelView implements ITurnTrackerView {

	private TitlePanel titlePanel;
	private GameStatePanel gameStatePanel;
	private JPanel [] playerPanel;
	private JPanel [] indicatorPanel;
	private JLabel [] playerPoints;
	private JLabel [] playerRoad;
	private JLabel [] playerArmy;
	private Image longestRoadImage;
	private Image largestArmyImage;

	private final int NUM_PLAYERS = 4;
	private final int FONT_SIZE = 13;
	
	public TurnTrackerView(TitlePanel titlePanel, GameStatePanel gameStatePanel) {
		
		this.titlePanel = titlePanel;
		this.gameStatePanel = gameStatePanel;
		
		this.setPreferredSize(new Dimension(350, 100));
		this.setLayout(new GridLayout(2,2,3,3));
		this.setBorder(BorderFactory.createEmptyBorder(3,3,3,3)); 
		
		playerPanel = new JPanel[NUM_PLAYERS];
		indicatorPanel = new JPanel[NUM_PLAYERS];
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			playerPanel[i] = new JPanel();
			this.add(playerPanel[i]);
		}
		
		playerPoints = new JLabel[NUM_PLAYERS];
		playerRoad = new JLabel[NUM_PLAYERS];
		playerArmy = new JLabel[NUM_PLAYERS];
		
		longestRoadImage = ImageUtils.loadImage("images/misc/road.png").getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		largestArmyImage = ImageUtils.loadImage("images/misc/army.png").getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	}

	@Override
	public ITurnTrackerController getController() {
		return (ITurnTrackerController)super.getController();
	}
	
	@Override
	public void setController(IController controller) {
		super.setController(controller);
		this.gameStatePanel.setButtonAction(new IAction() {
			@Override
			public void execute()
			{
				getController().endTurn();
			}
		});
	}

	@Override
	public void setLocalPlayerColor(CatanColor value) {
		
		titlePanel.setLocalPlayerColor(value);
		gameStatePanel.setButtonColor(value);
	}

	@Override
	public void initializePlayer(int playerIndex, String playerName,
			CatanColor playerColor) {
				
		playerPanel[playerIndex].setLayout(new BorderLayout());
		
		JLabel name = new JLabel(playerName);
		name.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		Font labelFont = name.getFont();
		labelFont = labelFont.deriveFont(Font.BOLD, FONT_SIZE);
		name.setFont(labelFont);
		playerPanel[playerIndex].add(name, BorderLayout.WEST);
		
		indicatorPanel[playerIndex] = new JPanel();
			//could put a null check here
		if (playerColor == CatanColor.NULL || playerColor == null) {
			//default to white?
			indicatorPanel[playerIndex].setBackground(CatanColor.WHITE.getJavaColor());
		}
		else {  //it's not null, ok to use real color
			indicatorPanel[playerIndex].setBackground(playerColor.getJavaColor());
		}
		playerPanel[playerIndex].add(indicatorPanel[playerIndex], BorderLayout.CENTER);
		
		playerArmy[playerIndex] = new JLabel();
		playerArmy[playerIndex].setIcon(new ImageIcon(largestArmyImage));
		indicatorPanel[playerIndex].add(playerArmy[playerIndex]);
		playerArmy[playerIndex].setVisible(false);
		
		playerRoad[playerIndex] = new JLabel();
		playerRoad[playerIndex].setIcon(new ImageIcon(longestRoadImage));
		indicatorPanel[playerIndex].add(playerRoad[playerIndex]);
		playerRoad[playerIndex].setVisible(false);
		
		playerPoints[playerIndex] = new JLabel("0");
		playerPoints[playerIndex].setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		playerPoints[playerIndex].setFont(labelFont);
		playerPanel[playerIndex].add(playerPoints[playerIndex], BorderLayout.EAST);

		if (playerColor == CatanColor.NULL || playerColor == null) {
			//default to white?
			playerPanel[playerIndex].setBackground(CatanColor.WHITE.getJavaColor());
		}
		else {  //it's not null, ok to use real color
			playerPanel[playerIndex].setBackground(playerColor.getJavaColor());

		}

			playerPanel[playerIndex].setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
	}

	@Override
	public void updatePlayer(int playerIndex, int points, boolean highlight,
			boolean largestArmy, boolean longestRoad) {
		playerArmy[playerIndex].setVisible(largestArmy);
		playerRoad[playerIndex].setVisible(longestRoad);
		playerPoints[playerIndex].setText(String.format("%d", points));
		
		
		if(highlight)
			playerPanel[playerIndex].setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 3));
		else
			playerPanel[playerIndex].setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
	}

	@Override
	public void updateGameState(String stateMessage, boolean enable) {

		gameStatePanel.updateGameState(stateMessage, enable);
	}

	public void updateColors(Player[] players){
		for(int i = 0; i < players.length; i++){

			if (players[i].getColor() == CatanColor.NULL || players[i].getColor() == null) {
				//default to white
				playerPanel[i].setBackground(CatanColor.WHITE.getJavaColor());
				indicatorPanel[i].setBackground(CatanColor.WHITE.getJavaColor());
			}
			else {  //it's not null, ok to use real color
				playerPanel[i].setBackground(players[i].getColor().getJavaColor());
				indicatorPanel[i].setBackground(players[i].getColor().getJavaColor());

			}

		}
	}
	
}


