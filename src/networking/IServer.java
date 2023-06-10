package networking;

public interface IServer {
	public void sendChatMessage(String content);
	public void sendKickUser(String kickNickname);
	public boolean checkCanStartGame();
	public void sendClose();
}
