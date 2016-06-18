package com.NonGuI;

/**
 * 
 * @author Sagnik This is superclass for all weather object that gets weather
 *         data from the organizer, therefore they have setters and getters
 */
public class ReadsWeatherData extends Weather {
	public ReadsWeatherData(int number) {
		setNumber(number);
	}

	protected int number = 1;

	public int getNumber() {
		return number;
	}

	protected void setNumber(int number) {
		this.number = number;
	}

	// if the the parameter passed is a / then it is empty, hence the flag is
	// set to true
	public void setMaxTemp(String maxTemp) {
		if (!maxTemp.equals("/")) {
			this.maxTemp = Double.parseDouble(maxTemp);
			setMinTempFlag(true);
		}
	}

	// if the the parameter passed is a / then it is empty, hence the flag is
	// set to true
	public void setMinTemp(String minTemp) {
		if (!minTemp.equals("/")) {
			this.minTemp = Double.parseDouble(minTemp);
			setMinTempFlag(true);
		}
	}

	// if the the parameter passed is a / then it is empty, hence the flag is
	// set to true
	public void setAvgTemp(String avgTemp) {
		if (!avgTemp.equals("/")) {
			this.avgTemp = Double.parseDouble(avgTemp);
			setAvgTempFlag(true);
		}
	}

	public double getTotalRain() {
		return totalRain;
	}

	// if the the parameter passed is a / then it is empty, hence the flag is
	// set to true
	public void setTotalRain(String totalRain) {
		if (!totalRain.equals("/")) {
			this.totalRain = Double.parseDouble(totalRain);
			setTotalRainFlag(true);
		}
	}

	public double getTotalSnow() {
		return totalSnow;
	}

	// if the the parameter passed is a / then it is empty, hence the flag is
	// set to true
	public void setTotalSnow(String totalSnow) {
		if (!totalSnow.equals("/")) {
			this.totalSnow = Double.parseDouble(totalSnow);
			setTotalSnowFlag(true);
		}
	}

	// if the the parameter passed is a / then it is empty, hence the flag is
	// set to true
	public void setTotalPrecip(String totalPrecip) {
		if (!totalPrecip.equals("/")) {
			this.totalPrecip = Double.parseDouble(totalPrecip);
			setTotalPrecipFlag(true);
		}
	}
}