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
 * a game menu
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 */
class GameMenu
extends JPanel {
	// FIXME(jashankj): this class name is quite bad
	// FIXME(jashankj): variable names
	private JButton playNow;
	private JButton loadGame;
	private JButton campaign;
	private JButton settings;
	private JButton exit;


	public GameMenu(Controller c) {
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
		return this.playNow;
	}

	public JButton getSettings() {
		return this.settings;
	}

	public JButton getCampaign() {
		return this.campaign;
	}

	public JButton getExit() {
		return this.exit;
	}

	public JButton getLoadGame() {
		return this.loadGame;
	}


	// NOTE methods below this line are private
	// ----------------------------------
	/**
	 * Helper function to make all the buttons required for the Menu
	 *
	 * @param c		The controller which acts as the button listener.
	 */
	private void makeButtons(Controller c) {
		playNow = makeButton("Play Now",c);
		loadGame = makeButton("Load Game", c);
		campaign = makeButton("Campaign",c);
		settings = makeButton("Settings",c);
		exit = makeButton("Exit",c);
		this.add(playNow);
		this.add(loadGame);
		this.add(campaign);
		this.add(settings);
		this.add(exit);
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
