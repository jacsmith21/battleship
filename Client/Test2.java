import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Test2 extends JFrame implements MouseListener, MouseMotionListener, ActionListener{
	private int WIDTH = 436-20; //FIX this
	private int HEIGHT = 459-20; //FIX this, should be the same
	private int SIDE = 40;
	private Color WATER = new Color(64,164,223);
	final int MAX_ROW = 10;
	final int MAX_COL = 10;
	final boolean DEBUG = true;
	final int LENGTH = 4;
	final int BORDER = 0;
	final Color WATER_BORDER = new Color(82,187,245);
	final Color SHIP = new Color(100,100,100);
	final Color SELECTED = new Color(255,250,205);
	
	final int[] X_POSITIONS = {0, 1, 2, 3};
	final int[] Y_POSITIONS = {0, 0, 0, 0};
	final int[] LENGTHS = {5,4,3,2};
	final char[] ORIENTATIONS = {'v','v','v','v'}; //v = VERTICLE, h = HORIZONTAL
	final String[] NAMES = {"AC","CR","SB","FR"};
	
	
	private JPanel panel;
	GridBagConstraints c;
    private int mouseX;
    private int mouseY;
	private ShipLabel[] ships;
	
	public Test2(){
		setSize(WIDTH,HEIGHT);
		
		panel = new JPanel(new GridBagLayout());
		
		panel.setBackground(WATER_BORDER);
		panel.setPreferredSize(new Dimension(400,400));
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		ships = new ShipLabel[X_POSITIONS.length];
		for(int i = 0; i < X_POSITIONS.length; i++){
			ships[i] = createShip(X_POSITIONS[i], Y_POSITIONS[i], LENGTHS[i], NAMES[i], ORIENTATIONS[i]);
			if(DEBUG) System.out.println("Created ship at (" + ships[i].getX() + "," + ships[i].getY() + ")");
		}
		
		setGrid(panel);
		
		this.add(panel);
	}
	
	
	private ShipLabel createShip(int x, int y, int length, String name, char orientation){
		if(DEBUG) System.out.println("Setting ship at x: " + y + ", y: " + x + ", Dememsion: " + SIDE + ", " + SIDE*length);
		ShipLabel ship = new ShipLabel(x, y, length, name, orientation);
		c = new GridBagConstraints();
		c.gridheight = length; //So that it is able to span multiple grid cells
		ship.setOpaque(true);
		if(orientation == 'h') ship.setPreferredSize( new Dimension(length*SIDE,SIDE) ); 
        else ship.setPreferredSize( new Dimension(SIDE, length*SIDE) );
		ship.addMouseMotionListener(this);
        ship.addMouseListener(this);
		c.gridheight = length; //Settings span to length
        ship.setBackground(SHIP);
        c.gridx = x; //Flipped coordinates
        c.gridy = y;
		c.weightx = 1; //To avoid clumping
		c.weighty = 1; //To avoid clumping
		panel.add(ship, c);
		return ship;
	}
	
	/** Sets the grid of the passed in GridLayout panel, initializes everything to water
		@param panel the panel to set the grid
		@player the player character to identify the board
		@return the button array
	*/	
	private GridButton[][] setGrid(JPanel panel){
		GridButton[][] temp = new GridButton[MAX_ROW][MAX_COL];
		for(int i = 0; i < MAX_ROW; i++){
			for(int j = 0; j < MAX_COL; j++){
				c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = j;
				c.weightx = 1; //To avoid clumping
				c.weighty  = 1; //To avoid clumping
				GridButton button = new GridButton(i, j);
				button.setBorderPainted(true);
				button.setBackground(WATER);
				button.setBorder(BorderFactory.createLineBorder(WATER_BORDER));
				//button.addActionListener(this);
				button.setPreferredSize(new Dimension(SIDE,SIDE));
				temp[i][j] = button;
				panel.add(button, c);
			}
		}
		return temp;
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(DEBUG) System.out.println("Rotating");
		for(ShipLabel ship : ships){
			if(ship.isSelected()){
				int length = ship.getLength()*SIDE;
				if(ship.getWidth() < SIDE+1){ //If VERTICLE, switch HORIZONTAL
					if(ship.getX() + length > WIDTH){ //if rotating puts it outside the right side, move left
						ship.setBounds(SIDE*(MAX_COL-ship.getLength()), ship.getY(), length, SIDE);
					} else{
						ship.setBounds(ship.getX(), ship.getY(), length, SIDE);
					}
				} else{ //If HORIZONTAL, switch VERTICLE
					if(ship.getY() + length > WIDTH){ //if rotating puts it outside the bottom, move up
						ship.setBounds(ship.getX(), SIDE*(MAX_ROW-ship.getLength()), SIDE, length);
					} else{
						ship.setBounds(ship.getX(), ship.getY(), SIDE, length);
					}
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		for(ShipLabel ship : ships){
			if (e.getSource() == ship) {
				ship.setDrag(true);
				for(ShipLabel s : ships){
					s.setBackground(SHIP);
					s.setSelected(false);
				}
				ship.setSelected(true);
				ship.setBackground(SELECTED);
				if(DEBUG) System.out.println("You clicked ship " + ship.getName());
				break;
			}
		}
    }

	/** When mouse is released, it snaps the ship to the nearest grid location
		@param e the mouse event
	*/
    public void mouseReleased(MouseEvent e) {
		for(ShipLabel ship : ships){
			if(ship.isDrag()){
				ship.setDrag(false);
				if(DEBUG) System.out.println("You released ship " + ship.getName());
				
				int length = ship.getLength();
				
				int curPosX = ship.getX();
				int curPosY = ship.getY();
				
				int cellX = curPosX / SIDE;
				int cellY = curPosY / SIDE;

				int newX = SIDE*(cellX); 
				int newY = SIDE*(cellY);
				
				if(DEBUG) System.out.println("x: " + curPosX + ", cell: " + cellX + ", snapped x: " + newX);
				if(DEBUG) System.out.println("y: " + curPosY + ", cell: " + cellY + ", snapped y: " + newY);
				
				if(ship.getWidth() > SIDE+1){ //If HORIZONTAL
					if(newX + length*SIDE > WIDTH){
						cellX = MAX_COL-length; //new x cell
						newX = SIDE*(cellX);
						if(DEBUG) System.out.println("Error: new x cell: " + cellX);
					} 
					if(newY + SIDE > WIDTH){
						cellY = MAX_ROW-1; //new y cell
						newY = SIDE*(cellY);
						if(DEBUG) System.out.println("Error: new y cell: " + cellY);
					}
				} else{ //If VERTICLE
					if(newY + length*SIDE > WIDTH){ //If it goes off the grid at the bottom
						cellY = MAX_ROW-length;
						newY = SIDE*(cellY); //new y cell
						if(DEBUG) System.out.println("Error new y cell: " + cellY);
					}
					if(newX + SIDE > WIDTH){
						cellX = MAX_COL-1; //new x cell
						newX = SIDE*(cellX);
						if(DEBUG) System.out.println("Error new x cell: " + cellX);
					}        
				}
				ship.setBounds(newX, newY , ship.getWidth(), ship.getHeight());			
				break;
			}
		}
    }

    public void mouseDragged(MouseEvent e) {
		for(ShipLabel ship : ships){
			if(ship.isDrag()){
				int newX = panel.getX();
				int newY = panel.getY();
				int x = ship.getX();
				int y = ship.getY();
				mouseX = e.getX();
				mouseY = e.getY();
				if(DEBUG) System.out.println("x: " + (newX + mouseX + x) + ", y: " + (newY + mouseY + y));
				ship.setBounds(newX + mouseX + x, newY + mouseY + y, ship.getWidth(), ship.getHeight());
			}
		}
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
	
	public static void main(String[] args){
		new Test2() .setVisible(true);
	}
}