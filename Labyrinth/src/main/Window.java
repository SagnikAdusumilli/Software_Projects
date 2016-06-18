package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import manager.FrameMouseDragController;

public class Window {
	
	private JFrame frame;
	private JPanel panel;
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	
	public Window(){
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		panel = new Board();
		frame.add(panel);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FrameMouseDragController dragControl = new FrameMouseDragController(frame); //initialize the drag control that will be added to the frame
		frame.addMouseListener(dragControl); //add the dragControl to the frame as a mouse listener
		frame.addMouseMotionListener(dragControl); //add the dragControl to the frame as a mouse motion listener as well
		frame.setVisible(true);
	}
	
}
