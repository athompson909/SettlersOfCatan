package client.join;

import client.ClientFacade;
import client.ClientUser;
import client.base.OverlayView;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.utils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementation for the player waiting view, which is displayed when the user is
 * waiting for other players to join their game
 */
@SuppressWarnings("serial")
public class PlayerWaitingView extends OverlayView implements IPlayerWaitingView {

	private final int LABEL_TEXT_SIZE = 40;
	private final int BUTTON_TEXT_SIZE = 28;
	private final int AI_TEXT_SIZE = 20;
	private final int BORDER_WIDTH = 10;
	private final int NUMBER_OF_PLAYERS = 4;

	private JLabel label;
	private JButton addAiButton;
	private JPanel aiPanel;
	private JPanel center;
	private SpinnerListModel aiModel;
	private JSpinner aiChoices;

	public PlayerWaitingView() {

		System.out.println("PLAYERWAITINGVIEW: constructor called:");

		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));
		
		//set the heading at the top of the pane
		label = new JLabel("Player Waiting View");
		FontUtils.setFont(label, LABEL_TEXT_SIZE);
		this.add(label, BorderLayout.NORTH);
		
		//create the center panel that displays player info
		center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		this.add(center, BorderLayout.CENTER);	
		
		//create the AI panel for the bottom of the pane
		aiPanel = new JPanel();
		aiPanel.setLayout(new BoxLayout(aiPanel, BoxLayout.Y_AXIS));
		
		//create the AI type panel
		JPanel aiTypePanel = new JPanel();
		aiTypePanel.setLayout(new BoxLayout(aiTypePanel, BoxLayout.X_AXIS));
		
		aiTypePanel.add(Box.createHorizontalGlue());
		
		JLabel aiTypeLabel = new JLabel("Select AI Type:");
		FontUtils.setFont(aiTypeLabel, AI_TEXT_SIZE);
		aiTypePanel.add(aiTypeLabel);
		
		aiTypePanel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		aiModel = new SpinnerListModel();
		aiChoices = new JSpinner(aiModel);
		((JSpinner.DefaultEditor)aiChoices.getEditor()).getTextField().setEditable(false);
		FontUtils.setFont(aiChoices, AI_TEXT_SIZE);
		aiTypePanel.add(aiChoices);

		aiTypePanel.add(Box.createHorizontalGlue());
		
		aiPanel.add(aiTypePanel);
		
		aiPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		//create the AI button panel
		JPanel aiButtonPanel = new JPanel();
		aiButtonPanel.setLayout(new BoxLayout(aiButtonPanel, BoxLayout.X_AXIS));
				
		aiButtonPanel.add(Box.createHorizontalGlue());
		
		addAiButton = new JButton("Add a computer player");
		addAiButton.addActionListener(actionListener);
		FontUtils.setFont(addAiButton, BUTTON_TEXT_SIZE);
		aiButtonPanel.add(addAiButton);
		
		aiButtonPanel.add(Box.createHorizontalGlue());
		
		aiPanel.add(aiButtonPanel);
		
		aiPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		//add the AI panel
		this.add(aiPanel, BorderLayout.SOUTH);


		//SET AI CHOICES -
		String[] allAIsArr = ClientFacade.getInstance().listAI();
		setAIChoices(allAIsArr);
	}

	//listener for the "add AI player" button
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addAiButton) {

				System.out.println("PLAYERWAITINGVIEW: ADD AI BTN pushed");

				getController().addAI();

				//now call setPlayers here:
				//ask server for gamelist
				GameInfo[] newGameInfoArr = ClientFacade.getInstance().gamesList();
				//get the list of players from the server response using the current gameID
				GameInfo currGameInfo = newGameInfoArr[ClientUser.getInstance().getCurrentGameID()];
				//use that list of players to do setPlayers() here
				List<PlayerInfo> newPlayerInfoList = currGameInfo.getPlayers();
				//turn it into an array
				PlayerInfo[] setThesePlayerInfos = newPlayerInfoList.toArray(new PlayerInfo[newPlayerInfoList.size()]);
				//give that array of PlayerInfos to setPlayers()
				setPlayers(setThesePlayerInfos);

				//we may need to do SetPlayers() earlier than this.
				//PlayerWaitingView needs to show the players existing in the game too
			}
		}	
	};

	@Override
	public IPlayerWaitingController getController() {
		return (IPlayerWaitingController)super.getController();
	}

	@Override
	public void setPlayers(PlayerInfo[] value) {

		//set header label indicating how many players are still needed
		String labelText = "";
		if(value.length == NUMBER_OF_PLAYERS){
			labelText = "This game is ready to go!";
			addAiButton.setEnabled(false);
            startGame();
		}
		else{
			labelText = ("Waiting for Players: Need " + (NUMBER_OF_PLAYERS-value.length) + " more");
			addAiButton.setEnabled(true);
		}
		
		label.setText(labelText);
		
		//the center panel contains the individual player panels
		center.removeAll();
		
		//build an individual player panel and add it to the center panel
		for(int i = 0; i < value.length; i++){
			String builtString = (i+1) + " " + value[i].getName();
			JPanel playerPanel = new JPanel();
			playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); //left justify the text in the panel
			playerPanel.setPreferredSize(new Dimension(200,50));
			playerPanel.setBackground(value[i].getColor().getJavaColor()); //set the background color of the player
			JLabel playerLabel = new JLabel(builtString, SwingConstants.LEFT); //justify the text left
			FontUtils.setFont(playerLabel, LABEL_TEXT_SIZE);
			playerPanel.add(playerLabel);
			center.add(playerPanel);
			
			//add space between player panels
			Dimension minSize = new Dimension(5, 10);
			Dimension prefSize = new Dimension(5, 10);
			Dimension maxSize = new Dimension(Short.MAX_VALUE, 10);
			center.add(new Box.Filler(minSize, prefSize, maxSize));			
		}
	}

    /**
     * this has everything to do with starting the game when ready
     */
	private void startGame() {
	    closeModal();
    }

	//This list of AITypes should come from the server: listAICommand
	//we can access the server/ClientFacade since the ClientFacade is a singleton!
	@Override
	public void setAIChoices(String[] value) {	

		System.out.println("PLAYERWAITINGVIEW: setAIChoices called:");

		java.util.List<String> choiceList = new ArrayList<String>();
		//java.util.List<String> choiceList = ClientFacade.getInstance().listAI();

		//adds all Strings from String[] into an ArrayList<String>
		// ... for some reason. we couldn't just start with an ArrayList<String>? lol
		for (String v : value) {
			choiceList.add(v);
		}
		
		aiModel.setList(choiceList);
		
		if (value.length > 0) {
			aiChoices.setValue(value[0]);
		}
	}

	@Override
	public String getSelectedAI() {
		return (String)aiChoices.getValue();
	}

}

