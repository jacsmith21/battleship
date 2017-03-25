import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Test extends JFrame implements MouseListener, MouseMotionListener, ActionListener{
	private int WIDTH = 436; //FIX this
	private int HEIGHT = 459; //FIX this, should be the same
	private int SIDE = 40;
	private Color WATER = new Color(64,164,223);
	final int MAX_ROW = 10;
	final int MAX_COL = 10;
	final boolean DEBUG = true;

	
	private JPanel panel;
    private JLabel label1;
    private JLabel label2;
	GridBagConstraints c;
    private int mouseX;
    private int mouseY;
    private boolean drag;
	
	
	public Test(){
		setSize(WIDTH,HEIGHT);
		
		JPanel panel = new JPanel(new GridBagLayout());
		mouseX = 200;
		mouseY = 100;
		drag = false;
		
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(400,400));
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		GridBagConstraints c;
		
		//Adding ship first
		int n = 4; //length of ships
		c = new GridBagConstraints();
		label1 = new JLabel();
		label1.setOpaque(true);
        label1.setPreferredSize(new Dimension(SIDE, n*SIDE+2*(n-1))); //Setting size to 1 grid by 4 grids
		label1.addMouseMotionListener(this);
        label1.addMouseListener(this);
		c.gridheight = n; //Settings span to length
		label1.setBounds(mouseX, mouseY, SIDE, SIDE*4); //No clue
        label1.setBackground(Color.BLACK);
        c.gridx = 0; //Initial location
        c.gridy = 0;
		c.weightx = 1; //To avoid clumping
		c.weighty  = 1; //To avoid clumping
        panel.add(label1, c);
		
		//Button in bottom right cell
		c = new GridBagConstraints();
        JButton rotate = new JButton("r");
        rotate.addActionListener(this);
		rotate.setPreferredSize(new Dimension(SIDE,SIDE));
        c.gridx = 9;
        c.gridy = 9;
		c.weightx = 1; //To avoid clumping
		c.weighty  = 1; //To avoid clumping
        panel.add(rotate, c);
		
		
		//Adding water labels underneath
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = j;
				c.weightx = 1; //To avoid clumping
				c.weighty  = 1; //To avoid clumping
				c.insets = new Insets(1,1,1,1); //Settign border in between components
				JLabel label = new JLabel();
				label.setOpaque(true);
				label.setPreferredSize(new Dimension(SIDE,SIDE));
				label.setBackground(WATER);
				panel.add(label, c);
			}
		}
		
		this.add(panel);
		
	}
	
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == label1) {
            drag = true;
			if(DEBUG) System.out.println("You clicked the label!");
        }
    }

    public void mouseReleased(MouseEvent e) {
        drag = false;
        int curPosX = label1.getX();
        int curPosY = label1.getY();

        int cellX = curPosX / SIDE;
        int cellY = curPosY / SIDE;

        cellX = cellX * SIDE;
        cellY = cellY * SIDE;

        if(label1.getWidth() > SIDE*2){ //If Horizontal
            if(cellX + 250 > WIDTH){
                cellX = 250;
            } if(cellY + 50 > WIDTH){
                cellY = 450;
            }
        } else{ //If Verticle
            if(cellY + 250 > WIDTH){
                cellY = 250;
            } if(cellX + 50 > WIDTH){
                cellX = 450;
            }        
		}

        label1.setBounds(cellX, cellY , label1.getWidth(), label1.getHeight());

    }

    public void mouseDragged(MouseEvent e) {
        if (drag) {
            int newX = panel.getX();
            int newY = panel.getY();
            int x = label1.getX();
            int y = label1.getY();
            mouseX = e.getX();
            mouseY = e.getY();
            label1.setBounds(newX + mouseX + x, newY + mouseY + y, label1.getWidth(), label1.getHeight());
        }
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}

    public void actionPerformed(ActionEvent e) {
        if(label1.getWidth() < SIDE*2){ //If verticle
            if(label1.getX() + 250 > WIDTH){
                label1.setBounds(250, label1.getY(), 250, 50);
            } else{
                label1.setBounds(label1.getX(), label1.getY(), 250, 50);
            }
        } else{
            if(label1.getY() + 250 > WIDTH){
                label1.setBounds(label1.getX(), 250, 50, 250);
            } else{
                label1.setBounds(label1.getX(), label1.getY(), 50, 250);
            }
        }
    }
	
	public static void main(String[] args){
		new Test() .setVisible(true);
	}
}