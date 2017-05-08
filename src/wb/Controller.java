package wb;

import java.awt.Event;

class Controller {


	private View v;
	private Model m;

	public Controller() {
		v = new View();
		m = new Model();
		// this.makeModel(filePath)
	}

	public void makeModel(String filePath) {
		m = new Model(filePath);
	}

	/// this is pseudo java
	public void run() {
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
	private boolean processEvent(Event e) {
		/*
		switch (e.getKeyCode()) {
		case KeyEvent.VK_KP_UP:
			return m.makeMove(0);

		case KeyEvent.VK_KP_RIGHT:
			return m.makeMove(1);

		case KeyEvent.VK_KP_DOWN:
			return m.makeMove(2);

		case KeyEvent.VK_KP_LEFT:
			return m.makeMove(3);
		}
		*/

		/* FIXME jashankj: why is this `false`?
            alexh -- attempts to update game state. If no update is made
            then return false indicates no need to re-render. See main control loop
            above.
        */
		return false;
	}
}
