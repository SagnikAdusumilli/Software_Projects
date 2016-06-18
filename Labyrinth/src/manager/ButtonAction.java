package manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import object.Player;

public class ButtonAction implements ActionListener{

	public static JButton buttonDisabled;

	private JLabel[][] cell, playerCell;
	private JLabel[][] individualPiece;
	private JButton[][] buttons;
	private int counterX;
	private int counterY;
	private ImageIcon[][] copy = new ImageIcon[27][27];
	private ImageIcon[][] playerCopy = new ImageIcon[27][27];

	private ImageIcon[][] pieceLost = new ImageIcon[3][3];

	private Player player1, player2;

	public ButtonAction(JLabel[][] cell, JLabel[][] playerCell, JLabel[][] individualPiece, JButton[][] buttons, int counterX, int counterY, Player player1, Player player2){
		this.cell = cell;
		this.playerCell = playerCell;
		this.individualPiece = individualPiece;
		this.buttons = buttons;
		this.counterX = counterX;
		this.counterY = counterY;
		this.player1 = player1;
		this.player2 = player2;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {


		for(int i = 3; i<24; i++){
			for(int j = 3; j<24; j++){
				copy[i][j] = (ImageIcon) cell[i][j].getIcon();
			}
		}

		for(int i = 1; i<8; i++){
			for(int j = 1; j<8; j++){
				playerCopy[i*3+1][j*3+1] = (ImageIcon) playerCell[i*3+1][j*3+1].getIcon();
			}
		}


		for(int i = 3; i<24; i++){
			for(int j = 3; j<24; j++){
				if((j>=counterX*3&&j<counterX*3+3)||(i>=counterY*3&&i<counterY*3+3)){

					int targetX = j;
					int targetY = i;
					if(counterY==0)
						targetY-=3;
					else if(counterY==8)
						targetY+=3;

					if(counterX==0)
						targetX-=3;
					else if(counterX==8)
						targetX+=3;

					ImageIcon targetIcon;
					targetIcon = copy[targetY][targetX];

					if(targetY>=24){
						pieceLost[targetY-24][targetX%3] = copy[targetY-21][targetX];
						targetIcon = (ImageIcon) individualPiece[targetY-24][targetX%3].getIcon();
					}else if (targetY<3){
						pieceLost[targetY][targetX%3] = copy[targetY+21][targetX];
						targetIcon = (ImageIcon) individualPiece[targetY][targetX%3].getIcon();
					}

					if(targetX>=24){
						pieceLost[targetY%3][targetX-24] = copy[targetY][targetX-21];
						targetIcon = (ImageIcon) individualPiece[targetY%3][targetX-24].getIcon();
					}else if (targetX<3){
						pieceLost[targetY%3][targetX] = copy[targetY][targetX+21];
						targetIcon = (ImageIcon) individualPiece[targetY%3][targetX].getIcon();
					}

					cell[i][j].setIcon(targetIcon);




					ImageIcon targetPlayerIcon;
					targetPlayerIcon = playerCopy[targetY][targetX];


					if(targetY>=24){
						targetPlayerIcon = playerCopy[targetY-21][targetX];
					}else if (targetY<3){
						targetPlayerIcon = playerCopy[targetY+21][targetX];
					}

					if(targetX>=24){
						targetPlayerIcon = playerCopy[targetY][targetX-21];
					}else if (targetX<3){
						targetPlayerIcon = playerCopy[targetY][targetX+21];
					}

					playerCell[i][j].setIcon(targetPlayerIcon);

				}
			}
		}


		for(int i = 0;i<3;i++){
			for(int j = 0;j<3;j++){
				if(pieceLost[i][j]!=AssetManager.player1&&pieceLost[i][j]!=AssetManager.player2)
					individualPiece[i][j].setIcon(pieceLost[i][j]);
			}
		}

		int playerCount = 0;
		for(int i = 1; i<8; i++){
			for(int j = 1; j<8; j++){
				if(playerCell[i*3+1][j*3+1].getIcon()==AssetManager.player1||playerCell[i*3+1][j*3+1].getIcon()==AssetManager.player2){
					playerCount++;
				}
			}
		}


		for(int i = 0; i<27;i++){
			for(int j = 0;j<27;j++){
				if(playerCell[i][j].getIcon()==AssetManager.player1){
					if(playerCount==2){
						player1.updatePosition(i/3, j/3);
					}else if(playerCount==1){
						player1.updatePosition(i/3, j/3);
						player2.updatePosition(i/3, j/3);
					}
				}else if(playerCell[i][j].getIcon()==AssetManager.player2){
					if(playerCount==2){
						player2.updatePosition(i/3, j/3);
					}else if(playerCount==1){
						player1.updatePosition(i/3, j/3);
						player2.updatePosition(i/3, j/3);
					}
				}
			}
		}


		int targetR = counterY;
		int targetC =counterX;
		if(targetR == 0)
			targetR = 8;
		else if(targetR == 8)
			targetR = 0;

		if(targetC == 0)
			targetC = 8;
		else if(targetC == 8)
			targetC = 0;


		if(buttonDisabled!=null)
			buttonDisabled.setEnabled(true);
		buttons[targetR][targetC].setEnabled(false);
		buttonDisabled =buttons[targetR][targetC];

	}
}
