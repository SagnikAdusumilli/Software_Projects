package object;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import manager.AssetManager;
import manager.Method;

public class Player {

	private int x;
	private int y;
	private JLabel[][] playerCell,cell;
	private ImageIcon playerIcon;
	private ArrayList<String> targets;
	private JButton[] buttons;
	private String name;

	public Player(int y, int x, JLabel[][] playerCell, JLabel[][] cell, ImageIcon playerIcon, String name){
		this.x = x;
		this.y = y;
		this.playerCell = playerCell;
		this.cell = cell;
		this.playerIcon = playerIcon;
		this.playerCell[3*x+1][3*y+1].setIcon(playerIcon);
		this.targets = new ArrayList<>();
		while(targets.size()<5){
			String newInt = Integer.toString(Method.getRandomInt(1, 24));
			if(!targets.contains(newInt))
				targets.add(newInt);
		}

		this.buttons = new JButton[5];
		for(int i = 0; i<5;i++){
			buttons[i] = new JButton(targets.get(i));
		}
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public  ArrayList<String> getTargets() {
		return targets;
	}

	public JButton[] getButtons() {
		return buttons;
	}



	public void updatePosition(int x, int y){
		this.x = x;
		this.y = y;

		if(AssetManager.treasuresIcon.contains(cell[this.getX()][this.getY()].getIcon())){
			targets.remove(Integer.toString(1+AssetManager.treasuresIcon.indexOf(cell[this.getX()][this.getY()].getIcon())));
			for(JButton eachButton: buttons){
				if(!targets.contains(eachButton.getText())){
					eachButton.setEnabled(false);
				}
			}
		}
	}


	public int getX() {
		return x*3+1;
	}

	public int getY() {
		return y*3+1;
	}
	
	public int getX9() {
		return x;
	}

	public int getY9() {
		return y;
	}

	public ImageIcon getPlayerIcon() {
		return playerIcon;
	}

}
