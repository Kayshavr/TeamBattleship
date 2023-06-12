package server;

import java.io.IOException;
import model.Room;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ConnectionToDatabase.Cnx;

import static java.lang.Thread.sleep;

public class RunServers {
    private static Server[] servers = new Server[101];
    public static void serverCreator(Room room, Statement st) throws IOException, SQLException {
        int portNumber = room.getPortNumber();
        int roomNumber = room.getRoomNumber();
        servers[roomNumber] = new Server(portNumber);
        servers[roomNumber].start();
        System.out.println("Room " + roomNumber + " has started!");

        //Update Room state
        String newRoomState = "running";
        String query3 = "UPDATE room_tbl SET roomState='"+newRoomState+"' WHERE portNumber="+portNumber;
        st.executeUpdate(query3);
    }

    public static void serverStop(Room room, Statement st) throws IOException, SQLException {
        int roomNumber = room.getRoomNumber();
        if(servers[roomNumber].isAlive()){
            System.out.println("Server is Alive!!");
        }
        else{
            System.out.println("Server is stopped");
            //Update Room state
            String newRoomState = "idle";
            int portNumber = room.getPortNumber();
            String query3 = "UPDATE room_tbl SET roomState='"+newRoomState+"' WHERE portNumber="+portNumber;
            st.executeUpdate(query3);
        }
    }
    public static void main(String[] args){
        //Reset all values
        try{
            Cnx connectionClass = new Cnx(); //create connection
            Connection connection = connectionClass.getConnection(); //create connection
            Statement st = connection.createStatement();

            String query1 = "UPDATE room_tbl SET roomState='idle'";
            st.executeUpdate(query1);
            String query2 = "UPDATE room_tbl SET roomPlayerCount=0";
            st.executeUpdate(query2);
            String query3 = "UPDATE room_tbl SET roomPlayers=''";
            st.executeUpdate(query3);

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        for (int i = 9000; i <= 9100; i++){
//            servers[i-9000] = new Server(i);
//        }
//
//        for (int i = 0; i <= 100; i++){
//            servers[i].start();
//        }
        while(true) {
            try {

                Cnx connectionClass = new Cnx(); //create connection
                Connection connection = connectionClass.getConnection(); //create connection

                Statement st = connection.createStatement();

                String query1 = "SELECT COUNT(*) FROM room_tbl";
                String query2 = "SELECT * FROM room_tbl";

                ResultSet result1 = st.executeQuery(query1); //execute query
                result1.next();
                int rows = result1.getInt(""); //  to get how many active game rooms are there


                System.out.println("Rooms managed: " + Integer.toString(rows));

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

                //Check which rooms are active
                for (Room room : roomList) {
                    String roomState = room.getRoomState();
//                    System.out.println("Roomstate: "+ roomState);
                    if (roomState.equals("idle")) {
                        //Do nothing

                    } else if (roomState.equals("create")) {
                        //Create room
                        System.out.println("Create Room");
                        serverCreator(room, st);
                    } else if (roomState.equals("running")) {
                        //Do nothing
                        int roomNumber = room.getRoomNumber();
                        System.out.println("Room Number " + Integer.toString(roomNumber) + " is running!");
                    } else if (roomState.equals("finished")) {
                        //Close Room
                        System.out.println("Close Room");
                        serverStop(room, st);
                    } else if (roomState.equals("exited")) {
                        //Close Room
                        System.out.println("Close Room");
                        serverStop(room, st);
                    }
                }
                connection.close();
                sleep(2000);

            } catch (SQLException e) {
                System.out.println("SQL error!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

