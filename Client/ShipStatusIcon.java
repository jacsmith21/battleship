import javax.swing.*;

public class ShipStatusIcon extends JLabel{
	String name;

	public ShipStatusIcon(String name){ this.name = name; }
	
	public String getName(){ return name; }
}
