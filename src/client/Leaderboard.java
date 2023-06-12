package client;

import model.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.*;
import ConnectionToDatabase.Cnx;

public class Leaderboard{
    private ArrayList<Score> leaderboardData; // Replace this with your actual data source
    private JFrame frame;
    private JLabel titleLabel;
    public Leaderboard() {
        // Set up the frame
        titleLabel = new JLabel("Leaderboard Ranking");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));


        // Create the scrollable pane
        JScrollPane scrollPane = new JScrollPane();

        // Create the table to display the leaderboard data
        JTable leaderboardTable = new JTable();
        scrollPane.setViewportView(leaderboardTable);
        scrollPane.setBackground(new Color(158, 216, 240));

        // Add data to the leaderboard table (replace with your actual data source)
        try {
            Cnx connectionClass = new Cnx(); //create connection
            Connection connection = connectionClass.getConnection(); //create connection

            Statement st = connection.createStatement();

            String query1 = "SELECT COUNT(*) FROM user_tbl";
            String query2 = "SELECT * FROM user_tbl ORDER BY score DESC";

            ResultSet result1 = st.executeQuery(query1); //execute query
            result1.next();
            int rows = result1.getInt("");

            leaderboardData = new ArrayList<>();

            ResultSet result2 = st.executeQuery(query2);
            for (int i = 0; i < rows; i++) {
                result2.next();
                String username = result2.getString("username");
                int gameRoom = result2.getInt("gameRoom");
                int score = result2.getInt("score");

                leaderboardData.add(new Score(username, gameRoom, score));
                //list.add(new Appointment(appointmentNo,date,time)); //add appointment into arraylist
            }

            String[] columnNames = {"Player", "Score"};
            String[][] rowData = new String[leaderboardData.size()][2];
            int i = 0;
            for(Score score: leaderboardData){
                rowData[i][0] = score.getUsername().trim();
                rowData[i][1] = Integer.toString(score.getScore()).trim();
                i++;
            }
            leaderboardTable.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Set up the buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(158, 216, 240));
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create the back button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the back button action here
                System.out.println("Back");
                StartMenu startMenu = new StartMenu();
                startMenu.setVisible(true);
                frame.dispose();
            }
        });
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.LIGHT_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.WHITE);
            }
        });
        buttonsPanel.add(backButton);

        // Create the refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(Color.WHITE);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the refresh button action here
                // Add data to the leaderboard table (replace with your actual data source)
                try {
                    Cnx connectionClass = new Cnx(); //create connection
                    Connection connection = connectionClass.getConnection(); //create connection

                    Statement st = connection.createStatement();

                    String query1 = "SELECT COUNT(*) FROM user_tbl";
                    String query2 = "SELECT * FROM user_tbl";

                    ResultSet result1 = st.executeQuery(query1); //execute query
                    result1.next();
                    int rows = result1.getInt("");

                    leaderboardData = new ArrayList<>();

                    ResultSet result2 = st.executeQuery(query2);
                    for (int i = 0; i < rows; i++) {
                        result2.next();
                        String username = result2.getString("username");
                        int gameRoom = result2.getInt("gameRoom");
                        int score = result2.getInt("score");

                        leaderboardData.add(new Score(username, gameRoom, score));
                        //list.add(new Appointment(appointmentNo,date,time)); //add appointment into arraylist
                    }

                    String[] columnNames = {"Player", "Score"};
                    String[][] rowData = new String[leaderboardData.size()][2];
                    int i = 0;
                    for(Score score: leaderboardData){
                        rowData[i][0] = score.getUsername().trim();
                        rowData[i][1] = Integer.toString(score.getScore()).trim();
                        i++;
                    }
                    leaderboardTable.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames));
                } catch (SQLException f) {
                    throw new RuntimeException(f);
                }
            }
        });
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(Color.LIGHT_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(Color.WHITE);
            }
        });
        buttonsPanel.add(refreshButton);


        // Show the frame
        frame = new JFrame("Leaderboard");
        frame.setTitle("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(158, 216, 240));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(400, 400));
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonsPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Leaderboard();
            }
        });
    }
}

