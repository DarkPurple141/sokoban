package wb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.Collection;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The main game controller.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
public class Controller
extends JFrame
implements ActionListener, ComponentListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private static final int FPS = 30;
	private static final int SCREEN_HEIGHT = 512;
	private static final int SCREEN_WIDTH = 512;

	private ScoreParser scores;
	private GameView v;

	private JPanel gameButtons;
	private JPanel gameWindow;
	private JPanel panels;
	private JButton startButton = null;
	private JButton restartButton = null;
	private JButton skipButton = null;

	private String[] savedGames;
	private String currLevelPath;

	private Mode state;
	private Board b;
	private GameMenu m;
	private boolean running;
	private boolean gg;
	private boolean moving = false;
	private double moveIncrement = 0.2;
	private int gameNum;
	private int campaignNum;
	private int moves;
	private int campaignMoves;
	private String playerName;

	private Difficulty gameDifficulty;

	private Settings gameSettings;

	public Controller() {
		super();

		gameSettings = new Settings();

		this.gameDifficulty = Difficulty.MEDIUM;
		// Start a background generator thread as fast as we can.
		this.threadGen(0);

		this.scores = new ScoreParser();
		this.playerName = "admin";
		this.gameNum = 0;
		this.state = Mode.NORMAL;
		//this.populateSavedGames("saved");

		// FIXME(jashankj): expurgate view code
		super.setBackground(Color.BLACK);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(SCREEN_WIDTH-1, SCREEN_HEIGHT+50); // 511 by 511 works.
		super.setTitle("Warehouse Boss V0.3");

		this.panels = new JPanel();
		this.panels.setLayout(new CardLayout());
		this.m = new GameMenu(this);
		this.v = new GameView();

		this.gameWindow = new JPanel();
		this.gameWindow.setLayout(new BorderLayout());
		this.v.setLayout(new GridBagLayout());
		this.gameButtons = new JPanel();
		this.gameButtons.setLayout(new GridLayout(1,2));

		this.startButton = new JButton("Start");
		this.startButton.addActionListener(this);
		this.gameButtons.add(this.startButton);

		this.restartButton = new JButton("Restart");
		this.restartButton.addActionListener(this);
		this.gameButtons.add(this.restartButton);

		this.skipButton = new JButton("Skip");
		this.skipButton.addActionListener(this);
		this.gameButtons.add(this.skipButton);

		this.panels.add(this.m);
		this.gameWindow.add(this.v, BorderLayout.CENTER);
		this.gameWindow.add(this.gameButtons, BorderLayout.SOUTH);
		this.panels.add(this.gameWindow);

		// FIXME(jashank): get the type here right
		Container cp = super.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(this.panels, BorderLayout.CENTER);

		this.addComponentListener(this);
		this.addKeyListener(this);

		super.pack();
		super.setFocusable(true);
		super.setVisible(true);

		this.threadGen(1);
	}

	// Work in progress
	private void gameLayout() {
		this.makeModel(false);
		this.startButton.setText("Start");

		this.newGame();
		this.switchLayout();

		this.v.validate();

		super.revalidate();
		super.repaint();
	}

	private void switchLayout() {
		CardLayout layout = (CardLayout)(this.panels.getLayout());
		layout.next(panels);
	}

	public void newGame() {
		this.moves = 0;
		this.gg = false;

		// FIXME(jashankj): hey what? why don't we hand off a new board?
		this.v.resetBoard(this.b);
		this.v.hideLabel();

		// XXX(jashankj): should we launch the game thread?
		this.drawGame();
	}

	private void makeModel(boolean reset) {
		// FIXME(jashankj): split this apart
		if (reset) {
			b = FileIO.XML2Board(currLevelPath);
		} else {
			try {
				// FIXME(jashankj): use path building
				if (state == Mode.NORMAL) {
					currLevelPath = "levels/" + Integer.toString(gameNum);
				} else if (state == Mode.CAMPAIGN) {
					currLevelPath = "campaign/" + Integer.toString(campaignNum);
				}
				b = FileIO.XML2Board(currLevelPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// initially created to prevent more than one thread being instantiated-- AH
	// but could now be easily brought inline.
	public void runGameLoop() {
		// FIXME(jashankj): either move this or gameLoop inline.
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}

	private void threadGen(int id) {
		Thread loop = new Thread() {
			public void run() {
				SokobanGenerator.generateLevel(10, 10, id, gameDifficulty);
			}
		};
		loop.start();
	}

	private void gameLoop() {
		int delay = 1000 / FPS;

		// FIXME(jashankj): move into the gameLoop thread
		while (this.gameSettings.isRunning()) {
			// do stuff
			updateGameState();
			drawGame();
			if (b.isFinished()) {
				// FIXME(jashankj): is this a switch?
				if (gameSettings.getState() == Mode.CAMPAIGN) {
					campaignNum++;
				} 

				v.showLabel("<html>Congrats!<br>Moves: " +
							Integer.toString(moves)+"</html>");
				this.running = false;
				gameSettings.setGameOver(true);
				startButton.setText("Next");

				if (campaignNum > 9) {
					logCampaignScore();
					v.showLabel(scores.getScoreTable());
					/// HACKS LIE AHEAD
					try {
						Thread.sleep(3000); // 10fps
					} catch (InterruptedException e) {
						e.printStackTrace();
						// FIXME(jashankj): what's on this branch?
					}
					/// END HACKS
					switchLayout();
				}
			}

			try {
				Thread.sleep(delay); // 10fps
			} catch (InterruptedException e) {
				e.printStackTrace();
				// FIXME(jashankj): what's on this branch?
			}
		}

	}

	private void logCampaignScore() {
		this.campaignMoves += this.moves;
		this.gameSettings.updateScores(this.campaignMoves);
	}

	private void updateGameState() {
		// update animatables
		// move by standard length
		for (GamePiece p : b.gamePieceIterator()) {
			p.animFrame(moveIncrement);
		}
	}

	private void drawGame() {
		this.validate();
		this.repaint();
		/// calls paint in all child components
	}

	@Override public void
	keyPressed (KeyEvent e) {
		// If there's no board, bail.
		if (this.b == null) {
			return;
		}

		this.processEvent(e);
	}

	@Override public void
	keyReleased (KeyEvent e) {}

	@Override public void
	keyTyped (KeyEvent e) {}

	/*
	 * Update model to new state.
	 * directions in clockwise form.
	 * 0, 1, 2, 3 corresponding UP, RIGHT, DOWN, LEFT
	 */
	public void processEvent(KeyEvent e) {
		if (!running) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				v.hideLabel();
				switchLayout();
				gameSettings.setGameOver(false);
				return;
			}
		}
		int curr = e.getKeyCode();

		// possibly change to vector format
		this.moving = false;
		for (Player p : this.b.getPlayers()) {
			if (p.isMoving()) {
				this.moving = true;
			}
		}

		if (this.moving || !gameSettings.isRunning()) {
			return;
		}

		// FIXME(jashankj): doMove in direction
		switch (curr) {
		case KeyEvent.VK_UP:
			this.moves++;
			this.b.doMove(0);
			break;

		case KeyEvent.VK_RIGHT:
			this.moves++;
			this.b.doMove(1);
			break;

		case KeyEvent.VK_DOWN:
			this.moves++;
			this.b.doMove(2);
			break;

		case KeyEvent.VK_LEFT:
			this.moves++;
			this.b.doMove(3);
			break;

		case KeyEvent.VK_ENTER:
			System.out.println("SAVE");
			FileIO.saveGame(this.b, "saved/save");
			break;
		}

		if ('0' <= e.getKeyChar() && e.getKeyChar() <= '6') {
			this.b.debug(0, e.getKeyChar() - '0');
		}

		// FIXME(jashankj): yoda-code
		if ('u' == e.getKeyChar()) {
			this.b.undo();
		}
	}

	// FIXME(jashankj): slice apart!
	public void actionPerformed(ActionEvent e) {
		// FIXME(jashankj): compare-by-reference?
		// is that an issue? -- AH
		Object s = e.getSource();

		if (s == startButton) {
			gameSettings.setRunning(!gameSettings.isRunning());
			if (gameSettings.isRunning()) {
				if (gameSettings.getGameOver()) {
					campaignMoves += moves;
					if (gameSettings.getState() == Mode.NORMAL)
						FileIO.removeFile(currLevelPath);
					gameNum++;
					makeModel(false);
					threadGen(gameNum+1);
					startButton.setText("Start");
				}
				startButton.setText("Stop");
				newGame();
				runGameLoop();
			} else {
				startButton.setText("Start");
			}

		} else if (s == skipButton) {
			gameSettings.setRunning(false);
			if (gameSettings.getState() == Mode.NORMAL)
				FileIO.removeFile(currLevelPath);
			gameNum++;
			makeModel(false);
			threadGen(gameNum+1);
			newGame();
			startButton.setText("Start");

		} else if (s == restartButton) {
			gameSettings.setRunning(false);
			makeModel(true);
			newGame();
			startButton.setText("Start");

		} else if (s == m.getPlayNow()) {
			gameSettings.setState(Mode.NORMAL);
			gameLayout();

		} else if (s == m.getExit()) {
			System.exit(0);

		} else if (s == m.getSettings()) {
			processSettings();

		} else if (s == m.getCampaign()) {
			// pre-defined missions that get harder
			gameSettings.setState(Mode.CAMPAIGN);
			campaignNum = 0;
			campaignMoves = 0;
			gameLayout();
			//Make it start a campaign

		} else if (s == m.getLoadGame()) {
			gameSettings.setState(Mode.LOAD);
			String curr = (String)JOptionPane.showInputDialog(
				this,
				"Which game did you want to load?\n",
				"Load Game",
				JOptionPane.PLAIN_MESSAGE,
				null,
				savedGames,
				null);

			//If a string was returned, say so.
			if ((curr != null) && (curr.length() > 0)) {
				this.currLevelPath = "saved/" + curr;
				gameLayout();
			}
		}
		this.requestFocusInWindow();
	}

	private void processSettings() {
		requestGameDifficulty();
		requestGameSpeed();
		requestPlayerName();
		FileIO.saveSettings(gameSettings);
	}

	private void requestGameDifficulty() {
		String[] difficulty = {"Medium", "Easy", "Hard"};
		String curr = (String)JOptionPane.showInputDialog(
			this, "Select default difficulty:\n",
			"Difficulty", JOptionPane.PLAIN_MESSAGE,
			null, difficulty, null);

		if (curr == null) {
			return;
		}
		boolean needRegen = false;
		switch (curr) {
		case "Easy":
			if (gameSettings.getDifficulty() != Difficulty.EASY){
				needRegen = true;
			}
			gameSettings.setDifficulty(Difficulty.EASY);
			break;

		case "Medium":
			if (gameSettings.getDifficulty() != Difficulty.MEDIUM){
				needRegen = true;
			}
			gameSettings.setDifficulty(Difficulty.MEDIUM);
			break;

		case "Hard":
			if (gameSettings.getDifficulty() != Difficulty.HARD){
				needRegen = true;
			}
			gameSettings.setDifficulty(Difficulty.HARD);
			break;
		}

		if (needRegen){
			FileIO.deleteAllLevels();
			gameNum = 0;
			this.threadGen(0);
			this.threadGen(1);
		}
	}

	private void requestGameSpeed () {
		String[] g_speed = {"Medium", "Slow", "Fast"};
		String speed = (String)JOptionPane.showInputDialog(
			this, "Select game speed:\n",
			"Game Speed", JOptionPane.PLAIN_MESSAGE,
			null, g_speed, null);

		if (speed == null) {
			return;
		}

		switch (speed) {
		case "Slow":
			gameSettings.setMoveIncrement(0.1);
			break;

		case "Medium":
			gameSettings.setMoveIncrement(0.2);
			break;

		case "Fast":
			gameSettings.setMoveIncrement(0.6);
			break;
		}
	}

	private void requestPlayerName () {
		gameSettings.setPlayerName((String)JOptionPane.showInputDialog(
			this, "Enter your name:\n",
			"Config", JOptionPane.QUESTION_MESSAGE));
	}

	@Override public void
	componentResized (ComponentEvent ce) {
		Component evSource = (Component)ce.getSource();
		if (this != evSource) {
			return;
		}

		if (this.v == null) {
			return;
		}

		this.v.resizeSprites();
	}

	@Override public void
	componentMoved (ComponentEvent ce) {}

	@Override public void
	componentShown (ComponentEvent ce) {}

	@Override public void
	componentHidden (ComponentEvent ce) {}
}
