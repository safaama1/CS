import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Display extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int FLIGHT_TIME_MS = 1000;//the time that takes the ball to get to the player
	
	private int oldX ;
	private int oldY ;
	private int newX ;
	private int newY ;

	private static final int SIZE = 256;
	private int a = SIZE / 2;
	private int b = a;
	private int r =2 * SIZE / 5;
	
	private int receiver ;
	private int owner;
	/**
	 * 
	 */
	private  Game game;
	private Players players;

	private Ball ball;

	private double dx;
	private double dy;

	public Display(Game game) {
		super(true);
		this.setPreferredSize(new Dimension(SIZE, SIZE));
		this.game=game;
	}

	public void placePlayers(Players players) {
		this.players=players;
		//setPlayersPosition(players);
		repaint();
	}

	public void fly(Ball ball) {

		this.ball=ball;
		setPlayersPosition(players);
		
		Player p1=findPlayer(ball.getOwner());
		Player p2=findPlayer(ball.getReceiver());
		
		owner=p1.GetName();
		receiver=p2.GetName();

//		placeX=(int) p1.getPoint().getX();
//		placeY=(int) p1.getPoint().getY();
		
		Point point1=new Point();
		Point point2=new Point();
		
		point1=p1.getPoint();
		point2=p2.getPoint();
		
		double m =(point2.getY()-point1.getY())/(point2.getX()-point1.getX());
	
		if (Math.abs(m)>1) {
			
			if(m>0) {
				if(point1.getX()>point2.getX() && point1.getY()>point2.getY()) {
					dx=-1;
					dy=(-1)*m;
				}
				if(point1.getX()<point2.getX() && point1.getY()<point2.getY()) {
					dx=1;
					dy=m;
				}
				
			}
			if(m<0) {
				if(point1.getX()>point2.getX() && point1.getY()<point2.getY()) {
					dx=-1;
					dy=(-1)*m;
				}
				if(point1.getX()<point2.getX() && point1.getY()>point2.getY()) {
					dx=1;
					dy=m;
				}
			}
		}else if(Math.abs(m)<1&& m!=0) {
			
			if(m>0) {
				if(point1.getX()>point2.getX() && point1.getY()>point2.getY()) {
					dy=-1;
					dx=(-1)*(1/m);
				}
				if(point1.getX()<point2.getX() && point1.getY()<point2.getY()) {
					dy=1;
					dx=1/m;
				}
			}
			if(m<0) {
				if (point1.getX() > point2.getX() && point1.getY() < point2.getY()) {
					dy = 1;
					dx = (1 / m);
				}
				if (point1.getX() < point2.getX() && point1.getY() > point2.getY()) {
					dy = -1;
					dx = (-1)*(1 / m);
				}
			
			}
			
		}else if(Math.abs(m)==0) {
			if(point1.getX()>point2.getX()) {
				dy=0;
				dx=-1;
			}
			else if(point1.getX()<point2.getX()) {
				dy=0;
				dx=1;
			}
		}
		
		if (Double.isInfinite(m))
		{
			if(point1.getX()==point2.getX()) {
				if(point1.getY()>point2.getY()) {
					dy=-1;
					dx=0;
				}
				if(point1.getY()<point2.getY()) {
					dy=1;
					dx=0;
				}	
			}
			if(point1.getY()==point2.getY()) {
				if(point1.getX()>point2.getX()) {
					dx=-1;
					dy=0;
				}
				if(point1.getY()<point2.getY()) {
					dx=1;
					dy=0;
				}	
			}
		   
		}
		oldX=(int)point1.getX();
		oldY=(int)point1.getY();
		newX=((int)point1.getX());
		newY=((int)point1.getY());
		try {
			Thread.sleep(FLIGHT_TIME_MS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (point1.getX() != 0 && point1.getY() != 0) {
			if (point1.getX() == point2.getX() || point1.getY() == point2.getY()) {

				while (newX != point2.getX() || newY != point2.getY()) {

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					move();
					repaint();
				}

			}

			else {
				while (newX != point2.getX() && newY != point2.getY()) {

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					move();
					repaint();
				}
			}
		}
	}

	private void move() {
		oldX = newX;
		oldY = newY;
		newX +=(int) dx;
		newY +=(int) dy;
	}
	public static void create(Display display) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add( display);
		f.pack();
		f.setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(getBackground());
		g.fillRect(oldX-10, oldY-15, 20, 20);
		g.setColor(Color.blue);
		g.fillOval(newX-10, newY-15, 20, 20);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		a = getWidth() / 2;
		b = getHeight() / 2;
		int m = Math.min(a, b);
		r = 7 * m / 10;
		int r2 = Math.abs(m - r) / 2;
		
		int playersCount =players.getCount();
		for (int i = 0; i < playersCount; i++) {
			double t = 2 * Math.PI * i / playersCount;
			int x = (int) Math.round(a + r * Math.cos(t));
			int y = (int) Math.round(b + r * Math.sin(t));
			if(receiver==i) {
				g2d.setColor(Color.GREEN);
				g2d.drawOval(x - r2, y - r2,  2* r2, 1*r2);	
				g2d.drawString(players.getPlayer(i).GetName()+"", x-3, y+3-Math.round(r2/2));
			}
			else if(owner==i) {
				g2d.setColor(Color.RED);
				g2d.drawOval(x - r2, y - r2,  2* r2, 1*r2);	
				g2d.drawString(players.getPlayer(i).GetName()+"", x-3, y+3-Math.round(r2/2));
			}
			else {
				g2d.setColor(Color.BLACK);
				g2d.drawOval(x - r2, y - r2,  2* r2, 1*r2);	
				g2d.drawString(players.getPlayer(i).GetName()+"", x-3, y+3-Math.round(r2/2));
			}
		}  
		
	}
	public Player findPlayer(Player player) {
		for(int i=0;i<players.getCount();i++) {
			if(players.getPlayer(i).GetName()==player.GetName())
				return players.getPlayer(i);
		}
		return null;
	}
	public void setPlayersPosition(Players players) {
		a = getWidth() / 2;
		b = getHeight() / 2;
		
		int m = Math.min(a, b);
		r = 7 * m / 10;
		int r2 = Math.abs(m - r) / 2;
		int playersCount =players.getCount();
		
		for (int i = 0; i < playersCount; i++) {
			double t = 2 * Math.PI * i / playersCount;
			int x = (int) Math.round(a + r * Math.cos(t));
			int y = (int) Math.round(b + r * Math.sin(t));
			players.getPlayer(i).setPoint2((double)x, (double)y);
			
		}
		
	}

}