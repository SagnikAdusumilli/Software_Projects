package com.GuI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import com.Data.DataOrganizer;
import com.NonGuI.City;
import com.NonGuI.Day;
import com.NonGuI.Month;
/** This class displays a box at the right side of the map interface
 *  
 * @author Calvin
 *
 */
public class PopUpPanel extends JPanel implements ActionListener {

	private ImageIcon windsorImage = new ImageIcon("./Images/windsor.png");
	private ImageIcon markhamImage = new ImageIcon("./Images/Markham.png");
	private ImageIcon ottawaImage = new ImageIcon("./Images/Ottawa.png");
	private ImageIcon charlotteTownImage = new ImageIcon("./Images/CharlotteTown.png");
	private ImageIcon quebecImage = new ImageIcon("./Images/Quebec.png");

	private JPanel imagePanel;
	private JLabel cityLabel;

	private String name;
	private JLabel nameLabel;

	private JComboBox<String> yearsMenu;
	private JComboBox<String> monthMenu;

	final private int START_YEAR = 1998;
	final private int END_YEAR = 2007;

	private DataOrganizer organizer;

	private JPanel reportPanel;
	private JLabel maxTempLabel;
	private JLabel minTempLabel;
	private JLabel occurrenceMax;
	private JLabel occurrenceMin;
	private JLabel increaseLabel;
	private JLabel decreaseLabel;
	private JLabel increaseIntervalLabel;
	private JLabel decreaseIntervalLabel;
	private JPanel datePanel;
	private JToggleButton toggleButton;
	private JLabel monthLabel;

	private boolean intialized;


	String[] months = { "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" };

	/** Constructor of popUpPanel()
	 * 
	 * @param organizer ; height ; width
	 */

	public PopUpPanel(DataOrganizer organizer, int height, int width) {

		//Set the suze if the popUpPanel
		setPreferredSize(new Dimension(width/4, height));
		setOpaque(true);
		setBackground(Color.GRAY);

		this.organizer = organizer;

		// Setting globals to new
		reportPanel = new JPanel();
		imagePanel = new JPanel();
		occurrenceMax = new JLabel("");
		maxTempLabel = new JLabel("");
		occurrenceMin = new JLabel("");
		minTempLabel = new JLabel("");
		cityLabel = new JLabel();
		increaseIntervalLabel = new JLabel();
		decreaseIntervalLabel = new JLabel();
		increaseLabel = new JLabel();
		decreaseLabel = new JLabel();
		datePanel = new JPanel();

		createLayout();
	}

