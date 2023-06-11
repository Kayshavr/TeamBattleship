package client;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

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
	private JPanel shipSelectPanel;
	private JPanel gamePanel;

	private JButton continueButton;
	private JLabel descriptionLabel;
	private JLabel infoBox;
	private JLabel currentTurnLabel;
	private JTextField nameTextField;
	private JTextField roomNumberField;
	private JLabel titleLabel;
	private JPanel centerPanel;
	private JLabel nameLabel;
	private JLabel roomNumberLabel;
	private JPanel bottomPanel;
	private JButton joinButton;
	private JButton backButton;

	private int[][] playerShips;
	private Player player;
	private String lastTurn;
	
	private PlayerGrid playerGrid;
	private EnemyGrid enemyGrid;
	
	private String host;
	private String port;
	private String name;
	private Socket socket;
	private ServerConnection serverCon;
	
	// Stats
	private int hits = 0;
	private int misses = 0;
	private int shipsLeft = 10;
	
	public Client() {

		titleLabel = new JLabel("Join a new room.");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

		centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setBackground(new Color(158, 216, 240));
		GridBagConstraints g = new GridBagConstraints();

		nameLabel = new JLabel("Name:");
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		nameTextField = new JTextField(15);
		nameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		roomNumberLabel = new JLabel("Room Number:");
		roomNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		roomNumberField = new JTextField(15);
		roomNumberField.setFont(new Font("Arial", Font.PLAIN, 16));

		centerPanel.add(nameLabel);
		centerPanel.add(nameTextField);

		bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setBackground(new Color(158, 216, 240));

		backButton = new JButton("Back");

		// Connection panel

		joinButton = new JButton("Connect!");
		joinButton.addActionListener(e ->
		{
			String hostInput = "localhost";
			String roomNumber = roomNumberField.getText();
			int portInput = Integer.parseInt(roomNumber) + 9000;
			String nameInput = nameTextField.getText();
			if(hostInput != null && !hostInput.isBlank() && roomNumber != null && !roomNumber.isBlank() && nameInput != null && !nameInput.isBlank())
			{
				host = hostInput;
				port = Integer.toString(portInput);
				name = nameInput;
				System.out.println("Host: " + host);
				System.out.println("Port: " + port);
				System.out.println("Name: " + name);

				titleLabel.setVisible(false);
				centerPanel.setVisible(false);
				bottomPanel.setVisible(false);

				player = new Player(nameInput);
				
				// Sak savienojumu ar serveri
				try
				{
					socket = new Socket(host, Integer.parseInt(port));
					serverCon = new ServerConnection(socket, player, this);
					serverCon.start();
					
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
					frame.setTitle("[" + player.getPlayerName() + "] Ship setup");
					frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
					frame.add(shipSelectPanel, BorderLayout.CENTER);
				}				
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Host/port/name cannot be left blank.");
			}
		});

		backButton.addActionListener(e ->
		{
			System.out.println("Back");
			StartMenu startMenu = new StartMenu();
			startMenu.setVisible(true);
			frame.dispose();
		});
        
		// Izkartojuma definicijas
		g.insets = new Insets(5,5,5,5);
		g.fill = GridBagConstraints.VERTICAL;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 1;
		centerPanel.add(nameLabel, g);
		g.gridx = 2;
		g.gridy = 1;
		centerPanel.add(nameTextField, g);
		g.gridx = 0;
		g.gridy = 2;
		centerPanel.add(roomNumberLabel, g);
		g.gridx = 2;
		g.gridy = 2;
		centerPanel.add(roomNumberField, g);
		g.gridx = 2;
		g.gridy = 4;
		centerPanel.add(joinButton, g);
		bottomPanel.add(backButton);
      
        // Gaidisanas panelis
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
		
		
		// Izveido tuksu lietotaja laukumu
        playerShips = new int[10][10];
        for (int i = 0; i < playerShips.length; i++) {
			for (int j = 0; j < playerShips[i].length; j++) {
				playerShips[i][j] = 0;
			}
		}

        // Laukuma uzstadisana
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
			// Continue buttton pressed
			
			player.setPlayerField(setupGrid.returnField());
			player.setPlayerShips(setupGrid.getShips());
			
			playerGrid = new PlayerGrid(player.getPlayerField());
			enemyGrid = new EnemyGrid(serverCon);
			
			// Nosutam serverim savus speles datus
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

 
        
        frame = new JFrame("Connect to server");
		frame.setTitle("Join Room");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(158, 216, 240));
		frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(400, 400));
		frame.add(titleLabel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
			declareWinner(lastTurn);
			
		
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
	
	
	public void declareWinner(String winner)
	{
		//Close the connection and open the message box
		JOptionPane.showMessageDialog(frame,"Player: " + winner + " has won.");
		frame.dispose();
		serverCon.closeConnection();
		System.exit(0);		
	}

	public static void main(String[] args) {
		new Client();
	}
}
