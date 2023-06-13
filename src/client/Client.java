package client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

import ConnectionToDatabase.Cnx;

import javax.swing.*;

import model.Message;
import model.Player;
import model.Shot;
import ui.EnemyGrid;
import ui.GameSetupGrid;
import ui.PlayerGrid;


public class Client {

	private JFrame frame;
	private JFrame waitFrame;
	private JPanel waitPanel;
	private JPanel messagePanel;
	private JPanel shipSelectPanel;
	private JPanel gamePanel;

	private JButton continueButton;
	private JLabel descriptionLabel;
	private JLabel infoBox;
	private JLabel currentTurnLabel;

	private int[][] playerShips;
	private Player player;
	private String lastTurn;
	private DefaultListModel<String> listModel;
	
	private PlayerGrid playerGrid;
	private EnemyGrid enemyGrid;
	
	private String host;
	private String port;
	private String name;
	private int roomNumber;
	private Socket socket;
	private ServerConnection serverCon;
	
	// Stats
	private int hits = 0;
	private int misses = 0;
	private int shipsLeft = 4;
	
	public Client(String hostInput, String portInput, String nameInput, int roomNumber) {
		host = hostInput;
		port = portInput;
		name = nameInput;
		this.roomNumber = roomNumber;

		player = new Player(name);
      
        // Waiting panel
		waitFrame = new JFrame("Waiting for other player...");		
		waitPanel = new JPanel();
		
        JLabel waitLabel = new JLabel("Waiting for other player...");
        waitLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
        waitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitPanel.add(waitLabel);
        
		waitFrame.setLocationRelativeTo(null);
		waitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		waitFrame.setLayout(new BorderLayout());
		waitFrame.setMinimumSize(new Dimension(500, 100));
		waitFrame.add(waitPanel, BorderLayout.CENTER);
		waitFrame.pack();
		waitFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Interrupt the ServerConnection thread
				serverCon.quit();
				// Close the frame and exit the application
				waitFrame.dispose();
				System.exit(0);
			}
		});

		// Create the scroll pane and list
		JList<String> messageList = new JList<>();
		listModel = new DefaultListModel<>();
		messageList.setModel(listModel);
		JScrollPane scrollPane = new JScrollPane(messageList);

		// Create the text box and submit button
		JTextField textField = new JTextField();
		JButton submitButton = new JButton("Submit");
		submitButton.setBackground(Color.WHITE);

		// Add a hover effect to the submit button
		submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				submitButton.setBackground(Color.LIGHT_GRAY);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				submitButton.setBackground(Color.WHITE);
			}
		});

		// Add an action listener to the submit button
		submitButton.addActionListener(e -> {
			String message = textField.getText();
			if (!message.isEmpty()) {
				sendMessage(message);
				textField.setText("");
			}
		});

		// Create a panel for the text box and submit button
		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.setBackground(Color.GRAY);
		inputPanel.add(textField, BorderLayout.CENTER);
		inputPanel.add(submitButton, BorderLayout.EAST);

		messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messagePanel.setBackground(new Color(158, 216, 240));

		messagePanel.add(scrollPane, BorderLayout.CENTER);
		messagePanel.add(inputPanel, BorderLayout.SOUTH);


		// Display the JFrame