	/*** Displays the city names and its image.
	 * @param name
	 *  */
	public void display(String name) {

		//Sets properties for the font of the city name.
		nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 35));
		nameLabel.setText("<html> <font color = white> <u><br> " + name
				+ "</u></br> </font> </html>");

		if (name.equals("Windsor")) { //If name is equal to Windsor, set the cityLabel to windsor image.
			cityLabel.setIcon(windsorImage);
		}

		if (name.equals("Markham")) {
			cityLabel.setIcon(markhamImage);
		}

		if (name.equals("Charlotte Town")) {
			cityLabel.setIcon(charlotteTownImage);
		}

		if (name.equals("Quebec City")) {
			cityLabel.setIcon(quebecImage);
		}

		if (name.equals("Ottawa")) {
			cityLabel.setIcon(ottawaImage);
		}


		minTempLabel.setText("");
		maxTempLabel.setText("");

		monthMenu.setEnabled(true);
		yearsMenu.setEnabled(true);

		repaint();
		revalidate();
	}
	/*** Displays all the JPanels and JLabels of the entire map interface for popUpPanel()
	 * 
	 *  */
	public void createLayout() {
		setLayout(new GridLayout(5, 1)); // (Rows, Columns)

		imagePanel.setOpaque(true);
		imagePanel.add(cityLabel); 
		add(imagePanel); //Adds an image of the city based from the display() method

		JPanel namePanel = new JPanel();

		//Sets up the city name panel. Displays the name of the city.
		nameLabel = new JLabel();
		namePanel.setOpaque(true);
		namePanel.setBackground(Color.RED);
		namePanel.add(nameLabel);
		add(namePanel);

		datePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		//Display instructions for the users
		JLabel yearsLabel = new JLabel("Choose year"); 

		String[] years = new String[END_YEAR - START_YEAR + 1];

		for (int i = START_YEAR; i <= END_YEAR; i++) {
			years[i - START_YEAR] = "" + i;
		}

		//Sets up a JCombox so the user can select a specific year.
		yearsMenu = new JComboBox<>(years);
		yearsMenu.setActionCommand("Year Menu");
		yearsMenu.addActionListener(this);

		//Adds the JComboBox and the instructions
		datePanel.add(yearsLabel);
		datePanel.add(yearsMenu);

		//Displays instructions for the sers
		monthLabel = new JLabel("Choose month");

		//Sets up a JCombox so the user can select a specific month.
		monthMenu = new JComboBox<>(months);
		monthMenu.setActionCommand("Month Menu");
		monthMenu.addActionListener(this);

		//Adds the JComboBox and the instructions
		monthMenu.setEnabled(false);
		yearsMenu.setEnabled(false);

		//Togglebutton to display the JComboBox of months to display daily data
		toggleButton = new JToggleButton("Displaying Monthly Data", false);
		toggleButton.setActionCommand("Toggle");
		toggleButton.addActionListener(this);

		add(datePanel);

		//Sets up a toggle panel for the touggleButton
		JPanel togglePanel = new JPanel();
		togglePanel.add(toggleButton);
		add(togglePanel);

		//The panel where it will display maximum temperature, minimum temperature, and the intervals
		reportPanel.setLayout(new GridLayout(4, 2));

		//First row
		reportPanel.add(occurrenceMax); //When the max temperature occurred
		reportPanel.add(maxTempLabel);

		//Second Row
		reportPanel.add(occurrenceMin);//When the min temperature occurred
		reportPanel.add(minTempLabel);

		add(reportPanel);

	}

	/*** To display the maximum temperature and its occurrence when selecting years */
	public double getMaxTempYear(City city, int year) {

		double maxTemp = 0;
		int count = 0;

		for (Month m : city.getYear(year).getMonthsList()) { //For every month in a specific year

			if (count == 0) { //If count is 0
				occurrenceMax.setText("Occ. Month: "
						+ months[m.getNumber() - 1]); //Subtract 1 to start at array zero. 
				maxTemp = m.getMaxTemp(); //set first month's temperature as max.
			}

			count++;

			if (maxTemp < m.getMaxTemp()) { //if the max temp is less than the next month's temperature
				occurrenceMax.setText("Occ. Month: "
						+ months[m.getNumber() - 1]);
				maxTemp = m.getMaxTemp(); //Set the next month's temperature as the max temp
			}
		}
		return maxTemp;
	}

	/*** To display the maximum temperature and its occurrence when selecting months*/
	public double getMaxTempMonth(City city, int year, int month) {

		double maxTemp = 0;
		int count = 0;

		for (Day d : city.getYear(year).getMonth(month).getDaysList()) { //For every day in a specific year and a specific month

			if (count == 0) { //If count is 0
				occurrenceMax.setText("Occ. Day: " + d.getNumber());
				maxTemp = d.getMaxTemp(); //set day month's temperature as max.
			}

			count++;

			if (maxTemp < d.getMaxTemp()) { //If the max temperature for a day is less than the next day's temperature
				occurrenceMax.setText("Occ. Day: " + d.getNumber());
				maxTemp = d.getMaxTemp(); //set day month's temperature as max.
			}
		}
		return maxTemp;
	}

	/*** To display the minimum temperature and its occurrence when selecting years*/
	public double getMinTempYear(City city, int year) {

		double minTemp = 0;
		int count = 0;

		// Every month in months' list
		for (Month m : city.getYear(year).getMonthsList()) { //For every month in a specific year

			if (count == 0) {
				occurrenceMin.setText("Occ. Month: "
						+ months[m.getNumber() - 1]);
				minTemp = m.getMinTemp();
			}

			count++;

			if (minTemp > m.getMinTemp()) {
				occurrenceMin.setText("Occ. Month: "
						+ months[m.getNumber() - 1]);
				minTemp = m.getMinTemp();
			}
		}
		return minTemp;
	}

	/*** To display the minimum temperature and its occurrence when selecting months*/
	public double getMinTempMonth(City city, int year, int month) {

		double minTemp = 0;
		int count = 0;

		for (Day d : city.getYear(year).getMonth(month).getDaysList()) {

			if (count == 0) {
				occurrenceMin.setText("Occ. Day: " + d.getNumber());
				minTemp = d.getMinTemp();
			}

			count++;

			if (minTemp > d.getMinTemp()) {
				occurrenceMin.setText("Occ. Day: " + d.getNumber());
				minTemp = d.getMinTemp();
			}
		}
		return minTemp;
	}

	/*** In order to display the increasing intervals when selecting years, it must have three variables and 
	 * follows moves 
	 * @param city; year
	 * */
	public String increaseIntervalYear(City city, int year) {

		int count = 1;
		int beginningNumber = 0; //Starting interval
		
		//Main variables
		double nextNumber = 0;
		double previousNumber = 0;
		double middleNumber = 0;

		String increaseIntervals = " ";
		String intervalText = " ";

		for (Month m : city.getYear(year).getMonthsList()) {

			if (count == 1) { //if count is equal to 1, 
				beginningNumber = 1; //Set the first interval value to 1. 
				previousNumber = m.getAvgTemp(); //Set the first main variable as the temperature.
			}

			if (count == 2){ //If count is equal to 2
				middleNumber = m.getAvgTemp(); // Set the second main variable as the temperature
			}

			if (count > 3){ //If count is great than 3, start shifting forward 
				previousNumber = middleNumber; //The second main variable becomes the first main variable
				middleNumber = nextNumber; //The third main variable becomes the second main variable
			}

			nextNumber = m.getAvgTemp(); // Set the third main variable as the temperature

			//Ignore minimum code. This is checking for decreasing intervals but doesn't display anything. Required for program to function
			if (middleNumber < nextNumber && middleNumber < previousNumber && count > 2) { //If the middleNumber is less than the previous number and next number, it is a minimum point
				beginningNumber = count - 1; //Subtract 1 to refresh the new interval accurately
			}

			if (middleNumber > nextNumber && middleNumber > previousNumber && count > 2){ //If the middleNumber is greater than the previous number and next number, it is a maximum point
				increaseIntervals = increaseIntervals.concat(months[beginningNumber - 1] //-1 because array starts at 0 and not 1
						+ " to " + months[count - 2] + " | "); // -1 because array starts at 0. Another -1 because the value should be using the second main variable.
				beginningNumber = count - 1; //Subtract 1 to refresh the new interval accurately
			}

			count++;

			if (count == city.getYear(year).getMonthsList().size()){ //If the count is at its greatest
				if (nextNumber > middleNumber){ //if the end number is greater than the middle number, it is an increasing interval/maximum point.
					increaseIntervals = increaseIntervals.concat(months[beginningNumber - 1]
							+ " to " + months[count - 1] + " | "); // -1 because array starts at 0.
				}
			}
		}

		intervalText = intervalText.concat(" " + increaseIntervals + "\n");

		return intervalText;

	}
	
	/*** In order to display the decreasing intervals when selecting years, it must have three variables and 
	 * follows moves. Planned algorithm is the same, this time it displays the decreasing intervals neatly. 
	 * @param city; year
	 * */
	public String decreaseIntervalYear(City city, int year) {

		int count = 1;
		int beginningNumber = 0; //Starting interval
		double nextNumber = 0;
		double previousNumber = 0;
		double middleNumber = 0;

		String decreaseIntervals = " ";
		String intervalText = " ";


		for (Month m : city.getYear(year).getMonthsList()) {

			if (count == 1) { //if count is equal to 1, set the first number to 1.
				beginningNumber = 1;
				previousNumber = m.getAvgTemp();
			}

			if (count == 2){
				middleNumber = m.getAvgTemp();
			}

			if (count > 3){
				previousNumber = middleNumber;
				middleNumber = nextNumber;
			}

			nextNumber = m.getAvgTemp();

			if (middleNumber < nextNumber && middleNumber < previousNumber && count > 2) {

				decreaseIntervals = decreaseIntervals.concat(months[beginningNumber - 1]
						+ " to " + months[count - 2] + " | ");
				beginningNumber = count - 1;
			}

			//Ignore Maximum
			if (middleNumber > nextNumber && middleNumber > previousNumber && count > 2){
				beginningNumber = count - 1;
			}

			count++;

			if (count == city.getYear(year).getMonthsList().size()){ //If count reached its limit

				if (nextNumber < middleNumber) { //if the count's temperature is less than the middlenumber, then it is decreasing/minimum point
					decreaseIntervals = decreaseIntervals.concat(months[beginningNumber - 1]
							+ " to " + months[count - 1] + " | ");
				}
			}

		}

		intervalText = intervalText.concat(" " + decreaseIntervals + "\n");

		return intervalText;

	}

	/*** In order to display the increasing intervals when selecting months, it must have three variables and 
	 * follows moves.
	 * @param city; year; month
	 * */
	public String increaseIntervalMonth(City city, int year, int month){

		int count = 1;
		int beginningNumber = 0; //Starting interval
		double nextNumber = 0;
		double previousNumber = 0;
		double middleNumber = 0;

		String increaseIntervals = " ";
		String intervalText = " ";

		for (Day d : city.getYear(year).getMonth(month).getDaysList()) {

			if (count == 1) { //if count is equal to 1, set the first number to 1.
				beginningNumber = 1;
				previousNumber = d.getAvgTemp();
			}

			if (count == 2){ //
				middleNumber = d.getAvgTemp();
			}

			if (count > 3){
				previousNumber = middleNumber;
				middleNumber = nextNumber;
			}

			nextNumber = d.getAvgTemp();

			//Ignore minimum code
			if (middleNumber < nextNumber && middleNumber < previousNumber && count > 2) {
				beginningNumber = count - 1;
			}
			
			
			//Sets the English numerals based on the numbers.
			if (middleNumber > nextNumber && middleNumber > previousNumber && count > 2){

				if (beginningNumber/10 > 1 || beginningNumber/10 == 0){

					switch(beginningNumber % 10) {

					case 1: 
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;

					case 2:
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;

					case 3 :
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;

					default:
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;
					}
				}
				else  {
					increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
							+ " to " + (count - 1) + "th | ");
				}

				beginningNumber = count - 1;
			}

			count++;

			if (count == city.getYear(year).getMonth(month).getDaysList().size()+1){

				if (nextNumber > middleNumber){

					if (beginningNumber/10 > 1 || beginningNumber/10 == 0){
						switch(beginningNumber % 10) {

						case 1: 
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;

						case 2:
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;

						case 3 :
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;

						default:
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									increaseIntervals = increaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;
						}
					}
				}
			}
		}

		intervalText = intervalText.concat(increaseIntervals);

		return intervalText;
	}

	/*** In order to display the decreasing intervals when selecting months, it must have three variables and 
	 * follows moves.
	 * @param city; year; month
	 * */
	public String decreaseIntervalMonth(City city, int year, int month){

		int count = 1;
		int beginningNumber = 0; //Starting interval
		double nextNumber = 0;
		double previousNumber = 0;
		double middleNumber = 0;

		String decreaseIntervals = " ";
		String intervalText = " ";

		for (Day d : city.getYear(year).getMonth(month).getDaysList()) {

			if (count == 1) { //if count is equal to 1, set the first number to 1.
				beginningNumber = 1;
				previousNumber = d.getAvgTemp();
			}

			if (count == 2){ //
				middleNumber = d.getAvgTemp();
			}

			if (count > 3){
				previousNumber = middleNumber;
				middleNumber = nextNumber;
			}

			nextNumber = d.getAvgTemp();

			if (middleNumber < nextNumber && middleNumber < previousNumber && count > 2) {


				if (beginningNumber/10 > 1 || beginningNumber/10 == 0){
					switch(beginningNumber % 10) {

					case 1: 
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;

					case 2:
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;

					case 3 :
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;

					default:
						if ((count-1)/10 > 1 || count/10 == 0){
							switch((count-1)% 10){

							case 1: 
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "st | ");
								break;

							case 2:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "nd | ");
								break;

							case 3 :
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "rd | ");
								break;
							default:
								decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
										+ " to " + (count - 1) + "th | ");
								break;

							}
						}
						break;
					}
				}
				else  {
					decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
							+ " to " + (count - 1) + "th | ");
				}

				beginningNumber = count - 1;
			}

			//Ignore Maximum
			if (middleNumber > nextNumber && middleNumber > previousNumber && count > 2){
				beginningNumber = count - 1;
			}

			count++;

			if (count == city.getYear(year).getMonth(month).getDaysList().size()+1){

				if (nextNumber < middleNumber) {
					if (beginningNumber/10 > 1 || beginningNumber/10 == 0){
						switch(beginningNumber % 10) {

						case 1: 
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"st" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;

						case 2:
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"nd" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;

						case 3 :
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"rd" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;

						default:
							if ((count-1)/10 > 1 || count/10 == 0){
								switch((count-1)% 10){

								case 1: 
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "st | ");
									break;

								case 2:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "nd | ");
									break;

								case 3 :
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "rd | ");
									break;
								default:
									decreaseIntervals = decreaseIntervals.concat(beginningNumber+"th" 
											+ " to " + (count - 1) + "th | ");
									break;

								}
							}
							break;
						}
					}
				}
			}
		}

		intervalText = intervalText.concat(decreaseIntervals);

		return intervalText;
	}

	/*** Getter */
	public String getName() {
		return name;
	}

	/*** Setter */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Toggle")){

			if (toggleButton.isSelected()){
				toggleButton.setText("Displaying Daily Data");
				datePanel.add(monthLabel);
				datePanel.add(monthMenu);
			}
			else  {
				toggleButton.setText("Displaying Monthly Data");
				datePanel.remove(monthLabel);
				datePanel.remove(monthMenu);
			}
		}

		// If using Year dropbox menu.
		if (e.getActionCommand().equals("Year Menu")) {

			increaseLabel.setText("Increasing intervals");
			decreaseLabel.setText("Decreasing intervals");

			if (!intialized){ //To make sure scrollpanes are not added at the start of the program.

				intialized = true;

				//Third Row
				reportPanel.add(increaseLabel);
				JScrollPane increasePane = new JScrollPane(increaseIntervalLabel);
				increasePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				reportPanel.add(increasePane);

				//Fourth Row
				reportPanel.add(decreaseLabel);			
				JScrollPane decreasePane = new JScrollPane(decreaseIntervalLabel);
				decreasePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				reportPanel.add(decreasePane);

			}
			//Displays the data
			switch (name) {
			case ("Charlotte Town"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempYear(organizer.getCharlotteTown(),
								yearsMenu.getSelectedIndex()) + "°C");
			minTempLabel.setText("Min Temperature: "
					+ getMinTempYear(organizer.getCharlotteTown(),
							yearsMenu.getSelectedIndex()) + "°C");
			increaseIntervalLabel.setText(increaseIntervalYear(organizer.getCharlotteTown(),
					yearsMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(decreaseIntervalYear(organizer.getCharlotteTown(),
					yearsMenu.getSelectedIndex()));

			break;
			case ("Markham"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempYear(organizer.getMarkham(),
								yearsMenu.getSelectedIndex())+ "°C");
			minTempLabel.setText("Min Temperature: "
					+ getMinTempYear(organizer.getMarkham(),
							yearsMenu.getSelectedIndex()) + "°C");
			increaseIntervalLabel.setText(increaseIntervalYear(organizer.getMarkham(),
					yearsMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(decreaseIntervalYear(organizer.getMarkham(),
					yearsMenu.getSelectedIndex()));
			break;
			case ("Windsor"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempYear(organizer.getWindsor(),
								yearsMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min Temperature: "
					+ getMinTempYear(organizer.getWindsor(),
							yearsMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(increaseIntervalYear(organizer.getWindsor(),
					yearsMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(decreaseIntervalYear(organizer.getWindsor(),
					yearsMenu.getSelectedIndex()));
			break;
			case ("Ottawa"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempYear(organizer.getOttawa(),
								yearsMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min Temperature: "
					+ getMinTempYear(organizer.getOttawa(),
							yearsMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(increaseIntervalYear(organizer.getOttawa(),
					yearsMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(decreaseIntervalYear(organizer.getOttawa(),
					yearsMenu.getSelectedIndex()));
			break;
			case ("Quebec"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempYear(organizer.getQuebecCity(),
								yearsMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min Temperature: "
					+ getMinTempYear(organizer.getQuebecCity(),
							yearsMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(increaseIntervalYear(organizer.getQuebecCity(),
					yearsMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(decreaseIntervalYear(organizer.getQuebecCity(),
					yearsMenu.getSelectedIndex()));
			break;
			}

		}
		// Else if using Month dropbox menu
		else if (e.getActionCommand().equals("Month Menu")) {
			switch (name) {
			
			//Displays the data
			case ("Charlotte Town"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempMonth(organizer.getCharlotteTown(),
								yearsMenu.getSelectedIndex(),
								monthMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min temperature: "
					+ getMinTempMonth(organizer.getCharlotteTown(),
							yearsMenu.getSelectedIndex(),
							monthMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(" "
					+ increaseIntervalMonth(organizer.getCharlotteTown(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(" "
					+ decreaseIntervalMonth(organizer.getCharlotteTown(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			break;
			case ("Markham"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempMonth(organizer.getMarkham(),
								yearsMenu.getSelectedIndex(),
								monthMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min temperature: "
					+ getMinTempMonth(organizer.getMarkham(),
							yearsMenu.getSelectedIndex(),
							monthMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(" "
					+ increaseIntervalMonth(organizer.getMarkham(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(" "
					+ decreaseIntervalMonth(organizer.getMarkham(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			break;
			case ("Windsor"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempMonth(organizer.getWindsor(),
								yearsMenu.getSelectedIndex(),
								monthMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min temperature: "
					+ getMinTempMonth(organizer.getWindsor(),
							yearsMenu.getSelectedIndex(),
							monthMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(" "
					+ increaseIntervalMonth(organizer.getWindsor(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(" "
					+ decreaseIntervalMonth(organizer.getWindsor(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			break;
			case ("Ottawa"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempMonth(organizer.getOttawa(),
								yearsMenu.getSelectedIndex(),
								monthMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min temperature: "
					+ getMinTempMonth(organizer.getOttawa(),
							yearsMenu.getSelectedIndex(),
							monthMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(" "
					+ increaseIntervalMonth(organizer.getOttawa(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(" "
					+ decreaseIntervalMonth(organizer.getOttawa(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			break;
			case ("Quebec"):
				maxTempLabel.setText("Max Temperature: "
						+ getMaxTempMonth(organizer.getQuebecCity(),
								yearsMenu.getSelectedIndex(),
								monthMenu.getSelectedIndex()) + "°C" );
			minTempLabel.setText("Min temperature: "
					+ getMinTempMonth(organizer.getQuebecCity(),
							yearsMenu.getSelectedIndex(),
							monthMenu.getSelectedIndex()) + "°C" );
			increaseIntervalLabel.setText(" "
					+ increaseIntervalMonth(organizer.getQuebecCity(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			decreaseIntervalLabel.setText(" "
					+ decreaseIntervalMonth(organizer.getQuebecCity(),
							yearsMenu.getSelectedIndex(), monthMenu.getSelectedIndex()));
			break;
			}
		}


		repaint();
		revalidate();

	}

}
