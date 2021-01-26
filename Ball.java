public class Ball {
	private Player owner;
	private Player receiver;
	private boolean in_flight;
	
	public Ball(Player owner,Player receiver,boolean in_flight )
	{
		this.owner=owner;
		this.receiver=receiver;
		this.in_flight=in_flight;
	}
	
	public Player getOwner() {
		return owner.copy();
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	public Player getReceiver() {
		return receiver;
	}
	public void setReceiver(Player receiver) {
		this.receiver = receiver;
	}
	public boolean isIn_flight() {
		return in_flight;
	}
	public void setIn_flight(boolean in_flight) {
		this.in_flight = in_flight;
	}
	

}
