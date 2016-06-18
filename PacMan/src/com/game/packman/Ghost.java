package com.game.packman;
import javax.swing.ImageIcon;

public class Ghost extends Mover {
 public Ghost (int row, int column, int dRow, int dColumn, boolean isDead, ImageIcon image){
	 super(row, column, isDead, image);
 }
}
