package com.NonGuI;

import java.util.ArrayList;

/**
 * 
 * @author Sagnik
 * holds annual weatherly data 
 * has a list of months that hold mothly data
 * this is a child class of Weather
 */
public class Year extends Weather {
	int year;
	// holds monthly data
	private ArrayList<Month> months = new ArrayList();
	
	public Year(int year){
		setYear(year);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public ArrayList<Month> getMonthsList(){
		return months;
	}
	
	public Month getMonth(int index){
		return months.get(index);
	}
	
	public void calcuateAvgTemp(){
		double avgTemp = 0;
		for(Month m: months){
			avgTemp += m.getAvgTemp();
		}
		
		this.avgTemp = avgTemp/months.size();
	}
	
}
