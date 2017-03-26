/** Main java file, will control game flow */
import javax.swing.*;
import java.awt.*;

public class Client{
	public static void main(String[] args){
		final int HEIGHT = 611;
		final int WIDTH = 1027;
		final boolean DEBUG = true;
		
		JFrame frame = new JFrame("Battleship");
		frame.setSize(WIDTH,HEIGHT);
		Container cp = frame.getContentPane(); //Getting content pane
		
		//Game panel
		Game game = new Game(); //Game panel
		frame.setContentPane(game); //Setting panel to content pane
		
		Thread t = new Thread(new Runnable(){
        	public void run() {
				while(!game.isFinished()){
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){
						System.out.println(e.getMessage());
					}
				}
				if(DEBUG) System.out.println("Game Ended");
		    }
		});
		t.start();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		System.out.println(frame.getContentPane().getSize());
		frame.setResizable(false);
		frame.setVisible(true);
	}
}