//		add(scrollPane, BorderLayout.CENTER);
//		add(inputPanel, BorderLayout.SOUTH);
//		setVisible(true);


		// Creates an empty grid for user
        playerShips = new int[10][10];
        for (int i = 0; i < playerShips.length; i++) {
			for (int j = 0; j < playerShips[i].length; j++) {
				playerShips[i][j] = 0;
			}
		}

        // Battlefield construction
        GameSetupGrid setupGrid = new GameSetupGrid(this);
	    
        shipSelectPanel = new JPanel();
        shipSelectPanel.setLayout(new GridBagLayout());
	    GridBagConstraints gbc2 = new GridBagConstraints();
        
        JLabel titleLabel1 = new JLabel("Setup your ships - press 'MOUSE2' to rotate.");
        titleLabel1.setFont(new Font("Verdana", Font.PLAIN, 24));
        titleLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        
        descriptionLabel = new JLabel("Place ship with size: 4");
        descriptionLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton continueButton = new JButton("Continue");
        this.continueButton = continueButton;
        continueButton.setVisible(false);
        continueButton.addActionListener(e ->
		{
			frame.setMinimumSize(new Dimension(300, 200));

			// Continue button pressed

			// Game Started
			player.setPlayerField(setupGrid.returnField());
			player.setPlayerShips(setupGrid.getShips());
			
			playerGrid = new PlayerGrid(player.getPlayerField());
			enemyGrid = new EnemyGrid(serverCon);
			
			// Send game data to the server
			serverCon.sendPlayerObject();
			serverCon.setReady(true);
	        
			
		    GridBagConstraints gbc3 = new GridBagConstraints();
		    
	        currentTurnLabel = new JLabel("Current Turn: ");
	        currentTurnLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
	        currentTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		    
	        JLabel yourFieldLabel = new JLabel("Your field");
	        yourFieldLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
	        yourFieldLabel.setHorizontalAlignment(SwingConstants.CENTER);
		    
	        JLabel enemyFieldLabel = new JLabel("Enemy field");
	        enemyFieldLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
	        enemyFieldLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        
	        infoBox = new JLabel("");
			infoBox.setText("Hits: 0 misses: 0 accuracy: 0% Enemy ships left: 4");
	        infoBox.setFont(new Font("Verdana", Font.PLAIN, 24));
	        infoBox.setHorizontalAlignment(SwingConstants.CENTER);
	        
	        gbc3.insets = new Insets(5,50,5,50);
	        gbc3.fill = GridBagConstraints.HORIZONTAL;       
	        gbc3.gridwidth = 2;
	        gbc3.gridx = 0;
	        gbc3.gridy = 0;
	        gamePanel.add(currentTurnLabel, gbc3);
	        gbc3.gridwidth = 1;
	        gbc3.gridx = 0;
	        gbc3.gridy = 1;
	        gamePanel.add(yourFieldLabel, gbc3);
	        gbc3.gridx = 1;
	        gbc3.gridy = 1;
	        gamePanel.add(enemyFieldLabel, gbc3);	        
	        gbc3.gridx = 0;
	        gbc3.gridy = 2;
	        gamePanel.add(playerGrid, gbc3);
	        gbc3.gridx = 1;
	        gbc3.gridy = 2;
	        gamePanel.add(enemyGrid, gbc3);
	        gbc3.gridwidth = 2;
	        gbc3.gridx = 0;
	        gbc3.gridy = 3;
	        gamePanel.add(infoBox, gbc3);
	        
			shipSelectPanel.setVisible(false);
			frame.setMinimumSize(new Dimension(1400, 1000));
			frame.setTitle("[" + player.getPlayerName() + "] " + "Battleships - Playing on: " + host);
			frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			frame.add(gamePanel, BorderLayout.CENTER);
			frame.add(messagePanel, BorderLayout.SOUTH);
		});
        
        gbc2.insets = new Insets(5,50,5,50);
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        shipSelectPanel.add(titleLabel1, gbc2);
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        shipSelectPanel.add(descriptionLabel, gbc2);
        gbc2.gridx = 0;
        gbc2.gridy = 2;
	    shipSelectPanel.add(setupGrid, gbc2);
        gbc2.gridx = 0;
        gbc2.gridy = 3;
        shipSelectPanel.add(continueButton, gbc2);
        
        
        // Setup Game panel
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridBagLayout());
        
        frame = new JFrame("Game Room");
		frame.setTitle("Game Room");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(158, 216, 240));
		frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(400, 400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		// Connection to server
		try
		{
			socket = new Socket(host, Integer.parseInt(port));
			serverCon = new ServerConnection(socket, player, this);
			serverCon.start();

			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					// Interrupt the ServerConnection thread
					serverCon.quit();
					// Close the frame and exit the application
					frame.dispose();
					System.exit(0);
				}
			});

		}
		catch(IOException ex)
		{
			System.out.println("Error connection to server " + ex.getMessage());
			JOptionPane.showMessageDialog(frame, "Error connecting to server");
			frame.dispose();
			System.exit(1);

		}
		finally
		{
			frame.setMinimumSize(new Dimension(1400, 1000));
			frame.setBackground(new Color(158, 216, 240));
			frame.setTitle("[" + player.getPlayerName() + "] Ship setup");
			frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			frame.add(shipSelectPanel, BorderLayout.CENTER);
		}

	}
	
	public void editDescription(String newLabel)
	{
		descriptionLabel.setText(newLabel);
	}
	
	public void enableContinueButton()
	{
		continueButton.setVisible(true);
	}


	
	public void processMessage(Message message) {
		
		Shot shot = message.getShot();
		currentTurnLabel.setText("Current turn: " + message.getNextTurn());
		
		System.out.println("----Message----");
		System.out.println("Hit: " + message.isHit());
		System.out.println("Sink: " + message.isSink());
		System.out.println("Victory: " + message.isVictory());
		System.out.println("Next turn: " + message.getNextTurn());
		System.out.println("Shot: " + message.getShot());
		System.out.println("Ship: " + message.getShip());
		
		

		if(message.getShot() == null)
		{
			if(player.getPlayerName().equals(message.getNextTurn()))
				enemyGrid.setShooting(true);
			
			lastTurn = message.getNextTurn();
			currentTurnLabel.setText("Current turn: " + message.getNextTurn());
			return;
		}

		// Process result of the previous step
		if(lastTurn.equals(player.getPlayerName()))
		{
			// 1.If the previous one was a player game - change to the opponent's game
			
			if(message.isHit())
			{
				hits++;
				
				enemyGrid.setColor(shot.getY(), shot.getX(), Color.red, true);
				if(message.isSink())
				{
					enemyGrid.drawBoundingBox(message.getShip());
					shipsLeft--;
				}
			}
			else
			{
				misses++;
				
				enemyGrid.setColor(shot.getY(), shot.getX(), new Color(158, 216, 240), true);
				enemyGrid.setShooting(false);
			}
			
			float acc = (float) hits / (hits + misses) * 100;
			infoBox.setText("Hits: " + hits + " misses: " + misses + " accuracy: " + Math.round(acc) + "% Enemy ships left: " + shipsLeft);
		}
		else
		{
			// 2. If the previous one was an enemy game - change to the player game
			
			if(message.isHit())
			{
				playerGrid.setColor(shot.getY(), shot.getX(), Color.red);
			}				
			else
			{
				playerGrid.setColor(shot.getY(), shot.getX(), Color.blue);
				enemyGrid.setShooting(true);
			}				
		}
		
		
		
		if(message.isVictory())
			declareWinner(lastTurn, hits, misses, shipsLeft);
			
		
		lastTurn = message.getNextTurn();
		currentTurnLabel.setText("Current turn: " + message.getNextTurn());
	}
	
	
	public void openWaitingScreen()
	{
		waitFrame.setVisible(true);
		frame.setVisible(false);
		
	}
	
	public void closeWaitingScreen()
	{
		frame.setVisible(true);
		if(waitFrame.equals(null))
			return;
		waitFrame.setVisible(false);
	}
	
	
	public void declareWinner(String winner, int hits, int misses, int shipsLeft)
	{
		int score = ((hits*10 - misses*2) * (4 - shipsLeft))*100;
		try {
			Cnx connectionClass = new Cnx(); //create connection
			Connection connection = connectionClass.getConnection(); //create connection
			Statement st = connection.createStatement();

			String query1 = "INSERT INTO user_tbl (username,gameRoom,score) VALUES ('"+name+"',"+roomNumber+","+score+")";
			st.executeUpdate(query1);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


		//Close the connection and open the message box
		JOptionPane.showMessageDialog(frame,"Player: " + winner + " has won.");
		frame.dispose();
		serverCon.closeConnection();
		//if the server is to be reset we need to something here as well
		System.exit(0);
	}

	public void exit()
	{
//		frame.dispose();
//		serverCon.interrupt();
//		serverCon.closeConnection();
		System.exit(0);
	}

	//this function is used to send the message retrieve after send button is clicked
	public void sendMessage(String msg) {
		String name = player.getPlayerName();
		msg = name + ": " + msg;
		serverCon.sendChatMsg(msg);
	}

	public void displayMessage(String msg) {
		System.out.println("Incoming Message: " +msg);
		// swing code to display in the Jframe
		listModel.addElement(msg);
	}

	public static void main(String[] args) {
//		new Client();
	}
}
