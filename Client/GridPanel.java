import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GridPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
	//General constants
	final int MAX_ROW = 10;
	final int MAX_COL = 10;
	final int SIDE = 42; //gridbutton square side length
	final String ERROR = "err";
	final Color WATER = new Color(64,164,223);
	final Color WATER_BORDER = new Color(82,187,245);
	final Color HIT = new Color(255, 40, 40);
	final Color MISS = new Color(255,255,255);
	final Color SHIP = new Color(100,100,100);
	final Color SELECTED = new Color(255,250,205);
	final char HORIZONTAL = 'h';
	final char VERTICLE = 'v';
	
	//Initial ship info
	final int[] X_POSITIONS = {0, 1, 2, 3};
	final int[] Y_POSITIONS = {0, 0, 0, 0};
	final int[] LENGTHS = {5,4,3,2};
	final char[] ORIENTATIONS = {VERTICLE,VERTICLE,VERTICLE,HORIZONTAL}; //v = VERTICLE, h = HORIZONTAL
	final String[] NAMES = {"AC","CR","SB","FR"};
	
	private Game game;
	private GridButton[][] buttons;
	private ShipLabel[] ships;
	private GridButton button; //The last GridButton that has been clicked on
	
	private final boolean DEBUG = false;
	
	public GridPanel(Game game){
		this.game = game;
		this.setLayout(new GridBagLayout());
	}
	
	public int numberOfShips(){
		return ships.length;
	}
	
	/** Sets the grid of the panel, initializes everything to water */
	public void initGrid(){
		GridBagConstraints c = new GridBagConstraints();
		buttons = new GridButton[MAX_ROW][MAX_COL];
		for(int i = 0; i < MAX_ROW; i++){
			for(int j = 0; j < MAX_COL; j++){
				c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = j;
				c.weightx = 1; //To avoid clumping
				c.weighty  = 1; //To avoid clumping
				GridButton button = new GridButton(j, i);
				button.setOpaque(true);
				button.setBorderPainted(true);
				button.setBackground(WATER);
				button.setBorder(BorderFactory.createLineBorder(WATER_BORDER));
				button.addActionListener(this);
				button.setPreferredSize(new Dimension(SIDE,SIDE));
				buttons[i][j] = button;
				this.add(button, c);
			}
		}	
	}
	
	public void initShips(){
		ships = new ShipLabel[X_POSITIONS.length];
		for(int i = 0; i < X_POSITIONS.length; i++){
			ships[i] = new ShipLabel(X_POSITIONS[i], Y_POSITIONS[i], LENGTHS[i], NAMES[i], ORIENTATIONS[i], this);
			if(DEBUG) System.out.println("Created ship at (" + ships[i].getXCell() + "," + ships[i].getYCell() + ")");
			this.addShip(ships[i]);
		}
	}
	
	public void addShip(ShipLabel ship){
		GridBagConstraints c = new GridBagConstraints();
		int x = ship.getXCell();
		int y = ship.getYCell();
		int length = ship.getLength();
		if(ship.getOrientation() == VERTICLE){
			if(DEBUG) System.out.println("Setting ship vertically @ x: " + x + ", y: " + y);
			ship.setPreferredSize( new Dimension(SIDE,length*SIDE) );
			c.gridheight = length; //So that it is able to span multiple grid cells
        }else{
			if(DEBUG) System.out.println("Setting ship horizontally x: " + x + ", y: " + y);
			ship.setPreferredSize( new Dimension(length*SIDE,SIDE) );
			c.gridwidth = length;
		}
		c.gridx = x;
        c.gridy = y;
		c.weightx = 1; //To avoid clumping
		c.weighty = 1; //To avoid clumping
		this.add(ship, c);
	}
	
	/** Sets the buttons in the array to non clickable */
	public void setUnclickable(){
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[0].length; j++){
				buttons[i][j].setEnabled(false);
			}
		}
	}
	
	/** Sets the buttons in the array to clickable */
	public void setClickable(){
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[0].length; j++){
				buttons[i][j].setEnabled(true);
			}
		}
	}
	
	/** Re adds all of the buttons (sets them to the back of the panel hierarchy) */
	public void refreshButtons(){
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[0].length; j++){
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = j;
				c.weightx = 1; //To avoid clumping
				c.weighty  = 1; //To avoid clumping
				this.remove(buttons[i][j]);
				this.add(buttons[i][j],c);
			}
		}
	}
	
	public void refreshShips(){
		for(ShipLabel ship : ships){
			this.remove(ship);
			this.addShip(ship);
		}
	}
	
	/** Updates the enemy's board depending on the message recieved back from the server
		@param message the message recieved back from the server
	*/
	public void updateEnemyBoard(String message){
		//button = most recent clicked button
		if(message.contains("sunk")){
			String ship = (message.split(" "))[1];
			button.setBackground(HIT);
			game.updateEnemyStatusIcons(ship);
		}else if(message.contains("hit")){
			button.setBackground(HIT);
		}else{
			button.setBackground(MISS);
		}
		button.setEnabled(false);
	}
	
	public void updateUserBoard(String message){
		int[] coordinate = getRowCol( (message.split(","))[0] );
		button = buttons[coordinate[0]][coordinate[1]]; //[row][col]
		if(message.contains("sunk")){
			String ship = (  ((message.split(","))[1]  ).split(" "))[1]; 
			button.setBackground(HIT);
			game.updateUserStatusIcons(ship);
			button.setHit(true);
			addHitGridButton(coordinate[0],coordinate[1]);
		}else if(message.contains("hit")){
			button.setBackground(HIT);
			button.setHit(true);
			addHitGridButton(coordinate[0],coordinate[1]);
		}else{
			button.setBackground(MISS);
		}
	}
	
	public void addHitGridButton(int i, int j){
		GridButton button = new GridButton(i,j);
		button.setOpaque(true);
		button.setBorderPainted(true);
		button.setPreferredSize(new Dimension(SIDE,SIDE));
		button.setEnabled(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = i;
		c.gridy = j;
		c.weightx = 1; //To avoid clumping
		c.weighty  = 1; //To avoid clumping
		button.setBackground(HIT);
		this.add(button,c);
		refreshShips();
		refreshButtons();
	}
	
	private int[] getRowCol(String coordinate){
		char c = coordinate.charAt(0);
        int x = c - 65; //Starts at 0
		int y = Integer.parseInt(coordinate.substring(1)) - 1; //Starts at 0
		int[] temp = {x, y};
		return temp;
	}
	
	public void rotate(){
		for(ShipLabel ship : ships){
			if(ship.isSelected()){
				int length = ship.getLength()*SIDE;
				if(ship.getWidth() < SIDE+1){ //If VERTICLE, switch HORIZONTAL
					if(ship.getX() + length > game.WIDTH){ //if rotating puts it outside the right SIDE, move left
						ship.setBounds(SIDE*(MAX_COL-ship.getLength()), ship.getY(), length, SIDE);
					} else{
						ship.setBounds(ship.getX(), ship.getY(), length, SIDE);
					}
				} else{ //If HORIZONTAL, switch VERTICLE
					if(ship.getY() + length > game.WIDTH){ //if rotating puts it outside the bottom, move up
						ship.setBounds(ship.getX(), SIDE*(MAX_ROW-ship.getLength()), SIDE, length);
					} else{
						ship.setBounds(ship.getX(), ship.getY(), SIDE, length);
					}
				}
				
				int newXCell = ship.getX()/SIDE;
				int newYCell = ship.getY()/SIDE;
				
				if(ship.getOrientation() == HORIZONTAL){
					if(DEBUG) System.out.println("Rotating to verticle @ x: " + newXCell + ", y: " + newYCell);
					ship.setOrientation(VERTICLE);
				}
				else{
					if(DEBUG) System.out.println("Rotating to horizontal @ x: " + newXCell + ", y: " + newYCell);
					ship.setOrientation(HORIZONTAL);
				}
				ship.setXCell(newXCell);
				ship.setYCell(newYCell);
				addShip(ship);
				this.refreshButtons();
				this.invalidate();
				this.repaint();
			}
		}
	}
	
	public void sendShipLocations(){
		String toSend = "";
		int x;
		int y;
		for(int i = 0; i < ships.length; i++){
			ShipLabel ship = ships[i];
			if(i != 0) toSend += ",";
			toSend += ship.getName() + " ";
			x = ship.getXCell();
			y = ship.getYCell();
			for(int j = 0; j < ship.getLength(); j++){
				if(j != 0) toSend += " ";
				toSend += "" + (char)(x+65) + (y+1);
				if(ship.getOrientation() == HORIZONTAL) x++;
				else y++;
			}	
		}
		for(ShipLabel ship : ships) ship.setBackground(SHIP);
		this.revalidate();
		this.repaint();
		game.sendMessage(toSend);
		game.newMessageThread(); //gets return message (ack1 / ack2)
	}
	
	/** The actionPerformed method for the grid buttons */
	public void actionPerformed(ActionEvent e){
		if(game.isAttack()){
			button = (GridButton)e.getSource();
			game.sendMessage(button.getCoordinate()); //Sends attack
			game.newMessageThread(); //Gets attack response
		}
	}
	
	public void mousePressed(MouseEvent e){
		if(game.isSettingShips()){
			for(ShipLabel ship : ships){
				if (e.getSource() == ship) {
					ship.setDrag(true);
					ship.setSelected(true);
					ship.setBackground(SELECTED);
					if(DEBUG) System.out.println("You clicked ship " + ship.getName());
				}else{
					ship.setBackground(SHIP);
					ship.setSelected(false);
				}
			}
		}
    }

	/** When mouse is released, it snaps the ship to the nearest grid location
		@param e the mouse event
	*/
    public void mouseReleased(MouseEvent e) {
		if(game.isSettingShips()){	
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
						if(newX + length*SIDE > game.WIDTH){
							cellX = MAX_COL-length; 
							newX = SIDE*(cellX);
							if(DEBUG) System.out.println("Error: new x cell: " + cellX);
						}else if(newX < 0){
							cellX = 0;
							newX = 0;
						} 
						if(newY + SIDE > game.WIDTH){
							cellY = MAX_ROW-1; 
							newY = SIDE*(cellY);
							if(DEBUG) System.out.println("Error: new y cell: " + cellY);
						}else if(newY < 0){
							cellY = 0;
							newY = 0;
						}
					} else{ //If VERTICLE
						if(newY + length*SIDE > game.WIDTH){ //If it goes off the grid at the bottom
							cellY = MAX_ROW-length;
							newY = SIDE*(cellY); 
							if(DEBUG) System.out.println("Error new y cell: " + cellY);
						}else if(newY < 0){
							cellY = 0;
							newY = 0;
						}
						if(newX + SIDE > game.WIDTH){
							cellX = MAX_COL-1;
							newX = SIDE*(cellX);
							if(DEBUG) System.out.println("Error new x cell: " + cellX);
						} else if(newX < 0){
							cellX = 0;
							newX = 0;
						}       
					}
					ship.setBounds(newX, newY , ship.getWidth(), ship.getHeight());
					ship.setXCell(cellX);
					ship.setYCell(cellY);
					this.remove(ship);
					this.addShip(ship);
					this.refreshButtons();
					this.invalidate();
					this.repaint();
					break;
				}
			}
		}
    }

    public void mouseDragged(MouseEvent e) {
		if(game.isSettingShips()){	
			for(ShipLabel ship : ships){
				if(ship.isDrag()){
					int x = ship.getX();
					int y = ship.getY();
					int mouseX = e.getX();
					int mouseY = e.getY();
					ship.setBounds(mouseX + x, mouseY + y, ship.getWidth(), ship.getHeight());
				}
			}
		}
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}
