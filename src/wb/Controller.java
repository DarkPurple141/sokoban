package wb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Controller
extends JFrame
implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int SCREEN_WIDTH = 512;
	private static int SCREEN_HEIGHT = 512;

	private String levelPath;
	private GameView v;
	private Board b;
	private boolean running;
	private boolean paused = false;
	private int fps = 30;
   	private int frameCount = 0;
	private JButton startButton = null;
	private JButton restartButton = null;
	private JButton pauseButton = null;

	public Controller(String path) {
		super("Warehouse Boss V0.2");
		this.levelPath = path;
		makeModel(path);
		constructorHelper();
	}

	public Controller() {
		super("Warehouse Boss V0.2");
		//TODO Generate level
		constructorHelper();
	}

	private void constructorHelper() {
		v = new GameView(b);
		v.setLayout(new GridBagLayout());
		Container cp = getContentPane();
	    cp.setLayout(new BorderLayout());
	    JPanel p = new JPanel();
	    p.setLayout(new GridLayout(1,2));
		startButton = new JButton("Start");
		restartButton = new JButton("Restart");
		pauseButton = new JButton("Pause");
	    p.add(startButton);
	    p.add(pauseButton);
	    p.add(restartButton);
	    cp.add(v, BorderLayout.CENTER);
	    cp.add(p, BorderLayout.SOUTH);
	    addComponentListener(new ResizeListener(this));
		addKeyListener(new WBListener(this));
		startButton.addActionListener(this);
      	restartButton.addActionListener(this);
      	pauseButton.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(SCREEN_WIDTH-1,SCREEN_HEIGHT+50); // 511 by 511 works.
		this.setFocusable(true);
		this.setVisible(true);
	}

	public void newGame() {
		makeModel(this.levelPath);
		v.resetBoard(b);
		v.hideLabel();
	}


	private void makeModel(String filePath) {
		b = new Board(filePath);
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
			}try {
        		Thread.sleep(delay); // 10fps
    		} catch (InterruptedException e) {
    		}
			frameCount++;
		}
	}

	private void updateGameState() {
		// update animatables
		// move by standard length
		double standard = 0.2;
		for(GamePiece curr : b.getCrates()) {
			curr.animFrame(standard);
		}
		for(GamePiece curr : b.getPlayers()) {
			curr.animFrame(standard);
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
		if (!this.running || this.paused) {
			return;
		}
        int curr = e.getKeyCode();
        // possibly change to vector format
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
				b.saveGame("saved/save");

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
				newGame();
            	startButton.setText("Stop");
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
			this.running = false;
         	newGame();
			this.running = true;
			runGameLoop();
      	}
		this.requestFocusInWindow();
   	}

	public void resizeView() {
		v.resizeSprites();		
	}
}
