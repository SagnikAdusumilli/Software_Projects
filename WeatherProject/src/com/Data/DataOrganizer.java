package com.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.NonGuI.City;
import com.NonGuI.Day;
import com.NonGuI.Month;
import com.NonGuI.Year;

/**
 * 
 * @author Sagnik
 * This class reads data from the data files and puts them into city objects, thereby organizing the data by cities
 */
public class DataOrganizer {

	private static BufferedReader br;
	
	// after this line in the files, useful data starts
	private static final String TO_SKIP = "Spd of Max Gust Flag";
	
	// pathways of the files that need to be read (daily data)
	private static final String CHARLOTTE_DAILY = "."+File.separator+"Data"+File.separator+"CharlotteTown-daily-%d.csv";
	private static final String QUEBEC_DAILY = "."+File.separator+"Data"+File.separator+"Quebec-daily-%d.csv";
	private static final String MARKHAM_DAILY = "."+File.separator+"Data"+File.separator+"Markham-daily-%d.csv";
	private static final String WINDSOR_DAILY = "."+File.separator+"Data"+File.separator+"Windsor-daily-%d.csv";
	private static final String OTTAWA_DAILY = "."+File.separator+"Data"+File.separator+"Ottawa-daily-%d.csv";
	
	// pathways of the files that need to be read (monthly data)
	private static final String QUEBEC_MONTHLY = "."+File.separator+"Data"+File.separator+"Quebec-monthly.csv";
	private static final String CHARLOTTE_MONTHLY = "."+File.separator+"Data"+File.separator+"CharlotteTown-monthly.csv";
	private static final String MARKHAM_MONTHLY = "."+File.separator+"Data"+File.separator+"Markham-monthly.csv";
	private static final String WINDSOR_MONTHLY= "."+File.separator+"Data"+File.separator+"Windsor-monthly.csv";
	private static final String OTTAWA_MONTHLY = "."+File.separator+"Data"+File.separator+"Ottawa-monthly.csv";
	
	// holds monthly data
	private static ArrayList<String[]> monthlyData = new ArrayList<>();
	// holds daily data
	private static ArrayList<String[]> dailyData = new ArrayList<>();

	// fixed columns of the data we are looking for (daily data)
	private static final int YEAR_INDEX = 1;
	private static final int MONTHLY_MAX_TEMP = 3;
	private static final int MONTHLY_MIN_TEMP = 5;
	private static final int MONTHLY_AVG_TEMP = 7;
	private static final int MONTHLY_RAIN_INDEX = 13;
	private static final int MONTHLY_SNOW_INDEX = 15;
	private static final int MONTHLY_PRECIP_INDEX = 17;

	// fixed columns of the data we are looking for (monthly data)
	private static final int MONTH_INDEX = 2;
	private static final int DAY_INDEX = 3;
	private static final int DAILY_MAX_TEMP = 5;
	private static final int DAILY_MIN_TEMP = 7;
	private static final int DAILY_AVG_TEMP = 9;
	private static final int DAILY_RAIN_INDEX = 15;
	private static final int DAILY_SNOW_INDEX = 17;
	private static final int DAILY_PRECIP_INDEX = 19;

	// marks the starting and the ending interval
	private static  int END_YEAR = 2007;
	private static  int START_YEAR = 1998;
	
	// these are the different cities that hold the data
	private City quebecCity = new City("Quebec City");
	private City charlotteTown = new City("CharlotteTown");
	private City markham = new City("Markham");
	private City ottawa = new City("Ottawa");
	private City windsor = new City("Windsor");

	public DataOrganizer(int startYear, int endYear) throws IOException {
		
		// setting the starting and the ending interval
		this.START_YEAR = startYear;
		this.END_YEAR = endYear;
		
       
    // getting and inserting monthly data for quebecCity
		getData(QUEBEC_MONTHLY, monthlyData); 
		insertMonthlyData(quebecCity);
		// calculating the average temperature for all the years
		quebecCity.calculateAvgTemp();
		
		 // getting and inserting monthly data for Charlotte town
		getData(CHARLOTTE_MONTHLY, monthlyData);
		insertMonthlyData(charlotteTown);
		// calculating the average temperature for all the years
		charlotteTown.calculateAvgTemp();
		
		 // getting and inserting monthly data for Markham
		getData(MARKHAM_MONTHLY, monthlyData);
		insertMonthlyData(markham);
		markham.calculateAvgTemp();
		
		 // getting and inserting monthly data for Windsor
		getData(WINDSOR_MONTHLY, monthlyData);
		insertMonthlyData(windsor);
		// calculating the average temperature for all the years
		windsor.calculateAvgTemp();
		
		 // getting and inserting monthly data for Ottawa
		getData(OTTAWA_MONTHLY, monthlyData);
		insertMonthlyData(ottawa);
		// calculating the average temperature for all the years
		ottawa.calculateAvgTemp();
		
		// inserting all the daily data into the cites
		insertTotalDailyData(QUEBEC_DAILY, quebecCity);
		insertTotalDailyData(CHARLOTTE_DAILY, charlotteTown);
		insertTotalDailyData(MARKHAM_DAILY, markham);
		insertTotalDailyData(OTTAWA_DAILY, ottawa);
		insertTotalDailyData(WINDSOR_DAILY, windsor);
		
	}
	
	/**
	 *  gets the data from the specified file name and stores into the data arraylist
	 * @param fileName
	 * @param data
	 * @throws IOException
	 */

