package wb;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Controller
extends JFrame
implements ActionListener {
	private static final double MOVE_INCREMENT = 0.1;

	private static final long serialVersionUID = 1L;
	private static int SCREEN_WIDTH = 512;
	private static int SCREEN_HEIGHT = 512;

	private String levelPath;
	private GameView v;
	private JPanel gameButtons;
	private JPanel gameWindow;
	private JPanel panels;
	private Board b;
	private Menu m;
	private boolean running;
	private boolean paused = false;
	private int fps = 30;
	private JButton startButton = null;
	private JButton restartButton = null;
	private JButton pauseButton = null;
	private boolean moving = false;

	public Controller(String path) {
		super("Warehouse Boss V0.3");
		this.levelPath = path;
		makeModel(path);
		constructorHelper();
	}

	public Controller() {
		super("Warehouse Boss V0.3");
		//TODO Generate level
		constructorHelper();
	}

	private void constructorHelper() {
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
		switchLayout();
	    v.resetBoard(b);
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
		makeModel(this.levelPath);
		v.resetBoard(b);
		v.hideLabel();
		paused = false;
		pauseButton.setText("Pause");
		drawGame();
	}


	private void makeModel(String filePath) {
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

	private void gameLoop() {
		int delay = 1000/fps;

		while (running) {
			if (!paused) {
				// do stuff
				updateGameState();
				drawGame();
				if(b.isFinished()){
					v.showLabel("Congrats!");
					this.running = false;
					startButton.setText("Start");
				}
			} try {
        		Thread.sleep(delay); // 10fps
    		} catch (InterruptedException e) {
    		}
		}
		
		// probs go to score menu or somethign?!
		switchLayout();
		
	}

	private void updateGameState() {
		// update animatables
		// move by standard length
		for(Iterator<GamePiece> p = b.gamePieceIterator(); p.hasNext();) {
			p.next().animFrame(MOVE_INCREMENT);
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
		if (!running || this.paused) {
			return;
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
      		this.gameLayout();
      	}
		this.requestFocusInWindow();
   	}

	public void resizeView() {
		if (v==null) return;
		v.resizeSprites();
	}
}
