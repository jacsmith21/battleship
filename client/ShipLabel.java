import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShipLabel extends JLabel{
	private int x;
	private int y;
	private int length;
	private String name; 
	private char orientation;
	private boolean drag;
	private boolean selected;
	private GridPanel panel;

	private final boolean DEBUG = false;
	
	public ShipLabel(int x, int y, int length, String name, char orientation, GridPanel panel){
		this.x = x;
		this.y = y;
		this.length = length;
		this.name = name;
		this.orientation = orientation;
		this.drag = false;
		this.selected = false;
		
		//super calls
		addMouseMotionListener(panel);
		addMouseListener(panel);
		setBorder(BorderFactory.createLineBorder(panel.WATER_BORDER));
		setBackground(panel.SHIP);
		setOpaque(true);
	}
	
	public int getXCell(){ return x; }
	
	public int getYCell(){ return y; }
	
	public int getLength(){ return length; }
	
	public String getName(){ return name; }
	
	public boolean isDrag(){ return drag; }
	
	public boolean isSelected(){ return selected; }
	
	public char getOrientation(){ return orientation; }
	
	public void setXCell(int x){ this.x = x; }
	
	public void setYCell(int y){ this.y = y; }
	
	public void setOrientation(char orientation){ this.orientation = orientation; }
	
	public void setDrag(boolean drag){ this.drag = drag; }
	
	public void setSelected(boolean selected){ this.selected = selected; }

}
