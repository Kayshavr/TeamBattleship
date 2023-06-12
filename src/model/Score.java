package model;

public class Score {
    private int score;
    private int gameRoom;
    private String username;

    public Score(String username, int gameRoom, int score){
        this.username = username;
        this.gameRoom = gameRoom;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }
    public int getGameRoom() {
        return gameRoom;
    }

    public int getScore() {
        return score;
    }


    public String[] getAll(){
        String[] all_info_list= {username, Integer.toString(gameRoom), Integer.toString(score)};
        return all_info_list;
    }

    public String printAll(){
        String all_info = "username: " + username+ " gameRoom: " + Integer.toString(gameRoom) + " score: " + Integer.toString(score) + " ";
        return all_info;
    }

}
