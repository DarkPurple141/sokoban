package wb;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @brief a game menu
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @date May 2017
 */
class Menu
extends JPanel {
	// FIXME(jashankj): this class name is quite bad
	// FIXME(jashankj): this isn't serialisable
	// FIXME(jashankj): variable names
	private static final long serialVersionUID = 4427029878332103500L;
	private JButton PlayNow;
	private JButton LoadGame;
	private JButton Campaign;
	private JButton Settings;
	private JButton Exit;


	public Menu(Controller c) {
		super();
		this.setLayout(new GridLayout(0,1,10,10));
		this.setBorder(new EmptyBorder(80,140,20,140));
		JLabel title = new JLabel("WAREHOUSE BOSS",JLabel.CENTER);
		title.setFont(new Font("Cardinal",Font.BOLD,36));
		title.setForeground(Color.white);
		this.add(title);
		this.makeButtons(c);
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.setVisible(true);
	}

	public JButton getPlayNow() {
		return this.PlayNow;
	}

	public JButton getSettings() {
		return this.Settings;
	}

	public JButton getCampaign() {
		return this.Campaign;
	}

	public JButton getExit() {
		return this.Exit;
	}

	public JButton getLoadGame() {
		return this.LoadGame;
	}


	// NOTE methods below this line are private
	// ----------------------------------
	/**
	 * Helper function to make all the buttons required for the Menu
	 *
	 * @param c		The controller which acts as the button listener.
	 */
	private void makeButtons(Controller c) {
		PlayNow = makeButton("Play Now",c);
		LoadGame = makeButton("Load Game", c);
		Campaign = makeButton("Campaign",c);
		Settings = makeButton("Settings",c);
		Exit = makeButton("Exit",c);
		this.add(PlayNow);
		this.add(LoadGame);
		this.add(Campaign);
		this.add(Settings);
		this.add(Exit);
	}

	/**
	 * Helper function to make a new JButton for the Menu.
	 *
	 * @param name	labelName of the Button
	 * @param c		The main controller which deals with events on the button
	 * @return		The new button.
	 */
	private JButton makeButton(String name, Controller c) {
		JButton n;
		n = new JButton(name);
		Insets margin = new Insets(20,20,20,20);
		n.setMargin(margin);
		n.addActionListener(c);
		return n;
	}

}
