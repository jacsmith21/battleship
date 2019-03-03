import javax.swing.*;

public class ShipStatusIcon extends JLabel{
	private String name;
	private boolean sunk; 

	public ShipStatusIcon(String name){ 
		this.name = name;
		sunk = false; 
	}
	
	public String getName(){ return name; }
	
	public void setSunk(){ sunk = true; }
	
	public boolean isSunk(){ return sunk; }
}
