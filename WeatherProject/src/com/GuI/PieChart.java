package com.GuI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.JPanel;
/**
 * displays the given data in the form of a pie chart
 * @author Sagnik
 *
 */
public class PieChart extends JPanel {
	
	// setting the dimensions of the display
	private int height;
	private int width;
	
	// the orgin of the chart
	private int startX;
	private int startY;
	
	// dimensions of the chart
	private int chartHeight;
	private int chartLength;
	
	// paddings
	private int legendSpace = 5;
	private int titleSpace = 5;
	private int horizontalPadding = 5;
	
	// the dimensions of the legend
	private int lengendWidth;
	private int legendHeight;

	private String title;
	
	// error message displayed when no data is present
	private String message = "No Data Available";
	
	// holds the data values
	public ArrayList<String> labels;
	// maps the data value to a color
	public TreeMap<Double, Color> sectors;
	
	/** 
	 * setting the starting co-oridates of the chart and its dimensions
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public PieChart(int x, int y, int width, int height) {

		setPreferredSize(new Dimension(width, height));

		startX = x;
		startY = y;

		this.height = height;
		this.width = width;

		lengendWidth = height / 30;
		legendHeight = width / 30;

		labels = new ArrayList<>();
		sectors = new TreeMap<>();
	}
	
	/**
	 *  add a new sector to a chart
	 * @param value
	 * @param color
	 * @param label
	 */
	public void addSector(double value, Color color, String label) {
		sectors.put(value, color);
		labels.add(label);
	}
	/**
	 * draws the chart
	 */
	public void drawChart() {
		chartHeight = (height - (legendSpace + titleSpace));
		chartLength = (width - (2 * horizontalPadding));

		repaint();
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		// making the graphics smoother
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// getting the measurements of the font
		FontMetrics metrics = g2.getFontMetrics();

		// drawing background
		g2.setColor(Color.white);
		g2.fillRect(startX, startY, width, height);

		// drawing the title
		if (title != null) {
			g2.setColor(Color.BLACK);
			g2.setFont(new Font(null, Font.BOLD, chartHeight / 24));
			g2.drawString(title, (width / 2 -metrics.stringWidth(title)/2) / 2, height / 15);
		}
		
		// the angle will determine the shape of the sector
		double angle = 0;
		// this is the spacing between legends
		int spacing = 10;

		if (!sectors.isEmpty()) {
			g2.setFont(new Font("", Font.PLAIN, height / 30));
			int count = 0;
			
			for (Double d : sectors.keySet()) {
				
				// drawing arcs
				g2.setColor(sectors.get(d));
				g2.fillArc(chartLength / 4, chartHeight / 4, chartLength / 2, chartHeight / 2, (int) angle,
						(int) Math.ceil(d * 360 / 100));
				
				// incrementing the angle based on the value
				angle += (d / 100) * 360;

				// drawing the legend
				spacing += legendHeight + (height / 60);
				g2.fillRect(chartLength * 5 / 9, startY + ((int)(chartHeight *0.72)) + spacing, lengendWidth, legendHeight);

				// writing the labels
				g2.setColor(Color.BLACK);
				g2.drawString(labels.get(count), (chartLength * 5 / 9) + (width / 24),
						startY + ((int)(chartHeight * 0.72)) + spacing + metrics.getHeight() / 2);
				count++;
			}

		} else {
			// if the data set is empty them display error message
			g2.setColor(Color.BLUE);
			g2.setFont(new Font(null, Font.PLAIN, chartLength / 24));
			g2.drawString(message, width / 2 - metrics.stringWidth(message),
					height / 2 - metrics.getHeight() / 2);
		}

	}
	
	// setters and getters for the error message
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
