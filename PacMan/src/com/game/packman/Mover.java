package com.game.packman;
import javax.swing.ImageIcon;

public abstract class Mover {

	private int row;
	private int column;
	private int dRow;
	private int dColumn;
	private boolean isDead;
	private ImageIcon image;

	public Mover(int row, int column, boolean isDead, ImageIcon image) {
		super();
		this.row = row;
		this.column = column;
		this.isDead = isDead;
		this.image = image;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getdRow() {
		return dRow;
	}

	public void setdRow(int dRow) {
		this.dRow = dRow;
	}

	public int getdColumn() {
		return dColumn;
	}

	public void setdColumn(int dColumn) {
		this.dColumn = dColumn;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public void move() {
		row += dRow;
		column += dColumn;
	}

	public void setDirection(int dir) {
		dRow = 0;
		dColumn = 0;

		switch (dir) {
		case 0: // moving left
			dColumn = -1;
			break;
		case 1:  // up
			dRow = -1;
			break;
		case 2: // moving right
			dColumn = 1;
			break;
		case 3: // moving down
			dRow = 1;
			break;
		}
	}
	
	public int getDirection() {
		if(dRow == 0 && dColumn == -1 ){
			return 0; // moving left
		}else if(dRow == 0 && dColumn == 1 ){
			return 2; // moving right
		}else if(dRow == 1 && dColumn == 0 ){
			return 3; // moving down
		}else if(dRow == -1 && dColumn == 0 ){
			return 1; // moving up
		}
	    return 0;
	}
	
	public int getNextRow(){
		return row +dRow;
	}
	
	public int getNextColumn(){
		return column + dColumn;
	}


}
