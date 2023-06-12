package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import ConnectionToDatabase.Cnx;

public class CreateRoom{
    private JFrame frame;
    private JTextField nameTextField;
    private JLabel titleLabel;
    private JPanel centerPanel;
    private JLabel nameLabel;
    private JPanel bottomPanel;
    private JButton createButton;
    private JButton backButton;
    private String host;
    private String port;
    private String name;

    public CreateRoom() {
        titleLabel = new JLabel("Create a new room.");
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

        centerPanel.add(nameLabel);
        centerPanel.add(nameTextField);

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(158, 216, 240));


        createButton = createButton("Create");
        backButton = new JButton("Back");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create");
                String hostInput = "localhost";
                String nameInput = nameTextField.getText();
                if(nameInput != null && !nameInput.isBlank())
                {

//                    titleLabel.setVisible(false);
//                    centerPanel.setVisible(false);
//                    bottomPanel.setVisible(false);
//                    frame.dispose();
//
//                    Client client = new Client(hostInput, Integer.toString(portInput), nameInput);

                    //Create a game room and return
                    System.out.println("Connecting to Server...");
                    try {
                        Cnx connectionClass = new Cnx(); //create connection
                        Connection connection = connectionClass.getConnection(); //create connection
                        Statement st = connection.createStatement();

                        String query1 = "SELECT (roomNumber),(portNumber) FROM room_tbl WHERE roomState='idle'";
                        ResultSet result1 = st.executeQuery(query1); //execute query
                        result1.next();
                        int roomNumber = result1.getInt("roomNumber");
                        int portNumber = result1.getInt("portNumber");

                        String query2 = "UPDATE room_tbl SET roomState='create' WHERE portNumber="+portNumber;
                        st.executeUpdate(query2);

                        boolean created = false;
                        while(!created) {
                            String query3 = "SELECT roomState FROM room_tbl WHERE portNumber="+portNumber;
                            ResultSet result2 = st.executeQuery(query3); //execute query
                            result2.next();
                            String roomState = result2.getString("roomState");
                            if (roomState.equals("running")) {
                                JOptionPane.showMessageDialog(frame, "Game room has been created!!! Your room number is: "+ roomNumber);
                                created = true;
                                System.out.println("Room is created");
                                System.out.println("Connected to Game Room " + roomNumber);

                                //Update Player Number
                                int roomPlayerCount = 1;
                                String query4 = "UPDATE room_tbl SET roomPlayerCount='" + roomPlayerCount + "' WHERE portNumber=" + portNumber;
                                st.executeUpdate(query4);

                                titleLabel.setVisible(false);
                                centerPanel.setVisible(false);
                                bottomPanel.setVisible(false);
                                frame.dispose();

                                Client client = new Client(hostInput, Integer.toString(portNumber), nameInput, roomNumber);


                            } else {
                                System.out.println("Waiting for room creation...");
                            }
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                else
                {
                    JOptionPane.showMessageDialog(frame, "Name cannot be left blank.");
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
        g.gridx = 2;
        g.gridy = 4;
        centerPanel.add(createButton, g);

        bottomPanel.add(backButton);

        frame = new JFrame("Create server");
        frame.setTitle("Create Room");
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

    private JButton createButton(String text) {
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

    private JButton createButton(String text, Icon icon) {
        JButton button = createButton(text);
        button.setIcon(icon);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CreateRoom();
            }
        });
    }
}
