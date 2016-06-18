package com.NonGuI;
/**
 * 
 * @author Sagnik
 * This class is an indirect superclass for all the classes that hold weather data (City, day, Year, Month)
 */
public abstract class Weather { // setters were not included as some child classes may not need setters
	// the flags are markers for missing data
	private boolean avgTempFlag, mxTempFlag, minTempFlag, totalSnowFlag, totalRainFlag, totalPrecipFlag;
	
	protected double avgTemp, maxTemp, minTemp, totalSnow, totalRain, totalPrecip;

	public double getAvgTemp() {
		return avgTemp;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public double getTotalSnow() {
		return totalSnow;
	}

	public double getTotalRain() {
		return totalRain;
	}

	public double getTotalPrecip() {
		return totalPrecip;
	}
	
	public boolean isAvgTempFlag() {
		return avgTempFlag;
	}

	public void setAvgTempFlag(boolean avgTempFlag) {
		this.avgTempFlag = avgTempFlag;
	}

	public boolean isMxTempFlag() {
		return mxTempFlag;
	}

	public void setMxTempFlag(boolean mxTempFlag) {
		this.mxTempFlag = mxTempFlag;
	}

	public boolean isMinTempFlag() {
		return minTempFlag;
	}

	public void setMinTempFlag(boolean minTempFlag) {
		this.minTempFlag = minTempFlag;
	}

	public boolean isTotalSnowFlag() {
		return totalSnowFlag;
	}

	public void setTotalSnowFlag(boolean totalSnowFlag) {
		this.totalSnowFlag = totalSnowFlag;
	}

	public boolean isTotalRainFlag() {
		return totalRainFlag;
	}

	public void setTotalRainFlag(boolean totalRainFlag) {
		this.totalRainFlag = totalRainFlag;
	}

	public boolean isTotalPrecipFlag() {
		return totalPrecipFlag;
	}

	public void setTotalPrecipFlag(boolean totalPrecipFlag) {
		this.totalPrecipFlag = totalPrecipFlag;
	}
	
	

}
