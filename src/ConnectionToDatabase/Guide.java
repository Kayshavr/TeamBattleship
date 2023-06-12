
package ConnectionToDatabase;

import model.Room;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


public class Guide {
    /*
    example on how to connect to database and how to query/insert to database, note that this is just an example, there's no appointment table inside our database 
    */
    public static void main(String[]args)throws SQLException{
        //ArrayList<Appointment> list = new ArrayList<>(); //arraylist to store appointments, commented because Appointment class have not been created


        /**get data from database**/
//....................................................................................................................................
        Cnx connectionClass = new Cnx(); //create connection
        Connection connection = connectionClass.getConnection(); //create connection

        Statement st = connection.createStatement();

        // Insert rooms
//        for (int i = 9000; i <= 9100; i++){
//            int roomNumber = i-9000;
//            int portNumber = i;
//            String roomState = "idle";
//            String roomPlayers = "";
//            int roomPlayerCount = 0;
//            String query3 = "INSERT INTO room_tbl (roomNumber, portNumber, roomState, roomPlayers, roomPlayerCount) VALUES ("+roomNumber+","+portNumber+",'"+roomState+"','"+roomPlayers+"',"+roomPlayerCount+")";
//            st.executeUpdate(query3);
//        }

        String query1 = "SELECT COUNT(*) FROM room_tbl"; // 'appointment' here is the table inside database
        String query2 = "SELECT* FROM room_tbl"; //example of query statement to get data from database

        ResultSet result1 = st.executeQuery(query1); //execute query 
        result1.next();
        int rows =  result1.getInt(""); //  to get how many rows(instances of data) that table 'appointment' have


        System.out.println("Rooms Available: " + Integer.toString(rows));
        System.out.println("Rooms: ");

        ArrayList<Room> roomList = new ArrayList<>();

        ResultSet result2 = st.executeQuery(query2);
        for (int i = 0; i < rows; i++) { //to get every rows inside 'appointment' table and add the data into arraylist
            result2.next();
            int roomNumber = result2.getInt("roomNumber"); // the name inside " " must be the same as the name of the columns(fields) inside table 'appointment'
            int portNumber = result2.getInt("portNumber");
            String roomState = result2.getString("roomState");
            String roomPlayers = result2.getString("roomPlayers");
            int roomPlayerCount = result2.getInt("roomPlayerCount");

            roomList.add(new Room(roomNumber, portNumber, roomState, roomPlayers, roomPlayerCount));
            //list.add(new Appointment(appointmentNo,date,time)); //add appointment into arraylist
        }

        for(Room room: roomList){
            System.out.println(room.printAll());
        }
//        connection.close(); //to close connection
//..........................................................................................................................




        /**insert data into database**/
//...........................................................................................................................

//        Cnx connectionClass = new Cnx(); //commented because already declared 
//        Connection connection = connectionClass.getConnection();  
//        Statement st = connection.createStatement();

//        String username1 = "Someone123";
//        int userID1 = 10001;
//        int gameRoom1 = 10;
//        int score1 = 100;
//        String query3 = "INSERT INTO user_tbl (username, userID, gameRoom, score) VALUES ('"+username1+"',"+userID1+","+gameRoom1+","+score1+")";
//        st.executeUpdate(query3);
        connection.close();
//...........................................................................................................................
    }
}
