/**
 * @author Ori Engelberg <turht50@gmail.com>
 * @version 1.1
 * @since 2016-03-21 */
public class Point {

	private int x;
	private int y;

	/**
	 * Contractor - Give the point a x and y values.
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculate the distance between 2 points.
	 */
	public double distance(Point other) {
		if (other == null) {
			return 0;
		}
		double x1, x2, y1, y2;
		x1 = this.getX();
		x2 = other.getX();
		y1 = this.getY();
		y2 = other.getY();
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

	/**
	 * Initialize the point x value.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Initialize the point y value.
	 */
	public int getY() {
		return this.y;
	}

    /**
	 * create point from x & coordinates 
	 */
	public static Point deserialize(String point_srl){
		String[] parts = point_srl.split(",");
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		return new Point(x,y);
	}

	@Override
    /**
	 * check if two point are equal
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Point point = (Point) o;

		if (x != point.x) return false;
		return y == point.y;
	}
}
