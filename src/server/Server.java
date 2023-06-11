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
			ServerSocket listener = new ServerSocket(this.PORT);
			System.out.println("Server running. Waiting for connections...");

			try {
				// Gaida klientu savienojumus
				while (true) {
					Socket client = listener.accept();
					System.out.println("New client connected");

					// Izveido un saak clientHandler thread
					ClientHandler clientThread = new ClientHandler(client, this);
					clients.add(clientThread);
					clientThread.start();
				}
			} finally {
				listener.close();
			}
		}catch (IOException e){
			System.out.println("Noooooo!");
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
		// Ja nav noradits porta nr. tad uzstada default port.
		if(args.length < 1)
			this.PORT = 8989;
		else
			this.PORT = Integer.parseInt(args[0]);
		
		
		
		ServerSocket listener = new ServerSocket(this.PORT);
		System.out.println("Server running. Waiting for connections...");
		
		try
		{
			// Gaida klientu savienojumus
			while(true)
			{
				Socket client = listener.accept();
				System.out.println("New client connected");
				
				// Izveido un saak clientHandler thread
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
}
