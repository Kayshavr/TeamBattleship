package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame {
    private JButton joinRoomButton;
    private JButton createRoomButton;
    private JButton quitButton;
    private JLabel battleshipTitle;
    private JPanel startMenuPanel;

    public StartMenu() {

        //create Start Menu Panel
        startMenuPanel = new JPanel();
        startMenuPanel.setLayout(new GridBagLayout());
        startMenuPanel.setBackground(new Color(158, 216, 240));
        GridBagConstraints g = new GridBagConstraints();
        setTitle("Battleship Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(158, 216, 240));
        setLayout(new FlowLayout());

        battleshipTitle = new JLabel("Welcome to Battleship!");
        battleshipTitle.setFont(new Font("Verdana", Font.PLAIN, 24));
        battleshipTitle.setHorizontalAlignment(SwingConstants.CENTER);
        joinRoomButton = createButton("Join Room");
        createRoomButton = createButton("Create Room");
        quitButton = createButton("Quit");

        joinRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Join room");
                JoinRoom joinRoom = new JoinRoom();
                joinRoom.setVisible(true);
                dispose();
            }
        });

        createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create room");
                CreateRoom createRoom = new CreateRoom();
                createRoom.setVisible(true);
                dispose();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        g.insets = new Insets(5,5,5,5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridwidth = 2;
        g.gridx = 0;
        g.gridy = 0;
        startMenuPanel.add(battleshipTitle, g);
        g.gridx = 1;
        g.gridy = 1;
        startMenuPanel.add(joinRoomButton, g);
        g.gridx = 1;
        g.gridy = 2;
        startMenuPanel.add(createRoomButton, g);
        g.gridx = 1;
        g.gridy = 4;
        startMenuPanel.add(quitButton, g);

        add(startMenuPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(400, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StartMenu();
            }
        });
    }
}

