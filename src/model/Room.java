package model;

public class Room {
    private int roomNumber;
    private int portNumber;
    private String roomState;
    private String roomPlayers;
    private int roomPlayerCount;

    public Room(int roomNumber, int portNumber, String roomState, String roomPlayers, int roomPlayerCount){
        this.roomNumber = roomNumber;
        this.portNumber = portNumber;
        this.roomState = roomState;
        this.roomPlayers = roomPlayers;
        this.roomPlayerCount = roomPlayerCount;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getRoomState() {
        return roomState;
    }

    public String getRoomPlayers() {
        return roomPlayers;
    }

    public int getRoomPlayerCount() {
        return roomPlayerCount;
    }

    public String[] getAll(){
        String[] all_info= {Integer.toString(roomNumber), Integer.toString(portNumber), roomState, roomPlayers, Integer.toString(roomPlayerCount)};
        return all_info;
    }

    public String printAll(){
        String all_info = "roomNumber: " + Integer.toString(roomNumber) + " portNumber: " + Integer.toString(portNumber) + " roomState: " + roomState + " roomPlayers: " + roomPlayers + " roomPlayerCount: " + Integer.toString(roomPlayerCount) + " ";
        return all_info;
    }

}
