import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Leaderboards extends JPanel implements ActionListener{
	private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
	
	//Components
	private JPanel leaderboardsPanel; //Top level panel
	private Client client; //Parent
	private JPanel topThreePanel;
	private JPanel userScorePanel;
	private JLabel shipImage; 
	private JLabel battleshipTitle;
	private JLabel leaderboardsTitle;
	private JButton home;
	private JButton help;
	private JButton settings;
	private String username;
	private JLabel[][] labels;
	
	private final int CELL_WIDTH = 143;
	private final int CELL_HEIGHT = 40;

    public Leaderboards(Client client){
		this.client = client;
		
		//Init NETBEANS components
        initComponents();
		
		//better names
		leaderboardsPanel = jPanel1;
		shipImage = jLabel2;
		battleshipTitle = jLabel1;
		leaderboardsTitle = jLabel3;
		home = jButton5;
		help =  jButton2;
		settings = jButton3;
		topThreePanel = jPanel2;
		userScorePanel = jPanel3;
		
		this.addActionListeners();
		this.add(leaderboardsPanel);
    }
	
	public void setName(String username){ this.username = username; }
	
	public void initLeaderboards(){
		String userScore = client.receive();
		String topThree = client.receive();
		initTopThree(topThree);
		initUserScore(userScore);
		setBackgroundColor(client.getBackgroundColor());
		setFontColor(client.getFontColor());
	}
	
	public void initTopThree(String message){
		labels = new JLabel[5][5]; //5 columns, 5 rows
		topThreePanel.setLayout(new GridBagLayout());
		GridBagConstraints c;
		JLabel label;
		String[] all = message.split(",");
		String[] ranks = {"1","2","3"};
		String[] names = new String[3];
		String[] wins = new String[3];
		String[] loses = new String[3];
		String[] scores = new String[3];
		for(int i = 0; i < 3; i++) names[i] = all[i*4];
		for(int i = 0; i < 3; i++) wins[i] = all[i*4+1];
		for(int i = 0; i < 3; i++) loses[i] = all[i*4+2];
		for(int i = 0; i < 3; i++) scores[i] = all[i*4+3];
		addColumn("rank",ranks,0);
		addColumn("username",names,1);
		addColumn("wins",wins,2);
		addColumn("losses",loses,3);
		addColumn("scores",scores,4);
	}
	
	public void initUserScore(String message){
		userScorePanel.setLayout(new GridBagLayout());
		String[] data = message.split(",");
		
		GridBagConstraints c = new GridBagConstraints(); 
		
		//Adding username
		JLabel label = formatLabel(username);
		formatConstraints(c,1,0);
		userScorePanel.add(label,c);
		labels[1][4] = label;
		
		//Adding blank label for player rank
		label = formatLabel(" "); //Needs space
		formatConstraints(c,0,0);
		userScorePanel.add(label,c);
		labels[0][4] = new JLabel();
		
		//Adding user data
		for(int i = 0; i < 3; i++){
			label = formatLabel(data[i]);
			formatConstraints(c,i+2,0);
			userScorePanel.add(label,c);
			labels[i+2][4] = label;
		}
	}
	
	public void addColumn(String name, String[] data, int col){
		GridBagConstraints c = new GridBagConstraints(); 
		JLabel label = formatLabel(name);
		formatConstraints(c,col,0);
		topThreePanel.add(label,c);
		labels[col][0] = label;
		for(int i = 0; i < 3; i++){
			label = formatLabel(data[i]);
			formatConstraints(c,col,i+1);
			topThreePanel.add(label,c);
			labels[col][i+1] = label;
		}
	}
	
	public JLabel formatLabel(String text){
		JLabel label = new JLabel(text);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setFont( new Font("Orbitron", 0, 14) );
		return label;
	}
	
	public void formatConstraints(GridBagConstraints c, int x, int y){
		c.gridx = x;
		c.gridy = y;
		c.weightx = 1; //To avoid clumping
		c.weighty  = 1; //To avoid clumping
	}
	
	public void addActionListeners(){
		home.addActionListener(this);
		settings.addActionListener(this);
		help.addActionListener(this);
	}
	
	public void setFontColor(Color color){
		battleshipTitle.setForeground(color);
		leaderboardsTitle.setForeground(color);
		home.setForeground(color);
		help.setForeground(color);
		settings.setForeground(color);
		userScorePanel.setBorder(BorderFactory.createLineBorder(color));
		topThreePanel.setBorder(BorderFactory.createLineBorder(color));
		for(int i = 0; i < labels.length; i++){
			for(int j = 0; j < labels[0].length; j++){
				try{
					labels[i][j].setForeground(color);
				}catch(NullPointerException e){
					System.out.println("Not initialized: " + i + "," + j);
				}
			}
		}
	}
	
	public void setBackgroundColor(Color color){
		if(color == color.WHITE) shipImage.setIcon(client.blackShip);
		else shipImage.setIcon(client.whiteShip);
		this.setBackground(color);
		shipImage.setBackground(color);
		leaderboardsPanel.setBackground(color);
		userScorePanel.setBackground(color);
		topThreePanel.setBackground(color);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == home){
			client.displayHome();
		}else if(e.getSource() == help){
			client.displayHelp();
		}else if(e.getSource() == settings){
			client.displaySettings();
		}
	}
                        
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

		jPanel2.setMinimumSize(new Dimension(431,162));
		jPanel2.setPreferredSize(new Dimension(431,162));
		jPanel3.setMinimumSize(new Dimension(431,40));
		jPanel3.setPreferredSize(new Dimension(431,40));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1027, 611));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Orbitron", 0, 15)); // NOI18N
        jButton2.setText("help");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Orbitron", 0, 15)); // NOI18N
        jButton3.setText("settings");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);

        jLabel1.setFont(new java.awt.Font("Orbitron", 0, 24)); // NOI18N
        jLabel1.setText("Battleship");

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Orbitron", 0, 15)); // NOI18N
        jButton5.setText("home");
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton5.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButton5.setName(""); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setOpaque(true);

        jLabel3.setFont(new java.awt.Font("Orbitron", 0, 48)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Leaderboards");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 431, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(298, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5)
                        .addComponent(jButton2)
                        .addComponent(jButton3))
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }
}