	public static void getData(String fileName, ArrayList<String[]> data) throws IOException {
		data.clear(); // resets the array, because it may contain already
		String line;
		boolean skip = true;
		br = new BufferedReader(new FileReader(new File(fileName)));
		while ((line = br.readLine()) != null) {// adding annual monthly data
			line += System.lineSeparator();
			// if the skip line has not come yet don't read
			if (!skip) {
				// remove all the ,
				line = line.replaceAll("[^0-9,.-]", "");
				// place a / for all the empty data sets
				line = line.replaceAll(",,", ",/,").replaceAll(",,", ",/,");
				// add a new line
				line += System.lineSeparator();
				// add the data to the arraylist passed in the parameter
				data.add(line.split(","));
			}
			if (line.contains(TO_SKIP)) {
				// set skip to false once the skip line occured
				skip = false;
			}
		}
	}

	/**
	 *  inserts all the monthly data into the city from start year to end year
	 * @param city
	 */
	public static void insertMonthlyData(City city) {

		Year currentYear = new Year(START_YEAR);
		
		int startingIndex = 0;
		for(String[] s: monthlyData){ 
			// start feeding in the monthly data only when the start year has occurred
			if(s[YEAR_INDEX].equals(String.valueOf(START_YEAR))){
				break;
			}
			startingIndex++;
		}
		
		for (int month = startingIndex; month < (END_YEAR - START_YEAR + 1) * 12 + startingIndex; month++) {
			// runs 12 times the numbers of years because each year has 12 months
			if(month%12==0){// create a year every 12 years;
			currentYear = new Year(Integer.parseInt(monthlyData.get(month)[YEAR_INDEX]));
			}
//			System.out.println("Year************: " + Integer.parseInt(monthlyData.get(month)[YEAR_INDEX])+"City: " +city.getName() );

			Month currentMonth = new Month((month % 12) + 1);

			// setting maxTemp, minTemp, avgTemp, rain, snow precipitation
			currentMonth.setAvgTemp(monthlyData.get(month)[MONTHLY_AVG_TEMP]);
			currentMonth.setTotalRain(monthlyData.get(month)[MONTHLY_RAIN_INDEX]);
			currentMonth.setTotalSnow(monthlyData.get(month)[MONTHLY_SNOW_INDEX]);
			currentMonth.setTotalPrecip(monthlyData.get(month)[MONTHLY_PRECIP_INDEX]);


			// add currenMont to current Year
			currentYear.getMonthsList().add(currentMonth);
			if(month%12==0){
				currentYear.calcuateAvgTemp();
				city.getYearsList().add(currentYear); // add current year to the list of years every 12 months
			}
													
		}
		
	}
	/**
	 * insets all daily data from the file specified to the city specified
	 * @param fileName
	 * @param city
	 * @throws IOException
	 */
	public void insertTotalDailyData(String fileName, City city) throws IOException{
		for (int i = START_YEAR; i <= END_YEAR; i++) {
			// getting daily data from start year to end year
			getData(String.format(fileName, i), dailyData); 
			// inserting daily data from star year to end year
			insertDailyData(city, i);
			city.getYear(i-START_YEAR).getMonth(11).calulateExtremeTemp();
		}
	}
	
	/**
	 * inserts the daily data for the year specified into the city specified
	 * @param city
	 * @param year
	 */
	public static void insertDailyData(City city, int year) {
		year = year-START_YEAR;
		int currentMonthNum = 1;
		int day = 0;
		
		// the currentMonth months holds the data for the mothly data of the current moth
		Month currentMonth = city.getYear(year).getMonthsList().get(currentMonthNum-1);
		
		while (currentMonthNum == currentMonth.getNumber()) {
			
			// keeping track of the month through the month column
			currentMonthNum = Integer.parseInt(dailyData.get(day)[MONTH_INDEX]);
//			System.out.println("Month*********: "+currentMonthNum+" City: "+city.getName());

			// if the month changes
			if (currentMonthNum != currentMonth.getNumber()) {
				// Calculate the min and max temperature of the current month
				currentMonth.calulateExtremeTemp();
				
				// change the month
				currentMonth = city.getYear(year).getMonthsList().get(currentMonthNum-1);
			}

			Day currentDay = new Day(Integer.parseInt(dailyData.get(day)[DAY_INDEX]));

			// setting maxTemp, minTemp, avgTemp, rain, snow precipitation
			currentDay.setMaxTemp(dailyData.get(day)[DAILY_MAX_TEMP]);
			currentDay.setMinTemp(dailyData.get(day)[DAILY_MIN_TEMP]);
			currentDay.setAvgTemp(dailyData.get(day)[DAILY_AVG_TEMP]);
			currentDay.setTotalRain(dailyData.get(day)[DAILY_RAIN_INDEX]);
			currentDay.setTotalSnow(dailyData.get(day)[DAILY_SNOW_INDEX]);
			currentDay.setTotalPrecip(dailyData.get(day)[DAILY_PRECIP_INDEX]);
			
			currentMonth.getDaysList().add(currentDay);
			day++;
			if (day >= dailyData.size()) {
//				System.out.println("end of data set");
				break;
			}
		}
	}
	
	// getters for getting the data for a particular city
	public City getQubecCity(){
		return quebecCity;
	}
	
	public City getCharlotteTown(){
		return charlotteTown;
	}

	public City getQuebecCity() {
		return quebecCity;
	}

	public City getMarkham() {
		return markham;
	}

	public City getOttawa() {
		return ottawa;
	}

	public City getWindsor() {
		return windsor;
	}
	
	
}
