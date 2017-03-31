import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Test extends JFrame{
	public Test(){
		new EndGameDialog("win") .setVisible(true);
		setSize(100,100);
	}
	
	public static void main(String[] args){
		new Test() .setVisible(true);
	}
}