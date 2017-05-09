package wb;

import java.awt.event.KeyEvent;

public class Controller {

	private View v;
	private Model m;

	public Controller(String path) {
		this.makeModel(path);
		v = new View(new WBListener(this),m.getBoard()); // this is not good for future.
		//m = new Model();
		//this.makeModel(filePath)
	}

	public Controller() {
		this.makeModel("level1.xml");
		v = new View(new WBListener(this),m.getBoard());
	}

	private void makeModel(String filePath) {
		m = new Model(filePath);
	}

	public void newGame() {
		// creates a new game after old one has finished.
		return;
	}

	/// this is pseudo java

	public void run() {
		while (true) {
			continue;
		}
       /* while (true) {
            if (processEvent(e)) {
                v.render();
            }
        }
        */
		/*
		while (true) {
			if (gameOver) { // check current games state
				break;
			}
			for (Event e : View) { // this is pseudo code
				// update game board.
				if (processEvent(e)) {
					v.render();
				}
				if (gameOver) {
					break;
				}
			}
		}
		*/
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
