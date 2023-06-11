package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinRoom extends JFrame {
    private JTextField nameTextField;
    private JTextField roomNumberField;
    private JLabel titleLabel;
    private JPanel centerPanel;
    private JLabel nameLabel;
    private JLabel roomNumberLabel;
    private JPanel bottomPanel;
    private JButton joinButton;
    private JButton backButton;

    public JoinRoom() {
        setTitle("Join Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(158, 216, 240));
        setLayout(new BorderLayout());

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
        backButton = joinButton("Back", new ImageIcon("left_arrow_icon.png"));

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Join");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back");
                StartMenu startMenu = new StartMenu();
                startMenu.setVisible(true);
                dispose();
            }
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

        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(400, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
