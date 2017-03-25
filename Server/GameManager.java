import java.util.ArrayList;

/**
 * Created by Tristen Tulkens and Jacob Smith on 2017-02-10.
 */
public class GameManager {
	
	private final DEBUG = true;

    private Board player1Board;
    private Board player2Board;
    private ServerConnection player1;
    private ServerConnection player2;
    private String player1Message;
    private String player2Message;

    public GameManager(){
        player1 = new ServerConnection();
        player2 = new ServerConnection();
        player1.createServer(2043);
        player2.createServer(2044);
        player1.startConnection();
        player2.startConnection();
        System.out.println("All clients connected");
        initializeBoards();
        loop();
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
            while(p1Turn && !gameOver) {
                player1Message = player1.recieve();
                if (!Data.isValidCoordinate(player1Message)) {
                    player1.send("err");
                } else{
                    String result = player2Board.checkIfHit(player1Message);
                    player1.send(result);
                    if(result.contains("win")){
                        player2.send(player1Message+","+result.replace("win", "loss"));
						gameOver = true;
                    } else {
                        player2.send(player1Message + "," + result);
                    }
                    p1Turn = false;
                    p2Turn = true;
                }
            }
            while(p2Turn && !gameOver) {
                player2Message = player2.recieve();
                if (!Data.isValidCoordinate(player2Message)) {
                    player2.send("err");
                } else{
                    String result = player1Board.checkIfHit(player2Message);
                    player2.send(result);
                    if(result.contains("win")){
                        player1.send(player2Message+","+result.replace("win", "loss"));
						gameOver = true;
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
