package com.game.packman;

import javax.swing.ImageIcon;

public class PacMan extends Mover {
	private static final ImageIcon[][] faces = new ImageIcon[4][2];

	public PacMan(int row, int column, int dRow, int dColumn, boolean isDead, ImageIcon image) {
		super(row, column, isDead, image);
		// TODO Auto-generated constructor stub

		faces[0][0] = new ImageIcon("images/PacLeftClosed.bmp"); // left
		faces[0][1] = new ImageIcon("images/PacLeftOpen.bmp");
		
		faces[1][0] = new ImageIcon("images/PacUpClosed.bmp");  // up
		faces[1][1] = new ImageIcon("images/PacUpOpen.bmp");
		
		faces[2][0] = new ImageIcon("images/PacRightClosed.bmp"); // right 
		faces[2][1] = new ImageIcon("images/PacRightOpen.bmp");

		faces[3][0] = new ImageIcon("images/PacDownClosed.bmp"); //down
		faces[3][1] = new ImageIcon("images/PacDownOpen.bmp");

	}

	public void setImage(int position, int mouth) {
		super.setImage(faces[position][mouth]);
	}
}
