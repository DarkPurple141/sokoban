package wb;

/**
 * The direction of movement
 *
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
enum Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT;

	/**
	 *
	 * Converts an integer into a direction
	 *
	 * @return The direction enum represented by the int
	 * @param input : The integer to convert to a Direction
	 */
	public static Direction int2Dir(int input) {
		switch (input % 4) {
		case 0:
			return UP;

		case 1:
			return RIGHT;

		case 2:
			return DOWN;

		case 3:
			return LEFT;
		}

		return null;
	}


	/**
	 *
	 * Converts a direction into an integer
	 *
	 * @return The int encoding of a direction
	 * @param input : The direction to convert to an int
	 */
	public static int dir2Int(Direction input) {
		switch (input) {
		case UP:
			return 0;

		case RIGHT:
			return 1;

		case DOWN:
			return 2;

		case LEFT:
			return 3;
		}

		return -1;
	}


	/**
	 *
	 * Reverses a direction
	 *
	 * @return The direction that is the input reversed
	 * @param input : The direction to be reversed
	 */
	public static Direction reverseDir(Direction input) {
		switch (input) {
		case UP:
			return DOWN;

		case RIGHT:
			return LEFT;

		case DOWN:
			return UP;

		case LEFT:
			return RIGHT;
		}

		return null;
	}
}
