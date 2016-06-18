package ai;

import javax.swing.JButton;
import javax.swing.JLabel;

import manager.AssetManager;
import object.Player;

/**
 * 
 * @author Leo
 *	Example of an AI
 */
public class PlayerAI {

	private JLabel[][] board;
	private JLabel[][] buttons;
	private JLabel[][] individualPiece;
	private Player player1, player2;

	/**constructor should be made within the board class after when the board is initialized
	 *if you want to use the buttons and rotate pass it in in the constructer, if you dont just use your own
	 *
	 *if you want to see where you should add the AI go to "Board class" and line 89
	 * @param board
	 * @param buttons
	 * @param individualPiece
	 * @param player1
	 * @param player2
	 */
	public PlayerAI(JLabel[][] board, JButton[][] buttons, JLabel[][] individualPiece, JButton rotate, Player player1, Player player2){
		/*
		 * use 27*27 board,
		 * use 9*9 buttons,
		 * use 3*3 individualPiece
		 * 
		 * I just realize the (9*9 2D array with the board with String values does not update, if you really want to use it, try update it in the ButtonAction class
		 * However, I highly suggest you to work with the 27*27 board, thus you dont need to care about the direction or type of the piece, also you can use the maze solving method that we did before
		 */

		//example things that you can do

		player1.getX(); //this will return players position in 27*27 grid
		player1.getX9(); //this will return players position in 9*9 grid
		//same with player2
		//(if you want to change the name of the method or other settings go look into the code)
		buttons[2][0].doClick();//this will click the button[2][0] (pull the row by 1).remeber this will disable the other button on the other side, when you code be careful

		/**
		 * i think for checking what a piece represent, it is better if you do it this way
		 */


		//if it is a wall
		System.out.println(board[0][0].getIcon()==AssetManager.wall);

		//if it is a route
		System.out.println(board[0][0].getIcon()==AssetManager.route);

		//if it is a treasure (if you think the code is too long change it by create a method for it)
		System.out.println(AssetManager.treasuresIcon.contains(board[0][0].getIcon()));

		//check what treasure it is (make sure you check if it is a treasure first) (it is +1 because indexof start at 0)
		System.out.println(1+AssetManager.treasuresIcon.indexOf((board[0][0].getIcon())));


		//if you want to rotate the individual piece (remeber if you rotate u need to rotate is back later)
		rotate.doClick();

		//if you just want to get the piece (you can do the exact same thing)
		System.out.println(individualPiece[0][0].getIcon()==AssetManager.wall);

		//to get the player's targets (same with player2)
		player1.getTargets(); //this is an arrayList, this will update
		
		
		
		/*
		 * so now you have access to both players position and their targets
		 * you also can get position in the board, and check what is? A wall, route, treasure, and also you can get what treasure number it is
		 * you can claso get the individual piece
		 * 
		 * at this state you can basically give the "best" move
		 * 
		 * extra things you can do is rotate and shift the rows or columns with the buttons and code I provided you with
		 * 
		 */

	}

	/**this method should be execute when buttons is clicked or whenever it is settted to
	 * based on the parameter passed in with the constructer, calculate
	 */
	public void calculate(){

	}
}
