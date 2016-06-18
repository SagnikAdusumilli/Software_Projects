package com.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
/**
 * 
 * @author Sagnik
 * This class downloads the required weather files from the net
 */
public class Downloader {
	
	// this is the URL that is used to download the files
	private static final String DATA_URL = "http://climate.weather.gc.ca/climateData/bulkdata_e.html?format=csv&stationID=%d&Year=%d&Month=4&Day=1&timeframe=%d&submit=Download+Data";
	
	// each city is marked by an ID, by changing the ID in the URL, it downloads files for different cities
	private static final int MARKHAM_ID = 4841;
	private static final int CHARLOTTETOWN_ID = 6526;
	private static final int QUEBEC_ID = 26892;
	private static final int OTTAWA_ID = 5585;
	private static final int WINDSOR_ID  = 4716;

	// stores all the files in the data folder
	private static final File DATA_FILE_DIRECTORY = new File("Data");
	// this is the format of the name of the daily data files
	private static final String DAILY_FILE_NAME = "%s-daily-%d.csv";
	// this is a format of the name of the mothly data files
	private static final String MONTHLY_FILE_NAME = "%s-monthly.csv";
	
	/**
	 * Josiah
	 * creates the data folder if the folder does not exist
	 */
	public Downloader(){
		if (!DATA_FILE_DIRECTORY.exists()) {
		    System.out.println("Creating 'Data' Folder");
		    boolean createdFolder = false;
		    try{
		        DATA_FILE_DIRECTORY.mkdir();
		        createdFolder = true;
		    } 
		    catch(SecurityException e){
		    
		    }        
		    if(createdFolder){    
		        System.out.println("Created 'Data' Folder");  
		    }
		}
	}
	
	/**
	 * @author Sagnik
	 * downloads all the files for all the cites
	 */
	public void runDownloader(){
		
		downloadData("Quebec", 1998, 2007, DATA_URL, QUEBEC_ID);
		downloadData("CharlotteTown", 1998, 2007, DATA_URL, CHARLOTTETOWN_ID);
		downloadData("Markham", 1998, 2007, DATA_URL, MARKHAM_ID );
		downloadData("Ottawa",1998,2007,DATA_URL, OTTAWA_ID);
		downloadData("Windsor",1998,2007,DATA_URL,WINDSOR_ID);
		
	}

	/**
	 * @author Sagnik
	 * This method downloads daily weather data files and also the monthly weather file from a city ranging from
	 * startYear to endYear, both inclusive
	 * the id field is used to identify the city data we want
	 * @param city
	 * @param startYear
	 * @param endYear
	 * @param link
	 * @param id
	 */

	public static void downloadData(String city, int startYear, int endYear, String link, int id) {
		for (int i = startYear; i <= endYear; i++) {
			String url = "";
			URL website;
			ReadableByteChannel rbc = null;
			try {
				if (i == startYear) {
					// frame 3 is passed into the url which signifies monthly data
					url = String.format(link, id, endYear, 3);

					website = new URL(url);
					rbc = Channels.newChannel(website.openStream());
					
					// Downloading the data
					FileOutputStream fos = new FileOutputStream(String.format("."+File.separator+"Data"+File.separator+MONTHLY_FILE_NAME, city));
					System.out.println("Downloading: " + String.format(MONTHLY_FILE_NAME, city));
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

				}

				// frame 2 is passed into the url which daily data monthly data
				url = String.format(link, id, i, 2);
				website = new URL(url);
				rbc = Channels.newChannel(website.openStream());
				
				// Downloading the data
				FileOutputStream fos = new FileOutputStream(String.format("."+File.separator+"Data"+File.separator+DAILY_FILE_NAME, city, i));
				System.out.println("Downloading: " + String.format(DAILY_FILE_NAME, city, i));
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

			} catch (IOException e) {
				System.err.println("Error occurred");
			}
		}
	}

}
