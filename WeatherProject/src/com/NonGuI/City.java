package com.NonGuI;

import java.util.ArrayList;

/**
 * 
 * @author Sagnik
 * Holds the weather data of a city
 * it has the list of Years that holds the data of each year
 * is a subclass of Weather class
 */
public class City extends Weather {
	private String name;
	
	// holds yearly data
	private ArrayList<Year> years = new ArrayList<>();

	public City(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Year> getYearsList() {
		return years;
	}

	public Year getYear(int index) {
		return years.get(index);
	}
	
	/**
	 * calculating the average temp for all the years
	 */
	public void calculateAvgTemp() {
		double avgTemp = 0;
		for (Year y : years) {
			// adding up the total temperatures
			avgTemp += y.getAvgTemp();
		}
		// getting the averageTemp by dividing by number of years
		this.avgTemp = avgTemp / years.size();
	}
}
