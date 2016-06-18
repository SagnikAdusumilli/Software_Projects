package com.GuI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.sun.javafx.font.Metrics;

import javafx.scene.transform.Affine;
/**
 * 
 * @author Sagnik
 * Draws a line chart based on the data points given to it
 */
public class LineChart extends JPanel {
	
	// setting the padding
	private int paddingHorizontal = 80;
	private int paddingVertical = 50;
	
	// setting the size of the data point
	private int pointSizeX = 10;
	private int pointSizeY = 10;
	
	// stores the location of the Y-axis
	private double originY;
	
	// Dimensions of the chart
	private int width, height;
	
	// various colors for the elements present in the graph
	private Color lineColor = new Color(44, 102, 230, 180);
	private Color gridColor = new Color(200, 200, 200, 200);
	private Color labelColor = Color.DARK_GRAY;
	
	// stores the data points
	private ArrayList<Point> points;

	// setting the length of the major marker 
	private int majorHatchLength = 10;
	// setting the length of the minor marker 
	private int minorHatchLength = 5;
	
	// setting the major marker interval
	private int majorHatchInterval = 2;
	// setting the minor interval
	private double minorHatchInterval = 0.5;
	
	// scales for the axes
	private double yScale;
	private double xScale;

	// length of each axis
	private int yAxisLength;
	private int xAxisLength;
	
	// stores the various line styles of the graph
	private BasicStroke[] strokes;

	// various titles in the graph
	private String graphTitle;
	private String XAxisTitle;
	private String YAxisTitle;
	
	// shows this message when there is no data
	private String errorMessage = "No Data Available";
	
	/**
	 * Initializes the height and width and makes the graph visible
	 * @param width
	 * @param height
	 */
	public LineChart(int width, int height) {
		setOpaque(true);
		setSize(new Dimension(width, height));

		this.width = width;
		this.height = height;

		points = new ArrayList<>();
	}
	
	/**
	 * adds data point to the list of points
	 * @param x
	 * @param y
	 */
	public void addData(Number x, Number y) {
		points.add(new Point(x, y));
	}
	
