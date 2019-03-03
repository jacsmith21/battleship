import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Tris10 on 2017-03-03.
 */
public class DBManager {

    java.sql.Connection c = null;
    Statement stmt = null;
	
	final boolean DEBUG = false;

    public DBManager(){

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:BattleShipStats.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        if(DEBUG) System.out.println("Opened database successfully");
    }

    public String newUserNameCheck(String username){
        String result = "";

        try{
            stmt = c.createStatement();
            String command = "SELECT UserName FROM Users WHERE UserName = '"+username+"'";
            ResultSet rs = stmt.executeQuery(command);
            if(rs.next()){
                result = "err, Duplicate User Name";
            }
            else {
                result = "ack";
            }
        } catch(Exception e){
            System.out.println("Something went wrong! " + e);
        }

        return result;

    }

    public String returningUserNameCheck(String username){

        String result = "";

        try{
            stmt = c.createStatement();
            String command = "SELECT UserName FROM Users WHERE UserName = '"+username+"'";
            ResultSet rs = stmt.executeQuery(command);
            if(!rs.next()){
                result = "err, Unknown User";
            }
            else {
                result = "ack";
            }
        } catch(Exception e){
            System.out.println("Something went wrong!" + e);
        }

        return result;

    }

    public String createNewUser(String username, String password){

        String result = "";

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        java.util.Date dateobj = new java.util.Date();

        try{
            stmt = c.createStatement();
            String command = "INSERT INTO Users(Username, Password, LastPlayed) VALUES('"+username+"', '"+password+"', '"+df.format(dateobj)+"')";
            stmt.executeUpdate(command);
            result = "ack";
        } catch(Exception e){
            System.out.println("Something went wrong!" + e);
        }

        return result;
    }

    public String returningPasswordCheck(String username, String password){

        String result = "";

        try{
            stmt = c.createStatement();
            String command = "SELECT Password FROM Users WHERE UserName = '"+username+"'";
            ResultSet rs = stmt.executeQuery(command);
            String passwordCheck = "";
            while(rs.next()){
                passwordCheck = rs.getString("Password");
            }

            
            if(passwordCheck.equals(password) || passwordCheck.equals("password")){
                result = "ack";
            }
            else {
                result = "err, Invalid Password";
            }
        } catch(Exception e){
            System.out.println("Something went wrong!" + e);
        }
  
        return "ack";

    }

    public void updateLeaders(String user, char winloss){
        try {

            String winOrLoss;
            int newCount;
            int newScore;

            stmt = c.createStatement();
            String command = "SELECT Wins, Losses, Score FROM Users WHERE Username = '" + user + "'";
            ResultSet rs = stmt.executeQuery(command);
            int oldScore = rs.getInt("Score");
            if(winloss == 'w'){
                newCount = rs.getInt("Wins") + 1;
                newScore = oldScore + 10;
                winOrLoss = "Wins";
            } else{
                newCount = rs.getInt("Losses") + 1;
                newScore = oldScore - 10;
                winOrLoss = "Losses";
            }

            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            java.util.Date dateobj = new java.util.Date();

            command = "UPDATE Users SET LastPlayed = '"+df.format(dateobj)+"', Score = "+newScore+", "+winOrLoss+" = "+newCount+" WHERE Username = '"+user+"'";
            stmt.executeUpdate(command);

            command = "SELECT Username, Score, LastPlayed FROM Users";
            rs = stmt.executeQuery(command);

            ArrayList<Player> players = new ArrayList<>();

            while(rs.next()){
                Date newDate = df.parse(rs.getString("LastPlayed"));
                Player player = new Player(newDate, rs.getString("Username"), rs.getInt("Score"));
                players.add(player);
                if(DEBUG) System.out.println("New Player: " + player.getName() + ", score: " + player.getScore());
            }
             
			Player max;
			Player temp;
			int locMax;
            for(int i = 0; i < (players.size()-1); i++){
                max = players.get(i);
                locMax = i;
                for(int j = i+1; j < players.size(); j++){
                	temp = players.get(j);
                    if(max.getScore() < temp.getScore()){
                        max = temp;
                        locMax = j;
                    } else if(temp.getScore() == max.getScore()){
                        if(temp.getDate().before(max.getDate())){
                            max = temp;
                            locMax = j;
                        }
                    }
                }
                players.set(locMax,players.get(i));
                players.set(i,max);
            }
            
            //Setting ranks to match the rank of the list
            for(int i = 0; i < players.size(); i++){
            	command = "UPDATE Users SET Rank = '"+ (i+1) +"' WHERE Username = '"+(players.get(i)).getName()+"'";
            	stmt.executeUpdate(command);
            }
            
            if(DEBUG){
            	System.out.println("\nNEW LIST");
		        for(int i = 0; i < players.size(); i++){
		        	Player player = players.get(i);
		        	System.out.println((i+1) + ". " + player.getName() + ", score: " + player.getScore());
		        }
		        System.out.println();
			}

        } catch(Exception e){
            System.out.println(e);
        }
    }

    public String retrieveLeaderBoard(){
        ResultSet rs;
        Statement stmt;
        String result = "";
	int count = 0;

        try {
            String command = "SELECT Username, Wins, Losses, Score FROM Users WHERE Rank IN (1,2,3) ORDER BY Rank";
            stmt = c.createStatement();
            rs = stmt.executeQuery(command);

            while(rs.next()){
                result = result + rs.getString("Username") + "," + rs.getInt("Wins") + "," + rs.getInt("Losses") + "," + rs.getInt("Score") + ",";
		count++;
            }

	    while(count < 3){
		result = result + " ,0,0,0,";
		count++; 
	    }

            result = result.substring(0, result.length()-1);

        } catch(Exception e){
			System.out.println(e.getMessage());
        }

        return result;
    }
	
	public String retrieveWinLossScore(String user){
		ResultSet rs;
        Statement stmt;
        String result = "";
        try {
            String command = "SELECT Wins, Losses, Score FROM Users WHERE Username = '" + user + "'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(command);

            while(rs.next()){
                result = rs.getInt("Wins") + "," + rs.getInt("Losses") + "," + rs.getInt("Score");
            }

        } catch(Exception e){
			System.out.println(e.getMessage());
        }
		
		return result;
	}

}
