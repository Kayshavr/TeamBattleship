
package ConnectionToDatabase;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Guide {
    /*
    example on how to connect to database and how to query/insert to database, note that this is just an example, there's no appointment table inside our database 
    */
    public static void main(String[]args)throws SQLException{
        String username = null;
        int userID = 0;
        int gameRoom = 0;
        int score = 0;
        //ArrayList<Appointment> list = new ArrayList<>(); //arraylist to store appointments, commented because Appointment class have not been created


        /**get data from database**/
//....................................................................................................................................
        Cnx connectionClass = new Cnx(); //create connection
        Connection connection = connectionClass.getConnection(); //create connection

        Statement st = connection.createStatement();
        String query1 = "SELECT COUNT(*) FROM user_tbl"; // 'appointment' here is the table inside database
        String query2 = "SELECT* FROM user_tbl"; //example of query statement to get data from database

        ResultSet result1 = st.executeQuery(query1); //execute query 
        result1.next();
        int rows =  result1.getInt(""); //  to get how many rows(instances of data) that table 'appointment' have

        ResultSet result2 = st.executeQuery(query2);
        for (int i = 0; i < rows; i++) { //to get every rows inside 'appointment' table and add the data into arraylist
            result2.next();
            username = result2.getString("username"); // the name inside " " must be the same as the name of the columns(fields) inside table 'appointment'
            userID = result2.getInt("userID");
            gameRoom = result2.getInt("gameRoom");
            score = result2.getInt("score");
            //list.add(new Appointment(appointmentNo,date,time)); //add appointment into arraylist
            System.out.println("Username: " + username);
        }

//        connection.close(); //to close connection
//..........................................................................................................................




        /**insert data into database**/
//...........................................................................................................................

//        Cnx connectionClass = new Cnx(); //commented because already declared 
//        Connection connection = connectionClass.getConnection();  
//        Statement st = connection.createStatement(); 
        String username1 = "Someone123";
        int userID1 = 10001;
        int gameRoom1 = 10;
        int score1 = 100;
        String query3 = "INSERT INTO user_tbl (username, userID, gameRoom, score) VALUES ('"+username1+"',"+userID1+","+gameRoom1+","+score1+")";
        st.executeUpdate(query3);
        connection.close();
//...........................................................................................................................
    }
}
