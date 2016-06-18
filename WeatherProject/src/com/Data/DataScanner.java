package com.Data;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.Data.Downloader;
import com.GuI.MainInterface;
import com.GuI.OutputInterface;
import com.GuI.Splash;
import com.GuI.Updater;
import com.NonGuI.Output;

public class DataScanner extends JFrame{

	protected String globalText = "Scanning...";
	private static String DATA_FILE_DIRECTORY = "Data";
	private JPanel updater;
	private JLabel information;
	private JProgressBar downloadProgressBar = new JProgressBar();
	private Boolean mustUpdate;
	private String city;
	private Integer globalValue;
	private ShowProgress progress = new ShowProgress();
	private File currentDirectory;
	private Integer startYear, endYear;
	private OutputInterface OI;

	public DataScanner(int startYear, int endYear){

		this.startYear = startYear;
		this.endYear = endYear;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(200, 120));
		setTitle("Patcher");
		setIconImage(new ImageIcon("Images/Icon.png").getImage());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation((screenSize.width/2)-100, (screenSize.height/2)-120);
		//setLocationRelativeTo(null);

		updater = new JPanel(new FlowLayout());
		information = new JLabel(globalText);

		add(updater);
		updater.add(information);
		updater.add(downloadProgressBar);

		OI = new OutputInterface();  // Create console visual of patching progression
		setVisible(true);
		System.out.printf("%d-%d\n", startYear, endYear);
		if(Splash.swapYear || Updater.swapYear)
			System.out.println("Switching start year and end year...");

		currentDirectory = new File(DATA_FILE_DIRECTORY);  // Create a "Data" folder for user if non-existent
		if (!currentDirectory.exists()) {
			System.out.println("Creating 'Data' Folder");
			boolean createdFolder = false;
			try{
				currentDirectory.mkdir();
				createdFolder = true;
			} 
			catch(SecurityException e){

			}        
			if(createdFolder){    
				System.out.println("Created 'Data' Folder");  
			}
		}
		
		Timer refresh = new Timer();
		refresh.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				information.setText(globalText);
				updater.revalidate();
			}
		}, 50, 50);
		
		downloadProgressBar.setStringPainted(true);
		progress.execute();

		/*try {
			Thread.sleep(1000);
		} catch(InterruptedException e){
			Thread.currentThread().interrupt();
		}*/

	}

	class ShowProgress extends SwingWorker<Integer, Void>{

		public Integer doInBackground() throws Exception{
			{
				for(globalValue = 0; globalValue <= 100; globalValue++){
				downloadProgressBar.setValue(globalValue);
				revalidate();
				}
			}
			scanDirectory(currentDirectory);
			getFileDirectory(startYear, endYear);
			return downloadProgressBar.getValue();
		}

		public void done(){
			setInformationText("Scan Completed");
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e){
				Thread.currentThread().interrupt();
			}
			OI.removeFrame();
			setVisible(false);  // Terminate when finished patching
			init();
			dispose();
		}
		
	}

	private void setInformationText(String newText){
		globalText = newText;
	}
	
	public void init(){

		MainInterface mainFrame;  // Finish patching will initiate the main program
		try {
			mainFrame = new MainInterface(startYear, endYear);
			mainFrame.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getFileDirectory(int start, int end){

		//downloadProgressBar.setValue(0);
		int startYear = start;
		int endYear = end;

		System.out.println("Scanning...");

		for(int i = 0; i <= 4; i++){

			switch(i){
			case 0:	city = "Quebec"; break;
			case 1: city = "CharlotteTown"; break;
			case 2: city = "Markham"; break;
			case 3:	city = "Ottawa"; break;
			case 4: city = "Windsor";
			}

			for(int j = startYear; j <= endYear; j++){

				// Scan for files according to city and years chosen by user
				//downloadProgressBar.setValue( ((((j-(startYear-1)+1)/(endYear-(startYear-1)+1))*i+1)/5)*100 );  // Show progress
				//globalValue =  ((((j-(startYear-1)+1)/(endYear-(startYear-1)+1))*i+1)/5)*100;
				File currentDFile = new File(String.format("Data/%s-daily-%d.csv", city, j));
				File currentMFile = new File(String.format("Data/%s-monthly.csv", city));
				if(currentDFile.exists()){
					System.out.println(String.format("Scanned: %s-daily-%d.csv", city, j));
					continue;
				}
				else if(currentMFile.exists()){
					System.out.println(String.format("Scanned: %s-monthly.csv", city));
					continue;
				}
				else{
					System.out.println("Detected missing files!");
					mustUpdate = true;  // Enable update to run if missing files are detected
					break;
				}

			}
			
			//downloadProgressBar.setValue(progress.get());

		}

		if(mustUpdate == null){
			System.out.println("All files detected!");
		}
		else{
			updater.remove(downloadProgressBar);  // Cannot run JProgressBar because cannot track Downloader class progress
			updater.remove(information);
			updater.add(new JLabel("                                                         "));
			updater.add(information);
			setInformationText("Updating...");
			updater.add(new JLabel("                                                         "));
			updater.add(new JLabel("Please wait"));
			updater.revalidate();
			repaint();  // Repaint components to remove JProgressBar
			updateFiles();  // Run update method if missing files are detected
		}

	}

	private void updateFiles(){

		/*Runnable downloadProgress = new Runnable(){
			public void run() {
				for(int i = 1; i <= 55; i++)
					downloadProgressBar.setValue((i/55)*100);
			}
		};*/
		Downloader runDownloader = new Downloader();  // Create downloader class to download files off the internet source
		runDownloader.runDownloader();
		//new Thread(downloadProgress).start();

	}

	public void scanDirectory(File directory) {
		//downloadProgressBar.setValue(0);
		System.out.println("Scanning...");

		/*Runnable downloadProgress = new Runnable(){
			public void run() {
				for(int i = 1; i <= 55; i++)
					downloadProgressBar.setValue((i/55)*100);
			}
		};
		new Thread(downloadProgress).start();*/

		try {  // File scanner
			File[] files = directory.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println(file.getCanonicalPath());
					scanDirectory(file);
				} else {
					System.out.println(file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}