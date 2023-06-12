package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.Message;
import model.Player;
import model.Shot;

public class Server extends Thread {
	private int PORT;
	private ServerSocket listener;
	public ArrayList<ClientHandler> clients = new ArrayList<>();
	public ArrayList<Player> players = new ArrayList<>();
	
	public Player currentTurn;
	public boolean gameStarted = false;
	public boolean playersConnected = false;

	
	public Server(int PORT) throws IOException {
		this.PORT = PORT;
	}

	@Override
	public void run(){

		try {
			listener = new ServerSocket(this.PORT);
			System.out.println("Server running. Waiting for connections...");

			try {
				// Waiting for client connections
				while (true) {
					Socket client = listener.accept();
					System.out.println("New client connected");

					// Creates and calls the clientHandler thread
					ClientHandler clientThread = new ClientHandler(client, this);
					clients.add(clientThread);
					clientThread.start();
				}
			} finally {
				listener.close();
			}
		}catch (IOException e){
			System.out.println("Room disconnected!");
		}
	}

	public void interrupt() {
		try {
			listener.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Message processShot(Shot shot)
	{
		Message message = new Message();
		Player enemy = null;
		boolean hit = false;
		boolean sink = false;
		boolean victory = false;
		
		for (Player p : this.players)
			if(!p.equals(this.currentTurn))
				enemy = p;
		
		
		int status = enemy.getPlayerField()[shot.getY()][shot.getX()];
		
		if(status == 0)
		{
			// Miss
			hit = false;
			this.currentTurn = enemy;
		}
		else
		{
			// Hit
			hit = true;
			enemy.changeStatus(shot.getY(), shot.getX(), -1);
			
			// Check if the whole ship sunk
			if(sink = enemy.sunken(shot.getY(), shot.getX()))
			{
				message.setShip(enemy.getShipByCoordinates(shot.getY(), shot.getX()));
			}
			
			victory = enemy.checkAllSunken();
		}
		
		message.setShot(shot);
		message.setNextTurn(this.currentTurn.getPlayerName());
		message.setHit(hit);
		message.setSink(sink);
		message.setVictory(victory);
		
		return message;
	}
	

	public void main(String[] args) throws IOException {
		this.PORT  = 8989;

		ServerSocket listener = new ServerSocket(this.PORT);
		System.out.println("Server running. Waiting for connections...");
		
		try
		{
			while(true)
			{
				Socket client = listener.accept();
				System.out.println("New client connected");

				ClientHandler clientThread = new ClientHandler(client, this);
				clients.add(clientThread);
				clientThread.start();
			}
		}
		finally
		{
			listener.close();
		}
	}

	public void resetServer()
	{
//		currentTurn = null;
//		gameStarted = false;
//		playersConnected = false;
//		players.clear();
//
//		for (ClientHandler clientHandler : clients) {
//			try {
//				clientHandler.interrupt();
//				clientHandler.in.close();
//				clientHandler.out.close();
//				clientHandler.client.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		clients.clear();

	}
}
