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
	final int LENGTH = 4;
	final int BORDER = 2;

	
	private JPanel panel;
    private JLabel label1;
    private JLabel label2;
	GridBagConstraints c;
    private int mouseX;
    private int mouseY;
    private boolean drag;
	
	
	public Test(){
		setSize(WIDTH,HEIGHT);
		
		panel = new JPanel(new GridBagLayout());
		mouseX = 1;
		mouseY = 1;
		drag = false;
		
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(400,400));
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		GridBagConstraints c;
		
		//Adding ship first
		c = new GridBagConstraints();
		label1 = new JLabel();
		label1.setOpaque(true);
        label1.setPreferredSize(new Dimension(SIDE, LENGTH*SIDE+2*(LENGTH-1))); //Setting size to 1 grid by 4 grids
		label1.addMouseMotionListener(this);
        label1.addMouseListener(this);
		c.gridheight = LENGTH; //Settings span to length
		label1.setBounds(mouseX, mouseY, SIDE, SIDE*LENGTH); //No clue
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
		System.out.println(panel.getX());
		
		
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
		System.out.println(panel.getSize());
		
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

        int newX = getPosition(cellX); 
        int newY = getPosition(cellY);
		
		if(DEBUG) System.out.println("x: " + curPosX + ", cell: " + cellX + ", new x: " + newX);
		if(DEBUG) System.out.println("y: " + curPosY + ", cell: " + cellY + ", new y: " + newY);
		
        if(label1.getWidth() > SIDE+1){ //If HORIZONTAL
            if(newX + LENGTH*SIDE > WIDTH){
				cellX = MAX_COL-LENGTH; //new x cell
                newX = getPosition(cellX);
				if(DEBUG) System.out.println("Error new x cell: " + cellX);
            } if(newY + SIDE > WIDTH){
				cellY = MAX_ROW-1; //new y cell
                newY = getPosition(cellY);
				if(DEBUG) System.out.println("Error new y cell: " + cellY);
            }
        } else{ //If VERTICLE
            if(newY + LENGTH*SIDE > WIDTH){
				cellY = MAX_ROW-LENGTH;
                newY = getPosition(cellY); //new y cell
				if(DEBUG) System.out.println("Error new y cell: " + cellY);
            } if(newX + SIDE > WIDTH){
				cellX = MAX_COL-1; //new y cell
                newX = getPosition(cellX);
				if(DEBUG) System.out.println("Error new x cell: " + cellX);
            }        
		}

        label1.setBounds(newX, newY , label1.getWidth(), label1.getHeight());
    }
	
	public int getPosition(int loc){ // where 0 <= loc < col/row
		return loc*SIDE + loc*BORDER + BORDER/2; //SIDES + INNER BORDERS + OUTER BORDER
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
		int length = LENGTH*SIDE + (LENGTH-1)*BORDER + 1;
        if(label1.getWidth() < SIDE+1){ //If VERTICLE, switch HORIZONTAL
            if(label1.getX() + length > WIDTH){ //if rotating puts it outside the right side, move left
                label1.setBounds(getPosition(MAX_COL-LENGTH), label1.getY(), length, SIDE);
            } else{
                label1.setBounds(label1.getX(), label1.getY(), length, SIDE);
            }
        } else{ //If HORIZONTAL, switch VERTICLE
            if(label1.getY() + SIDE*LENGTH > WIDTH){ //if rotating puts it outside the bottom, move up
                label1.setBounds(label1.getX(), getPosition(MAX_ROW-LENGTH), SIDE, length);
            } else{
                label1.setBounds(label1.getX(), label1.getY(), SIDE, length);
            }
        }
    }
	
	public static void main(String[] args){
		new Test() .setVisible(true);
	}
}