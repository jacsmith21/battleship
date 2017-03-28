/** Main java file, will control game flow */
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Client extends JFrame{
	private Color fontColor; //TODO
	private Color backgroundColor; //TODO
	private ClientConnection server;
	
	
	//general constants	
	final int HEIGHT = 611;
	final int WIDTH = 1027;
	
	final boolean DEBUG = true;	
		
	public Client(){
		super("Battleship");
		
		this.setSize(WIDTH,HEIGHT);
		this.setResizable(false);
		Container cp = this.getContentPane(); //Getting content pane
		
		Scanner sc = new Scanner(System.in);
		int port = sc.nextInt();
		server = new ClientConnection();
		server.createConnection(port);
		
		//Game panel
		Game game = new Game(this); //Game panel
		this.setContentPane(game); //Setting panel to content pane
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	
	public static void main(String[] args){
		new Client() .setVisible(true);
	}
	
	public void send(String message){ server.send(message); }
	
	public String receive() { return server.receive(); }
}