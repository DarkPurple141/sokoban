package wb;

/**
 * this enum is a cardinal sin.
 *
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
enum Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT;

	public static Direction int2Dir(int input) {
		switch(input%4) {
			case 0: return UP;
			case 1: return RIGHT;
			case 2: return DOWN;
			case 3: return LEFT;
		}
		return null;
	}

	public static int dir2Int(Direction input) {
		switch (input) {
			case UP: return 0;
			case RIGHT: return 1;
			case DOWN: return 2;
			case LEFT: return 3;
		}
		return -1;
	}

	public static Direction reverseDir(Direction input) {
		switch (input) {
			case UP: return DOWN;
			case RIGHT: return LEFT;
			case DOWN: return UP;
			case LEFT: return RIGHT;
		}
		return null;
	}
}
