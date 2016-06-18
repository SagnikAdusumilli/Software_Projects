package com.GuI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import com.Data.DataOrganizer;
import com.NonGuI.City;
/**
 * 
 * @author Sagnik
 * This is a controller interface created to control the charts in the program
 */
public class ChartController extends JPanel implements ActionListener {
	
	// the interface in divied into 3 sections
	public JPanel leftInterface;
	public JPanel middleInterface;
	public JPanel rightInterface;
	
	// slider to change the display according to the time
	private JSlider yearSlider;
	private JSlider monthSlider;
	
	// tiles for the sliders
	private JLabel yearSliderLabel = new JLabel("Slide to change year");
	private JLabel monthSliderLabel = new JLabel("Slide to change month");
	
	// the range of data that can be displayed
	private static int endYear;
	private static int startYear;
	
	// marks the indices of the cities in the drop down menu
	public static final int quebec = 0;
	public static final int markham = 2;
	public static final int charlotteTown = 4;
	public static final int ottawa = 3;
	public static final int windsor = 1;
	
	// names for the radio buttons
	public static final String snow = "Snow fall";
	public static final String temperature = "Temperature";
	public static final String rain = "Rain fall";
	public static final String precip = "Total Precipitation";
	
	// radio buttons for controlling the parameter that is being displayed
	private JRadioButton tempBtn;
	private JRadioButton rainFallBtn;
	private JRadioButton snowFallBtn;
	private JRadioButton precipBtn;
	
	// stores the current parameter that is being displayed
	private String field = temperature;
	// the current city data that is being displayed
	private City selectedCity;
	// mode to swtich between monthly data and daily data
	// monthly (mode = 0)
    // daliy (mode = 1)
	private int mode;
	
	// drop down menu to allow the user to select the city he/she wants to view
	private JComboBox<String> cityList;
	// toggle button to switch between the daily data and monthly data
	private JToggleButton modeToggle;
	
	/**
	 * set the data interval and create the interfaces
	 * @param startYear
	 * @param endYear
	 */
	public ChartController(int startYear, int endYear) {

		setLayout(new FlowLayout(FlowLayout.CENTER, 100, 0));
		setPreferredSize(new Dimension(500, 240));
		
		ChartController.startYear = startYear;
		ChartController.endYear = endYear;

		createLeftInterface();
		add(leftInterface);

		createMiddleInterface();
		add(middleInterface);
		setSliders();

		createRightInteface();
		add(rightInterface);

	}
	/**
	 * creating the left interface
	 * it has a button to switch between monthly and daily data
	 */
	private void createLeftInterface() {
		leftInterface = new JPanel();
		leftInterface.setLayout(new GridLayout(1, 0));
		
		// creating the toggle button
		modeToggle = new JToggleButton("Monthly", false);
		modeToggle.setActionCommand("Toggle");
		
		// adding functionality to the toggle button
		modeToggle.addActionListener(this);
		
		// add the toggle button
		leftInterface.add(modeToggle);
	}
	
	/**
	 * create the middle interface
	 * it has radiobuttons to control the parameter the user is veiwing
	 */
	private void createMiddleInterface() {
		middleInterface = new JPanel();
		middleInterface.setLayout(new GridLayout(5, 0, 5, 0));
		
		// the layout for the radio buttons (flow layout)
		JPanel parameterPanel = new JPanel();
		FlowLayout layout = new FlowLayout();
		parameterPanel.setLayout(layout);
		
		// create and add radio button
		tempBtn = new JRadioButton(temperature);
		tempBtn.setActionCommand(temperature);
		tempBtn.setSelected(true);
		tempBtn.addActionListener(this);
		parameterPanel.add(tempBtn);
		
		// create and add radio button
		snowFallBtn = new JRadioButton(snow);
		snowFallBtn.setActionCommand(snow);
		snowFallBtn.addActionListener(this);
		parameterPanel.add(snowFallBtn);
		
		// create and add radio button
		rainFallBtn = new JRadioButton(rain);
		rainFallBtn.setActionCommand(rain);
		rainFallBtn.addActionListener(this);
		parameterPanel.add(rainFallBtn);
		
		// create and add radio button
		precipBtn = new JRadioButton(precip);
		precipBtn.setActionCommand(precip);
		precipBtn.addActionListener(this);
		parameterPanel.add(precipBtn);
		
		// add all the radio buttons to a group, this means that the user can only select one of them at a time
		ButtonGroup parameterGroup = new ButtonGroup();
		parameterGroup.add(tempBtn);
		parameterGroup.add(snowFallBtn);
		parameterGroup.add(rainFallBtn);
		parameterGroup.add(precipBtn);
		
		// add the parameter panel to the middle interface
		middleInterface.add(parameterPanel);
	}
	