	// draws the chart
	public void drawChart() {
		// setting the length of the axes
		yAxisLength = height - (paddingVertical * 2);
		xAxisLength = width - (paddingHorizontal * 2);

//		System.out.println("XAxis length: " + xAxisLength + " yAxis length: " + yAxisLength);
		
		// setting the scales if there are data points
		if (points.size() > 0) {
			// setting length of one unit
			if (findMinHeight().intValue() < 0) {
				yScale = (yAxisLength) / (findMaxHeight().doubleValue() - findMinHeight().doubleValue());
				originY = (findMaxHeight().doubleValue() * (yScale)) + paddingVertical;
			} else {
				yScale = (yAxisLength) / (findMaxHeight().doubleValue());
				originY = height - paddingVertical;
			}
			// setting the  x-scale
			xScale = xAxisLength / (points.size());
			
			// creating the strokes for the various lines
			setStrokes();
		}
		// adjusting the marker intevals according to the range of data
		if(findMaxHeight().doubleValue()>50){
			majorHatchInterval = 8;
			minorHatchInterval = 4.0;
		}else{
			majorHatchInterval = 2;
			minorHatchInterval = 0.5;
		}
		repaint();
	}
	/**
	 * returns the max value of all the data points
	 * @return
	 */
	public Number findMaxHeight() {
		Number maxHeight = 0;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getY().doubleValue() > maxHeight.doubleValue() || i == 0) {
				maxHeight = points.get(i).getY();
			}
		}
		return maxHeight = maxHeight.doubleValue() + 1;
	}
	
	/**
	 * returns the min value of all the data points
	 * @return
	 */
	public Number findMinHeight() {
		Number minHeight = 0;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getY().doubleValue() < minHeight.doubleValue() || i == 0) {
				minHeight = points.get(i).getY();
			}
		}
		return minHeight;
	}
	/**
	 * creates to strokes for the various line styles in the graph
	 */
	private void setStrokes() {
		strokes = new BasicStroke[4];
		float[] dash = { 2f };
		strokes[0] = new BasicStroke(1f);
		strokes[1] = new BasicStroke(3f); 
		strokes[2] = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f); // dashed
																												// lines
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		FontMetrics metrics = g2.getFontMetrics();

		// filling the background area
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);

		g2.setColor(Color.BLACK);

		// drawing title
		if (graphTitle != null) {
			g2.setFont(new Font(null, Font.PLAIN, 16));
			g2.drawString(graphTitle,
					(int) ((xAxisLength + 2 * paddingHorizontal) / 2 - (metrics.stringWidth(graphTitle) / 2)), 40);
		}

		// drawing the y-axisTitle
		if (YAxisTitle != null) {
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.rotate(-Math.PI / 2);
			
			// Rotating the text 
			Font font = new Font(null, Font.PLAIN, 16);
			Font rotatedFont = font.deriveFont(affineTransform);
			g2.setFont(rotatedFont);
			
			// drawing the text
			g2.drawString(YAxisTitle, 30,
					(int) ((height - (paddingVertical)) / 2 + metrics.stringWidth(YAxisTitle) / 2));

		}
		
		// drawing the x-axisTitle
		if (XAxisTitle != null) {
			g2.setFont(new Font(null, Font.PLAIN, 16));
			g2.drawString(XAxisTitle, (width) / 2 - (metrics.stringWidth(XAxisTitle) / 2),
					height - paddingVertical + metrics.getHeight() + 15);
		}
		
		// setting font for the markers
		g2.setFont(new Font(null, Font.PLAIN, 10));
		
		// drawing the y-axis
		g2.drawLine(paddingHorizontal, paddingVertical, paddingHorizontal, height - paddingVertical);
		// drawing the y=0
		g2.drawLine(paddingHorizontal, (int) originY, width - paddingHorizontal, (int) originY);
		// drawing the x-axis
		g2.drawLine(paddingHorizontal, height - paddingVertical, width - paddingHorizontal,
				height - paddingVertical);
		
		if (points.size() > 0) {


			// creating y-Marks
			for (double i = 0; i < yAxisLength / yScale; i += 0.5) {
				
				// translating the data point according the scale
				double point = paddingVertical + (yScale * i);
				
				// setting the label to display first to decimals
				String yLabel = String.format("%.2f", (originY / yScale) - (point / yScale));
				
				// getting the measurements for the label font
				metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				int labelHeight = metrics.getHeight();

				g2.setColor(Color.BLACK);
				g2.setStroke(strokes[0]); // stroke[0] is the small back line
				
				if (i % majorHatchInterval == 0) {
					// drawing major hatches
					g2.drawLine(paddingHorizontal, (int) point, paddingHorizontal + majorHatchLength, (int) point);
				} else if (i % minorHatchInterval == 0.0) {
					// drawing minor hatches
					g2.drawLine(paddingHorizontal, (int) point, paddingHorizontal + minorHatchLength, (int) point);
				}
				
				if (i % majorHatchInterval == 0.0) {
					// setting color grid lines for major intervals
					g2.setColor(Color.BLACK);
					
				} else if (i % minorHatchInterval == 0.0) {
					// setting color for grid lines for minor intervals
					g2.setColor(gridColor);
				}
				g2.setStroke(strokes[2]);
				if (i % majorHatchInterval == 0.0) {
					// drawing major grid lines
					g2.drawLine(paddingHorizontal + majorHatchLength, (int) point, width - paddingHorizontal,
							(int) point);
					
				} else if (i % minorHatchInterval == 0.0) {
					// drawing minor grid lines
					g2.drawLine(paddingHorizontal + minorHatchLength, (int) point, width - paddingHorizontal,
							(int) point);
				}

				if (i % majorHatchInterval == 0.0) {
					// drawing labels for major intervals
					g2.setColor(labelColor);
					g2.drawString(yLabel, paddingHorizontal - (labelWidth + 1), (int) (point + (labelHeight / 2)));
				}
			}

			// creating x-Marks
			for (int i = 0; i < points.size(); i++) {
				
				// translating the data point according the scale
				double pointX = paddingHorizontal + (xScale * points.get((int) i).getX().doubleValue());
				
				// setting the label to display first to decimals
				String xLabel = String.format("%.2f", (pointX - paddingHorizontal) / xScale);

				metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(xLabel);
				int labelHeight = metrics.getHeight();

				for (double x = 0.2; x <= 1.0; x += 0.2) {

					g2.setColor(Color.BLACK);
					g2.setStroke(strokes[0]);
					g2.drawLine((int) (xScale * (i + x) + paddingHorizontal), height - paddingVertical,
							(int) (xScale * (i + x) + paddingHorizontal), height - minorHatchLength - paddingVertical);

					if (x % 1.0 != 0.0) {
						g2.setColor(gridColor);
					} else {
						g2.setColor(Color.BLACK);
					}
					g2.setStroke(strokes[2]);
					// drawing dashed lines
					g2.drawLine((int) (xScale * (i + x) + paddingHorizontal), paddingVertical,
							(int) (xScale * (i + x) + paddingHorizontal), height - paddingVertical - minorHatchLength);
				}

				g2.setColor(Color.BLACK);
				g2.setStroke(strokes[0]);
				g2.drawLine((int) pointX, (int) height - paddingVertical, (int) pointX,
						(int) height - paddingVertical - majorHatchLength); // creating
																			// hatches

				if (!xLabel.equals("0.00")) { // drawing the labels
					g2.setColor(labelColor);
					g2.drawString(xLabel, (int) (pointX - (labelWidth / 2)),
							(int) (height - paddingVertical + labelHeight));
				}
			}

			// drawing lines
			g2.setColor(lineColor);
			for (int i = 0; i < points.size() - 1; i++) {
				// the first point's co-ordinates
				double x1 = (points.get(i).getX().doubleValue() * xScale) + paddingHorizontal;
				double y1 = originY - (points.get(i).getY().doubleValue() * yScale);
				
				// the second point's co-ordinates
				double x2 = (points.get(i + 1).getX().doubleValue() * xScale) + paddingHorizontal;
				double y2 = originY - (points.get(i + 1).getY().doubleValue() * yScale);
				
				// drawing a point at the location of the data point
				g2.fillRect((int) x1 - (pointSizeX / 2), (int) y1 - (pointSizeY / 2), pointSizeX, pointSizeY);
				g2.fillRect((int) x2 - (pointSizeX / 2), (int) y2 - (pointSizeY / 2), pointSizeX, pointSizeY);

				g2.setStroke(strokes[1]);
				g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
			}
		}
		else{
			// if there are no data points show the error message
			g2.setColor(lineColor);
			g2.setFont(new Font(null, Font.BOLD, 30));
			metrics = g2.getFontMetrics();
			g2.drawString(errorMessage, getWidth()/2-(metrics.stringWidth(errorMessage)/2), getHeight()/2-metrics.getHeight()/2);
		}
	}
	
	// various getters and setters that may be required
	public String getYAxisTitle() {
		return YAxisTitle;
	}

	public void setYAxisTitle(String YAxisTitle) {
		this.YAxisTitle = YAxisTitle;
	}

	public String getTitle() {
		return graphTitle;
	}

	public void setTitle(String title) {
		graphTitle = title;
	}

	public String getXAxisTitle() {
		return XAxisTitle;
	}

	public void setXAxisTitle(String xAxisTitle) {
		XAxisTitle = xAxisTitle;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
