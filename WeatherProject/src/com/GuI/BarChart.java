package com.GuI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.Data.DataOrganizer;
import com.GuI.BarChartController;

public class BarChart extends JPanel implements MouseMotionListener{

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double CHART_Y_MIN = (screenSize.height*13)/16;
	private double CHART_Y_MAX = (screenSize.height*1)/8;
	private double
	pos1 = ((screenSize.width*1)/16)+BarChartController.numericalIncrease,
	pos2 = ((screenSize.width*3)/16)+BarChartController.numericalIncrease,
	pos3 = ((screenSize.width*5)/16)+BarChartController.numericalIncrease,
	pos4 = ((screenSize.width*7)/16)+BarChartController.numericalIncrease,
	pos5 = ((screenSize.width*9)/16)+BarChartController.numericalIncrease;
	private double[] POSITION = {pos1, pos2, pos3, pos4, pos5};
	private double length = (CHART_Y_MIN - CHART_Y_MAX)*-1;
	private double qL, ctL, mL, oL, wL;
	private Color c1, c2, c3, c4, c5;
	private int commonWidth = 40;
	private int maxValue, minValue;
	private int interval;
	private JLabel graphTitle;
	private JLabel[] graphLabels = {new JLabel("Quebec City"), new JLabel("Charlotte Town"), 
			new JLabel("Markham"), new JLabel("Ottawa"), new JLabel("Windsor")};
	private String[] stringType = new String[18];
	private JLabel[] printVertical = new JLabel[18];

	Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	private JLabel mouseAxis = new JLabel(String.format("Axis: %d, %d", (int)mousePoint.x, (int)mousePoint.y));

