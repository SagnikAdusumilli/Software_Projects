package com.GuI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.GuI.MainInterface.GradientUI;

public class Options extends JPanel{

	private MainInterface mainInterface;  // Alter the Main Interface
	private JButton exit;
	private JButton update;
	private JLabel optionsTitle, colorText, fontText;
	private JLabel colorStyle;
	private JComboBox<Integer> colorBox;
	private JComboBox<String> fontBox;
	private Integer[] colorList = {1, 2, 3, 4, 5};  // Easy to add new colors
	private String[] fontList = {"Verdana", "Arial", "Times New Roman"};  // Easy to add new fonts
	private JButton colorSet, fontSet;

	public Options(MainInterface mainInterface){

		setPreferredSize(new Dimension(640, 480));  // Fixed at lowest resolution
		setLayout(null);
		this.mainInterface = mainInterface;
		optionsTitle = new JLabel("Options");
		add(optionsTitle);
		update = new JButton("Update");
		add(update);
		update.setBounds(420, 60, 120, 20);
		optionsTitle.setBounds(100, 60, 60, 20);
		colorText = new JLabel("Color Style");
		fontText = new JLabel("Font");
		add(colorText);
		add(fontText);
		colorText.setBounds(170, 180, 100, 20);
		fontText.setBounds(190, 260, 100, 20);
		colorBox = new JComboBox<>(colorList);
		fontBox = new JComboBox<>(fontList);
		add(colorBox);
		add(fontBox);
		colorBox.setBounds(250, 180, 120, 20);
		fontBox.setBounds(250, 260, 120, 20);
		colorSet = new JButton("Set");
		fontSet = new JButton("Set");
		add(colorSet);
		add(fontSet);
		colorSet.setBounds(390, 180, 60, 20);
		fontSet.setBounds(390, 260, 60, 20);
		exit = new JButton("Exit");
		add(exit);
		colorStyle = new JLabel("Basic (Woodland)");
		add(colorStyle);
		colorStyle.setBounds(250, 160, 100, 20);
		update.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				update();
				mainInterface.updated();
			}
		});
		// Program exit button to close program
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		// Easy to code color schemes - change (Integer)X and edit setText(X)
		colorSet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if((Integer)colorBox.getSelectedItem() == (Integer)1){
					mainInterface.setUIColor(new Color(192, 128, 128), new Color(96, 64, 64), new Color(128, 192, 128), new Color(64, 96, 64),
							new Color(128, 160, 192), new Color(255, 255, 255));
					colorStyle.setText("Basic (Woodland)");
					revalidate();
				}
				else if((Integer)colorBox.getSelectedItem() == (Integer)2){
					mainInterface.setUIColor(new Color(160, 64, 192), new Color(96, 0, 160), new Color(0, 128, 192), new Color(0, 64, 128),
							new Color(64, 32, 64), new Color(255, 224, 192));
					colorStyle.setText("Dark Skies");
					revalidate();
				}
				else if((Integer)colorBox.getSelectedItem() == (Integer)3){
					mainInterface.setUIColor(new Color(255, 128, 96), new Color(192, 96, 64), new Color(255, 192, 128), new Color(255, 160, 128),
							new Color(64, 32, 0), new Color(255, 255, 255));
					colorStyle.setText("Orange (Volcano)");
					revalidate();
				}
				else if((Integer)colorBox.getSelectedItem() == (Integer)4){
					mainInterface.setUIColor(new Color(0, 64, 32), new Color(0, 32, 0), new Color(64, 64, 64), new Color(32, 32, 32),
							new Color(0, 0, 0), new Color(0, 255, 0));
					colorStyle.setText("Matrix (Binary)");
					revalidate();
				}
				else if((Integer)colorBox.getSelectedItem() == (Integer)5){
					mainInterface.setUIColor(new Color(0, 192, 128), new Color(0, 160, 160), new Color(222, 255, 255), new Color(160, 222, 222),
							new Color(96, 128, 192), new Color(0, 96, 64));
					colorStyle.setText("Rainy Days");
					revalidate();
				}
			}
		});
		// Easy to code font choices - change "FONT_NAME" and setFont(MainInterface.X)
		fontSet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(fontBox.getSelectedItem() == "Verdana"){
					mainInterface.setFont(MainInterface.font1);
					revalidate();
				}
				else if(fontBox.getSelectedItem() == "Arial"){
					mainInterface.setFont(MainInterface.font2);
					revalidate();
				}
				else if(fontBox.getSelectedItem() == "Times New Roman"){
					mainInterface.setFont(MainInterface.font3);
					revalidate();
				}
			}
		});
		exit.setBounds(480, 400, 60, 20);

	}
	
	private void update(){
		new Updater();
	}

	public void paintComponent(Graphics g){

		super.paintComponent(g);

		// Paint white background
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(255, 255, 255));
		g2d.fillRect(0, 0, 640, 480);

	}

}
