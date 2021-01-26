import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random; 
public class Players implements Iterable<Player> 

{
	private static Random random = new Random();
	private List<Player> players;
	
	
	public Players()
	{
		this.players=new ArrayList<Player>();
	}
	/**
	 * add player p
	 */
	public synchronized void addPlayer(Player p)
	{
			players.add(p);
			System.out.println("Add Player "+p.GetName());
		
	}
	
	/**
	 * remove player p
	 */
	public synchronized void removePlayer(Player p) 
	{
			players.remove(p);
			System.out.println("Remove Player "+p.GetName());
	}

	
	/**
	 * @return a player which is not p.
	 */
	public synchronized Player getOther(Player currentPlayer) 
	{
		boolean keepSearch=true;
		int randomPlayerIndex=0;
		while(keepSearch==true)
		{
			randomPlayerIndex=random.nextInt(players.size());
			//if the players are different
			if(!currentPlayer.equals(players.get(randomPlayerIndex)))
			{
				//quits the wile
				keepSearch=false;
			}
		}
		return players.get(randomPlayerIndex);
	}

	/**
	 * @return the number of players
	 */
	public synchronized int getCount()
	{
			return players.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Player> iterator() 
	{
		Iterator<Player> iterator = players.iterator(); 
		return iterator;
	}
	public Player getPlayer(int i) {
		return players.get(i);
	}

}
