
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * 
 * @author Sagnik this class creates the graph and draws the function
 */
public class Graph extends Canvas {

	// the scales of the axes
	private double xScale = 2;
	private double yScale = 2;

	// this it the unit length for the axes
	private double ySpace;
	private double xSpace;

	// keeps track of the x-interval
	private double maxY = 10;
	private double minY = -10;

	// keeps track of the y-interval
	private double maxX = 20;
	private double minX = -20;

	// the dimension of the graph
	private float height, width;

	// the dimensions of the lables
	private float labelHeight, labelWidht;

	// the padding of the graph
	private final int PaddingVertical = 20;
	private final int PaddingHorizontal = 20;

	// the color for the axis
	private final Color AXIS_COLOR = Color.rgb(0, 0, 0);

	// the color for the lines that occur every major unit
	private final Color MAJOR_LINE_COLOR = Color.web("#B3B3B3");

	// the color for the lines that occur every minor unit (I did not use this
	// in the graph)
	private final Color POINT_COLOR = Color.rgb(211, 133, 131);

	// the size of each point drawing in the graph
	private final int POINT_SIZE = 2;

	// gc is the grapichs component that can draw on the canvas
	private GraphicsContext gc;

	/**
	 * the graph is set to the dimentions passed on to it
	 * 
	 * @param width
	 * @param height
	 */
	public Graph(int width, int height) {

		// setting the dimensions of the canvas
		super(width, height);

		// setting the dimentions of the canvas
		this.height = height;
		this.width = width;

		// initialzing the graphics component
		gc = getGraphicsContext2D();

		// drawing the axes
		drawAxes();
	}

	/**
	 * draws the point given according the the axes scales
	 * 
	 * @param x
	 * @param y
	 */
	public void drawPoint(double x, double y) {

		// these are the actual point that will be drawn on the graph
		double pointX, pointY;

		// setting co-ordinate for x
		pointX = (x - minX) / xScale * xSpace + PaddingHorizontal;

		// setting co-ordinate for y
		if (maxY > 0) {
			pointY = (maxY - y) / yScale * ySpace + PaddingVertical;
		} else {
			pointY = (-maxY - y) / yScale * ySpace + PaddingVertical;
		}

		// setting the color
		gc.setFill(POINT_COLOR);

		// drawing the point
		gc.fillOval(pointX - POINT_SIZE / 2, pointY - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);

	}

	/**
	 * draws the axes
	 */
	public void drawAxes() {

		// creating the spacing for x and y lines
		ySpace = yScale * (height - 4 * PaddingVertical) / ((maxY - minY));
		xSpace = xScale * (width - 4 * PaddingHorizontal) / ((maxX - minX));
		
		// keeps track of the number of the yLines
		double yCounter = maxY;
		
		// drawing the yLines
		for (double y = PaddingVertical; y <= height - PaddingVertical; y += ySpace) {
			
			// setting the color for the line
			gc.setStroke(MAJOR_LINE_COLOR);
			
			// if the line is the y-axis set the color to axis color
			if (yCounter == 0) {
				gc.setStroke(AXIS_COLOR);
			}
			
			// draw the line
			gc.strokeLine(0, y, width, y);


			// setting label measurements
			labelWidht = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(String.valueOf(yCounter), gc.getFont());
			labelHeight = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont()).getLineHeight();

			// if y-axis exists between the mixX and maxX then put the label near the y-axis
			if (0 >= minX && 0 <= maxX) {
				
				// find the location of the y-axis
				double yAxis = ((-minX) / xScale) * xSpace + PaddingHorizontal;
				
				// drawing the y-axis
				gc.setStroke(AXIS_COLOR);
				gc.strokeLine(yAxis, y, yAxis, height);
				
				// setting the color and font for labeling
				gc.setFill(Color.BLACK);
				gc.setFont(new Font("Times", 12));
				
				// labeling the y-lines
				if (yCounter != 0) {
					gc.fillText(String.valueOf(yCounter), yAxis - labelWidht - 5, y + (labelHeight / 4));
				}

			} else {
				// if y-axis is not present then label it above the y-lines
				gc.setFill(Color.GREY);
				gc.fillText(String.valueOf(yCounter), 5, y + labelHeight / 4);
			}
			
			// decrement the number by the y-scale 
			yCounter -= yScale;

		}
		
		// keeps track of the number of the xLines
		double xCounter = minX;
		
		// drawing the xLines
		for (double x = PaddingHorizontal; x <= width - PaddingHorizontal; x += xSpace) {
			
			// setting the color for the line
			gc.setStroke(MAJOR_LINE_COLOR);
			
			// if the line is the x-axis set the color to axis color
			if (xCounter == 0) {
				gc.setStroke(AXIS_COLOR);
			}
			// draw the line
			gc.strokeLine(x, height, x, 0);

			// label measurements
			labelWidht = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(String.valueOf(xCounter), gc.getFont());
			labelHeight = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont()).getLineHeight();
			
			// if the x-axis is present between the range
			if (0 >= minY && 0 <= maxY) {
				
				// Calculating the location of the xAxis
				double xAxis = (double) (((maxY) / yScale) * ySpace) + PaddingVertical;
				
				// drawing the x-axis
				gc.setStroke(AXIS_COLOR);
				gc.strokeLine(x, xAxis, width, xAxis);
				
				// setting the color and font for labeling
				gc.setFill(Color.BLACK);
				gc.setFont(new Font("Times", 12));

				if (xCounter != 0) {
					// label the xLines near the x-axis
					gc.fillText(String.valueOf(xCounter), x - (labelWidht / 2), xAxis + labelHeight);
				} else {
					// if the x = 0, don't label it, it was already labled in the y-axis
					gc.fillText(String.valueOf(xCounter), x - (labelWidht) - 5, xAxis + labelHeight);
				}

			} else {
				// draw label above the xLines if there is no x-axis
				gc.setFill(Color.GREY);
				gc.fillText(String.valueOf(xCounter), x - labelWidht / 2, height - PaddingVertical / 2);
			}
			
			// increment the number by the y-scale 
			xCounter += xScale;

		}
	}
	
	/**
	 * clears the graph and draws the graph again
	 */
	public void clear() {
		gc.clearRect(0, 0, width, height);
		drawAxes();
	}
	
	// getters and setters that may be required
	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getxScale() {
		return xScale;
	}

	public void setxScale(double xScale) {
		this.xScale = xScale;
	}

	public double getyScale() {
		return yScale;
	}

	public void setyScale(double yScale) {
		this.yScale = yScale;
	}

}
