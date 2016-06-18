package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import ai.AI;
import manager.AssetManager;
import manager.ButtonAction;
import manager.Method;
import manager.MouseInput;
import object.Player;
import object.PlayerControl;
import object.Solver;

public class Board extends JPanel{

	private JLabel[][] cell = new JLabel[27][27];
	private JLabel[][] playerCell = new JLabel[27][27];

	private String[][] maze = new String[9][9];
	private JButton[][] controlCell = new JButton[9][9];
	private JLabel[][] individualPiece = new JLabel[3][3];

	private JPanel gridPane;
	private JPanel playerGridPane;

	private JPanel controlPane;
	private JPanel piecePane;
	private JPanel rotatePiecePane;

	private PlayerControl player1Control;
	private PlayerControl player2Control;

	private Player player1, player2;
	private Solver solver1, solver2;

	private int counterImage = 0;

	public Board(){

		setLayout(null);


		gridPane = new JPanel(); 
		gridPane.setLayout(new GridLayout(27,27));
		gridPane.setBounds(0, 0, Window.HEIGHT, Window.HEIGHT);
		gridPane.setOpaque(false);


		playerGridPane = new JPanel();
		playerGridPane.setLayout(new GridLayout(27,27));
		playerGridPane.setBounds(0, 0, Window.HEIGHT, Window.HEIGHT);
		playerGridPane.setOpaque(false);


		controlPane = new JPanel();
		controlPane.setOpaque(false);
		controlPane.setLayout(new GridLayout(9,9));
		controlPane.setBounds(0, 0, Window.HEIGHT, Window.HEIGHT);

		piecePane = new JPanel();
		piecePane.setLayout(new GridLayout(3,3));
		piecePane.setOpaque(false);
		piecePane.setBounds(665, 250, 69, 69);

		rotatePiecePane = new JPanel();
		rotatePiecePane.setOpaque(false);
		rotatePiecePane.setBounds(640, 330, 120, 50);
		rotatePiecePane.setLayout(new GridLayout(1,0));
		JButton rotateLeft = new JButton("鈫� ");
		makeButtonTransparent(rotateLeft);
		JButton rotateRight  = new JButton("鈫�");
		makeButtonTransparent(rotateRight);
		rotatePiecePane.add(rotateLeft);
		rotatePiecePane.add(rotateRight);
		

		load();
		
		AI ai = new AI(cell, controlCell, individualPiece, rotateRight, player1, player2);
		/*
		PlayerAI playerAI = new PlayerAI(cell, controlCell, individualPiece, rotateRight, player1, player1);
		*/
		
		player1Control = new PlayerControl(player1,player2,solver1,playerCell,600,410,200,180,Color.orange, ai);
		player2Control = new PlayerControl(player2,player1,solver2,playerCell,600,0,200,180,Color.pink, ai);

		

		add(player1Control);
		add(player2Control);
		add(controlPane);
		add(playerGridPane);
		add(gridPane);
		add(piecePane);
		add(rotatePiecePane);
		revalidate();
		repaint();
		

		for(int counterX = 0;counterX< 9;counterX++){
			for(int counterY = 0;counterY< 9;counterY++){
				controlCell[counterX][counterY].addMouseListener(new MouseInput(player1Control,player2Control,controlCell));
				if(controlCell[counterX][counterY].getIcon()!=null)
					controlCell[counterX][counterY].addActionListener(new ButtonAction(cell,playerCell, individualPiece, controlCell, counterY, counterX, player1, player2));
			}
		}




		rotateLeft.addActionListener(e->{
			rotate(0);
		});

		rotateRight.addActionListener(e->{
			rotate(1);
		});
	}

	private void rotate(int direction){

		ImageIcon[][] pieceImage = new ImageIcon[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {

				if(direction==0)
					pieceImage[i][j] = (ImageIcon) individualPiece[j][3-i-1].getIcon();
				else if(direction==1)
					pieceImage[i][j] = (ImageIcon) individualPiece[3-j-1][i].getIcon();
			}
		}


		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				individualPiece[i][j].setIcon(pieceImage[i][j]);
			}
		}

	}

	private void makeButtonTransparent(JButton button){
		button.setOpaque(false);
		button.setContentAreaFilled(false);
	}

	public void load(){
		Scanner input = null;
		try {
			input = new Scanner(new File("maze.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int r= 0;
		while(input.hasNext()){
			maze[r]=input.nextLine().split(" ");
			for(int c=0;c<maze[r].length;c++){
				if(maze[r][c].equals("B")){
					controlCell[r][c] = new JButton();
					controlCell[r][c].setIcon(AssetManager.arrow);
					controlCell[r][c].setBorder(new LineBorder(Color.BLACK));
					makeButtonTransparent(controlCell[r][c]);
					controlPane.add(controlCell[r][c]);
				}else{
					controlCell[r][c] = new JButton();
					controlCell[r][c].setEnabled(false);
					controlCell[r][c].setOpaque(false);
					controlCell[r][c].setBorder(new LineBorder(Color.BLACK));
					controlPane.add(controlCell[r][c]);
				}

				for(int i = 0;i<9;i++){
					cell[r*3+i/3][c*3+i%3] = new JLabel();
					if(maze[r][c].equals("E")){
						maze[r][c]=Method.getRandom(AssetManager.piecesNeeded);
					}

					if(!maze[r][c].equals("B")&&Method.contains(AssetManager.toPosition.get(maze[r][c]), i)){
						if(i==4&&Method.contains(AssetManager.toPosition.get(maze[r][c]), -1)){
							cell[r*3+i/3][c*3+i%3].setIcon(AssetManager.treasuresIcon.get(counterImage));
							counterImage++;
						}else{
							cell[r*3+i/3][c*3+i%3].setIcon(AssetManager.route);
						}
					}else{
						cell[r*3+i/3][c*3+i%3].setIcon(AssetManager.wall);
					}
				}
			}
			r++;
		}

		for(int i = 0;i<9;i++){
			individualPiece[i/3][i%3] = new JLabel();

			if(Method.contains(AssetManager.toPosition.get(Method.getRandom(AssetManager.piecesNeeded)), i)){
				if(i==4&&Method.contains(AssetManager.toPosition.get(Method.getRandom(AssetManager.piecesNeeded)), -1)){
					individualPiece[i/3][i%3].setIcon(AssetManager.treasuresIcon.get(counterImage));
					counterImage++;
				}else{
					individualPiece[i/3][i%3].setIcon(AssetManager.route);
				}
			}else{
				individualPiece[i/3][i%3].setIcon(AssetManager.wall);
			}
			piecePane.add(individualPiece[i/3][i%3]);
		}


		for(int i = 0;i< 27;i++){
			for(int j = 0;j< 27;j++){
				gridPane.add(cell[i][j]);
			}
		}


		for(int i = 0;i< 27;i++){
			for(int j = 0;j< 27;j++){
				playerCell[i][j]= new JLabel();
				playerGridPane.add(playerCell[i][j]);
			}
		}

		player1 = new Player (1,1,playerCell, cell,AssetManager.player1, "Player1");
		player2 = new Player (7,7,playerCell, cell,AssetManager.player2, "Player2");

		solver1 = new Solver(cell, player1);
		solver2 = new Solver(cell, player2);

	}

}
