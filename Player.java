

public class Player extends Thread {
	private Game game;
	private int name;
	private int timeMS; // the active time in miliseconds.
	private Point point;

	/**
	 * @param name
	 *            - the name of the player
	 * @param game
	 *            - the game the player participate
	 * @param timeActiveM
	 *            - the time (minutes) the player will play
	 */
	public Player(int name, Game game, int timeActiveM) {
		//super();
		this.name = name;
		this.game = game;
		this.timeMS = timeActiveM * Game.TIME_SIMULATION_FACTOR;
	}

	public void run() 
	{
		try {
			//the time witch the player will be active in the game
			sleep(timeMS);
			//removes the player after his time in the game ended
			game.removePlayer(this);
		} catch (InterruptedException e) {
			System.out.println("Player "+ name + " has left");
		}
	}
	
	public Player copy()
	{
		Player a=new Player(name,game,timeMS);
		return a;
	}

	public boolean equals(Player a)
	{
		if(this.name==a.GetName()) {
			return true;	
		}
		return false;
	}
	

	public int GetName()
	{
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getTimeMS() {
		return timeMS;
	}

	public void setTimeMS(int timeMS) {
		this.timeMS = timeMS;
	}
	
	public void setPoint2(double x,double y) {
		point=new Point();
		point.setX(x);
		point.setY(y);
	}
	public Point getPoint() {
		return point.copy();
	}
}
