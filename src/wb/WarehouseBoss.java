package wb;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * @brief The main entry point for the Warehouse Boss game
 * @date 2017-04-11
 */
public class WarehouseBoss {
	public static void main (String[] args) {
		
		try {
			 GraphicsEnvironment ge = 
			     GraphicsEnvironment.getLocalGraphicsEnvironment();
			 ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/Cardinal.ttf")));
		} catch (IOException|FontFormatException e) {
			//Handle exception
		}

		Controller c = new Controller("saved/save.xml");
		c.runGameLoop();
		//SokobanGenerator g = new SokobanGenerator(5,5);
	}
}