	public BarChart(){
		setLayout(null);
		Timer refresh = new Timer();
		addMouseMotionListener(this);
		setSize(screenSize.width, screenSize.height);
		JLabel chartAxis = new JLabel(String.format("Chart: %d - %d", (int)CHART_Y_MIN, (int)CHART_Y_MAX));
		JLabel chartLength = new JLabel(String.format("Length: %d", (int)(CHART_Y_MIN - CHART_Y_MAX)));
		//graphTitle = new JLabel("Quebec");
		for(int i = 0; i <= 4; i++)
			add(graphLabels[i]);
		graphLabels[0].setBounds((int)pos1, (int)CHART_Y_MIN+BarChartController.numericalIncrease/2,
				200, 20);
		graphLabels[1].setBounds((int)pos2, (int)CHART_Y_MIN+BarChartController.numericalIncrease/2,
				200, 20);
		graphLabels[2].setBounds((int)pos3, (int)CHART_Y_MIN+BarChartController.numericalIncrease/2,
				200, 20);
		graphLabels[3].setBounds((int)pos4, (int)CHART_Y_MIN+BarChartController.numericalIncrease/2,
				200, 20);
		graphLabels[4].setBounds((int)pos5, (int)CHART_Y_MIN+BarChartController.numericalIncrease/2,
				200, 20);
		interval = 1;
		for(int i = 0; i <= 17; i++){
			printVertical[i] = new JLabel();
			add(printVertical[i]);
		}
		setType("Temperature ^°Cv  ");
		add(chartAxis);
		add(chartLength);
		//add(graphTitle);
		chartAxis.setBounds(20, 20, 200, 20);
		chartLength.setBounds(20, 40, 120, 20);		
		//graphTitle.setBounds((int)pos3, (int)CHART_Y_MAX-40, 200, 20);
		refresh.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				repaint();
				revalidate();
			}
		}, 1000, 50);
	}

	@Override
	public void mouseMoved(MouseEvent e){
		remove(mouseAxis);
		mousePoint.x = e.getX();
		mousePoint.y = e.getY();
		mouseAxis = new JLabel(String.format("Axis: %d, %d", (int)mousePoint.x, (int)mousePoint.y));
		add(mouseAxis);
		mouseAxis.setBounds(20, 60, 200, 20);
	}

	@Override
	public void mouseDragged(MouseEvent e){

	}

	public void setMaxValue(double value){
		maxValue = (int)value;
	}

	public void setMinValue(double value){
		minValue = (int)value;
	}

	public void setLength(double qLength, double ctLength, double mLength, double oLength, double wLength){
		if(maxValue-minValue > 120){
			interval = 4;
		}
		else if(maxValue-minValue > 80){
			interval = 3;
		}
		else if(maxValue-minValue > 40){
			interval = 2;
		}
		else{
			interval = 1;
		}
		if(qLength >= 0){
			qL = (qLength-minValue)/(maxValue-minValue)*length;
		}
		else if(qLength < 0){
			qL = (qLength-maxValue)/(maxValue-minValue)*(length*-1);
		}
		if(ctLength >= 0){
			ctL = (ctLength-minValue)/(maxValue-minValue)*length;
		}
		else if(ctLength < 0){
			ctL = (ctLength-maxValue)/(maxValue-minValue)*(length*-1);
		}
		if(mLength >= 0){
			mL = (mLength-minValue)/(maxValue-minValue)*length;
		}
		else if(mLength < 0){
			mL = (mLength-maxValue)/(maxValue-minValue)*(length*-1);
		}
		if(oLength >= 0){
			oL = (oLength-minValue)/(maxValue-minValue)*length;
		}
		else if(oLength < 0){
			oL = (oLength-maxValue)/(maxValue-minValue)*(length*-1);
		}
		if(wLength >= 0){
			wL = (wLength-minValue)/(maxValue-minValue)*length;
		}
		else if(wLength < 0){
			wL = (wLength-maxValue)/(maxValue-minValue)*(length*-1);
		}
	}

	public void setTitle(String title){
		graphTitle.setText(title);
		revalidate();
	}

	public void setType(String type){
		for(int i = 0; i <= 17; i++){
			stringType[i] = Character.toString(type.charAt(i));
			remove(printVertical[i]);
			printVertical[i] = new JLabel(stringType[i]);
			add(printVertical[i]);
			printVertical[i].setBounds((int)(pos1-(BarChartController.numericalIncrease*2)), (int)(CHART_Y_MIN+length)+(i*20), 20, 80);
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(255, 255, 255));
		g2d.fillRect(0, 0, (int)screenSize.width, (int)screenSize.height);

		/*g.setColor(Color.WHITE);
		g.fillRect((int)POSITION[0], (int)CHART_Y_MIN, commonWidth, (int)length);
		g.fillRect((int)POSITION[1], (int)CHART_Y_MIN, commonWidth, (int)length);
		g.fillRect((int)POSITION[2], (int)CHART_Y_MIN, commonWidth, (int)length);
		g.fillRect((int)POSITION[3], (int)CHART_Y_MIN, commonWidth, (int)length);
		g.fillRect((int)POSITION[4], (int)CHART_Y_MIN, commonWidth, (int)length);*/

		c1 = new Color(204, 0, 0);
		g.setColor(c1);
		g.fillRect((int)POSITION[0], (int)CHART_Y_MIN, commonWidth, (int)qL);

		c2 = new Color(204, 204, 0);
		g.setColor(c2);
		g.fillRect((int)POSITION[1], (int)CHART_Y_MIN, commonWidth, (int)ctL);

		c3 = new Color(0, 204, 0);
		g.setColor(c3);
		g.fillRect((int)POSITION[2], (int)CHART_Y_MIN, commonWidth, (int)mL);

		c4 = new Color(0, 0, 204);
		g.setColor(c4);
		g.fillRect((int)POSITION[3], (int)CHART_Y_MIN, commonWidth, (int)oL);

		c5 = new Color(204, 0, 204);
		g.setColor(c5);
		g.fillRect((int)POSITION[4], (int)CHART_Y_MIN, commonWidth, (int)wL);

		g2d.setColor(Color.BLACK);
		for(int i = 0; i <= maxValue-minValue; i+=interval){
			g2d.drawString(String.format("%d.00",  minValue+i), (screenSize.width/256)+BarChartController.numericalIncrease, (int)(CHART_Y_MIN+(length/(maxValue-minValue))*i));
			g2d.draw(new Line2D.Double((screenSize.width/32)+BarChartController.numericalIncrease, (int)CHART_Y_MIN+(length/(maxValue-minValue))*i, ((screenSize.width*19)/32+commonWidth)+BarChartController.numericalIncrease, (int)CHART_Y_MIN+(length/(maxValue-minValue))*i));
		}

		/*AffineTransform g2dtCopy = g2d.getTransform();
		g2d.drawString(stringType, (int)pos1-BarChartController.numericalIncrease, (int)CHART_Y_MIN+(int)length/2);
		g2d.rotate(Math.toRadians(45));
		g2d.setTransform(g2dtCopy);*/

	}

}
