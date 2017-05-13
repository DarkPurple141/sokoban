package wb;

import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.*;

public class Controller extends JFrame implements ActionListener {

	private GameView v;
	private Board b;
	private boolean running;
	private boolean paused = false;
	private int fps = 10;
   	private int frameCount = 0;
	private JButton startButton = new JButton("Start");
   	private JButton restartButton = new JButton("Restart");
   	private JButton pauseButton = new JButton("Pause");
	private static int SCREEN_WIDTH = 512;
    private static int SCREEN_HEIGHT = 512;
	private String levelPath;

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
		Container cp = getContentPane();
	    cp.setLayout(new BorderLayout());
	    JPanel p = new JPanel();
	    p.setLayout(new GridLayout(1,2));
	    p.add(startButton);
	    p.add(pauseButton);
	    p.add(restartButton);
	    cp.add(v, BorderLayout.CENTER);
	    cp.add(p, BorderLayout.SOUTH);
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
		double standard = 0.33;
		for(GamePiece curr : b.getPieces()) {
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
		boolean change = false;
        // possibly change to vector format
		switch (curr) {
			case KeyEvent.VK_UP:
				System.out.println("UP");
				change = b.doMove(0);
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("RIGHT");
				change = b.doMove(1);
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("DOWN");
				change = b.doMove(2);
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("LEFT");
				change = b.doMove(3);
				break;
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == startButton) {
        	running = !running;
			if (running) {
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
			startButton.setText("Start");
         	newGame();
			updateGameState();
			drawGame();
      	}
		this.requestFocusInWindow();
   	}
}
