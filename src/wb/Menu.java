package wb;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel {
	
	private JButton PlayNow;
	private JButton Campaign;
	private JButton Settings;
	private JButton Exit;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4427029878332103500L;
	
	public Menu(Controller c) {
		super();
		this.setLayout(new GridLayout(4,1));
		this.makeButtons(c);
		this.setFocusable(true);
		this.setVisible(true);
	}
	
	private void makeButtons(Controller c) {
		PlayNow = makeButton("Play Now",c);
		Campaign = makeButton("Campaign",c);
		Settings = makeButton("Settings",c);
		Exit = makeButton("Exit",c);	
		this.add(PlayNow);
		this.add(Campaign);
		this.add(Settings);
		this.add(Exit);
	}
	
	private JButton makeButton(String name, Controller c) {
		JButton n;
		n = new JButton(name);
		n.addActionListener(c);
		return n; 
	}
	
	public JButton getPlayNow() {
		return this.PlayNow;
	}
	

}
