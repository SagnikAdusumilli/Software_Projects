package com.GuI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.Data.DataScanner;

public class Updater extends JFrame implements ActionListener{

	private BufferedImage imageBackground;	
	private Image background;
	private JPanel splashScreen;
	private JButton startButton;
	private JLabel information;
	private JProgressBar loader;
	private JComboBox<Integer> startYearSelection, endYearSelection;
	private Integer[] years = new Integer[10];
	public static boolean swapYear = false;

	public Updater(){

		setSize(new Dimension(320, 240));
		setTitle("Patcher");
		setIconImage(new ImageIcon("Images/Icon.png").getImage());
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout()); 
		setLocationRelativeTo(null);
		try{ 
			imageBackground = ImageIO.read(new File("Images/Map.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		background = imageBackground.getScaledInstance(320, 240, Image.SCALE_SMOOTH);
		//setContentPane(new JLabel(new ImageIcon(background)));  // Draw "Map.png" as the background for JFrame
		
		splashScreen = new JPanel();   
		GridLayout gLay = new GridLayout(4, 3, 0, 25);
		splashScreen.setLayout(gLay);
		gLay.layoutContainer(this);
		add(splashScreen);

		splashScreen.add(new JPanel());
		information = new JLabel("Selection");
		splashScreen.add(information);
		splashScreen.add(new JPanel());

		int valuePosition = 0;
		for(int i = 1998; i <= 2007; i++){  // Fill in JComboBox
			years[valuePosition] = i;
			valuePosition++;
		}
		startYearSelection = new JComboBox<>(years);
		endYearSelection = new JComboBox<>(years);

		splashScreen.add(new JLabel("Start Year"));
		splashScreen.add(new JPanel());
		splashScreen.add(startYearSelection);
		splashScreen.add(new JLabel("End Year"));
		splashScreen.add(new JPanel());
		splashScreen.add(endYearSelection);

		splashScreen.add(new JPanel());
		startButton = new JButton("Load");
		splashScreen.add(startButton);
		splashScreen.add(new JPanel());
		startButton.addActionListener(this);
		revalidate();

	}
	
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource() == startButton){
			
			// When "Load" JButton is clicked,
			setVisible(false);
			if((Integer)startYearSelection.getSelectedItem() > (Integer)endYearSelection.getSelectedItem())
				swapYear = true;
			int startYear = Math.min((Integer)startYearSelection.getSelectedItem(), (Integer)endYearSelection.getSelectedItem());
			int endYear = Math.max((Integer)startYearSelection.getSelectedItem(), (Integer)endYearSelection.getSelectedItem());
			System.out.printf("%d-%d\n", startYear, endYear);
			new DataScanner(startYear, endYear);  // Activate Data Scanner to scan for files based on the integers given
			dispose();
			
		}
		
	}

	public void paintComponent(Graphics g){
		
		paintComponent(g);
		g.drawImage(background, 0, 0, this);
		
	}
	
}
