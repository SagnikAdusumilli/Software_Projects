package com.GuI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.Data.DataOrganizer;
import com.GuI.BarChart;

public class BarChartController extends JPanel{

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private BarChart chart;
	private Integer startYear, endYear;

	static public int numericalIncrease = 50;
	private int hGap = screenSize.height/7;

	private JPanel topLayout, midLayout, botLayout;
	private String[] MONTH_LABEL = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	private JLabel empty = new JLabel("");
	private JLabel title = new JLabel("Settings");
	private JLabel toggleText = new JLabel("Select Type");
	private JLabel yearText = new JLabel("Select Year");
	private JLabel monthText = new JLabel("Select Month");
	private String[] city = {"Quebec", "Charlotte Town", "Markham", "Ottawa", "Windsor"};
	private JComboBox citySelector = new JComboBox(city);
	private JButton selectCity = new JButton("Select");
	private JRadioButton temperatureSelect = new JRadioButton("Temperature");
	private JRadioButton precipitationSelect = new JRadioButton("Precipitation");
	ButtonGroup selectionToggle = new ButtonGroup();
	private JSlider selectYear;
	private JSlider selectMonth;
	
	protected int dataType = 0;

	public BarChartController(DataOrganizer dataOrganizer, BarChart chart, Integer startYear, Integer endYear) throws IOException{

		this.chart = chart;
		selectYear = new JSlider(JSlider.HORIZONTAL, startYear, endYear, startYear);
		selectMonth = new JSlider(JSlider.HORIZONTAL, 0, 11, 0);
		// Initial values
		chart.setMaxValue(getMaxTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
		chart.setMinValue(getMinTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
		chart.setLength(dataOrganizer.getQuebecCity().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
				dataOrganizer.getCharlotteTown().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
				dataOrganizer.getMarkham().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
				dataOrganizer.getOttawa().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
				dataOrganizer.getWindsor().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp());
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, hGap));
		setPreferredSize(new Dimension((int)((screenSize.width*5)/16)-numericalIncrease, screenSize.height));
		topLayout = new JPanel();
		topLayout.setLayout(new GridLayout(1, 2, 50, 0));
		topLayout.add(citySelector);
		topLayout.add(selectCity);
		selectionToggle.add(temperatureSelect);
		selectionToggle.add(precipitationSelect);
		midLayout = new JPanel();
		midLayout.setLayout(new GridLayout(1, 2, 50, 0));
		midLayout.add(temperatureSelect);
		midLayout.add(precipitationSelect);
		temperatureSelect.setSelected(true);
		botLayout = new JPanel();
		botLayout.setLayout(new GridLayout(2, 1, 0, hGap));
		selectYear.setPreferredSize(new Dimension((screenSize.width*1)/4, hGap/2));
		botLayout.add(selectYear);
		selectYear.setMajorTickSpacing(1);
		selectYear.setPaintTicks(true);
		selectYear.setPaintLabels(true);
		selectMonth.setPreferredSize(new Dimension((screenSize.width*1)/4, hGap/2));
		botLayout.add(selectMonth);
		selectMonth.setMajorTickSpacing(4);
		selectMonth.setMinorTickSpacing(1);
		selectMonth.setPaintTicks(true);
		Hashtable monthlyLabelTable = new Hashtable();
		for(int i = 0; i <= 11; i++){
			monthlyLabelTable.put(new Integer(i), new JLabel(MONTH_LABEL[i]));
		}
		selectMonth.setLabelTable(monthlyLabelTable);
		selectMonth.setPaintLabels(true);
		//add(topLayout);
		add(midLayout);
		add(botLayout);

		selectCity.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				selectCity();				
			}
		});

		temperatureSelect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dataType = 0;
				chart.setType("Temperature ^°Cv  ");
				// Update information method
				chart.setMaxValue(getMaxTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue())+1);
				chart.setMinValue(getMinTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
				chart.setLength(dataOrganizer.getQuebecCity().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getCharlotteTown().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getMarkham().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getOttawa().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getWindsor().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp());
			}
		});
		
		precipitationSelect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dataType = 1;
				chart.setType("Precipitation ^mmv");
				// Update information method
				chart.setMaxValue(getMaxPre(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue())+4);
				chart.setMinValue(getMinPre(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
				chart.setLength(dataOrganizer.getQuebecCity().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
						dataOrganizer.getCharlotteTown().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
						dataOrganizer.getMarkham().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
						dataOrganizer.getOttawa().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
						dataOrganizer.getWindsor().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip());
			}
		});

		selectYear.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				if(dataType == 0){
				// Update information method
				chart.setMaxValue(getMaxTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue())+1);
				chart.setMinValue(getMinTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
				chart.setLength(dataOrganizer.getQuebecCity().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getCharlotteTown().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getMarkham().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getOttawa().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
						dataOrganizer.getWindsor().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp());
				}
				else if(dataType == 1){
				// Update information method
					chart.setMaxValue(getMaxPre(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue())+4);
					chart.setMinValue(getMinPre(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
					chart.setLength(dataOrganizer.getQuebecCity().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
							dataOrganizer.getCharlotteTown().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
							dataOrganizer.getMarkham().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
							dataOrganizer.getOttawa().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
							dataOrganizer.getWindsor().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip());
				}
			}
		});

		selectMonth.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				if(dataType == 0){
				// Update information method
					chart.setMaxValue(getMaxTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue())+1);
					chart.setMinValue(getMinTemp(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
					chart.setLength(dataOrganizer.getQuebecCity().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
							dataOrganizer.getCharlotteTown().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
							dataOrganizer.getMarkham().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
							dataOrganizer.getOttawa().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp(),
							dataOrganizer.getWindsor().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getAvgTemp());
					}
					else if(dataType == 1){
					// Update information method
						chart.setMaxValue(getMaxPre(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue())+4);
						chart.setMinValue(getMinPre(dataOrganizer, selectYear.getValue()-1998, selectMonth.getValue()));
						chart.setLength(dataOrganizer.getQuebecCity().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
								dataOrganizer.getCharlotteTown().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
								dataOrganizer.getMarkham().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
								dataOrganizer.getOttawa().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip(),
								dataOrganizer.getWindsor().getYear(selectYear.getValue()-1998).getMonth(selectMonth.getValue()).getTotalPrecip());
					}
			}
		});

	}

	private void selectCity(){
		chart.setTitle((String) citySelector.getSelectedItem());
	}
	
	private double getMaxPre(DataOrganizer dataOrganizer, int year, int month){
		double highestPrecipitation = dataOrganizer.getQuebecCity().getYear(year).getMonth(month).getTotalPrecip();
		for(int i = 0; i < 4; i++){
			switch(i){
			case 0:
				if(dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getTotalPrecip() > highestPrecipitation){
					highestPrecipitation = dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getTotalPrecip();
				}
			case 1:
				if(dataOrganizer.getMarkham().getYear(year).getMonth(month).getTotalPrecip() > highestPrecipitation){
					highestPrecipitation = dataOrganizer.getMarkham().getYear(year).getMonth(month).getTotalPrecip();
				}
			case 2:
				if(dataOrganizer.getOttawa().getYear(year).getMonth(month).getTotalPrecip() > highestPrecipitation){
					highestPrecipitation = dataOrganizer.getOttawa().getYear(year).getMonth(month).getTotalPrecip();
				}
			case 3:
				if(dataOrganizer.getWindsor().getYear(year).getMonth(month).getTotalPrecip() > highestPrecipitation){
					highestPrecipitation = dataOrganizer.getWindsor().getYear(year).getMonth(month).getTotalPrecip();
				}
			}
		}
		return highestPrecipitation;
	}
	
	private double getMinPre(DataOrganizer dataOrganizer, int year, int month){
		double lowestPrecipitation = dataOrganizer.getQuebecCity().getYear(year).getMonth(month).getTotalPrecip();
		for(int i = 0; i < 4; i++){
			switch(i){
			case 0:
				if(dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getTotalPrecip() < lowestPrecipitation){
					lowestPrecipitation = dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getTotalPrecip();
				}
			case 1:
				if(dataOrganizer.getMarkham().getYear(year).getMonth(month).getTotalPrecip() < lowestPrecipitation){
					lowestPrecipitation = dataOrganizer.getMarkham().getYear(year).getMonth(month).getTotalPrecip();
				}
			case 2:
				if(dataOrganizer.getOttawa().getYear(year).getMonth(month).getTotalPrecip() < lowestPrecipitation){
					lowestPrecipitation = dataOrganizer.getOttawa().getYear(year).getMonth(month).getTotalPrecip();
				}
			case 3:
				if(dataOrganizer.getWindsor().getYear(year).getMonth(month).getTotalPrecip() < lowestPrecipitation){
					lowestPrecipitation = dataOrganizer.getWindsor().getYear(year).getMonth(month).getTotalPrecip();
				}
			}
		}
		return lowestPrecipitation;
	}


	private double getMaxTemp(DataOrganizer dataOrganizer, int year, int month){
		double highestTemperature = dataOrganizer.getQuebecCity().getYear(year).getMonth(month).getMaxTemp();
		for(int i = 0; i < 4; i++){
			switch(i){
			case 0:
				if(dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getMaxTemp() > highestTemperature){
					highestTemperature = dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getMaxTemp();
				}
			case 1:
				if(dataOrganizer.getMarkham().getYear(year).getMonth(month).getMaxTemp() > highestTemperature){
					highestTemperature = dataOrganizer.getMarkham().getYear(year).getMonth(month).getMaxTemp();
				}
			case 2:
				if(dataOrganizer.getOttawa().getYear(year).getMonth(month).getMaxTemp() > highestTemperature){
					highestTemperature = dataOrganizer.getOttawa().getYear(year).getMonth(month).getMaxTemp();
				}
			case 3:
				if(dataOrganizer.getWindsor().getYear(year).getMonth(month).getMaxTemp() > highestTemperature){
					highestTemperature = dataOrganizer.getWindsor().getYear(year).getMonth(month).getMaxTemp();
				}
			}
		}
		return highestTemperature;
	}

	private double getMinTemp(DataOrganizer dataOrganizer, int year, int month){
		double lowestTemperature = dataOrganizer.getQuebecCity().getYear(year).getMonth(month).getMinTemp();
		for(int i = 0; i < 4; i++){
			switch(i){
			case 0:
				if(dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getMinTemp() < lowestTemperature){
					lowestTemperature = dataOrganizer.getCharlotteTown().getYear(year).getMonth(month).getMinTemp();
				}
			case 1:
				if(dataOrganizer.getMarkham().getYear(year).getMonth(month).getMinTemp() < lowestTemperature){
					lowestTemperature = dataOrganizer.getMarkham().getYear(year).getMonth(month).getMinTemp();
				}
			case 2:
				if(dataOrganizer.getOttawa().getYear(year).getMonth(month).getMinTemp() < lowestTemperature){
					lowestTemperature = dataOrganizer.getOttawa().getYear(year).getMonth(month).getMinTemp();
				}
			case 3:
				if(dataOrganizer.getWindsor().getYear(year).getMonth(month).getMinTemp() < lowestTemperature){
					lowestTemperature = dataOrganizer.getWindsor().getYear(year).getMonth(month).getMinTemp();
				}
			}
		}
		return lowestTemperature;
	}
}
