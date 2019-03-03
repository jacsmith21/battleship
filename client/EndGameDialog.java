import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EndGameDialog extends JDialog{
	
	public EndGameDialog(Client client, String message){
		ImageIcon commanderIcon = null;
		try{
			commanderIcon = new ImageIcon(  getClass().getResource("images/black_commander.png")  );
		}catch(Exception e){
			System.out.println("Image folder not found!");
		}
		
		this.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setUndecorated(true);
		
		this.getContentPane().setBackground( Color.WHITE );
		this.setLayout(new FlowLayout());
		
		JLabel commanderLabel = new JLabel();
		commanderLabel.setIcon(commanderIcon);
		
		JLabel label = new JLabel();
		if(message.contains("win")) label.setText("Congratulations commander! You won!");
		else label.setText("Better luck next time commander!");
		label.setSize(50, 12);
		
		this.add(commanderLabel);
		this.add(label);
		
		JButton returnToGame = new JButton("Return");
		returnToGame.setPreferredSize(new Dimension(75, 40));
		returnToGame.setFont(new java.awt.Font("Orbitron", 0, 12));
		returnToGame.setBackground(Color.WHITE);
		returnToGame.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		returnToGame.setFocusPainted(false);
		returnToGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose(); //RETURN
			}
		});


		this.add(returnToGame);

		this.setPreferredSize(new Dimension(370, 175));
		this.pack();
	
		this.setLocationRelativeTo(client);

		this.repaint();
	}
}