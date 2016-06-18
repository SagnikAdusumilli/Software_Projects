import java.math.BigDecimal;

/**
 * holds the coordinates of a point
 * 
 * @author Sagnik
 *
 */
public class Point {
	// stores the nearest decimal to round
	private final int PLACES = 0;
	
	// co-ordinates
	double x, y;

	// Initialing the point
	public Point(double x, double y) {
		setX(x);
		setY(y);
	}

	// incase the point is not one
	public Point() {

	}

	// getters and setters
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = round(x, PLACES);
	}

	public void setY(double y) {
		this.y = round(y, PLACES);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	/**
	 * rounds a decimal number to the place secified
	 * @param number
	 * @param places
	 * @return
	 */
	private double round(double number, int places ){
		
		// stores the decimal
		BigDecimal bd = new BigDecimal(number);
		
		// round the decimal to the place specified
		bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
		
		// returing the double value
		return bd.doubleValue();
	}
}
