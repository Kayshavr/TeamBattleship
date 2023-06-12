package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import ConnectionToDatabase.Cnx;

public class JoinRoom{
    private JFrame frame;
    private JTextField nameTextField;
    private JTextField roomNumberField;
    private JLabel titleLabel;
    private JPanel centerPanel;
    private JLabel nameLabel;
    private JLabel roomNumberLabel;
    private JPanel bottomPanel;
    private JButton joinButton;
    private JButton backButton;
    private String host;
    private String port;
    private String name;

    public JoinRoom() {
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


        joinButton = joinButton("Join");
        backButton = new JButton("Back");

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Join");
                String hostInput = "localhost";
                String roomNumber = roomNumberField.getText();
                int portInput = Integer.parseInt(roomNumber) + 9000;
                String nameInput = nameTextField.getText();
                if(roomNumber != null && !roomNumber.isBlank() && nameInput != null && !nameInput.isBlank())
                {
                    System.out.println("Connecting to Server...");
                    try {
                        Cnx connectionClass = new Cnx(); //create connection
                        Connection connection = connectionClass.getConnection(); //create connection
                        Statement st = connection.createStatement();

                        String query1 = "SELECT (roomState),(roomPlayerCount) FROM room_tbl WHERE portNumber='"+ Integer.toString(portInput) +"'";

                        ResultSet result1 = st.executeQuery(query1); //execute query
                        result1.next();
                        String roomState = result1.getString("roomState");
                        int roomPlayerCount = result1.getInt("roomPlayerCount");

                        if(roomState.equals("running")){
                            System.out.println("Room is available");
                            if(roomPlayerCount < 2){
                                System.out.println("Connected to Game Room " + roomNumber);

                                //Update Player Number
                                roomPlayerCount = roomPlayerCount +1;
                                String query3 = "UPDATE room_tbl SET roomPlayerCount='"+roomPlayerCount+"' WHERE portNumber="+portInput;
                                st.executeUpdate(query3);

                                titleLabel.setVisible(false);
                                centerPanel.setVisible(false);
                                bottomPanel.setVisible(false);
                                frame.dispose();

                                Client client = new Client(hostInput, Integer.toString(portInput), nameInput, Integer.parseInt(roomNumber));
                            }else{
                                JOptionPane.showMessageDialog(frame, "Room is full!!! Please join or create another room.");
                            }

                        }else{
                            JOptionPane.showMessageDialog(frame, "Room has not been created!");
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(frame, "Name/Room Number cannot be left blank.");
                }
            }
        });

        backButton.addActionListener(e ->
        {
            System.out.println("Back");
            StartMenu startMenu = new StartMenu();
            startMenu.setVisible(true);
            frame.dispose();
        });

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

    private JButton joinButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 40));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.LIGHT_GRAY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JButton joinButton(String text, Icon icon) {
        JButton button = joinButton(text);
        button.setIcon(icon);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JoinRoom();
            }
        });
    }
}
