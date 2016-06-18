package com.GuI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.Data.DataOrganizer;
import com.NonGuI.City;

/** This class displays the map interface and its location tags
 * @author Calvin
 *
 */

public class Map extends JPanel implements MouseListener {
	// java.net.URL imgURL = getClass().getResource("Images/Map.png");
	private ImageIcon map = new ImageIcon("./Images/Map.png");
	private ImageIcon location = new ImageIcon("./Images/LocationTag.png");
	//JLabel locationTags[] = new JLabel[5]; //For some reason, mouseListener does not work with arrays
		
	private ImageIcon windsorImage = new ImageIcon("./Images/windsor.png");
	private ImageIcon markhamImage = new ImageIcon("./Images/windsor.png");
	private ImageIcon ottawaImage = new ImageIcon("./Images/windsor.png");
	private ImageIcon charlotteTownImage = new ImageIcon("./Images/windsor.png");
	private ImageIcon quebecImage = new ImageIcon("./Images/windsor.png");
	
	JLayeredPane layeredPane;
	PopUpPanel popUpPanel;

	JLabel ottawa;
	JLabel charlotteTown;
	JLabel windsor;
	JLabel quebec;
	JLabel markham;

	
	/** This consructor displays the layout of the map and manages screen resolution sizes.
	 *  
	 * @param organizer
	 *
	 */
	public Map(DataOrganizer organizer) {


		setLayout(new BorderLayout());
		setOpaque(true); // Makes background not transparent
		setBackground(new Color(0, 162, 255));
		JLabel mapLabel = new JLabel(map);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(screenSize);

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
		//System.out.println(screenSize.width + " " + screenSize.height);
		layeredPane.add(mapLabel, 1); // Layer 1?
		
		add(layeredPane, BorderLayout.CENTER);
		Insets insets = layeredPane.getInsets(); // Boundary of the frame
		
		popUpPanel = new PopUpPanel(organizer, screenSize.height, screenSize.width);
		add(popUpPanel, BorderLayout.EAST);
		
		Dimension size = mapLabel.getPreferredSize(); // Get the dimension of the JLabel
		
		// From left border of the frame, move 25 to the right. From the top, move 5 down. Set the width and height.
		mapLabel.setBounds((int) (screenSize.getWidth()/2) - map.getIconWidth()/2 , (int) (screenSize.getHeight()/2) - map.getIconHeight()/2, size.width, size.height);

		
		charlotteTown = new JLabel(location);
		charlotteTown.setPreferredSize(new Dimension(location.getIconWidth(),location.getIconHeight()));
		
		quebec = new JLabel(location);
		quebec.setPreferredSize(new Dimension(location.getIconWidth(),location.getIconHeight()));
		
		windsor = new JLabel(location);
		windsor.setPreferredSize(new Dimension(location.getIconWidth(),location.getIconHeight()));
		
		ottawa = new JLabel(location);
		ottawa.setPreferredSize(new Dimension(location.getIconWidth(),location.getIconHeight()));
		
		markham = new JLabel(location);
		markham.setPreferredSize(new Dimension(location.getIconWidth(),location.getIconHeight()));

		charlotteTown.setBounds((int) ((screenSize.getWidth() * 0.625)), (int) (screenSize.getHeight() * 0.595), location.getIconWidth(), location.getIconHeight()); // Charlotte Town
		layeredPane.add(charlotteTown, 0);
		charlotteTown.addMouseListener(this);
	
		quebec.setBounds((int) ((screenSize.getWidth()* 0.58)), (int) (screenSize.getHeight() * 0.63), location.getIconWidth(), location.getIconHeight()); // Quebec
		layeredPane.add(quebec, 0);
		quebec.addMouseListener(this);
		
		windsor.setBounds((int) ((screenSize.getWidth()* 0.538)), (int) (screenSize.getHeight() * 0.711), location.getIconWidth(), location.getIconHeight()); // Iqaluit
		layeredPane.add(windsor, 0);
		windsor.addMouseListener(this);

		ottawa.setBounds((int) ((screenSize.getWidth()* 0.56)), (int) (screenSize.getHeight() * 0.65), location.getIconWidth(), location.getIconHeight()); // Ottawa
		layeredPane.add(ottawa, 0);
		ottawa.addMouseListener(this);

		markham.setBounds ((int) ((screenSize.getWidth()* 0.55)), (int) (screenSize.getHeight() * 0.68), location.getIconWidth(), location.getIconHeight()); // Victoria
		layeredPane.add(markham, 0);
		markham.addMouseListener(this);


	}

	@Override
	public void mouseClicked(MouseEvent e) {

		/*If the a location tag is clicked, set the name of the city to the city name and displays the city's attributes through the
		*popUpPanel class
		*/
		if (e.getSource().equals(charlotteTown)) {
			popUpPanel.setName("Charlotte Town");
			popUpPanel.display("Charlotte Town");

		}
		if (e.getSource().equals(quebec)) {
			popUpPanel.setName("Quebec");
			popUpPanel.display("Quebec");

		}
		if (e.getSource().equals(windsor)) {
			popUpPanel.setName("Windsor");
			popUpPanel.display("Windsor");

		}
		if (e.getSource().equals(ottawa)) {
			popUpPanel.setName("Ottawa");
			popUpPanel.display("Ottawa");

		}
		if (e.getSource().equals(markham)) {
			popUpPanel.setName("Markham");
			popUpPanel.display("Markham");

		}

//		layeredPane.add(popUpPanel, 0);

//		Point b = MouseInfo.getPointerInfo().getLocation();
//		popUpPanel.setBounds((int) b.getX(), (int) b.getY()-50, popUpPanel.getWidth(), popUpPanel.getHeight());

//		System.out.println(b.getX() + " " + b.getY());

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
//		if(e.getSource().equals(popUpPanel)){
//			layeredPane.remove(popUpPanel);
//			repaint();
//			revalidate();
//		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}