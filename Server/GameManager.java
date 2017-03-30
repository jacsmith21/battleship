import java.util.ArrayList;

/**
 * Created by Tristen Tulkens and Jacob Smith on 2017-02-10.
 */
public class GameManager {

    DBManager db;
    private Board player1Board;
    private Board player2Board;
    private ServerConnection player1;
    private ServerConnection player2;
    private String player1Message;
    private String player2Message;
    
    final boolean DEBUG = true;

    public GameManager(){
        db = new DBManager();
        player1 = new ServerConnection();
        player2 = new ServerConnection();
        player1.createServer(2043);
        player2.createServer(2044);
        player1.startConnection();
        player2.startConnection();
        if(DEBUG) System.out.println("All clients connected");
        setupUsers();
        initializeBoards();
        loop();
    }

    public void setupUsers(){
        player1Message = player1.recieve();
        System.out.println("Received " + player1Message);
        String[] info = player1Message.split(",");
        if(info[0].equals("N")){
        	if(DEBUG) System.out.println("Signing up player one!");
            signup(info[1], player1);
            
        } else if(info[0].equals("R")){
        	if(DEBUG) System.out.println("Logging in player one!");
            login(info[1], player1);
        }

        player2Message = player2.recieve();
        String[] info2 = player2Message.split(",");
        if(info[0].equals("N")){
        	if(DEBUG) System.out.println("Signing up player two!");
            signup(info[1], player2);
        } else if(info[0].equals("R")){
        	if(DEBUG) System.out.println("Logging in player two!");
            login(info[1], player2);
        }
    }

    public void signup(String user, ServerConnection player){
    	if(DEBUG) System.out.println("Validating username!");
        String check = db.newUserNameCheck(user);
        player.send(check);
        while(!check.equals("ack")){
            user = player.recieve().split(",")[1];
            check = db.newUserNameCheck(user);
            player.send(check);
        }
        if(DEBUG) System.out.println("Validated! Finishing sign up procedure!");
        String password = player.recieve();
        db.createNewUser(user, password);
        player.send("ack");

    }

    public void login(String user, ServerConnection player){
        int fails = 0;
        if(DEBUG) System.out.println("Checking login username!");
        String check = db.returningUserNameCheck(user);
        player.send(check);
        while(!check.equals("ack")){
            user = player.recieve().split(",")[1];
            check = db.returningUserNameCheck(user);
            player.send(check);
        }
        if(DEBUG) System.out.println("Validated! Checking password!");
        String password = player.recieve();
        check = db.returningPasswordCheck(user, password);
        player.send(check);
        while(!check.equals("ack")){
            fails++;
            if(fails == 3){
                System.exit(0);
            }
            password = player.recieve();
            check = db.returningPasswordCheck(user, password);
            player.send(check);
        }
    }

    public void initializeBoards(){
        boolean isValid = false;
        while(!isValid) {
            player1Message = player1.recieve();
            ArrayList<String[]> player1Data = Data.splitData(player1Message);
            if (Data.validateData(player1Data)) {
                player1.send("ack, 1");
                player1Board = new Board(player1Data);
                isValid = true;
            } else {
                player1.send("err");
            }
        }
        isValid = false;
        while(!isValid) {
            player2Message = player2.recieve();
            ArrayList<String[]> player2Data = Data.splitData(player2Message);
            if (Data.validateData(player2Data)) {
                player2.send("ack, 2");
                player2Board = new Board(player2Data);
                isValid = true;
            } else {
                player2.send("err");
            }
        }
        player1.send("ok");
    }

    public void loop(){
        boolean gameOver = false;
        boolean p1Turn = true;
        boolean p2Turn = false;
        while(!gameOver){
            while(p1Turn) {
                player1Message = player1.recieve();
                if (!Data.isValidCoordinate(player1Message)) {
                    player1.send("err");
                } else{
                    String result = player2Board.checkIfHit(player1Message);
                    player1.send(result);
                    if(result.contains("win")){
                        player2.send(player1Message+","+result.replace("win", "loss"));
                        gameOver = true;
                        break;
                    } else {
                        player2.send(player1Message + "," + result);
                    }
                    p1Turn = false;
                    p2Turn = true;
                }
            }
            while(p2Turn) {
                player2Message = player2.recieve();
                if (!Data.isValidCoordinate(player2Message)) {
                    player2.send("err");
                } else{
                    String result = player1Board.checkIfHit(player2Message);
                    player2.send(result);
                    if(result.contains("win")){
                        player1.send(player2Message+","+result.replace("win", "loss"));
                        gameOver = true;
                        int gag = 4;
                        break;
                    } else {
                        player1.send(player2Message + "," + result);
                    }
                    p2Turn = false;
                    p1Turn = true;
                }
            }
        }
    }
}
