package manager;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

/**
 * mouse listener added to the frame so that frame can be dragged
 * @author Leo
 *
 */
public class FrameMouseDragController extends MouseAdapter{

	//the class that allow the frame to be dragged
	private Point mouseDownCompCoords; //store the current position of the frame
	private JFrame frame; //get a reference to the JFrame, the window

	/**
	 * 
	 * @param frame a reference to the main frame
	 */
	public FrameMouseDragController(JFrame frame){ 
		this.frame = frame; //reference to the window
	}
	/**
	 * when the mouse is released
	 */
	public void mouseReleased(MouseEvent e) {
		mouseDownCompCoords = null; //when the mouse is released set the point to zero
	}

	/**
	 * when the mouse is pressed
	 */
	public void mousePressed(MouseEvent e) {
		mouseDownCompCoords = e.getPoint(); //when the mouse is pressed down get the point
	}

	/**
	 * when the mouse is dragged
	 */
	public void mouseDragged(MouseEvent e) { 
		Point currCoords = e.getLocationOnScreen(); //when the mouse is dragged get the dragging position of the mouse
		frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y); //set the location of the frame to the x and y position
	}

}