	/**
	 * Creates the right interface
	 * this has a drop down menu to change the city
	 */
	private void createRightInteface() {

		rightInterface = new JPanel();
		rightInterface.setLayout(new GridLayout(6, 1));

		rightInterface.add(new JLabel("Select City"));

		String[] cities = { "Quebec City", "Windsor", "Markham", "Ottawa", "CharlotteTown" };
		cityList = new JComboBox<>(cities);

		cityList.setActionCommand("CityList");
		cityList.addActionListener(this);

		rightInterface.add(cityList);

	}
	/**
	 * sets the sliders that change the time period that is being viewed
	 */
	private void setSliders() {
		
		// creating the year slider
		yearSlider = new JSlider(JSlider.HORIZONTAL, startYear, endYear, startYear);
		yearSlider.setMajorTickSpacing(1);
		yearSlider.setPaintTicks(true);
		yearSlider.setPaintLabels(true);
		yearSlider.setFont(new Font("", Font.PLAIN, 11));
		middleInterface.add(yearSliderLabel);
		middleInterface.add(yearSlider);
		
		// creating the month slider
		monthSlider = new JSlider(JSlider.HORIZONTAL, 1, 12, 1);
		monthSlider.setMajorTickSpacing(1);
		monthSlider.setPaintTicks(true);
		monthSlider.setPaintLabels(true);
		monthSlider.setFont(new Font("", Font.PLAIN, 11));

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		// if the toggle button was clicked
		case "Toggle":
			if (modeToggle.isSelected()) {
				// switch the mode to daily
				modeToggle.setText("Daily");
				
				// add the month slider to the middle interface
				middleInterface.add(monthSliderLabel);
				middleInterface.add(monthSlider);
				middleInterface.repaint();
				middleInterface.revalidate();
				mode = 1;

			} else {
				// switch the mode to monthly
				modeToggle.setText("Monthly");
				
				// remove the month slider from middle interface
				middleInterface.remove(monthSlider);
				middleInterface.remove(monthSliderLabel);
				middleInterface.repaint();
				middleInterface.revalidate();
				mode = 0;
			}
			break;
			// if the raido buttons were cliked change the field accordingly
		case temperature:
			field = temperature;
			break;
		case rain:
			field = rain;
			break;
		case snow:
			field = snow;
			break;
		case precip:
			field = precip;
			break;

		}
	}
	
	// various getters and setters that may be required
	public JSlider getYearSlider() {
		return yearSlider;
	}

	public void setYearSlider(JSlider yearSlider) {
		this.yearSlider = yearSlider;
	}

	public JSlider getMonthSlider() {
		return monthSlider;
	}

	public void setMonthSlider(JSlider monthSlider) {
		this.monthSlider = monthSlider;
	}


	public JRadioButton getTempBtn() {
		return tempBtn;
	}

	public void setTempBtn(JRadioButton tempBtn) {
		this.tempBtn = tempBtn;
	}

	public JRadioButton getRainFallBtn() {
		return rainFallBtn;
	}

	public void setRainFallBtn(JRadioButton rainFallBtn) {
		this.rainFallBtn = rainFallBtn;
	}

	public JRadioButton getSnowFallBtn() {
		return snowFallBtn;
	}

	public void setSnowFallBtn(JRadioButton snowFallBtn) {
		this.snowFallBtn = snowFallBtn;
	}

	public JRadioButton getPrecipBtn() {
		return precipBtn;
	}

	public void setPrecipBtn(JRadioButton precipBtn) {
		this.precipBtn = precipBtn;
	}

	public String getField() {
		return field;
	}

	public int getMode() {
		return mode;
	}
	
	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}

	public City getSelectedCity() {
		return selectedCity;
	}

	public JComboBox<String> getCityList() {
		return cityList;
	}

	public JToggleButton getModeToggle() {
		return modeToggle;
	}

	

}
