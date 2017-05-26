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
class Controller
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

	/**
	 * Initialises the controller
	 * Starts the view
	 * Begins generating the procedural levels in the background
	 */
	public Controller() {
		super();

		this.gameDifficulty = Difficulty.MEDIUM;
		// Start a background generator thread as fast as we can.
		this.threadGen(0);

		this.scores = new ScoreParser();
		this.playerName = "admin";
		this.gameNum = 0;
		this.state = Mode.NORMAL;
		this.populateSavedGames("saved");

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

	/**
	 * Initialises the game window
	 */
	private void gameLayout() {
		this.makeModel(false);
		this.startButton.setText("Start");

		this.newGame();
		this.switchLayout();

		this.v.validate();

		super.revalidate();
		super.repaint();
	}

	/**
	 * Changes the main game window
	 */
	private void switchLayout() {
		CardLayout layout = (CardLayout)(this.panels.getLayout());
		layout.next(panels);
	}

	/**
	 * Resets all game variables
	 */
	public void newGame() {
		this.moves = 0;
		this.gg = false;

		// FIXME(jashankj): hey what? why don't we hand off a new board?
		this.v.resetBoard(this.b);
		this.v.hideLabel();

		this.drawGame();
	}

	/**
	 * Loads the board from the level file path
	 * @param reset if true reloads from the current path. If false loads the next level in order depending on the game type
	 */
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

	/**
	 * Starts the game loop
	 */
	public void runGameLoop() {
		// FIXME(jashankj): either move this or gameLoop inline.
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}

	/**
	 * Generates a new level in the background and saves to a file
	 * @param id the name of the file to save to in the levels folder
	 */
	private void threadGen(int id) {
		Thread loop = new Thread() {
			public void run() {
				SokobanGenerator.generateLevel(10, 10, id, gameDifficulty);
			}
		};
		loop.start();
	}

	/**
	 * The main game loop which governs the IO and rendering
	 */
	private void gameLoop() {
		int delay = 1000 / FPS;

		// FIXME(jashankj): move into the gameLoop thread
		while (running) {
			// do stuff
			updateGameState();
			drawGame();
			if (b.isFinished()) {
				// FIXME(jashankj): is this a switch?
				if (state == Mode.CAMPAIGN) {
					campaignNum++;
				} else if (state == Mode.NORMAL) {
					// FIXME: what's on this branch?
				}

				v.showLabel("<html>Congrats!<br>Moves: " +
							Integer.toString(moves)+"</html>");
				this.running = false;
				gg = true;
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

	/**
	 * Adds the move score to the high-scores board
	 */
	private void logCampaignScore() {
		this.campaignMoves += this.moves;
		this.scores.updateScores(this.playerName, this.campaignMoves);
	}

	/**
	 * Loads all the saved game strings into an array
	 * @param path the path to look in
	 */
	private void populateSavedGames(String path) {
		File dir = new File(path);

		Collection<String> files  = new ArrayList<String>();

		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();

			for (File file : listFiles) {
				if (file.isFile()) {
					files.add(file.getName());
				}
			}
		}

		this.savedGames = files.toArray(new String[]{});
	}

	/**
	 * Animates all the pieces which are currently in motion - changing their apparent position
	 */
	private void updateGameState() {
		// update animatables
		// move by standard length
		for (GamePiece p : b.gamePieces()) {
			p.animFrame(moveIncrement);
		}
	}

	/**
	 * Calls the view rendering components
	 */
	private void drawGame() {
		this.validate();
		this.repaint();
		/// calls paint in all child components
	}

	/**
	 * Called if a key is pressed without a board
	 * @param e the Keyevent object to hold information about the key pressed
	 */
	@Override public void
	keyPressed (KeyEvent e) {
		// If there's no board, bail.
		if (this.b == null) {
			return;
		}

		this.processEvent(e);
	}

	/**
	 * Called if a key is released without a board
	 * @param e the Keyevent object to hold information about the key pressed
	 */
	@Override public void
	keyReleased (KeyEvent e) {}

	/**
	 * Called if a key is typed without a board
	 * @param e the Keyevent object to hold information about the key pressed
	 */
	@Override public void
	keyTyped (KeyEvent e) {}

	/**
	 * The main handler for keys pressed
	 * @param e the Keyevent object to hold information about the key pressed
	 */
	public void processEvent(KeyEvent e) {
		if (!running) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				v.hideLabel();
				switchLayout();
				gg = false;
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

		if (this.moving || !this.running) {
			return;
		}

		// FIXME(jashankj): doMove in direction
		switch (curr) {
		case KeyEvent.VK_UP:
			this.moves++;
			this.b.doMove(Direction.UP);
			break;

		case KeyEvent.VK_RIGHT:
			this.moves++;
			this.b.doMove(Direction.RIGHT);
			break;

		case KeyEvent.VK_DOWN:
			this.moves++;
			this.b.doMove(Direction.DOWN);
			break;

		case KeyEvent.VK_LEFT:
			this.moves++;
			this.b.doMove(Direction.LEFT);
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

	/**
	 * The main action handler for GUI buttons pressed
	 * @param e the Keyevent object to hold information about the button pressed
	 */
	// FIXME(jashankj): slice apart!
	public void actionPerformed(ActionEvent e) {
		// FIXME(jashankj): compare-by-reference?
		// is that an issue? -- AH
		Object s = e.getSource();

		if (s == startButton) {
			running = !running;
			if (running) {
				if (gg) {
					campaignMoves += moves;
					if (state == Mode.NORMAL)
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
			running = false;
			if (state == Mode.NORMAL)
				FileIO.removeFile(currLevelPath);
			gameNum++;
			makeModel(false);
			threadGen(gameNum+1);
			newGame();
			startButton.setText("Start");

		} else if (s == restartButton) {
			running = false;
			makeModel(true);
			newGame();
			startButton.setText("Start");

		} else if (s == m.getPlayNow()) {
			state = Mode.NORMAL;
			gameLayout();

		} else if (s == m.getExit()) {
			System.exit(0);

		} else if (s == m.getSettings()) {
			processSettings();

		} else if (s == m.getCampaign()) {
			// pre-defined missions that get harder
			state = Mode.CAMPAIGN;
			campaignNum = 0;
			campaignMoves = 0;
			gameLayout();
			//Make it start a campaign

		} else if (s == m.getLoadGame()) {
			state = Mode.LOAD;
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

	/**
	 * Handles the collection of new settings
	 */
	private void processSettings() {
		requestGameDifficulty();
		requestGameSpeed();
		requestPlayerName();
	}

	/**
	 * Requests the difficulty setting
	 * Uses user input into a dropdown menu
	 */
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
			if (gameDifficulty != Difficulty.EASY){
				needRegen = true;
			}
			this.gameDifficulty = Difficulty.EASY;
			break;

		case "Medium":
			if (gameDifficulty != Difficulty.MEDIUM){
				needRegen = true;
			}
			this.gameDifficulty = Difficulty.MEDIUM;
			break;

		case "Hard":
			if (gameDifficulty != Difficulty.HARD){
				needRegen = true;
			}
			this.gameDifficulty = Difficulty.HARD;
			break;
		}

		if (needRegen){
			FileIO.deleteAllLevels();
			gameNum = 0;
			this.threadGen(0);
			this.threadGen(1);
		}
	}

	/**
	 * Requests the game speed setting
	 * Uses user input into a dropdown menu
	 */
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
			this.moveIncrement = 0.1;
			break;

		case "Medium":
			this.moveIncrement = 0.2;
			break;

		case "Fast":
			this.moveIncrement = 0.6;
			break;
		}
	}

	/**
	 * Requests the player name
	 * Uses user input into an input box
	 * used for the high-scores list
	 */
	private void requestPlayerName () {
		this.playerName = (String)JOptionPane.showInputDialog(
			this, "Enter your name:\n",
			"Config", JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * An event to handle the window being resized
	 * Redraws all tiles and pieces to fit the new dimensions
	 * @param ce the ComponentEvent which holds information about the resize
	 */
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
