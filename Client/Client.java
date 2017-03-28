/** Main java file, will control game flow */
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Client extends JFrame{
	//general constants	
	final int HEIGHT = 611;
	final int WIDTH = 1027;
	final ImageIcon BLACK_SHIP = new ImageIcon(  getClass().getResource("/black_ship.png")  );
	final ImageIcon WHITE_SHIP = new ImageIcon(  getClass().getResource("/white_ship.png")  );
	
	private Container cp;
	private Color fontColor;
	private Color backgroundColor;
	private ClientConnection server;
	private Pregame pregame;
	private Game game;
	
	final boolean DEBUG = true;	
		
	public Client(){
		super("Battleship");
		
		fontColor = Color.BLACK;
		backgroundColor = Color.WHITE;
		
		this.setSize(WIDTH,HEIGHT);
		this.setResizable(false);
		cp = this.getContentPane(); //Getting content pane
		/*
		Scanner sc = new Scanner(System.in);
		int port = sc.nextInt();
		server = new ClientConnection();
		server.createConnection(port);
		*/
		
		//Game panel
		pregame = new Pregame(this);
		game = new Game(this); //Game panel
		cp.add(pregame);
		//this.setContentPane(pregame); //Setting panel to content pane
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	
	public void play(){
		if(DEBUG) System.out.println("Going from pregame to game screen!");
		cp.remove(pregame);
		cp.add(game);
		cp.revalidate();
		cp.repaint();
	}
	
	public void displayHome(){
		if(DEBUG) System.out.println("Displaying home dialogue");
	}
	
	public void displayHelp(){
		if(DEBUG) System.out.println("Displaying help dialogue");
	}
	
	public void displaySettings(){
		if(DEBUG) System.out.println("Displaying settings dialogue");
		switchColors();
	}
	
	public void switchColors(){
		if(fontColor == Color.WHITE) fontColor = Color.BLACK;
		else fontColor = Color.WHITE;
		if(backgroundColor == Color.BLACK) backgroundColor = Color.WHITE;
		else backgroundColor = Color.BLACK;
		pregame.setBackgroundColor(backgroundColor);
		pregame.setFontColor(fontColor);
		pregame.repaint();
		game.setBackgroundColor(backgroundColor);
		game.setFontColor(fontColor);
		game.repaint();
	}
	
	
	public Color getFontColor(){ return fontColor; }
	
	public Color getBackgroundColor(){ return backgroundColor; }
	
	public void send(String message){ server.send(message); }
	
	public String receive() { return server.receive(); }
	
	public static void main(String[] args){
		new Client() .setVisible(true);
	}
}