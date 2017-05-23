package wb;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Controller
extends JFrame
implements ActionListener {
	private static double MOVE_INCREMENT = 0.2;

	private static final long serialVersionUID = 1L;
	private static int SCREEN_WIDTH = 512;
	private static int SCREEN_HEIGHT = 512;

	private GameView v;
	private JPanel gameButtons;
	private JPanel gameWindow;
	private JPanel panels;
	private String[] savedGames;
	private List<String> campaignPath;
	private String currLevelPath;
	private Mode state;
	private Board b;
	private Menu m;
	private boolean running;
	private boolean paused = false;
	private boolean gg;
	private int fps = 30;
	private JButton startButton = null;
	private JButton restartButton = null;
	private JButton pauseButton = null;
	private boolean moving = false;
	private int gameNum;
	private int campaignNum = 0;

//	public Controller(String path) {
//		super("Warehouse Boss V0.3");
//		this.currLevelPath = path;
//		makeModel(path);
//		constructorHelper();
//	}

	public Controller() {
		super("Warehouse Boss V0.3");
		threadGen();
		threadGen();
		constructorHelper();
	}

	private void constructorHelper() {
		gameNum = 0;
		campaignNum = 0;
		this.state = Mode.NORMAL;
		this.populateSavedGames("saved");
		this.populateCampaignGames("campaign");
		this.setBackground(Color.BLACK);
		panels = new JPanel();
		panels.setLayout(new CardLayout());
		m = new Menu(this);
		v = new GameView();
		gameWindow = new JPanel();
		gameWindow.setLayout(new BorderLayout());
		v.setLayout(new GridBagLayout());
	    gameButtons = new JPanel();
	    gameButtons.setLayout(new GridLayout(1,2));
	    makeButtons();
		panels.add(m);
		gameWindow.add(v, BorderLayout.CENTER);
	    gameWindow.add(gameButtons, BorderLayout.SOUTH);
		panels.add(gameWindow);
		Container cp = getContentPane();
	    cp.setLayout(new BorderLayout());	    
	    cp.add(panels,BorderLayout.CENTER);
	    addComponentListener(new ResizeListener(this));
		addKeyListener(new WBListener(this));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(SCREEN_WIDTH-1,SCREEN_HEIGHT+50); // 511 by 511 works.
		this.setFocusable(true);
		this.pack();
		this.setVisible(true);
	}
	
	// Work in progress
	private void gameLayout() {
		newGame();
		switchLayout();
	    v.validate();
	    this.revalidate();
	    this.repaint();
	}
	
	private void switchLayout() {
		CardLayout layout = (CardLayout)(panels.getLayout());
        layout.next(panels);
	}
	
	private void makeButtons() {
		startButton = new JButton("Start");
		restartButton = new JButton("Restart");
		pauseButton = new JButton("Pause");
		startButton.addActionListener(this);
      	restartButton.addActionListener(this);
      	pauseButton.addActionListener(this);
      	gameButtons.add(startButton);
	    gameButtons.add(pauseButton);
	    gameButtons.add(restartButton);
	}

	public void newGame() {
		gg = false;
		try {
			makeModel();
		} catch (Exception e) {
			System.out.println("Could not make new game");
			return;
		}
		v.resetBoard(b);
		v.hideLabel();
		paused = false;
		pauseButton.setText("Pause");
		drawGame();
	}

	private void makeModel() throws FileNotFoundException
	{
		String filePath = null;
		if (state == Mode.NORMAL) {
			filePath = "levels/" + Integer.toString(gameNum);
		} else if (state == Mode.CAMPAIGN) {
			filePath = "campaigns/" + Integer.toString(campaignNum);
		} else if (state == Mode.LOAD) {
			filePath = this.currLevelPath;
		}
		b = FileIO.XML2Board(filePath);
	}

	public void runGameLoop() {
		Thread loop = new Thread() {
	    	public void run() {
	        	gameLoop();
	     	}
	  	};
      	loop.start();
	}

	private void threadGen() {
		Thread loop = new Thread() {
			public void run() {
				SokobanGenerator.generateLevel(10, 10);
			}
		};
		loop.start();
	}

	private void gameLoop() {
		int delay = 1000/fps;

		while (running) {
			if (!paused) {
				// do stuff
				updateGameState();
				drawGame();
				if(b.isFinished()) {
					if(state == Mode.CAMPAIGN) {
						campaignNum++;
						try {
							makeModel();
						} catch(Exception e) {
							v.showLabel("<html>Congrats!<br><br>"
									+ "Press Enter to Return to Main Menu</html>");
						}

					} else if(state == Mode.NORMAL) {
						gameNum++;
						threadGen();
						try {
							makeModel();
						} catch (Exception e) {
							System.out.println("Normal exception");
						}

					}
					this.running = false;
					gg = true;
					startButton.setText("Start");
				}
			} try {
        		Thread.sleep(delay); // 10fps
    		} catch (InterruptedException e) {
    		}
		}
				
	}
	
	private void populateSavedGames(String path) {
		File dir = new File(path);

	    Collection<String> files  = new ArrayList<String>();

	    if(dir.isDirectory()){
	        File[] listFiles = dir.listFiles();

	        for(File file : listFiles){
	            if(file.isFile()) {
	                files.add(file.getName());
	            }
	        }
	    }

	    savedGames = files.toArray(new String[]{});
	}
	
	private void populateCampaignGames(String path) {
		campaignPath = new ArrayList<String>();
	}

	private void updateGameState() {
		// update animatables
		// move by standard length
		for(GamePiece p : b.gamePieceIterator()) {
			p.animFrame(MOVE_INCREMENT);
		}
	}

	private void drawGame() {
		this.validate();
		this.repaint();
		/// calls paint in all child components
	}

	/*
	 * Update model to new state.
	 * directions in clockwise form.
	 * 0, 1, 2, 3 corresponding UP, RIGHT, DOWN, LEFT
	 */
	public void processEvent(KeyEvent e) {
		if (!running) {
			if (gg) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					v.hideLabel();
					switchLayout();
					gg = false;
					return;
				}
			} else if (paused) {
				return;
			}
		}
        int curr = e.getKeyCode();
        // possibly change to vector format
		moving = false;
		for(Player p : b.getPlayers()) {
        	if(p.isMoving())
        		moving = true;
        }
        if(moving)
        	return;
		switch (curr) {
			case KeyEvent.VK_UP:
				System.out.println("UP");
				b.doMove(0);
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("RIGHT");
				b.doMove(1);
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("DOWN");
				b.doMove(2);
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("LEFT");
				b.doMove(3);
				break;
			case KeyEvent.VK_ENTER:
				System.out.println("SAVE");
				FileIO.saveGame(b, "saved/save");
				break;

		}
		if('0' <= e.getKeyChar() && e.getKeyChar() <= '6') {
			b.debug(0, e.getKeyChar() - '0');
		}

		if('u' == e.getKeyChar()){
			b.undo();
		}

	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == startButton) {
        	running = !running;
			if (running) {
				startButton.setText("Stop");
				newGame();
            	runGameLoop();
			} else {
            	startButton.setText("Start");
         	}
      	} else if (s == pauseButton) {
        	paused = !paused;
        	if (paused) {
            	pauseButton.setText("Unpause");
         	} else {
            	pauseButton.setText("Pause");
         	}
      	} else if (s == restartButton) {
			running = false;
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
			//Make it start a campaign
      	} else if (s==m.getLoadGame()) {
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
	
	private void processSettings() {
		String[] difficulty = {"Easy", "Medium", "Hard"};
  		String[] g_speed = {"Slow", "Medium", "Fast"};
  		String curr = (String)JOptionPane.showInputDialog(
                    this,
                    "Select default difficulty:\n",
                    "Difficulty",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    difficulty,
            		null);
  		String speed = (String)JOptionPane.showInputDialog(
                this,
                "Select game speed:\n",
                "Game Speed",
                JOptionPane.PLAIN_MESSAGE,
                null,
                g_speed,
        		null);
  		if (curr == null) {	}
  		else if (curr.equals("Easy"))
  			return;
  		else if (curr.equals("Medium"))
  			return;
  		else if (curr.equals("Hard"))
  			return;
  		
  		if (speed == null) {} 
  		else if (curr.equals("Slow"))
  			this.MOVE_INCREMENT = 0.1;
  		else if (curr.equals("Medium"))
  			this.MOVE_INCREMENT = 0.15;
  		else if (curr.equals("Fast"))
  			this.MOVE_INCREMENT = 0.2;
	}

	public void resizeView() {
		if (v==null) return;
		v.resizeSprites();
	}
}
