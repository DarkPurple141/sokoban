package wb;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * @brief The main entry point for the Warehouse Boss game
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 * @date 2017-04-11
 */
public class WarehouseBoss {
	public static void main (String[] args) {
		try {
			GraphicsEnvironment ge =
				GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(
				Font.createFont(
					Font.TRUETYPE_FONT, new File("assets/Cardinal.ttf")));
		} catch (IOException|FontFormatException e) {
			//Handle exception
		}

		Controller c = new Controller();
	}
}
