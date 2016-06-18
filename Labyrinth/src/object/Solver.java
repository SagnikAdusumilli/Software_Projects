package object;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import manager.AssetManager;

public class Solver {
	private JLabel[][] board;
	private Player player;
	private boolean solved = false;
	private HashSet<String> goodPosition;
	private ImageIcon[][] storedBoard;

	public Solver(JLabel[][] board, Player player){
		this.board = board;
		this.player = player;
		this.goodPosition = new HashSet<>();
		this.storedBoard = new ImageIcon[27][27];
	}
	
	public void solveWithNoDelay(){
		for(int i=0;i<27;i++){
			for(int j=0;j<27;j++){
				storedBoard[i][j] = (ImageIcon) board[i][j].getIcon();
			}	
		}
		goodPosition.clear();
		go(player.getX(), player.getY());
		for(int i=0; i<27;i++){
			for(int j=0; j<27;j++){
				if(board[i][j].getIcon()==AssetManager.white){
					board[i][j].setIcon(storedBoard[i][j]);
				}
			}	
		}
	}

	public void solve(){
		if(!solved){
			for(int i=0;i<27;i++){
				for(int j=0;j<27;j++){
					storedBoard[i][j] = (ImageIcon) board[i][j].getIcon();
				}	
			}

			goodPosition.clear();
			go(player.getX(), player.getY());
			solved = true;
			new Timer().schedule( 
					new TimerTask() {
						@Override
						public void run() {
							solve();
						}
					}, 
					100
					);
		}else{
			for(int i=0; i<27;i++){
				for(int j=0; j<27;j++){
					if(board[i][j].getIcon()==AssetManager.white){
						board[i][j].setIcon(storedBoard[i][j]);
					}
				}	
			}
			solved = false;
		}
	}

	public void go(int startX, int startY){
		goodPosition.add(startX/3+","+startY/3);
		if(board[startX][startY].getIcon()==AssetManager.route)
			board[startX][startY].setIcon(AssetManager.white);
		if (checkPosition(startX+1,startY)){
			go(startX+1,startY);
		}
		if (checkPosition(startX-1,startY)){
			go(startX-1,startY);
		}
		if (checkPosition(startX,startY+1)){
			go(startX,startY+1);
		}
		if (checkPosition(startX,startY-1)){
			go(startX,startY-1);
		}

	}

	private boolean checkPosition(int x, int y){
		if(board[x][y].getIcon()==AssetManager.wall||board[x][y].getIcon()==AssetManager.white){
			return false;
		}else{
			return true;
		}
	}

	public HashSet<String> getGoodPosition() {
		return goodPosition;
	}


}
