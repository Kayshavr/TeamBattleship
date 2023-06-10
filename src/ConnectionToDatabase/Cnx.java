
package ConnectionToDatabase;

import java.sql.DriverManager;

public class Cnx {

    public java.sql.Connection connection;

    public java.sql.Connection getConnection()
    {


        String userName = "battleshipteam";
        String password = "shipdestroyer1$";

        try{
            connection = DriverManager.getConnection("jdbc:sqlserver://battleshipdb.database.windows.net:1433;database=battleshipDB",userName,password);
        }
        catch(Exception e){
            e.printStackTrace();
        }


        return connection;
    }
}
