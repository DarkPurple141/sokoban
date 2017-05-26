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

		this.add(this.playNow = makeButton("Play Now", c));
		this.add(this.loadGame = makeButton("Load Game", c));
		this.add(this.campaign = makeButton("Campaign", c));
		this.add(this.settings = makeButton("Settings", c));
		this.add(this.exit = makeButton("Exit", c));

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
	 * Helper function to make a new JButton for the Menu.
	 *
	 * @param name	labelName of the Button
	 * @param c		The main controller which deals with events on the button
	 * @return		The new button.
	 */
	private JButton makeButton(String name, Controller c) {
		JButton n = new JButton(name);
		Insets margin = new Insets(20,20,20,20);
		n.setMargin(margin);
		n.addActionListener(c);
		return n;
	}
}
