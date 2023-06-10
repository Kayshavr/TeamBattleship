package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Message;
import model.Player;
import model.Shot;


public class ClientHandler extends Thread {

	private Socket client;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;

	
	public ClientHandler(Socket clientSocket)
	{
		try
		{
			this.client = clientSocket;
			this.out = new ObjectOutputStream(client.getOutputStream());
			this.in = new ObjectInputStream(client.getInputStream());
			out.writeObject("Succesfully created client thread.");
			
		}
		catch (IOException e)
		{
			System.out.println("Error creating client thread: " + e.getMessage());
		}
	}
	
	public void broadcast(Object message)
	{
		for (ClientHandler clientHandler : Server.clients) {
			try
			{
				clientHandler.getObjectWriter().writeObject(message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public ObjectOutputStream getObjectWriter() throws IOException
	{
		return out;
	}
	
	public void checkClientCount()
	{
		if(Server.clients.size() < 2)
		{
			broadcast("wait");
		}
		else
		{
			Server.playersConnected = true;
			broadcast("playersConnected");
		}
	}
	
	public void checkPlayerCount()
	{
		if(Server.players.size() < 2)
		{
			broadcast("wait");
		}
		else
		{
			broadcast("playersReady");
			Server.gameStarted = true;
			
			// The player who filled his field first will start the game first
			Server.currentTurn = Server.players.get(0);
			Message msg = new Message();
			msg.setNextTurn(Server.players.get(0).getPlayerName());
			msg.setShot(null);
			msg.setShip(null);
			broadcast(msg);			
		}	
	}
	
	public void run()
	{	
		if(!Server.playersConnected)
			checkClientCount();
		try
		{
			// Waiting for commands from the client
			while(true)
			{
				Object recievedObject = in.readObject();

				// Teams (strings) responsible for starting the game
				if(!Server.gameStarted)
				{
					
					String request = (String) recievedObject;
					// There is no 2 player - we process on request
					if(Server.players.size() < 2)
					{
						if(request.contains("nextPlayer"))
						{
							// Parse player objects for the server
							try
							{
								Player currentPlayer = (Player) in.readObject();
								Server.players.add(currentPlayer);
								System.out.println("New player: " + currentPlayer.getPlayerName());
								checkPlayerCount();
							}
							catch (ClassNotFoundException e)
							{
								System.out.println("Error reading player object on serverside");
								e.printStackTrace();
							}
						}
					}
				}
				else
				{				
					// Reading shot
					Shot shot = (Shot) recievedObject;
					System.out.println("New shot: (" + shot.getX() + ", " + shot.getY() + ")");
					
					// Logic
					Message response = Server.processShot(shot);
					broadcast(response);
					
					if(response.isVictory())
					{
						in.close();
						out.close();
						client.close();
						System.exit(0);
					}
				}				
			}
		}
		catch(IOException | ClassNotFoundException e)
		{
			System.out.println("Error parsing request " + e.getMessage());
		}
		finally
		{
			// When the server is shut down - we close it
			try
			{
				in.close();
				out.close();
				client.close();
			}
			catch(IOException e)
			{
				System.out.println("Error closing clientHandler ports " + e.getMessage());
			}
		}
	}
}
