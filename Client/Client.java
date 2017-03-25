/** Main java file, will control game flow */
import javax.swing.*;
import java.awt.*;

public class Client extends JFrame{
	private Container cp;
	private JPanel game;
	
	private int HEIGHT = 600;
	private int WIDTH = 1000;
	
	public Client(){
		super("Battleship");
		setSize(WIDTH,HEIGHT);
		
		cp = this.getContentPane(); //Getting content pane
		game = new Game(); //Game panel
		setContentPane(game); //Setting panel to content pane
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
	
	
	public static void main(String[] args){
		new Client() .setVisible(true);
	}
}