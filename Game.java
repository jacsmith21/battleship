import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Scanner;
import java.util.ArrayList;

/** TODO
	1. Add font / background colors to code so nothing is hard coded in and we can easily switch them in the settings (LAST)
	5. Make commander messsage look better (LAST)
	6. Add theme music (LAST)
	7. Add hit / miss sounds (LAST)
	10. Add endgame screen popup
*/

public class Game extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
	//NetBeans components
	private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
	
	//General constants
	final int MAX_ROW = 10;
	final int MAX_COL = 10;
	final int SIDE = 42;
	final String ERROR = "err";
	final Color WATER = new Color(64,164,223);
	final Color WATER_BORDER = new Color(82,187,245);
	final Color HIT = new Color(255, 40, 40);
	final Color MISS = new Color(255,255,255);
	final Color SHIP = new Color(100,100,100);
	final Color SELECTED = new Color(255,250,205);
	final int WIDTH = 420; //Enemy / user board width
	final char HORIZONTAL = 'h';
	final char VERTICLE = 'v';
	
	//Initial ship info
	final int[] X_POSITIONS = {0, 1, 2, 3};
	final int[] Y_POSITIONS = {0, 1, 0, 1};
	final int[] LENGTHS = {5,4,3,2};
	final char[] ORIENTATIONS = {VERTICLE,VERTICLE,VERTICLE,VERTICLE}; //v = VERTICLE, h = HORIZONTAL
	final String[] NAMES = {"AC","CR","SB","FR"};
	
	//for debugging
	final boolean DEBUG = true;
	
	//Game states
	private boolean settingShips;
	private boolean attack; 
	private boolean defend;
	private boolean gameComplete;
	
	//Drag and drop / baord
	GridBagConstraints c;
    private int mouseX;
    private int mouseY;
	
	//JComponents
	private JLabel commander; //Text from the commander
	private JPanel gamePanel; //Top level panel
	private JPanel userBoard;
	private JLabel username;
	private JPanel enemyBoard;
	private GridButton[][] userButtons;
	private GridButton[][] enemyButtons;
	private JButton submitButton;
	private JButton rotateButton;
	private GridButton button; //The last GridButton that has been clicked on
	private ShipLabel[] ships;
	
	
	//Font and background color (always Black or White)
	private Color fontColor;
	private Color backgroundColor;
	
	//Server connection
	private ClientConnection server;
	
	/** Constructor for the game JPanel */
    public Game(){
        initComponents(); //initializes the components (generated by NETBEANS)
		
		//better names
		gamePanel = jPanel1;
		userBoard = jPanel2;
		enemyBoard = jPanel4;
		commander = jLabel4;
		submitButton = jButton4;
		rotateButton = jButton6;
		username = jLabel15;
		
		userBoard.setLayout(new GridBagLayout());
		enemyBoard.setLayout(new GridBagLayout());
		
		this.add(gamePanel);//Adding game panel to this
		
		//Setting initial game states
		settingShips = true;
		attack = false; 
		defend = false;
		gameComplete = false;
		
		/*
		//Connecting to server
		Scanner sc = new Scanner(System.in);
		int port = sc.nextInt();
		server = new ClientConnection();
		server.createConnection(port);
		*/
		
		//Initializing boards
		ships = new ShipLabel[X_POSITIONS.length];
		for(int i = 0; i < X_POSITIONS.length; i++){
			ships[i] = createShip(X_POSITIONS[i], Y_POSITIONS[i], LENGTHS[i], NAMES[i], ORIENTATIONS[i]);
			if(DEBUG) System.out.println("Created ship at (" + ships[i].getXCell() + "," + ships[i].getYCell() + ")");
			setShip(ships[i],userBoard);
		}
		
		userButtons = setGrid(userBoard); //setting up user grid to blue
		enemyButtons = setGrid(enemyBoard); //setting up the enemy grid to blue
		setUnclickable(enemyButtons);
		commander.setText("<html>Set your <br>ships<br>commander!</html>");
    }

	public boolean isFinished(){
		return gameComplete;
	}
	
	private ShipLabel createShip(int x, int y, int length, String name, char orientation){
		ShipLabel ship = new ShipLabel(x, y, length, name, orientation);
		ship.setBorder(BorderFactory.createLineBorder(WATER_BORDER));
		ship.addMouseMotionListener(this);
        ship.addMouseListener(this);
        ship.setBackground(SHIP);
        ship.setOpaque(true);
        if(orientation == HORIZONTAL) ship.setPreferredSize( new Dimension(length*SIDE,SIDE) ); 
        else ship.setPreferredSize( new Dimension(SIDE, length*SIDE) );
        return ship;
	}
	
	public void setShip(ShipLabel ship, JPanel board){
		int x = ship.getXCell();
		int y = ship.getYCell();
		int length = ship.getLength();
		if(DEBUG) System.out.println("Setting ship at x: " + x + ", y: " + y);
		c = new GridBagConstraints();
		c.gridheight = length; //So that it is able to span multiple grid cells
        c.gridx = x;
        c.gridy = y;
		c.weightx = 1; //To avoid clumping
		c.weighty = 1; //To avoid clumping
		board.add(ship, c);
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
				GridButton button = new GridButton(j, i);
				button.setOpaque(true);
				button.setBorderPainted(true);
				button.setBackground(WATER);
				button.setBorder(BorderFactory.createLineBorder(WATER_BORDER));
				button.addActionListener(this);
				button.setPreferredSize(new Dimension(SIDE,SIDE));
				temp[i][j] = button;
				panel.add(button, c);
			}
		}
		return temp;
	}
	
	private void refreshUserButtons(){
		for(int i = 0; i < userButtons.length; i++){
			for(int j = 0; j < userButtons[0].length; j++){
				c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = j;
				c.weightx = 1; //To avoid clumping
				c.weighty  = 1; //To avoid clumping
				userBoard.remove(userButtons[i][j]);
				userBoard.add(userButtons[i][j],c);
			}
		}
	}
	
	/** Sets the buttons in the array to non clickable
		@param buttons The button array
	*/
	private void setUnclickable(GridButton[][] buttons){
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[0].length; j++){
				buttons[i][j].setEnabled(false);
			}
		}
	}
	
	/** Sets the buttons in the array to clickable
		@param buttons The button array
	*/
	private void setClickable(GridButton[][] buttons){
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[0].length; j++){
				buttons[i][j].setEnabled(true);
			}
		}
	}
	
	/** Starts a new thread to read a message from the server */
	private void newMessageThread(){
		Thread t = new Thread(new Runnable(){
        	public void run() {
		    		getMessage();
		    	}
			});
		t.start();
	}
	
	/** Starts the gameplay for both player one and player 2
		@param player the message from the server acknoledging the player number
	*/
	private void startGameplay(String message){
		setUnclickable(userButtons);
		refreshUserButtons();
		setClickable(enemyButtons);
		if(message.contains("1")){
			if(DEBUG) System.out.println("Player one ready to play");
			newMessageThread(); //gets ok message
		}else{
			if(DEBUG) System.out.println("Player two ready to play");
			startDefense();
		}
	}
	
	/** Starts the defense turn for the user (changes game states, new thread to read message) */
	private void startDefense(){
		if(DEBUG) System.out.println("On defense");
		commander.setText("<html>Defend!</html>");
		attack = false;
		defend = true;
		newMessageThread(); //Gets message on defense
	}
	
	/** Starts the attack turn for the user (changes game states, commander message) */
	private void startOffense(){
		if(DEBUG) System.out.println("On offense");
		commander.setText("<html>Attack!</html>");
		attack = true;
		defend = false;
	}
	
	/**Send a message, either to the server or to the command prompt
		@param message the string message to send
	*/
	private void sendMessage(String message){
		server.send(message);
		if(DEBUG) System.out.println("SENDING: " + message);
	}
	
	/**Recieves a message, either from the server or from the command line
		@return the recieved string message
	*/
	private void getMessage(){
		String message = server.recieve();
		if(DEBUG) System.out.println("RECEIVING: " + message);
		if(message.equals("ok")){
			startOffense();
		}else if(attack){
			if(!message.equals(ERROR)){
				updateEnemyBoard(message); //updates GUI
				if(message.contains("win")) endGame(message);
				else startDefense();
			}	
		}else if(defend){
			updateUserBoard(message); //updates GUI
			if(message.contains("loss")) endGame(message);
			else startOffense();
		}else if(settingShips){
			if(!message.equals(ERROR)){
				settingShips = false;
				gamePanel.remove(submitButton);
				gamePanel.remove(rotateButton);
				gamePanel.revalidate();
				gamePanel.repaint();
				startGameplay(message);
			}else{
				commander.setText("<html>Error!<br>Try again!</html>");
				if(DEBUG) System.out.println("Error with ship locations");
			}
		}
	}
	
	private void endGame(String message){
		if(DEBUG) System.out.println("Starting end game process");
		attack = false;
		defend = false;
		gameComplete = true;
		if(message.contains("win")) {} //TODO display popup
		//TODO lanch leaderboards
	}
	
	
	/** The actionPerformed method for the grid buttons */
	public void actionPerformed(ActionEvent e){
		if(attack){
			button = (GridButton)e.getSource();
			sendMessage(button.getCoordinate()); //Sends attack
			newMessageThread(); //Gets response
		}
	}
	
	/** Updates the enemy's board depending on the message recieved back from the server
		@param message the message recieved back from the server
	*/
	private void updateEnemyBoard(String message){
		if(message.contains("sunk")){
			button.setBackground(HIT); //button = most recent clicked button
		}else if(message.contains("hit")){
			button.setBackground(HIT);
		}else{
			button.setBackground(MISS);
		}
	}
	
	private void updateUserBoard(String message){
		int[] coordinate = getRowCol( (message.split(","))[0] );
		button = userButtons[coordinate[0]][coordinate[1]]; //[row][col]
		if(message.contains("sunk")){
			button.setBackground(HIT);
		}else if(message.contains("hit")){
			button.setBackground(HIT);
		}else{
			button.setBackground(MISS);
		}
	}
	
	private int[] getRowCol(String coordinate){
		char c = coordinate.charAt(0);
        int x = c - 65;
		int y = Integer.parseInt(coordinate.substring(1)) - 1; //Starts at 0
		int[] temp = {x, y};
		return temp;
	}
	
    public void jButton5ActionPerformed(java.awt.event.ActionEvent evt) { //HOME
		System.out.println("home");
		//TODO
	}

    public void jButton2ActionPerformed(java.awt.event.ActionEvent evt) { //HELP
        System.out.println("help");
		//TODO
	}

    public void jButton3ActionPerformed(java.awt.event.ActionEvent evt) { //SETTINGS
        System.out.println("settings");
		//TODO
	}

	/** The submit button on the board, sends the ships location to the server and starts a new message thread
		@param evt the action event
	*/
    public void jButton4ActionPerformed(java.awt.event.ActionEvent evt) { //SUBMIT
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
		sendMessage(toSend);
		newMessageThread(); //gets return message (ack1 / ack2)
	}

    public void jButton6ActionPerformed(java.awt.event.ActionEvent evt){ //ROTATING
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
				ship.setX(newXCell);
				ship.setY(newYCell);
				setShip(ship, userBoard);
				refreshUserButtons();
				userBoard.invalidate();
				userBoard.repaint();
			}
		}
	}

	public void mousePressed(MouseEvent e){
		if(settingShips){
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
				}
			}
		}
    }

	/** When mouse is released, it snaps the ship to the nearest grid location
		@param e the mouse event
	*/
    public void mouseReleased(MouseEvent e) {
		if(settingShips){	
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
						}else if(newX < 0){
							cellX = 0;
							newX = 0;
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
						}else if(newY < 0){
							cellY = 0;
							newY = 0;
						}
						if(newX + SIDE > WIDTH){
							cellX = MAX_COL-1; //new x cell
							newX = SIDE*(cellX);
							if(DEBUG) System.out.println("Error new x cell: " + cellX);
						}        
					}
					ship.setBounds(newX, newY , ship.getWidth(), ship.getHeight());
					ship.setX(cellX);
					ship.setY(cellY);
					userBoard.remove(ship);
					setShip(ship,userBoard);
					refreshUserButtons();
					userBoard.invalidate();
					userBoard.repaint();
					break;
				}
			}
		}
    }

    public void mouseDragged(MouseEvent e) {
		if(settingShips){	
			for(ShipLabel ship : ships){
				if(ship.isDrag()){
					int x = ship.getX();
					int y = ship.getY();
					mouseX = e.getX();
					mouseY = e.getY();
					ship.setBounds(mouseX + x, mouseY + y, ship.getWidth(), ship.getHeight());
				}
			}
		}
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
	
	private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new GridPanel();
        jPanel4 = new GridPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(254, 254, 254));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Cursor", 0, 12)); // NOI18N
        setName("Battleship"); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setRequestFocusEnabled(false);

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Orbitron", 0, 15)); // NOI18N
        jButton2.setText("help");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Orbitron", 0, 15)); // NOI18N
        jButton3.setText("settings");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Orbitron", 0, 15)); // NOI18N
        jButton5.setText("home");
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton5.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButton5.setName(""); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/commander.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Orbitron", 0, 11)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(200, 200, 200));
        //jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        jPanel2.setPreferredSize(new java.awt.Dimension(420, 420));
        //jPanel2.setLayout(new java.awt.GridLayout(10,10));

        jPanel4.setBackground(new java.awt.Color(200, 200, 200));
        //jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        jPanel4.setPreferredSize(new java.awt.Dimension(420, 420));
        //jPanel4.setLayout(new java.awt.GridLayout(10,10));

        jLabel6.setFont(new java.awt.Font("Orbitron", 0, 11)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ships.png"))); // NOI18N

        jLabel2.setIcon( new javax.swing.ImageIcon(getClass().getResource("/green_circle.png") )); // NOI18N
        jLabel2.setToolTipText("");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/green_circle.png"))); // NOI18N
        jLabel5.setToolTipText("");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/green_circle.png"))); // NOI18N
        jLabel7.setToolTipText("");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/green_circle.png"))); // NOI18N
        jLabel8.setToolTipText("");

        jLabel9.setFont(new java.awt.Font("Orbitron", 0, 11)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ships.png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/green_circle.png"))); // NOI18N
        jLabel10.setToolTipText("");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/green_circle.png"))); // NOI18N
        jLabel11.setToolTipText("");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/green_circle.png"))); // NOI18N
        jLabel12.setToolTipText("");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/green_circle.png"))); // NOI18N
        jLabel13.setToolTipText("");

        jLabel14.setFont(new java.awt.Font("Orbitron", 0, 24)); // NOI18N
        jLabel14.setText("Battleship");

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Orbitron", 0, 12)); // NOI18N
        jButton4.setText("submit");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Orbitron", 0, 12)); // NOI18N
        jButton6.setText("rotate");
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Orbitron", 0, 24)); // NOI18N
        jLabel15.setText("username");

        jLabel16.setFont(new java.awt.Font("Orbitron", 0, 24)); // NOI18N
        jLabel16.setText("Enemy");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(jLabel14)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(31, 31, 31)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel16)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3))
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)))
                .addGap(34, 34, 34))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jLabel14))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel8)
                                        .addGap(15, 15, 15))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel3))
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel12)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel11)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel10)
                                            .addGap(15, 15, 15))
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton6)))
        );
    }	
}
