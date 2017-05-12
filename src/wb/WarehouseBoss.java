package wb;

/**
 * @brief The main entry point for the Warehouse Boss game
 * @date 2017-04-11
 */
public class WarehouseBoss {
	public static void main (String[] args) {
		Controller c = new Controller("levels/level3.xml");
		c.runGameLoop();
	}
}
