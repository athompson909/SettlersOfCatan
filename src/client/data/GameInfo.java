package client.data;

import java.util.*;

/**
 * Used to pass game information into views<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique game ID</li>
 * <li>Title: Game title (non-empty string)</li>
 * <li>Players: List of players who have joined the game (can be empty)</li>
 * </ul>
 * 
 */
public class GameInfo
{
	private int id;
	private String title;
	private List<PlayerInfo> players;
	
	public GameInfo()
	{
		setId(-1);
		setTitle("");
		players = new ArrayList<PlayerInfo>();
        players.add(null);
        players.add(null);
        players.add(null);
        players.add(null);

	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void addPlayer(PlayerInfo newPlayer)
	{
		players.add(newPlayer);
	}
	
	public List<PlayerInfo> getPlayers()
	{
		return Collections.unmodifiableList(players);
	}

	public void setPlayers(List<PlayerInfo> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return "GameInfo{" +
				"id=" + id +
				", title='" + title + '\'' +
				", players=" + players +
				'}';
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GameInfo)) return false;

		GameInfo gameInfo = (GameInfo) o;

		if (id != gameInfo.id) return false;
		if (title != null ? !title.equals(gameInfo.title) : gameInfo.title != null) return false;

		//check players list
		if (players.size() != gameInfo.getPlayers().size())
			return false;
		for (int p = 0; p < players.size(); p++){
			if (!players.get(p).equals(gameInfo.getPlayers().get(p)))
				return false;
		}

		//passed all tests!
		return true;

	}

}

