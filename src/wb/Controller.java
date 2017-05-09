package wb;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller {

	private View v;
	private Model m;

	public Controller() {
		v = new View();
		//m = new Model();
		// this.makeModel(filePath)
	}

	public void makeModel(String filePath) {
		m = new Model(filePath);
	}

	/// this is pseudo java
	public void run() {
        while (true) {
            if (processEvent(e)) {
                v.render();
            }
        }
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
	private boolean processEvent(KeyEvent e) {

        int curr = e.getKeyCode();
        Player p = m.getPlayer();

        // possibly change to vector format

		switch (curr) {
		case KeyEvent.VK_KP_UP:
			return p.makeMove(0);

		case KeyEvent.VK_KP_RIGHT:
			return p.makeMove(1);

		case KeyEvent.VK_KP_DOWN:
			return p.makeMove(2);

		case KeyEvent.VK_KP_LEFT:
			return p.makeMove(3);
		}

		/* FIXME jashankj: why is this `false`?
            alexh -- attempts to update game state. If no update is made
            then return false indicates no need to re-render. See main control loop
            above.
        */
		return false;
	}
}
