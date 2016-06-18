package com.NonGuI;

import java.util.ArrayList;
/**
 * 
 * @author Sagnik
 * holds monthly data
 * holds an arraylist of days that hold daily data
 * is a child class of ReadsWeatherData because it gets data from the files
 */
public class Month extends ReadsWeatherData {
	
	// holds daily data
	private ArrayList<Day> days = new ArrayList<>();

	public Month(int number) {
		super(number);
	}

	public ArrayList<Day> getDaysList() {
		return days;
	}

	public void setDays(ArrayList<Day> days) {
		this.days = days;
	}
	
   // calculate the min and max temperature for the month
	public void calulateExtremeTemp(){
		for(int i =0; i<days.size(); i++ ){
			if(i==0){
				
				minTemp = days.get(i).getMinTemp();
				maxTemp = days.get(i).getMaxTemp();
				
			} 
			
			if(days.get(i).getMinTemp()<minTemp){
				
				minTemp = days.get(i).getMinTemp();
			}
			
			if(days.get(i).getMaxTemp()>maxTemp){
				maxTemp = days.get(i).getMaxTemp();
			}
		}
	}

}
