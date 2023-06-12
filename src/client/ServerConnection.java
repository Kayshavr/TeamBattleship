package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Message;
import model.Player;
import model.Shot;

public class ServerConnection extends Thread{

	private Socket server;
	private Client client;
	private Player player;
	private Shot shot;

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Message message;
	
	private boolean playersConnected = false;
	private boolean playersReady = false;
	private boolean ready = false;
	private boolean gameStarted = false;
	
	public ServerConnection(Socket server, Player player, Client client) throws IOException
	{
		this.server = server;
		this.client = client;
		this.player = player;
		this.out = new ObjectOutputStream(server.getOutputStream());
		this.in = new ObjectInputStream(server.getInputStream());		
	}
	
	public void setReady(boolean ready)
	{
		this.ready = ready;
	}
	
	public void sendShot(Shot shot)
	{
		this.shot = shot;
		try
		{
			out.writeObject(this.shot);
		}
		catch(IOException e)
		{
			System.out.println("Error sending shot: " + e.getMessage());
		}
	}
	
	public void closeConnection()
	{
		try {
			in.close();
			out.close();
			server.close();
		}
		catch (IOException e)
		{
			System.out.println("Error closing connection: " + e.getMessage());
		}
	}
	
	public void sendPlayerObject()
	{
		try
		{
			out.writeObject("nextPlayer");
			out.writeObject(player);
		}
		catch (IOException e)
		{
			System.out.println("Error sending Player object: " + e.getMessage());
		}
	}

	public void quit() {
		try {
			out.writeObject("clientQuit");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void sendChatMsg(String msg) {
		try {
			Message msgObj= new Message();
			msgObj.setChat(true);
			msgObj.setContent(msg);

			out.writeObject(msgObj);

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void run()
	{
		try
		{
			while(true)
			{
				Object recievedObject = in.readObject();
				if(recievedObject.toString().contains("end")){
					client.exit();
				}
				else if(!gameStarted)
				{
					String serverResponse = recievedObject.toString();
					System.out.println("[SERVER]: " + serverResponse);

					//If all users are not logged in - open waiting panel
					if(!playersConnected)
					{
						if(serverResponse.equals("wait"))
						{
							client.openWaitingScreen();
						}
						else if(serverResponse.equals("playersConnected"))
						{
							client.closeWaitingScreen();
							playersConnected = true;
							
						}
					}

					// Wait for players to be ready
					if(playersConnected && !playersReady && ready)
					{
						if(serverResponse.equals("wait"))
						{
							client.openWaitingScreen();
						}
						else if(serverResponse.equals("playersReady"))
						{
							client.closeWaitingScreen();
							playersReady = true;
							gameStarted = true;
						}
					}
				}
				else
				{
					message = (Message) recievedObject;
					if(message.isChat()){
						// Chat Message
						client.displayMessage(message.getContent());
					}else {
						// Process message
						client.processMessage(message);
					}
				}

			}
		}
		catch(IOException e)
		{
			System.out.println("Error retrieving message: " + e.getMessage());
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				in.close();
				out.close();
				server.close();
			}
			catch(IOException e)
			{
				System.out.println("Error closing socket: " + e.getMessage());
			}
		}
	}
}
