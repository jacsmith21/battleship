/** Main java file, will control game flow */
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.awt.event.*;
import javax.swing.border.*;
import java.applet.*;
import javax.sound.sampled.*;
import java.io.*;

/** TODO
	7. Add hit / miss sounds (LAST)
	10. Add color blind images
*/

public class Client extends JFrame{
	//general constants	
	final int HEIGHT = 611;
	final int WIDTH = 1027;
	final ImageIcon BLACK_SHIP = new ImageIcon(  getClass().getResource("/black_ship.png")  );
	final ImageIcon WHITE_SHIP = new ImageIcon(  getClass().getResource("/white_ship.png")  );
	final String MUSIC = "music/ThemeMusic.wav";
	
	
	private Container cp;
	private Color fontColor;
	private Color backgroundColor;
	private ClientConnection server;
	private Pregame pregame;
	private Game game;
	private Register register;
	private Login login;
	private Initial initial;
	private LineBorder border;
	private boolean loggedIn; //game state
	private boolean isColorBlind;
	private FloatControl musicControl;
	private int musicLevel;
	private int FXLevel;
	private File themeMusicFile;
	
	final boolean DEBUG = true;	
		
	public Client(){
		super("Battleship");
		
		try{
			themeMusicFile = new File(MUSIC);
			
		}catch(NullPointerException e){
			System.out.println(e.getMessage());
		}
		
		fontColor = Color.BLACK;
		backgroundColor = Color.WHITE;
		border = new LineBorder(Color.BLACK, 1);
		
		this.setSize(WIDTH,HEIGHT);
		this.setResizable(false);
		cp = this.getContentPane(); //Getting content pane
		
		
		Scanner sc = new Scanner(System.in);
		int port = sc.nextInt();
		server = new ClientConnection();
		server.createConnection(port);
		
		
		//Game panel
		register = new Register(this);
		login = new Login(this);
		pregame = new Pregame(this);
		game = new Game(this); //Game panel
		initial = new Initial(this);
		cp.add(initial);
		
		//Setting sound stuff
		musicLevel = 50;
		FXLevel = 50;
		loggedIn = false;
		isColorBlind = false;
		initMusic();
		game.initFX();
		setMusicLevel(musicLevel);
		game.setFXLevel(FXLevel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	
	public void initMusic(){
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(themeMusicFile);
			Clip themeMusic = AudioSystem.getClip();
			themeMusic.open(audioInputStream);
			musicControl = (FloatControl) themeMusic.getControl(FloatControl.Type.MASTER_GAIN);
			themeMusic.loop(0);
		}catch(UnsupportedAudioFileException e){
			System.out.println(e.getMessage());
		}catch(LineUnavailableException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void startGame(){
		if(DEBUG) System.out.println("Going from pregame to game screen!");
		cp.remove(pregame);
		cp.add(game);
		cp.revalidate();
		cp.repaint();
	}
	
	public void startPregame(String name){
		if(DEBUG) System.out.println("Going from registration to the pregame!");
		loggedIn = true;
		pregame.setName(name);
		cp.remove(login);
		cp.remove(register);
		cp.add(pregame);
		cp.revalidate();
		cp.repaint();
	}
	
	public void startLogin(){
		if(DEBUG) System.out.println("Starting login!");
		cp.remove(initial);
		cp.add(login);
		cp.revalidate();
		cp.repaint();
	}
	
	public void startRegister(){
		if(DEBUG) System.out.println("Starting registration!");
		cp.remove(initial);
		cp.add(register);
		cp.revalidate();
		cp.repaint();
	}
	
	public void returnHome(){
		cp.remove(initial);
		cp.remove(register);
		cp.remove(login);
		cp.remove(pregame);
		cp.remove(game);
		if(loggedIn) cp.add(pregame);
		else cp.add(initial);
		cp.revalidate();
		cp.repaint();
	}
	
	public void startLeaderboards(){
		cp.remove(game);
		//TODO Lauch Leaderboards
		cp.revalidate();
		cp.repaint();
	}
	
	public void displayHome(){
		if(DEBUG) System.out.println("Displaying home dialogue");
		JDialog d1=new JDialog();
		
		d1.getRootPane().setBorder(border);
		d1.setUndecorated(true);
		d1.setSize(300,75);
		
		d1.setLocationRelativeTo(this);
		d1.getContentPane().setBackground( Color.WHITE );
		d1.setLayout(new FlowLayout());
		
		JLabel check = new JLabel("Are you sure you want to return home?");
		check.setSize(50, 12);
		d1.add(check);

		JButton yes = new JButton("yes");
		yes.setSize(100,50);
		yes.setBackground(Color.WHITE);
		yes.setFocusPainted(false);
		yes.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				returnHome(); //YES
				if(DEBUG) System.out.println("ET PHONE HOME");
				d1.dispose();
			}
		});
		JButton no = new JButton("no");
		no.setSize(100, 50);
		no.setBackground(Color.WHITE);
		no.setFocusPainted(false);
		no.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				d1.dispose(); //NO
			}
		});

		JPanel yesnoButtons = new JPanel();
		yesnoButtons.setLayout(new GridLayout(1,2, 1, 1));
		yesnoButtons.setBorder(border);
		yesnoButtons.setBackground(Color.BLACK);
		yesnoButtons.add(yes);
		yesnoButtons.add(no);

		d1.add(yesnoButtons);
		
		d1.setVisible(true);
	}
	
	public void displayHelp(){
		if(DEBUG) System.out.println("Displaying help dialogue");
        System.out.println("help");
		//TO DO
		JDialog d1=new JDialog();
		d1.getContentPane().setBackground( Color.WHITE );
		d1.setUndecorated(true);
		d1.setSize(310,350);
		d1.setLocationRelativeTo(this);

		JPanel returnButton = new JPanel();
		returnButton.setBackground(Color.WHITE);

		// Set some layout
		d1.setLayout(new FlowLayout());
		d1.getRootPane().setBorder(border);

		JPanel tester = new JPanel();
		tester.setBackground(Color.WHITE);

		JPanel title = new JPanel();
		title.setLayout(new FlowLayout());
		title.setBackground(Color.WHITE);

		JLabel howTo = new JLabel("How to Play  ");
		JLabel battleShip = new JLabel("BattleShip");
		title.add(howTo);
		title.add(battleShip);

		battleShip.setFont(new java.awt.Font("Orbitron", 0, 25));
		JLabel heading = new JLabel("1. Setting Your Ships");
		heading.setFont(new java.awt.Font("Orbitron", 0, 15));
		JLabel info = new JLabel(convertToMultiline("Your ships are already set up on your board \ninitially. Drag and drop to move them around. \nIf you want to rotate, just click on a ship and \nthen click on the rotate button on the top right \ncorner. When you're done, hit submit."));
		JLabel heading2 = new JLabel("2. Playing the Game");
		heading2.setFont(new java.awt.Font("Orbitron", 0, 15));
		JLabel info2 = new JLabel(convertToMultiline("The goal is to sink all of the enemy's ships \nbefore they sink all of yours. Each ship needs \nto be hit at all of its locations before it is sunk. \nEach turn, both you and your enemy will be \ngiven a chance to attack each other's ships. \nWhen it is your turn, simply choose a location \non the enemy's board to attack."));

		JButton returnToGame = new JButton("Return");
		returnToGame.setPreferredSize(new Dimension(75, 40));
		returnToGame.setFont(new java.awt.Font("Orbitron", 0, 12));
		returnToGame.setBackground(Color.WHITE);
		returnToGame.setBorder(border);
		returnToGame.setFocusPainted(false);
		returnToGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				d1.dispose(); //RETURN
			}
		});

		d1.add(title);
		d1.add(heading);
		d1.add(info);
		d1.add(heading2);
		d1.add(info2);
		returnButton.add(returnToGame);
		d1.add(returnButton);

		//d1.add(tester);

		d1.setVisible(true);
	}
	
	public static String convertToMultiline(String orig)
	{
		return "<html>" + orig.replaceAll("\n", "<br>");
	}
	
	public void displaySettings(){
		if(DEBUG) System.out.println("Displaying settings dialogue");
		JDialog d1=new JDialog();

		d1.setUndecorated(true);
		d1.setSize(320,375);
		d1.setLocationRelativeTo(this);
		d1.setLayout(new FlowLayout());
		d1.getContentPane().setBackground( Color.WHITE );
		d1.getRootPane().setBorder(border);

		JPanel tester = new JPanel();
		tester.setPreferredSize(new Dimension(300, 300));
		tester.setBackground(Color.WHITE);

		// Set some layout
		tester.setLayout(new BoxLayout(tester, BoxLayout.PAGE_AXIS));

		JLabel test = new JLabel("Settings");
		test.setAlignmentX(CENTER_ALIGNMENT);
		test.setFont(new java.awt.Font("Orbitron", 0, 24));
		tester.add(test);

		JPanel settings = new JPanel();
		settings.setLayout(new GridLayout(4,2, 1, 1));
		settings.setBackground(Color.BLACK);
		settings.setBorder(border);

		JPanel musicVol = new JPanel();
		musicVol.setBackground(Color.WHITE);
		JLabel musicVolLabel = new JLabel("Music Volume");
		musicVolLabel.setAlignmentY(CENTER_ALIGNMENT);
		musicVol.add(musicVolLabel);

		JPanel musicSlider = new JPanel();
		musicSlider.setBackground(Color.WHITE);
		JSlider musicSliders = new JSlider();
		musicSliders.setValue(musicLevel);
		musicSliders.setAlignmentY(CENTER_ALIGNMENT);
		musicSliders.setBackground(Color.WHITE);
		musicSliders.setPreferredSize(new Dimension(130,20));
		musicSlider.add(musicSliders);

		JPanel FXVol = new JPanel();
		FXVol.setBackground(Color.WHITE);
		JLabel FXVolLabel = new JLabel("FX Volume");
		FXVolLabel.setAlignmentY(CENTER_ALIGNMENT);
		FXVol.add(FXVolLabel);

		JPanel FXVolSlide = new JPanel();
		FXVolSlide.setBackground(Color.WHITE);
		JSlider FXVolSlider = new JSlider();
		FXVolSlider.setValue(FXLevel);
		FXVolSlider.setAlignmentY(CENTER_ALIGNMENT);
		FXVolSlider.setPreferredSize(new Dimension(130,20));
		FXVolSlider.setBackground(Color.WHITE);
		FXVolSlide.add(FXVolSlider);

		JPanel backAndFontPanel = new JPanel();
		backAndFontPanel.setBackground(Color.WHITE);
		JLabel BFColor = new JLabel(convertToMultiline("Background & \nFont Colour"));
		BFColor.setAlignmentY(CENTER_ALIGNMENT);
		backAndFontPanel.add(BFColor);

		JPanel backAndFont = new JPanel();
		backAndFont.setBackground(Color.WHITE);
		JRadioButton BW = new JRadioButton("B: Black F: White");
		BW.setBackground(Color.WHITE);
		JRadioButton WB = new JRadioButton("B: White F: Black");
		WB.setBackground(Color.WHITE);
		ButtonGroup BWGroup = new ButtonGroup();
    	BWGroup.add(BW);
    	BWGroup.add(WB);
		backAndFont.add(BW);
		backAndFont.add(WB);
		if(fontColor == Color.black) BW.setSelected(true);
		else WB.setSelected(true);

		JPanel colorBlindPane = new JPanel();
		colorBlindPane.setBackground(Color.WHITE);
		JLabel colorBlind = new JLabel("Colour Blind");
		colorBlind.setAlignmentY(CENTER_ALIGNMENT);
		colorBlindPane.add(colorBlind);

		JPanel yesno = new JPanel();
		yesno.setBackground(Color.WHITE);
		JRadioButton yes = new JRadioButton("Yes");
		yes.setBackground(Color.WHITE);
		yes.setAlignmentY(CENTER_ALIGNMENT);
		JRadioButton no = new JRadioButton("No");
		no.setAlignmentY(CENTER_ALIGNMENT);
		no.setBackground(Color.WHITE);
		ButtonGroup yesNoGroup = new ButtonGroup();
    	yesNoGroup.add(yes);
    	yesNoGroup.add(no);
		yesno.add(yes);
		yesno.add(no);
		if(isColorBlind) yes.setSelected(true);
		else no.setSelected(true);

		settings.add(musicVol);
		settings.add(musicSlider);
		settings.add(FXVol);
		settings.add(FXVolSlide);
		settings.add(backAndFontPanel);
		settings.add(backAndFont);
		settings.add(colorBlindPane);
		settings.add(yesno);

		tester.add(settings);

		JButton restoreDefaults = new JButton("restore defaults");
		restoreDefaults.setPreferredSize(new Dimension(150, 30));
		restoreDefaults.setBackground(Color.WHITE);
		restoreDefaults.setFocusPainted(false);
		restoreDefaults.setFont(new java.awt.Font("Orbitron", 0, 12));
		restoreDefaults.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(DEBUG) System.out.println("User wants to restore default settings");
				isColorBlind = false;
				BW.setSelected(true);
				no.setSelected(true);
				musicSliders.setValue(50);
				FXVolSlider.setValue(50);
			}
		});

		JButton ok = new JButton("Apply Changes");
		ok.setPreferredSize(new Dimension(150, 30));
		ok.setBackground(Color.WHITE);
		ok.setFocusPainted(false);
		ok.setFont(new java.awt.Font("Orbitron", 0, 12));
		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(yes.isSelected()) game.setColorBlind();
				else game.setNotColorBlind();
				if(BW.isSelected()) setColors(Color.BLACK, Color.WHITE);
				else setColors(Color.WHITE, Color.BLACK);
				setMusicLevel(musicSliders.getValue());
				game.setFXLevel(musicSliders.getValue());
				musicLevel = musicSliders.getValue();
				FXLevel = FXVolSlider.getValue();
				d1.dispose();
			}
		});

		d1.add(tester);
		d1.add(restoreDefaults);
		d1.add(ok);
		this.revalidate();
		this.repaint();
		
		d1.setVisible(true);
	}
	
	public void setMusicLevel(int value){ //0 <= value <= 100
		double gain = value/100.0;
		float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
		musicControl.setValue(dB);
	}
	
	
	
	public void setColors(Color font, Color background){
		fontColor = font;
		backgroundColor = background;
		pregame.setBackgroundColor(backgroundColor);
		pregame.setFontColor(fontColor);
		pregame.repaint();
		game.setBackgroundColor(backgroundColor);
		game.setFontColor(fontColor);
		game.repaint();
		register.setBackgroundColor(backgroundColor);
		register.setFontColor(fontColor);
		register.repaint();
		login.setBackgroundColor(backgroundColor);
		login.setFontColor(fontColor);
		login.repaint();
		initial.setBackgroundColor(backgroundColor);
		initial.setFontColor(fontColor);
		initial.repaint();
	}
	
	
	public Color getFontColor(){ return fontColor; }
	
	public Color getBackgroundColor(){ return backgroundColor; }
	
	public void send(String message){ 
		if(DEBUG) System.out.println("SENDING: " + message);
		server.send(message); 
	}
	
	public String receive() { 
		String message = server.receive(); 
		if(DEBUG) System.out.println("RECEIVING: " + message);
		return  message;
	}
	
	public static void main(String[] args){
		new Client() .setVisible(true);
	}
}
