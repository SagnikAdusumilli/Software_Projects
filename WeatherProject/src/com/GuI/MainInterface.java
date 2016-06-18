package com.GuI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.Data.DataOrganizer;
import com.Data.DataScanner;
import com.Data.Downloader;
import com.NonGuI.City;
import com.NonGuI.Day;
import com.NonGuI.Month;
import com.NonGuI.Year;

public class MainInterface extends JFrame implements ActionListener, ChangeListener {

	private DataOrganizer dataOrganizer;

	private Map mapPanel;
	private JPanel barGraphPanel = new JPanel();
	private JPanel comparePanel;
	private JPanel optionsPanel;
	
	// (Sagnik's part line chart)
	// this is the panel that is in the line chart tab
	private JPanel linegraphPanel;
	// these are the line chart and the chartcontroller to control the line graph
	private ChartController lineChartController;
	private LineChart lineChart;

	private BarChart barGraph = new BarChart();
	private BarChartController barGraphController;
	
	// (Sagnik's part pie chart)
	// the chart controller controls the displays of the pie graphs
	private ChartController pieChartController;
	private PieChartGraph pieGraph1;
	private PieChartGraph pieGraph2;

	private Options options = new Options(this);

	private static int STARTYEAR;
	private static int ENDYEAR;

	private Dimension screenSize;

	private JTabbedPane tabbedPane = new JTabbedPane();


	// Default colors
	private Color bottomColorSelected = new Color(64, 96, 64);
	private Color topColorSelected = new Color(128, 192, 128);
	private Color bottomColor = new Color(96, 64, 64);
	private Color topColor = new Color(192, 128, 128);
	private Color backgroundColor = new Color(128, 160, 192);
	private Color textColor = ColorUIResource.white;

	static Font font1 = new Font("Verdana", Font.BOLD, 16);
	static Font font2 = new Font("Arial", Font.BOLD, 16);
	static Font font3 = new Font("Times New Roman", Font.BOLD, 16);

	public MainInterface(int startYear, int endYear) throws IOException {

		this.STARTYEAR = startYear;
		this.ENDYEAR = endYear;

		dataOrganizer = new DataOrganizer(startYear, endYear);
		barGraphController = new BarChartController(dataOrganizer, barGraph, startYear, endYear);

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		lineChart = new LineChart((int) screenSize.getWidth(), (int) (0.5 * screenSize.getHeight()));

		setTitle("Climate Change: Canada");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultLookAndFeelDecorated(true);
		// setIconImage(new ImageIcon("Images/Icon.png").getImage());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);

		font1 = new Font("Verdana", Font.BOLD, 16);

		UIManager.put("TabbedPane.borderColor", Color.white);
		UIManager.put("TabbedPane.foreground", textColor);
		UIManager.put("TabbedPane.background", backgroundColor);
		UIManager.put("TabbedPane.tabsOverlapBorder", true);

		tabbedPane.setUI(new GradientUI());

		populateMap();
		tabbedPane.addTab("Map", mapPanel);

		addBarChartPanel();
		tabbedPane.addTab("Bar Graph", barGraphPanel);

		// populateScatterGraphArea();
		populateLineGraphPanel();
		tabbedPane.addTab("Line Graph", linegraphPanel);

		// create compare tab
		populateComparePanel();
		tabbedPane.addTab("Compare", comparePanel);

		addOptionsPanel();
		tabbedPane.addTab("Options", optionsPanel);

		mainPanel.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setBorder(null);
		tabbedPane.setFont(font1);
		tabbedPane.setOpaque(true);

	}

	public void updated() {
		setVisible(false);
		dispose();
	}

	public void setUIColor(Color topColor, Color botColor, Color topSColor, Color botSColor, Color backColor,
			Color tColor) {
		this.topColor = topColor;
		bottomColor = botColor;
		topColorSelected = topSColor;
		bottomColorSelected = botSColor;
		backgroundColor = backColor;
		textColor = tColor;
		tabbedPane.setForeground(textColor);
		tabbedPane.setBackground(backgroundColor);
		repaint();
		revalidate();
	}

