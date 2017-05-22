package wb;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
		this.setLayout(new GridBagLayout());
		this.makeButtons(c);
		this.setFocusable(true);
		this.setVisible(true);
	}
	
	private void makeButtons(Controller c) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		PlayNow = makeButton("Play Now",c);
		Campaign = makeButton("Campaign",c);
		Settings = makeButton("Settings",c);
		Exit = makeButton("Exit",c);	
		this.add(PlayNow,gbc);
		this.add(Campaign,gbc);
		this.add(Settings,gbc);
		this.add(Exit,gbc);
	}
	
	private JButton makeButton(String name, Controller c) {
		JButton n;
		n = new JButton(name);
		Insets margin = new Insets(20,150,20,150);
		n.setMargin(margin);
		n.addActionListener(c);
		return n; 
	}
	
	public JButton getPlayNow() {
		return this.PlayNow;
	}
	

}
