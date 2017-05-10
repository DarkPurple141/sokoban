package wb;

import java.awt.event.KeyEvent;

public class Controller extends JFrame implements ActionListener {

	private gameView v = new GameView();
	private Model m;
	private boolean running;
	private boolean paused = false;
	private int fps = 10;
   	private int frameCount = 0;
	private JButton startButton = new JButton("Start");
   	private JButton quitButton = new JButton("Quit");
   	private JButton pauseButton = new JButton("Pause");

	public Controller(String path) {
		this.makeModel(path);
		Controller();
	}

	public Controller() {
		super("Warehouse Boss V0.2");
		this.makeModel("level1.xml");
		Container cp = getContentPane();
	    cp.setLayout(new BorderLayout());
	    JPanel p = new JPanel();
	    p.setLayout(new GridLayout(1,2));
	    p.add(startButton);
	    p.add(pauseButton);
	    p.add(quitButton);
	    cp.add(gamePanel, BorderLayout.CENTER);
	    cp.add(p, BorderLayout.SOUTH);
		startButton.addActionListener(this);
      	quitButton.addActionListener(this);
      	pauseButton.addActionListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		v = new View(new WBListener(this), m.getBoard());
	}


	private void makeModel(String filePath) {
		m = new Model(filePath);
	}

	public void newGame() {
		// creates a new game after old one has finished.
		return;
	}


	/// this is pseudo java

	public void runGameLoop() {
		Thread loop = new Thread() {
	    	public void run() {
	        	gameLoop();
	     	}
	  	};
      	loop.start();
	}

	private void gameLoop() {

		while (running) {

			if (!paused) {
				// do stuff
				updateGameState();
				drawGame();
			}
		}
	}

	private void updateGameState() {
		// update animatables
		// move by standard length
		float standard = 0.2;
		for (GamePiece curr : m.getMovables()) {
			float newx = curr.getCoord().getX();
			float newy = curr.getCoord().getY();
			if (curr.getTargetX() > newx) {
				newx+=standard;
			} else if (curr.getTargetX() < newx) {
				newx-=standard;
			}

			if (curr.getTargetX() > newy) {
				newy+=standard;
			} else if (curr.getTargetX() < newy) {
				newy-=standard;
			}
		}
	}

	private void drawGame() {
		v.paintTiles();
	}


	/*
	 * What's the view said has just happened?
	 * Update model to new state.
	 * directions in clockwise form.
	 * 0, 1, 2, 3 corresponding UP, RIGHT, DOWN, LEFT
	 */
	public void processEvent(KeyEvent e) {

        int curr = e.getKeyCode();
		boolean change = false;
        // possibly change to vector format
		switch (curr) {

			case KeyEvent.VK_UP:
				System.out.println("UP");
				change = m.doMove(0);
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("RIGHT");
				change = m.doMove(1);
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("DOWN");
				change = m.doMove(2);
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("LEFT");
				change = m.doMove(3);
				break;
		}

		if (change) {
			System.out.print("REPAINT OCCURRING\n");
			v.paintTiles();
		}

		/* FIXME jashankj: why is this `false`?
            alexh -- attempts to update game state. If no update is made
            then return false indicates no need to re-render. See main control loop
            above.
        */
	}
}
