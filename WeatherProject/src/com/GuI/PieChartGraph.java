package com.GuI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.Data.DataOrganizer;
import com.NonGuI.City;
/**
 * 
 * @author Sagnik
 * This is displays the data of a city in the form of a pie chart
 *
 */
public class PieChartGraph extends JPanel {
	
	// this is the pie chart
	private PieChart dataChart;
	
	// layout of the display
	JPanel layout = new JPanel();
	
	// indices for the cities in the drop down menu
	public static final int quebecIndex = 0;
	public static final int windosorIndex = 1;
	public static final int markhamIndex = 2;
	public static final int ottawaIndex = 3;
	public static final int chareotteTownIndex = 4;
	
	// drop down menu to change the city
	private JComboBox<String> cityList;
	// keeps track of the current city
	private City selectedCity;
	
	// organzier hold the data of the cities
	private DataOrganizer organizer;
	
	/**
	 *  sets the data interval and the size of the display
	 * @param startYear
	 * @param endYear
	 * @param width
	 * @param height
	 * @param oragnizer
	 */
	public PieChartGraph(int startYear, int endYear, int width, int height, DataOrganizer oragnizer) {
		
		this.organizer = oragnizer;

		setLayout(new BorderLayout());
		
		// create layout
		layout = new JPanel();
		layout.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		setPreferredSize(new Dimension(width, height));

		// add chart
		dataChart = new PieChart(0, 0, 320, 320);
		gc.fill = GridBagConstraints.BOTH;
		gc.gridx = 0;
		gc.gridy = 0;
		layout.add(dataChart, gc);

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 1;

		// adding city drop down menu
		String[] cities = { "Quebec City", "Windsor", "Markham", "Ottawa", "CharlotteTown" };
		cityList = new JComboBox<>(cities);
		add(layout, BorderLayout.NORTH);

		layout.add(cityList, gc);

	}
	
	
	// getters and setters that may be required
	public City getSelectedCity() {
		return selectedCity;
	}



	public PieChart getDataChart() {
		return dataChart;
	}






	public JComboBox<String> getCityList() {
		return cityList;
	}



	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}
	
	
	
	

}
