package model;

@SuppressWarnings("serial")
public class Message implements java.io.Serializable{

	private boolean hit;
	private boolean sink;
	private boolean victory;
	private String nextTurn;
	private Shot shot;
	private Ship Ship;
	private boolean chat = false;
	private String content = null;

	public Message() {
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean isSink() {
		return sink;
	}

	public void setSink(boolean sink) {
		this.sink = sink;
	}

	public boolean isVictory() {
		return victory;
	}

	public void setVictory(boolean victory) {
		this.victory = victory;
	}

	public String getNextTurn() {
		return nextTurn;
	}

	public void setNextTurn(String nextTurn) {
		this.nextTurn = nextTurn;
	}

	public Shot getShot() {
		return shot;
	}

	public void setShot(Shot shot) {
		this.shot = shot;
	}

	public Ship getShip() {
		return Ship;
	}

	public void setShip(Ship ship) {
		Ship = ship;
	}
	public void setChat(boolean chat) {this.chat = chat;}
	public boolean isChat() {return chat;}
	public void setContent(String content) {this.content = content;}
	public String getContent() {return content;}
}
