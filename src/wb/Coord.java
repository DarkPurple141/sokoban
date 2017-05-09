package wb;

//Change name to vector? Use vector math for directions?
public class Coord
{
	private int x;
	private int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Coord getNeighbour(int direction) {
		int initialx = x;
		int initialy = y;

		switch (direction) {
			case 0:
				initialy--;
				break;
			case 1:
				initialx++;
				break;
			case 2:
				initialy++;
				break;
			case 3:
				initialx--;
				break;
		}

		return new Coord(initialx, initialy);
	}

	public Coord addCoord(Coord vector) {
		return new Coord(x + vector.getX(), y + vector.getY());
	}
}
