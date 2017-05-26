package wb;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * The main entry point for the Warehouse Boss game
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
public class WarehouseBoss {
	/**
	 * The main insance of hte program
	 * Crates the controller which starts the game
	 * @param args the input arguments - do nothing
	 */
	public static void main (String[] args) {
		try {
			GraphicsEnvironment ge =
				GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(
				Font.createFont(
					Font.TRUETYPE_FONT, new File("assets/visitor2.ttf")));
		} catch (IOException|FontFormatException e) {
			//Handle exception
		}

		Controller c = new Controller();
	}
}