	public void setFont(Font font) {
		tabbedPane.setFont(font);
	}

	class GradientUI extends BasicTabbedPaneUI {

		public void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
				boolean isSelected) {

			Graphics2D g2d = (Graphics2D) g;
			GradientPaint gradient = new GradientPaint(x, y, topColor, x, y + h, bottomColor, true),
					selected = new GradientPaint(x, y, topColorSelected, x, y + h, bottomColorSelected, true);
			g2d.setPaint(!isSelected ? gradient : selected);
			g.fillRect(x, y, w, h);

		}

	}

	private void populateMap() {
//		mapPanel = new Map(dataOrganizer, STARTYEAR, ENDYEAR);
	}

	private void addBarChartPanel() {

		barGraphPanel = new JPanel();
		barGraphPanel.setLayout(new BorderLayout());
		barGraphPanel.add(barGraph, BorderLayout.CENTER);
		barGraphPanel.add(barGraphController, BorderLayout.EAST);

	}

	private void addOptionsPanel() {

		optionsPanel = new JPanel();
		optionsPanel.add(options);

	}
	/**
	 * @author Sagnik
	 * creating the compare panel that compares the data of 2 cities in the form of pie charts
	 */
	private void populateComparePanel() {
		// this diplay in the pie chart tab
		comparePanel = new JPanel();
		comparePanel.setLayout(new BorderLayout());
		
		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
		
		// creates the graph display for one side
		pieGraph1 = new PieChartGraph(STARTYEAR, ENDYEAR, (int) screenSize.getWidth() / 4, (int) screenSize.getHeight(),
				dataOrganizer);
		chartPanel.add(pieGraph1);
		
		pieGraph1.getCityList().addActionListener(this);
		
		// creates the graph display for the other side
		pieGraph2 = new PieChartGraph(STARTYEAR, ENDYEAR, (int) screenSize.getWidth() / 4, (int) screenSize.getHeight(),
				dataOrganizer);
		chartPanel.add(pieGraph2);
		
		pieGraph2.getCityList().addActionListener(this);
		
		// add the 2 graph displays to the main display
		comparePanel.add(chartPanel, BorderLayout.CENTER);
		
		// controller for controling the displayes
		pieChartController = new ChartController(STARTYEAR, ENDYEAR);

		// adding actionListeners to the field controllers
		pieChartController.getTempBtn().addActionListener(this);
		pieChartController.getRainFallBtn().addActionListener(this);
		pieChartController.getSnowFallBtn().addActionListener(this);
		pieChartController.getPrecipBtn().addActionListener(this);

		// adding changeLiterner to sliders
		pieChartController.getYearSlider().addChangeListener(this);
		pieChartController.getMonthSlider().addChangeListener(this);
		
		// don't need the right and left interfaces to control the pie graphs
		pieChartController.remove(pieChartController.rightInterface);
		pieChartController.remove(pieChartController.leftInterface);
		comparePanel.add(pieChartController, BorderLayout.SOUTH);
		
		// setting intial error message
		pieGraph1.getDataChart().setMessage("Select City To Begin");
		pieGraph2.getDataChart().setMessage("Select City To Begin");
		
		// draw the emplty charts first
		pieGraph1.getDataChart().drawChart();
		pieGraph2.getDataChart().drawChart();

	}
	/**
	 * @author Sagnik 
	 * @param city
	 * @param field
	 * @param year
	 * @param chart
	 * the chart specified draws the monthly average data of the city spefied in the year specified
	 */
	public void drawPieChart(City city, String field, int year, PieChart chart) {
		
		// clear the previous data
		chart.labels.clear();
		chart.sectors.clear();
		
		// set the new error message
		chart.setMessage("No Data Avaliable");
		
		// keeps track of the min and vax data values
		double minValue = 0;
		double maxValue = 0;
		// counts the sector
		int count = 0;


		// setting title for the pie chart (according to the field that was passed
		switch (field) {
		case ChartController.temperature:
			chart.setTitle(String.format("Average Temperature for %d", year + STARTYEAR));
			break;
		case ChartController.snow:
			chart.setTitle(String.format("Snow fall for %d", year + STARTYEAR));
			break;
		case ChartController.rain:
			chart.setTitle(String.format("Rain fall for %d", year + STARTYEAR));
			break;
		case ChartController.precip:
			chart.setTitle(String.format("Total Precipitation %d", year + STARTYEAR));
			break;
		}
		
		// sets the min and max data value for the field that was passed
		
		for (Month m : city.getYear(year).getMonthsList()) { // each moth in the current year

			for (Day d : m.getDaysList()) { // each day in the current month

				switch (field) {
				case ChartController.temperature:
					if (count == 0 || d.getAvgTemp() > maxValue) {
						if (d.isAvgTempFlag()) {
							maxValue = d.getAvgTemp();
						}
					}

					if (count == 0 || d.getAvgTemp() < minValue) {
						if (d.isAvgTempFlag()) {
							minValue = d.getAvgTemp();
						}
					}
					break;
				case ChartController.rain:
					if (count == 0 || d.getTotalRain() > maxValue) {
						if (d.isTotalRainFlag()) {
							maxValue = d.getTotalRain();
						}
					}

					if (count == 0 || d.getTotalRain() < minValue) {
						if (d.isTotalRainFlag()) {
							minValue = d.getTotalRain();
						}
					}
					break;
				case ChartController.snow:
					if (count == 0 || d.getTotalSnow() > maxValue) {
						if (d.isTotalSnowFlag()) {
							maxValue = d.getTotalSnow();
						}
					}

					if (count == 0 || d.getTotalSnow() < minValue) {
						if (d.isTotalSnowFlag()) {
							minValue = d.getTotalSnow();
						}
					}
					break;
				case ChartController.precip:
					if (count == 0 || d.getTotalPrecip() > maxValue) {
						if (d.isTotalPrecipFlag()) {
							maxValue = d.getTotalPrecip();
						}
					}

					if (count == 0 || d.getTotalPrecip() < minValue) {
						if (d.isTotalPrecipFlag()) {
							minValue = d.getTotalPrecip();
						}
					}
					break;
				}

				count++;
			}
		}

		double percentRange = .2 * (maxValue - minValue); // decided the smallest sector value of the mode in the pie chart

		if (percentRange > 0) {


			int[] ranges = new int[(int) Math.ceil((maxValue - minValue) / percentRange) + 1]; // stores all the values

			for (Month m : city.getYear(0).getMonthsList()) {
				for (Day d : m.getDaysList()) {
					switch (field) {// converts each data value into a percentage and adds them to list of values (according to field)
					case ChartController.precip:
						ranges[(int) Math.floor(d.getTotalPrecip() / percentRange)]++;
						break;

					case ChartController.temperature:
						ranges[(int) (Math.abs(Math.floor(d.getAvgTemp() + minValue)) / percentRange)]++;

						break;

					case ChartController.rain:
						ranges[(int) Math.floor(d.getTotalRain() / percentRange)]++;
						break;

					case ChartController.snow:
						ranges[(int) Math.floor(d.getTotalSnow() / percentRange)]++;
						break;

					}
				}
			}
			
			// random is used for the color for each sector
			Random r = new Random();
			for (int i = 1; i < ranges.length; i++) {
				if (ranges[i - 1] > 0) {
					// puts the percentage range into the chart according to the field
					switch (field) {
					case ChartController.snow:
						// converts the percent range into a sector angle
						chart.addSector((ranges[i - 1] / 365.0) * 100,
								new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
								// the tile of the legend for that sector
								String.format("%.2f to %.2f cm", percentRange * (i - 1), percentRange * (i)));
						break;

					case ChartController.rain:
						// converts the percent range into a sector angle
						chart.addSector((ranges[i - 1] / 365.0) * 100,
								new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
								// the tile of the legend for that sector
								String.format("%.2f to %.2f mm", percentRange * (i - 1), percentRange * (i)));
						break;

					case ChartController.temperature:
						// converts the percent range into a sector angle
						chart.addSector((ranges[i - 1] / 365.0) * 100,
								new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
								// the tile of the legend for that sector
								String.format("%.2f to %.2f C", percentRange * (i - 1), percentRange * (i)));
						break;

					case ChartController.precip:
						// converts the percent range into a sector angle
						chart.addSector((ranges[i - 1] / 365.0) * 100,
								new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
								// the tile of the legend for that sector
								String.format("%.2f to %.2f mm", percentRange * (i - 1), percentRange * (i)));
						;
						break;
					}
				}
			}
		}
		// draw the chart
		chart.drawChart();
		comparePanel.repaint();
		comparePanel.revalidate();

	}
	/**
	 * @author Sagnik
	 * creates the line chart panel in the line chart tab
	 */
	private void populateLineGraphPanel() {
		// Initializing the line char panel
		linegraphPanel = new JPanel();
		linegraphPanel.setLayout(new BorderLayout());
		
		linegraphPanel.add(lineChart, BorderLayout.CENTER);
		lineChart.setErrorMessage("Select City To Begin");
		
		// controller to control the line chart
		lineChartController = new ChartController(STARTYEAR, ENDYEAR);
		// adding controller to the main display
		linegraphPanel.add(lineChartController, BorderLayout.SOUTH);

		// adding actionListeners to the field controllers
		lineChartController.getTempBtn().addActionListener(this);
		lineChartController.getRainFallBtn().addActionListener(this);
		lineChartController.getSnowFallBtn().addActionListener(this);
		lineChartController.getPrecipBtn().addActionListener(this);

		// adding changeLiterner to sliders
		lineChartController.getYearSlider().addChangeListener(this);
		lineChartController.getMonthSlider().addChangeListener(this);

		// adding anctionListeners to combo box
		lineChartController.getCityList().addActionListener(this);

		// adding actionListeners to the toggle button;
		lineChartController.getModeToggle().addActionListener(this);
	}
	/**
	 * @author Sagnik
	 * @param city
	 * @param field
	 * @param year
	 * draws the line chart displaying the monthly data of the city specifed in the year specified
	 */
	private void drawChartMonthly(City city, String field, int year) {
		// clearing all the previous values
		lineChart.getPoints().clear();
		// setting the tile
		lineChart.setXAxisTitle("Month");
		// setting the error message
		lineChart.setErrorMessage("No Data Avialable");
		
		// inserts monthly data into the graph according to the field
		switch (field) {
		case ChartController.temperature:
			// inserting average temperature data for every month
			for (Month m : city.getYear(year - STARTYEAR).getMonthsList()) {
				if (m.isAvgTempFlag()) {
					lineChart.addData(m.getNumber(), m.getAvgTemp());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Temperature for %s %d", city.getName(), year));
				lineChart.setYAxisTitle("Temperature");
			}
			break;
		case ChartController.rain:
			// inserting average rain data for every month
			for (Month m : city.getYear(year - STARTYEAR).getMonthsList()) {
				if (m.isTotalRainFlag()) {
					lineChart.addData(m.getNumber(), m.getTotalRain());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Average Rain fall for %s %d", city.getName(), year));
				lineChart.setYAxisTitle("Rain fall");
			}
			break;
		case ChartController.snow:
			// inserting average snow data for every month
			for (Month m : city.getYear(year - STARTYEAR).getMonthsList()) {
				if (m.isTotalSnowFlag()) {
					lineChart.addData(m.getNumber(), m.getTotalSnow());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Average Snow fall for %s %d", city.getName(), year));
				lineChart.setYAxisTitle("Snow fall");
			}
			break;
		case ChartController.precip:
			// inserting average precipitation data for every month
			for (Month m : city.getYear(year - STARTYEAR).getMonthsList()) {
				if (m.isTotalPrecipFlag()) {
					lineChart.addData(m.getNumber(), m.getTotalPrecip());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Precipitation for %s %d", city.getName(), year));
				lineChart.setYAxisTitle("Total Precipiation");
			}
			break;
		}

		lineChart.drawChart();
	}
	/**
	 * @author Sagnik
	 * @param city
	 * @param field
	 * @param year
	 * draws the line chart displaying the daily data of the city specifed in the year specified in the month specified
	 */
	private void drawChartDaily(City city, String field, int year, int month) {
		// clearing all the previous data
		lineChart.getPoints().clear();
		lineChart.setXAxisTitle("Days");
		lineChart.setErrorMessage("No Data Avialable");
		
		// inserts daily data according to the filed
		switch (field) {
		case ChartController.temperature:
			// inserting average temperature data for every day
			for (Day d : city.getYear(year - STARTYEAR).getMonth(month - 1).getDaysList()) {
				if (d.isAvgTempFlag()) {
					lineChart.addData(d.getNumber(), d.getAvgTemp());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Temperature for %s %d Month: %d", city.getName(), year, month));
				lineChart.setYAxisTitle("Temperature");
			}
			break;
		case ChartController.rain:
			// inserting average rain data for every day
			for (Day d : city.getYear(year - STARTYEAR).getMonth(month - 1).getDaysList()) {
				if (d.isTotalRainFlag()) {
					lineChart.addData(d.getNumber(), d.getTotalRain());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Average Rain fall for %s Month: %d", city.getName(), year, month));
				lineChart.setYAxisTitle("Rain fall");
			}
			break;
		case ChartController.snow:
			// inserting average snow data for every day
			for (Day d : city.getYear(year - STARTYEAR).getMonth(month - 1).getDaysList()) {
				if (d.isTotalSnowFlag()) {
					lineChart.addData(d.getNumber(), d.getTotalSnow());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Average Snow fall for %s Month: %d", city.getName(), year, month));
				lineChart.setYAxisTitle("Snow fall");
			}
			break;
		case ChartController.precip:
			// inserting average precipitation data for every day
			for (Day d : city.getYear(year - STARTYEAR).getMonth(month - 1).getDaysList()) {
				if (d.isTotalPrecipFlag()) {
					lineChart.addData(d.getNumber(), d.getTotalPrecip());
				}
				// setting the the titles
				lineChart.setTitle(String.format("Precipitation for %s Month: %d", city.getName(), year, month));
				lineChart.setYAxisTitle("Total Precipiation");
			}
			break;
		}

		lineChart.drawChart();
	}
	// (Sagnik's part) (all the action listeners for graph components
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		// changes the field being displayed according to the filed with was clicked
		case ChartController.snow:
			if (e.getSource().equals(lineChartController.getSnowFallBtn())) {
				
				// draws the new line chart if the current display was line chart
				if (lineChartController.getMode() == 0) {
					// draws monthly data chart if the mode was monthly
					drawChartMonthly(lineChartController.getSelectedCity(), ChartController.snow,
							lineChartController.getYearSlider().getValue());
				} else {
					// draws daily data chart if the mode was daily
					drawChartDaily(lineChartController.getSelectedCity(), ChartController.snow,
							lineChartController.getYearSlider().getValue(),
							lineChartController.getMonthSlider().getValue());
				}
			} else {
				// draws the 2 pie charts if the pie charts were being displayed
				drawPieChart(pieGraph1.getSelectedCity(), ChartController.snow,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph1.getDataChart());

				drawPieChart(pieGraph2.getSelectedCity(), ChartController.snow,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph2.getDataChart());
			}
			break;
		case ChartController.rain:
			System.out.println("rain");

			if (e.getSource().equals(lineChartController.getRainFallBtn())) {
				
				// draws the new line chart if the current display was line chart
				if (lineChartController.getMode() == 0) {
					// draws monthly data chart if the mode was monthly
					drawChartMonthly(lineChartController.getSelectedCity(), ChartController.rain,
							lineChartController.getYearSlider().getValue());
				} else {
					// draws daily data chart if the mode was daily
					drawChartDaily(lineChartController.getSelectedCity(), ChartController.rain,
							lineChartController.getYearSlider().getValue(),
							lineChartController.getMonthSlider().getValue());
				}

			} else {
				// draws the 2 pie charts if the pie charts were being displayed
				drawPieChart(pieGraph1.getSelectedCity(), ChartController.rain,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph1.getDataChart());

				drawPieChart(pieGraph2.getSelectedCity(), ChartController.rain,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph2.getDataChart());
			}
			break;
		case ChartController.temperature:
			if (e.getSource().equals(lineChartController.getTempBtn())) {
				
				// draws the new line chart if the current display was line chart
				if (lineChartController.getMode() == 0) {
					// draws monthly data chart if the mode was monthly
					drawChartMonthly(lineChartController.getSelectedCity(), ChartController.temperature,
							lineChartController.getYearSlider().getValue());
				} else {
					// draws daily data chart if the mode was daily
					drawChartDaily(lineChartController.getSelectedCity(), ChartController.temperature,
							lineChartController.getYearSlider().getValue(),
							lineChartController.getMonthSlider().getValue());
				}

			} else {
				// draws the 2 pie charts if the pie charts were being displayed
				drawPieChart(pieGraph1.getSelectedCity(), ChartController.temperature,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph1.getDataChart());

				drawPieChart(pieGraph2.getSelectedCity(), ChartController.temperature,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph2.getDataChart());
			}
			break;
		case ChartController.precip:
			if (e.getSource().equals(lineChartController.getPrecipBtn())) {
				
				// draws the new line chart if the current display was line chart
				if (lineChartController.getMode() == 0) {
					// draws monthly data chart if the mode was monthly
					drawChartMonthly(lineChartController.getSelectedCity(), ChartController.precip,
							lineChartController.getYearSlider().getValue());
				} else {
					// draws daily data chart if the mode was daily
					drawChartDaily(lineChartController.getSelectedCity(), ChartController.precip,
							lineChartController.getYearSlider().getValue(),
							lineChartController.getMonthSlider().getValue());
				}
			} else {
				// draws the 2 pie charts if the pie charts were being displayed
				drawPieChart(pieGraph1.getSelectedCity(), ChartController.precip,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph1.getDataChart());
				drawPieChart(pieGraph2.getSelectedCity(), ChartController.precip,
						pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph2.getDataChart());
			}
			break;
		case "Toggle":
			// if the toggle button was clicked, switch mode accordingly
			if (lineChartController.getMode() == 1) {
				
				// draw monthly data line chart if the mode was switched to monthly
				if (e.getSource().equals(lineChartController.getModeToggle())) {
					drawChartMonthly(lineChartController.getSelectedCity(), lineChartController.getField(),
							lineChartController.getYearSlider().getValue());
				}
			} else if (e.getSource().equals(lineChartController.getModeToggle())) {
				
				// draw dialy data line chart if the mode was switched to daily
				drawChartDaily(lineChartController.getSelectedCity(), lineChartController.getField(),
						lineChartController.getYearSlider().getValue(),
						lineChartController.getMonthSlider().getValue());
			}

			break;
		case "CityList":
			// change the city accordingly if the city was changed from the drop down menu (for line chart)
			switch (lineChartController.getCityList().getSelectedIndex()) {
			case ChartController.quebec:
				lineChartController.setSelectedCity(dataOrganizer.getQubecCity());
				break;
			case ChartController.markham:
				lineChartController.setSelectedCity(dataOrganizer.getMarkham());
				break;
			case ChartController.ottawa:
				lineChartController.setSelectedCity(dataOrganizer.getOttawa());
				break;
			case ChartController.charlotteTown:
				lineChartController.setSelectedCity(dataOrganizer.getCharlotteTown());
				break;
			case ChartController.windsor:
				lineChartController.setSelectedCity(dataOrganizer.getWindsor());
				break;
			}
			if (lineChartController.getMode() == 0) {
				// draw monthly data line chart of the new city if the current mode is monthly
				drawChartMonthly(lineChartController.getSelectedCity(), lineChartController.getField(),
						lineChartController.getYearSlider().getValue());
			} else {
				//  draw monthly data line chart of the new city if the current mode is daily
				drawChartDaily(lineChartController.getSelectedCity(), lineChartController.getField(),
						lineChartController.getYearSlider().getValue(),
						lineChartController.getMonthSlider().getValue());
			}
			break;
		}
		
		// if the city list from graph1 was clicked then change and draw the data data of the new city
		if (e.getSource().equals(pieGraph1.getCityList())) { 
			
			// changing the city
			switch (pieGraph1.getCityList().getSelectedIndex()) {
			case PieChartGraph.markhamIndex:
				System.out.println("markham");
				pieGraph1.setSelectedCity(dataOrganizer.getMarkham());
				break;

			case PieChartGraph.quebecIndex:
				System.out.println("quebec");
				pieGraph1.setSelectedCity(dataOrganizer.getQubecCity());
				break;
			case PieChartGraph.chareotteTownIndex:
				System.out.println("charlotte town");
				pieGraph1.setSelectedCity(dataOrganizer.getCharlotteTown());

				break;
			case PieChartGraph.windosorIndex:
				System.out.println("windsor");
				pieGraph1.setSelectedCity(dataOrganizer.getWindsor());

				break;
			case PieChartGraph.ottawaIndex:
				System.out.println("ottawa");
				pieGraph1.setSelectedCity(dataOrganizer.getOttawa());
				break;

			}
			
			// draw the data for the new city in graph1
			drawPieChart(pieGraph1.getSelectedCity(), pieChartController.getField(),
					pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph1.getDataChart());
		}
		
		// if the city list from graph2 was clicked then change and draw the data data of the new city
		else if (e.getSource().equals(pieGraph2.getCityList())) {
			
			// changing the city
			switch (pieGraph2.getCityList().getSelectedIndex()) {
			
			case PieChartGraph.markhamIndex:
				System.out.println("markham");
				pieGraph2.setSelectedCity(dataOrganizer.getMarkham());
				break;

			case PieChartGraph.quebecIndex:
				System.out.println("quebec");
				pieGraph2.setSelectedCity(dataOrganizer.getQubecCity());
				break;
			case PieChartGraph.chareotteTownIndex:
				System.out.println("charlotte town");
				pieGraph2.setSelectedCity(dataOrganizer.getCharlotteTown());

				break;
			case PieChartGraph.windosorIndex:
				System.out.println("windsor");
				pieGraph2.setSelectedCity(dataOrganizer.getWindsor());

				break;
			case PieChartGraph.ottawaIndex:
				System.out.println("ottawa");
				pieGraph2.setSelectedCity(dataOrganizer.getOttawa());
				break;

			}
			// draw the data for the new city in graph2
			drawPieChart(pieGraph2.getSelectedCity(), pieChartController.getField(),
					pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph2.getDataChart());
		}
		
	}
	
	// (Sagnik's part) the action listeners for the sliders
	@Override
	public void stateChanged(ChangeEvent e) {
		// if the year slider value was changed in the line graph tab
		if (e.getSource().equals(lineChartController.getYearSlider())) {

			if (lineChartController.getMode() == 0) {
				// draw monthly data line chart of the new city if the current mode is monthly
				drawChartMonthly(lineChartController.getSelectedCity(), lineChartController.getField(),
						lineChartController.getYearSlider().getValue());

			} else if (lineChartController.getMode() == 1) {
				// draw dialy data line chart of the new city if the current mode is daily
				drawChartDaily(lineChartController.getSelectedCity(), lineChartController.getField(),
						lineChartController.getYearSlider().getValue(),
						lineChartController.getMonthSlider().getValue());

			}

			// if the month slider value was changed in line graph tab
		} else if (e.getSource().equals(lineChartController.getMonthSlider())) {
			// draw daily data line chart
			drawChartDaily(lineChartController.getSelectedCity(), lineChartController.getField(),
					lineChartController.getYearSlider().getValue(), lineChartController.getMonthSlider().getValue());

			// if the year slider value was changed in the line compare tab
		} else if (e.getSource().equals(pieChartController.getYearSlider())) {
			
			// draw pie chart for pie chart1
			drawPieChart(pieGraph1.getSelectedCity(), pieChartController.getField(),
					pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph1.getDataChart());
			
			// draw pie char for pie chart2
			drawPieChart(pieGraph2.getSelectedCity(), pieChartController.getField(),
					pieChartController.getYearSlider().getValue() - STARTYEAR, pieGraph2.getDataChart());
		}

	}

}
