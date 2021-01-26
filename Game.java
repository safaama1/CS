import java.awt.EventQueue;
import java.util.Iterator;
import java.util.Random;


public class Game extends Thread {

	private int nextPlayerIndex=0;
	private static Random random = new Random();

	/*
	 * Determines the ratio between the simulated time and running time.
	 * Specifically, this number states how many milliseconds should the program
	 * wait to simulate one minute, if periods in the constructor to store are
	 * specified in minutes. Thus, if 'TIME_SIMULATION_FACTOR' is set to 1, a
	 * service time of 1 minutes will be simulated as 1 milliseconds, and a service
	 * time of 10 minutes will be simulated as 10 milliseconds.
	 * 
	 * A good value is 1000, making the simulation clock run 60 faster than the
	 * processes it simulates. Then a service time of 1 minutes(=60 seconds) will be
	 * simulated as 1000 milliseconds(=1 second). Simulating a 1-hour game should
	 * take 1 minute.
	 * 
	 * For example, to simulate that a thread is sleeping x minutes. You should use
	 * sleep(x*TIME_SIMULATION_FACTOR);
	 */
	
	public static final int TIME_SIMULATION_FACTOR = 1000;
	public static final int FLIGHT_TIME_MS = 1000;

	private Players players;
	private Ball ball;
	private Display display;
	private double playerActiveMean;
	private double playerActiveVar;
	private double playerArrivalMean;
	private double playerArrivalVar;

	
	private long gameTimeMS;
	private long startTime;

	/**
	 * Constructor
	 *
	 */
	public Game(long timeForGameM, int numPlayers, double playerActiveMean, double playerActiveVar,
			double playerArrivalMean, double playerArrivalVar) throws Exception {
		if(numPlayers < 2) {
			throw new IllegalArgumentException("Players count should be more than 2");
		}
		this.gameTimeMS = timeForGameM *TIME_SIMULATION_FACTOR ;
		this.playerActiveMean = playerActiveMean;
		this.playerActiveVar = playerActiveVar;
		this.playerArrivalMean = playerArrivalMean;
		this.playerArrivalVar = playerArrivalVar;
		players=new Players();
		//adds the players
		for (int i = 0; i < numPlayers; ++i) {
			Player p = new Player(i, this, getPlayerActiveTime());
			players.addPlayer(p);
			p.start();
		}
		//keeps the index of the last player
		this.nextPlayerIndex=numPlayers;
		display =new Display(this);
	}

	public void run() {
		
		//keeps the time of when it started
		this.startTime = System.currentTimeMillis();
		Thread addNewPlayers = createAddingNewPlayerThread(this);
		
		addNewPlayers.start();
		Player firstOwner = players.getPlayer(0);
		ball = new Ball(firstOwner, players.getOther(firstOwner), true);
		System.out.println("Game Started");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Display.create(display);
			}
		});
		
		while (!isGameActive()) {
			

			
			if(players.getCount()>=2) {
				ball.setIn_flight(false);
				/*just to guarantee if the receiver is still in the game(if it is,we move the ball 
				 * to another player that still playing)  */
				Player nextOwnerPlayer = ball.getReceiver().isAlive()?ball.getReceiver():players.getOther(ball.getReceiver());
				display.placePlayers(players);
				ball.setOwner(nextOwnerPlayer);
				ball.setReceiver(players.getOther(nextOwnerPlayer));
				ball.setIn_flight(true);
				System.out.println(ball.getOwner().GetName()+"  is thowing to  "+ ball.getReceiver().GetName());
				display.fly(ball);
			}else {
				//waits until another players join the game
				System.out.println("Less than 2 players in the game (" + players.getCount() + "), will wait for new players.");
				display.placePlayers(players);
			}
		}
		
		GameOver.create();
		System.out.println("Game Over");
		// interrupt the thread of adding players 
		addNewPlayers.interrupt();
		//Interrupts every player that still didn't quit or removed
		Iterator<Player> playersIterator = players.iterator();
		while (playersIterator.hasNext()) {
			playersIterator.next().interrupt();
		}
	}

	private int getPlayerActiveTime() {
		return gaussian(this.playerActiveMean, this.playerActiveVar);
	}

	private int getNextPlayerArrivalTime() {
		return gaussian(this.playerArrivalMean, this.playerArrivalVar);
	}
  /* returns a new thread that adds the players (and runs every one of them)*/
	private Thread createAddingNewPlayerThread(Game game) {
		return new Thread() {
			public void run() {
				int nextPlayerIndexName = nextPlayerIndex;
				while (true) {
					try {
						sleep(getNextPlayerArrivalTime() * TIME_SIMULATION_FACTOR);
					} catch (InterruptedException e) {
						System.out.println( "createAddingNewPlayerThread is ended");
						return;
					}
					Player p = new Player(nextPlayerIndexName, game, getPlayerActiveTime());
					players.addPlayer(p);
					p.start();
					nextPlayerIndexName++;
				}
			}
		};
	}
/* function that checks if the time of the game is ended */
	private boolean isGameActive() {
		return this.startTime + this.gameTimeMS < System.currentTimeMillis();
	}

	/**
	 * gaussian - compute a random number drawn from a normal (Gaussian)
	 * distribution
	 *
	 * @param periodMean - the mean of the distribution
	 * @param periodVar  - the variance of the distribution
	 * @return
	 */
	public static int gaussian(double periodMean, double periodVar) {
		double period = 0;
		while (period < 1)
			period = periodMean + periodVar * random.nextGaussian();
		return ((int) (period));
	}

	public synchronized void removePlayer(Player player) {
		players.removePlayer(player);
	}
	

}