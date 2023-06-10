package model;

@SuppressWarnings("serial")
public class Message implements java.io.Serializable{

	private boolean hit;
	private boolean sink;
	private boolean victory;
	private String nextTurn;
	private Shot shot;
	private Ship Ship;

	private static final long serialVersionUID = 1L;

	private MessageType msgType;
	private String timestamp;
	private String nickname;
	private String content;

	public Message() {
	}

	public Message(String type, String timestamp, String nickname, String content)
	{
		this.msgType = MessageType.valueOf(type);
		this.timestamp = timestamp;
		this.nickname = nickname;
		this.content = content;
	}
	public Message(MessageType type, String timestamp, String nickname, String content)
	{
		this.msgType = type;
		this.timestamp = timestamp;
		this.nickname = nickname;
		this.content = content;
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

	public MessageType getMsgType() {return msgType;}
	public void setMsgType(MessageType msgType) {this.msgType = msgType;}
	public String getTimestamp() {return timestamp;}
	public void setTimestamp(String timestamp) {this.timestamp = timestamp;}
	public String getNickname() {return nickname;}
	public void setNickname(String nickname) {this.nickname = nickname;}
	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}

	public String toString()
	{
		return this.timestamp + " " + this.nickname + "(" + this.msgType.toString() + "): " + this.content;
	}
}
