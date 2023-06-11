package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRoom extends JFrame {
    private JTextField nameTextField;
    private JLabel titleLabel;
    private JPanel centerPanel;
    private JLabel nameLabel;
    private JPanel bottomPanel;
    private JButton createButton;
    private JButton backButton;

    public CreateRoom() {
        setTitle("Create Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(158, 216, 240));
        setLayout(new BorderLayout());

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
        backButton = createButton("Back", new ImageIcon("left_arrow_icon.png"));

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create");
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
        g.gridx = 2;
        g.gridy = 4;
        centerPanel.add(createButton, g);

        bottomPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(400, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
