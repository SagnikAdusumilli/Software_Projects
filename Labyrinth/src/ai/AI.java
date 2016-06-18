package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JLabel;

import manager.AssetManager;
import object.Player;

public class AI {
	private char[][] maze;
	private JLabel[][] board;
	int count;

	Player player1;
	Player player2;

	private Set<Integer> columnsAffected;
	private Set<Integer> rowsAffected;
	private Set<Integer> oppColumnsAffected;
	private Set<Integer> oppRowsAffected;
	private Set<Integer> nextColumnsAffected;
	private Set<Integer> nextRowsAffected;
	private int targetX;
	private int targetY;

	private int player1X;
	private int player1Y;
	private int player2X;
	private int player2Y;
	private int oppTreasure = 0;

	private final int maxDist = 27 * 27;
	private double minDist;
	private Point nearestPoint = new Point(0, 0);

	boolean initialized;
	boolean foundTreasure;
	boolean oppFoundTreasure;
	boolean foundNearestPoint;

	private int staticTargetCounter1 = 0;
	private int staticTargetCounter2 = 0;

	private ArrayList<Point> targetLocatons;

	private JLabel[][] indiviualPiece;
	private JButton[][] buttons;

	private char[][] extraPiece;

	Stack<int[]> path;
	Stack<int[]> visited;

	// board is 27x27
	// calculate method can be found in Player control line 69
	public AI(JLabel[][] board, JButton[][] buttons, JLabel[][] individualPiece, JButton rotate, Player player1,
			Player player2) {

		// 1- rotation 2, 3 - posY, posX, 4, 5- button pos 9x9 rows, columns

		this.player1 = player1;
		this.player2 = player2;
		this.board = board;
		this.buttons = buttons;

		targetLocatons = new ArrayList<>();

		// System.out.println(buttons[2][8].isEnabled());
		maze = new char[board.length][board[0].length];

		this.indiviualPiece = individualPiece;

		path = new Stack<>();
		columnsAffected = new TreeSet<>();
		rowsAffected = new TreeSet<>();
		oppColumnsAffected = new TreeSet<>();
		oppRowsAffected = new TreeSet<>();
		nextColumnsAffected = new TreeSet<>();
		nextRowsAffected = new TreeSet<>();

		updateMaze(this.board);

	}

	public void updateMaze(JLabel[][] board) {
		targetLocatons.clear();
		path.clear();

		player1X = player1.getY();
		player1Y = player1.getX();

		player2X = player2.getY();
		player2Y = player2.getX();

		staticTargetCounter2 = 0;
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {

				if (board[y][x].getIcon().equals(AssetManager.route)) {

					maze[y][x] = '.';

				} else if (board[y][x].getIcon().equals(AssetManager.wall)) {

					maze[y][x] = '#';

				} else if (AssetManager.treasuresIcon.contains(board[y][x].getIcon())) {

					if (player1.getTargets()
							.contains("" + (1 + AssetManager.treasuresIcon.indexOf(board[y][x].getIcon())))) {
						maze[y][x] = 'A';
						targetLocatons.add(new Point(x, y));

						if (!initialized) {
							if (((x / 3)) % 2 == 1 || ((y / 3)) % 2 == 1) {
								staticTargetCounter1++;
							}
							initialized = true;
						}

					} else if (player2.getTargets()
							.contains("" + (1 + AssetManager.treasuresIcon.indexOf(board[y][x].getIcon())))) {

						if (((x / 3)) % 2 == 1 || ((y / 3)) % 2 == 1) {
							staticTargetCounter2++;
						}

						maze[y][x] = 'B';
					} else {
						maze[y][x] = 'T';
					}
				}
			}
		}
		// maze[player1.getY()][player1.getX()] = '1';
		// maze[player2.getY()][player2.getX()] = '2';

	}

	// x- col y- row
	public void calculate() {

		// System.out.println("X: " + player1X + " Y: " + player1Y);
		foundTreasure = false;
		updateMaze(board);

		columnsAffected.clear();
		rowsAffected.clear();
		oppRowsAffected.clear();
		oppColumnsAffected.clear();
		nextColumnsAffected.clear();
		nextRowsAffected.clear();

		// System.out.println("starting from: "+player1X/3+" "+player1Y/3);
		// System.out.println("acutal starting point:
		// "+player1.getX9()+player1.getY9());

		searchTreasure();

		if (player2.getTargets().size() == 1 && player1.getTargets().size() == 5) {

		}
		// Leo's code is reversed so X is acutally Y

		// updateMaze(board);
	}

	public void searchTreasure() {

		generatePaths(getCopyOf(maze), player1X, player1Y, 'A', "ini");

		// attack
		int posX = 0;
		int posY = 0;
		int btnX = 0;
		int btnY = 0;
		int orientaton = 0;

		// if the treause is found in the first shot
		if (foundTreasure) {
			System.out.println("Treasure");

			Point randomBtn = getRandomButton();
			while(!buttons[randomBtn.getY()][randomBtn.getX()].isEnabled()){
				randomBtn = getRandomButton();
			}

			btnX = randomBtn.getX();
			btnY = randomBtn.getY();

			posX = targetX;
			posY = targetY;
			System.out.println("getting random btn");
			System.out.println(0 + " " + btnY + " " + btnX + " " + posY + " " + posX);

		}

		// if it is not found, then try inserting a piece and finding it
		else if (!foundTreasure) {
			// run simulations on all the c and r affected and check again

			if (rowsAffected.size() > 0) {
				ArrayList<char[][]> list = getAllOrientations(indiviualPiece);
				for (int r : rowsAffected) {

					updateMaze(board);
					updatePos();

					for (char[][] piece : list) {

						char[][] nBoard = new char[27][27];
						nBoard = getCopyOf(maze);

						if (buttons[r][0].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, r, 1);
							generatePaths(nBoard, player1X, player1Y, 'A', "");

						}

						// displayMaze(maze);

						// if treasure is found after inserting piece from top
						if (foundTreasure) {
							System.out.println("Treasure");
							System.out.println(list.indexOf(piece) + " " + r + " " + 0 + " " + targetY + " " + targetX);
							return;
						}
						// resetting the player value
						updatePos();

						updateMaze(board);

						nBoard = getCopyOf(maze);

						if (buttons[r][8].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, r, 7);
							generatePaths(nBoard, player1X, player1Y, 'A', "");
						}

						System.out.println();

						// if treasure if found after inserting piece from
						// bottom
						if (foundTreasure) {
							System.out.println("Treasure");
							System.out.println(list.indexOf(piece) + " " + r + " " + 8 + " " + targetY + " " + targetX);
							return;
						}

						updatePos();

					}
				}
			}
			updatePos();
			if (columnsAffected.size() > 0) {
				ArrayList<char[][]> list = getAllOrientations(indiviualPiece);
				for (int c : columnsAffected) {

					updateMaze(board);

					for (char[][] piece : list) {

						char[][] nBoard = new char[27][27];
						updateMaze(board);

						nBoard = getCopyOf(maze);
						if (buttons[0][c].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, 1, c);
							generatePaths(nBoard, player1X, player1Y, 'A', "");
						}

						System.out.println();
						// if treasure found after inserting from left
						if (foundTreasure) {
							System.out.println("Treasure");
							System.out.println(list.indexOf(piece) + " " + 0 + " " + c + " " + targetY + " " + targetX);
							return;
						}
						// resetting the player values
						updatePos();

						updateMaze(board);

						nBoard = getCopyOf(maze);

						if (buttons[8][c].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, 7, c);
							generatePaths(nBoard, player1X, player1Y, 'A', "");
						}

						System.out.println();
						// if treasure found after inserting from right
						if (foundTreasure) {
							System.out.println("Treasure");
							System.out.println(list.indexOf(piece) + " " + 8 + " " + c + " " + targetY + " " + targetX);
							return;
						}
						// resetting the player values
						updatePos();
					}
				}
			}

			updateMaze(board);
			// if treasure is not found after inserting piece everywhere
			System.out.println("NO TREASURE");
			System.out.println("trying to find closest point");
			
			int[] values = getClose(null, 1);
			
			orientaton = values[0];
			btnX = values[2];
			btnY = values[1];
			posX = values[4];
			posY = values[3];
			
			System.out.println(orientaton+" "+ btnY+" "+btnX+" "+posY+" "+posX);
			
			// System.out.println("getting random btn");
			// Point randomBtn = getRandomButton();
			//
			// btnX = randomBtn.getX();
			// btnY = randomBtn.getY();
			//
			// posX = player1.getY9();
			// posY = player1.getX9();

		}
	}

	public boolean checkOpp(char[][] board) {
		foundTreasure = false;

		generatePaths(board, player2.getY(), player2.getX(), 'B', "");

		if (foundTreasure) {
			System.out.println(true);
			return true;
		} else
			return false;
	}

	// attack the opponent
	public void attack(char[][] b) {

		foundTreasure = false;

		generatePaths(Arrays.copyOf(b, b.length), player2.getY(), player2.getX(), 'B', "attack");

		if (oppTreasure > 1) {

		} else {
			ArrayList<int[]> moves = preventNextMove(b);
			System.out.println(moves.get(0)[0]);
		}

	}

	public ArrayList<char[][]> getPieceOrientation(char[][] exPiece) {
		ArrayList<char[][]> allPieces = new ArrayList<>();

		allPieces.add(exPiece);

		for (int i = 0; i < 3; i++) {
			char[][] nPiece = clearBoard();

			char[][] prevPiece = allPieces.get(allPieces.size() - 1);
			nPiece[1][1] = prevPiece[1][1];
			nPiece[0][1] = prevPiece[1][0];
			nPiece[1][2] = prevPiece[0][1];
			nPiece[2][1] = prevPiece[1][2];
			nPiece[1][0] = prevPiece[2][1];
			allPieces.add(nPiece);
			// displayMaze(nPiece);
		}

		return allPieces;
	}

	public ArrayList<int[]> preventNextMove(char[][] m) {

		oppFoundTreasure = false;
		ArrayList<int[]> p = new ArrayList<>();
		char[][] nBoard;

		ArrayList<char[][]> allPieces = getAllOrientations(indiviualPiece);

		for (int r = 2; r < 8; r += 2) {
			updatePos();
			for (char[][] piece : allPieces) {
				nBoard = getCopyOf(m);

				if (buttons[r][0].isEnabled()) {
					nBoard = testInsertion(nBoard, piece, r, 1);
					generatePaths(getCopyOf(nBoard), player2X, player2Y, 'B', "");

					if (!oppFoundTreasure) {
						if (!checkOppNextTarget(getCopyOf(nBoard), getExtraPiece(), r, 8, player2X, player2Y)) {
							p.add(new int[] { allPieces.indexOf(piece), r, 0 });
						}
					}
				}
				updatePos();

				nBoard = getCopyOf(m);

				if (buttons[r][8].isEnabled()) {
					nBoard = testInsertion(nBoard, piece, r, 7);
					generatePaths(getCopyOf(nBoard), player2X, player2Y, 'B', "");

					if (!oppFoundTreasure) {
						if (!checkOppNextTarget(getCopyOf(nBoard), getExtraPiece(), r, 0, player2X, player2Y)) {
							p.add(new int[] { allPieces.indexOf(piece), r, 8 });
						}
					}
				}

				updatePos();

			}
		}

		for (int c = 2; c < 8; c += 2) {
			for (char[][] piece : allPieces) {
				nBoard = getCopyOf(m);

				if (buttons[0][c].isEnabled()) {
					nBoard = testInsertion(nBoard, piece, 1, c);
					generatePaths(getCopyOf(nBoard), player2X, player2Y, 'B', "");

					if (!oppFoundTreasure) {
						if (!checkOppNextTarget(getCopyOf(nBoard), getExtraPiece(), 8, c, player2X, player2Y)) {
							p.add(new int[] { allPieces.indexOf(piece), 0, c });
						}
					}
				}
				updatePos();

				nBoard = getCopyOf(m);

				if (buttons[8][c].isEnabled()) {
					nBoard = testInsertion(nBoard, piece, 7, c);
					generatePaths(getCopyOf(nBoard), player2X, player2Y, 'B', "");

					if (!oppFoundTreasure) {
						if (!checkOppNextTarget(getCopyOf(nBoard), getExtraPiece(), 0, c, player2X, player2Y)) {
							p.add(new int[] { allPieces.indexOf(piece), 8, c });
						}
					}
				}
				updatePos();

			}
		}

		return p;

	}

	public void updatePos() {
		player1X = player1.getY();
		player1Y = player1.getX();

		player2X = player2.getY();
		player2Y = player2.getX();
	}

	public char[][] getCopyOf(char[][] m) {
		char[][] n = new char[27][27];
		for (int r = 0; r < 27; r++)
			for (int c = 0; c < 27; c++)
				n[r][c] = m[r][c];

		return n;
	}

	public boolean checkOppNextTarget(char[][] maze, char[][] eP, int buttonR, int buttonC, int p2X, int p2Y) {

		// return true if the popped piece results in opp getting treasure
		char[][] nBoard;

		nextColumnsAffected.clear();
		nextRowsAffected.clear();

		generatePaths(getCopyOf(maze), player2X, player2Y, 'B', "next");

		if (oppFoundTreasure)
			return true;
		// opponent

		player2X = p2X;
		player2Y = p2Y;
		if (nextRowsAffected.size() > 0) {
			for (int r : nextRowsAffected) {
				for (char[][] piece : getPieceOrientation(eP)) {
					nBoard = getCopyOf(maze);

					if (buttonR != r && buttonC != 0) {
						nBoard = testInsertion(nBoard, piece, r, 1);
						generatePaths(nBoard, player2X, player2Y, 'B', "");
						if (!oppFoundTreasure) { // opponent
							return true;
						}
					}

					player2X = p2X;
					player2Y = p2Y;

					nBoard = getCopyOf(maze);

					if (buttonR != r && buttonC != 8) {
						nBoard = testInsertion(nBoard, piece, r, 7);
						generatePaths(nBoard, player2X, player2Y, 'B', "");
						if (!oppFoundTreasure) {
							return true;
						}
					}

					player2X = p2X;
					player2Y = p2Y;

				}
			}
		}
		player2X = p2X;
		player2Y = p2Y;

		if (nextColumnsAffected.size() > 0) {
			for (int c : nextColumnsAffected) {

				for (char[][] piece : getPieceOrientation(eP)) {

					nBoard = getCopyOf(maze);

					if (buttonR != 0 && buttonC == c) {
						nBoard = testInsertion(nBoard, piece, 1, c);
						generatePaths(nBoard, player2X, player2Y, 'B', "");
						if (!oppFoundTreasure) {
							return true;
						}
					}

					player2X = p2X;
					player2Y = p2Y;

					nBoard = getCopyOf(maze);

					if (buttonR != 8 && buttonC != c) {
						nBoard = testInsertion(nBoard, piece, 7, c);
						generatePaths(nBoard, player2X, player2Y, 'B', "");
						if (!oppFoundTreasure) {
							return true;
						}

					}

					player2X = p2X;
					player2Y = p2Y;

				}
			}
		}
		player2X = p2X;
		player2Y = p2Y;
		return false;
	}

	public char[][] testInsertion(char[][] maze, char[][] piece, int rIn, int cIn) {
		// clear all the existing targets
		targetLocatons.clear();

		char[][] board = getCopyOf(maze);
		char[][] eP = new char[3][3];

		if (rIn == 1) { // top
			if (cIn == player1X / 3 && player1Y / 3 == 7) {
				player1Y = 4;
			} else if (cIn == player1X / 3) {
				player1Y += 3;
			}
			if (cIn == player2X / 3 && player2Y / 3 == 7) {
				player2Y = 4;
			} else if (cIn == player2X / 3) {
				player2Y += 3;
			}

			for (int c = (cIn) * 3; c < (cIn + 1) * 3; c++) {
				for (int r = 23; r > 2; r--) {
					if (r > 20)
						eP[r % 3][c % 3] = board[r][c];
					if (r < 6)
						board[r][c] = piece[r % 3][c % 3];
					else
						board[r][c] = board[r - 3][c];
				}
			}

		}

		if (rIn == 7) { // bottom
			if (cIn == player1X / 3 && player1Y / 3 == 1) {
				player1Y = 22;
			} else if (cIn == player1X / 3) {
				player1Y -= 3;
			}
			if (cIn == player2X / 3 && player2Y / 3 == 1) {
				player2Y = 22;
			} else if (cIn == player2X / 3) {
				player2Y -= 3;
			}
			for (int c = (cIn) * 3; c < (cIn + 1) * 3; c++) {
				for (int r = 3; r < 24; r++) {
					if (r < 6)
						eP[r % 3][c % 3] = board[r][c];
					if (r > 20)
						board[r][c] = piece[r % 3][c % 3];
					else
						board[r][c] = board[r + 3][c];

				}
			}
		}

		if (cIn == 1) { // left
			if (rIn == player1Y / 3 && player1X / 3 == 7) {
				player1X = 4;
			} else if (rIn == player1Y / 3) {
				player1X += 3;
			}
			if (rIn == player2Y / 3 && player2X / 3 == 7) {
				player2X = 4;
			} else if (rIn == player2Y / 3) {
				player2X += 3;
			}
			for (int r = (rIn) * 3; r < (rIn + 1) * 3; r++) {
				for (int c = 23; c > 2; c--) {
					if (c > 20)
						eP[r % 3][c % 3] = board[r][c];
					if (c < 6)
						board[r][c] = piece[r % 3][c % 3];
					else
						board[r][c] = board[r][c - 3];
				}
			}
		}

		if (cIn == 7) { // right
			if (rIn == player1Y / 3 && player1X / 3 == 1) {
				player1X = 22;
			} else if (rIn == player1Y / 3) {
				player1X -= 3;
			}
			if (rIn == player2Y / 3 && player2X / 3 == 1) {
				player2X = 22;
			} else if (rIn == player2Y / 3) {
				player2X -= 3;
			}
			for (int r = (rIn) * 3; r < (rIn + 1) * 3; r++) {
				for (int c = 3; c < 24; c++) {
					if (c < 6)
						eP[r % 3][c % 3] = board[r][c];
					if (c > 20)
						board[r][c] = piece[r % 3][c % 3];
					else
						board[r][c] = board[r][c + 3];
				}
			}
		}

		setExtraPiece(eP);

		// reset the value of the targets

		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				if (board[y][x] == 'A') {
					targetLocatons.add(new Point(x, y));
				}
			}
		}
		return board;
	}

	public ArrayList<char[][]> getAllOrientations(JLabel[][] individualPiece) {

		ArrayList<char[][]> allPieces = new ArrayList<>();
		char[][] piece = new char[3][3];

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (individualPiece[r][c].getIcon().equals(AssetManager.wall))
					piece[r][c] = '#';
				if (individualPiece[r][c].getIcon().equals(AssetManager.route)) {
					piece[r][c] = '.';
				}
				if (AssetManager.treasuresIcon.contains(individualPiece[r][c].getIcon())) {

					if (player1.getTargets().contains(
							Integer.toString(1 + AssetManager.treasuresIcon.indexOf(individualPiece[r][c].getIcon()))))
						piece[r][c] = 'A';
					else if (player2.getTargets().contains(
							Integer.toString(1 + AssetManager.treasuresIcon.indexOf(individualPiece[r][c].getIcon()))))
						piece[r][c] = 'B';
					else
						piece[r][c] = 'T';

				}
			}
		}
		allPieces.add(piece);

		// 0-> original; 1, 2, 3-> rotated clockwise
		for (int i = 0; i < 3; i++) {
			char[][] nPiece = clearBoard();

			char[][] prevPiece = allPieces.get(allPieces.size() - 1);
			nPiece[1][1] = prevPiece[1][1];
			nPiece[0][1] = prevPiece[1][0];
			nPiece[1][2] = prevPiece[0][1];
			nPiece[2][1] = prevPiece[1][2];
			nPiece[1][0] = prevPiece[2][1];
			allPieces.add(nPiece);

		}

		return allPieces;

	}

	public char[][] clearBoard() {
		char[][] m = new char[3][3];
		for (int i = 0; i < m.length; i++)
			for (int x = 0; x < m[0].length; x++)
				m[i][x] = '#';

		return m;
	}

	public void setExtraPiece(char[][] eP) {
		extraPiece = eP;
	}

	public char[][] getExtraPiece() {
		return extraPiece;
	}

	public void colAndRows(int x, int y, String state) {
		if (state.equals("ini")) {
			if ((x / 3) % 2 == 1) {
				columnsAffected.add((x / 3) + 1);
			}
			if ((x / 3) % 2 == 0) {
				columnsAffected.add((x / 3));
			}

			if ((y / 3) % 2 == 1) {
				rowsAffected.add((y / 3) + 1);
			}
			if ((y / 3) % 2 == 0) {
				rowsAffected.add((y / 3));
			}
		} else if (state.equals("attack")) {
			if ((x / 3) % 2 == 1) {
				oppColumnsAffected.add((x / 3) + 1);
			}
			if ((x / 3) % 2 == 0) {
				oppColumnsAffected.add((x / 3));
			}

			if ((y / 3) % 2 == 1) {
				oppRowsAffected.add((y / 3) + 1);
			}
			if ((y / 3) % 2 == 0) {
				oppRowsAffected.add((y / 3));
			}
		} else if (state.equals("next")) {
			if ((x / 3) % 2 == 1) {
				nextColumnsAffected.add((x / 3) + 1);
			}
			if ((x / 3) % 2 == 0) {
				nextColumnsAffected.add((x / 3));
			}

			if ((y / 3) % 2 == 1) {
				nextRowsAffected.add((y / 3) + 1);
			}
			if ((y / 3) % 2 == 0) {
				nextRowsAffected.add((y / 3));
			}
		}

	}

	public void generatePaths(char m[][], int x, int y, char type, String state) {

		char[][] maze = getCopyOf(m);

		colAndRows(x, y, state);

		if (state.equals("count")) {
			double dist = getNearestDist(x, y);

			if (dist < minDist) {
				foundNearestPoint = true;
				minDist = dist;
				nearestPoint.setX(x);
				nearestPoint.setY(y);
			}
		}

		if (foundTreasure) {
			return;
		}
		if (type == 'B') {
			if (maze[y][x] == 'B') {
				oppTreasure++;
				oppFoundTreasure = true;
				targetX = x / 3;
				targetY = y / 3;
				if (!state.equals("attack"))
					return;
			}
		} else {
			if (maze[y][x] == 'A') {
				if (staticTargetCounter1 != 1) {
					foundTreasure = true;
					targetX = x / 3;
					targetY = y / 3;
					return;
				} else if ((y / 3) % 2 == 1 || (x / 3) % 2 == 1) {
					foundTreasure = true;
					targetX = x / 3;
					targetY = y / 3;
					return;
				}
			}
		}

		maze[y][x] = 'X';
		// int locationY = y;
		// int locationX = x;
		int[] points = { x, y };
		path.push(points);
		Random r = new Random();
		int dir;
		boolean foundNode = false;

		// 0 = up, 1 = right, 2 = down, 3 = right
		while (hasNext(x, y, maze)) {
			dir = r.nextInt(4);

			// System.err.println("direction: " + dir);
			switch (dir) {
			case 0: // check up
				if (isValid(x, y - 1, maze)) {

					if (maze[y - 1][x] != 'A' && maze[y - 1][x] != 'B') {
						maze[y - 1][x] = 'X';
					}

					foundNode = true;
					generatePaths(maze, x, y - 1, type, state);
					return;
				}
				break;
			case 1: // check right
				if (isValid(x + 1, y, maze)) {

					if (maze[y][x + 1] != 'A' && maze[y][x + 1] != 'B') {
						maze[y][x + 1] = 'X';
					}
					foundNode = true;
					generatePaths(maze, x + 1, y, type, state);
					return;
				}
				break;
			case 2: // check down
				if (isValid(x, y + 1, maze)) {

					if (maze[y + 1][x] != 'A' && maze[y + 1][x] != 'B') {
						maze[y + 1][x] = 'X';
					}
					foundNode = true;
					generatePaths(maze, x, y + 1, type, state);
					return;
				}
				break;
			case 3: // check left
				if (isValid(x - 1, y, maze)) {

					if (maze[y][x - 1] != 'A' && maze[y][x - 1] != 'B') {
						maze[y][x - 1] = 'X';
					}
					foundNode = true;
					generatePaths(maze, x - 1, y, type, state);
					return;
				}
			}
		}
		if (!foundNode) {
			// System.out.println("X " + x/3 + " " + " Y " + y/3);
			path.pop();
			if (!path.isEmpty()) {
				int[] previousPoints = path.pop();

				generatePaths(maze, previousPoints[0], previousPoints[1], type, state);
			}

		}
	}

	boolean hasNext(int x, int y, char[][] maze) {
		if (isValid(x, y - 1, maze)) {
			return true;
		}
		if (isValid(x + 1, y, maze)) {
			return true;
		}
		if (isValid(x, y + 1, maze)) {
			return true;
		}
		if (isValid(x - 1, y, maze)) {
			return true;
		}
		return false;
	}

	public boolean isValid(int x, int y, char[][] maze) {
		if (x < 0 || y < 0 || x >= maze[0].length || y >= maze.length) {
			return false;
		} else if (maze[y][x] == '.' || maze[y][x] == 'B' || maze[y][x] == 'A' || maze[y][x] == 'T') {
			return true;
		} else {
			return false;
		}
	}

	public int[] getClose(ArrayList<int[]> list, int mode) {
		// mode 0 = attack, mode 1 = getCloser
		int[] values = new int[5];
		minDist = maxDist;
		
		if (mode == 0) {

			for (int[] points : list) {
				foundNearestPoint = false;

				char[][] piece = getCopyOf(getAllOrientations(indiviualPiece).get(points[0]));
				generatePaths(testInsertion(getCopyOf(maze), piece, points[1], points[2]), player1X, player1Y, 'A',
						"count");

				if (foundNearestPoint) {
					values[0] = points[0];
					values[1] = points[1];
					values[2] = points[2];
					values[3] = nearestPoint.getX();
					values[4] = nearestPoint.getY();
					foundNearestPoint = false;
				}
				updatePos();
			}
		} 
		else {
			if (rowsAffected.size() > 0) {
				System.out.println("checking all rows");
				ArrayList<char[][]> orientations = getAllOrientations(indiviualPiece);

				for (int r : rowsAffected) {
					foundNearestPoint = false;
					updateMaze(board);
					updatePos();

					for (char[][] piece : orientations) {

						char[][] nBoard = new char[27][27];
						nBoard = getCopyOf(maze);

						if (buttons[r][0].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, r, 1);
							generatePaths(nBoard, player1X, player1Y, 'A', "count");
							
							if (foundNearestPoint) {
								
								values[0] = orientations.indexOf(piece);
								values[1] = r;
								values[2] = 0;
								values[3] = nearestPoint.getY() / 3;
								values[4] = nearestPoint.getX() / 3;
								
								foundNearestPoint = false;
							}
						}

						// resetting the player value
						updatePos();

						updateMaze(board);

						nBoard = getCopyOf(maze);

						if (buttons[r][8].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, r, 7);
							generatePaths(nBoard, player1X, player1Y, 'A', "count");
							
							if (foundNearestPoint) {
								
								values[0] = orientations.indexOf(piece);
								values[1] = r;
								values[2] = 8;
								values[3] = nearestPoint.getY() / 3;
								values[4] = nearestPoint.getX() / 3;
								
								foundNearestPoint = false;
							}
						}

						updatePos();

					}
				}
			}
			updatePos();
			if (columnsAffected.size() > 0) {
				System.out.println("checking all colums");
				ArrayList<char[][]> orientations = getAllOrientations(indiviualPiece);
				for (int c : columnsAffected) {
					foundNearestPoint = false;
					updateMaze(board);

					for (char[][] piece : orientations) {

						char[][] nBoard = new char[27][27];
						updateMaze(board);

						nBoard = getCopyOf(maze);
						if (buttons[0][c].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, 1, c);
							generatePaths(nBoard, player1X, player1Y, 'A', "count");
							
							if (foundNearestPoint) {
								
								values[0] = orientations.indexOf(piece);
								values[1] = 0;
								values[2] = c;
								values[3] = nearestPoint.getY() / 3;
								values[4] = nearestPoint.getX() / 3;
								
								foundNearestPoint = false;
							}
							
						}

						// resetting the player values
						updatePos();

						updateMaze(board);

						nBoard = getCopyOf(maze);

						if (buttons[8][c].isEnabled()) {
							nBoard = testInsertion(nBoard, piece, 7, c);
							generatePaths(nBoard, player1X, player1Y, 'A', "");
							
							if (foundNearestPoint) {
								
								values[0] = orientations.indexOf(piece);
								values[1] = 8;
								values[2] = c;
								values[3] = nearestPoint.getY() / 3;
								values[4] = nearestPoint.getX() / 3;
								
								foundNearestPoint = false;
							}
						}

						updatePos();
					}
				}
			}
		}
		return values;

	}

	public double getNearestDist(int x, int y) {
		double minDist = maxDist;
		double dist;
		for (Point p : targetLocatons) {
			dist = Math.hypot(Math.abs((double) p.getX() - x), Math.abs((double) p.getY() - y));
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	public void displayMaze(char maze[][]) {
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				System.out.print(maze[y][x] + " ");
			}
			System.out.println();
		}
		System.out.println();

	}

	public Point getRandomButton() {
		Point p = new Point(0, 0);

		Random r = new Random();
		int rand = r.nextInt(7) + 1;

		int randomR = r.nextInt(1);
		if (randomR == 0) {

			while (columnsAffected.contains(rand) || rand % 2 != 0) {
				rand = r.nextInt(7) + 1;
			}
			p.setY(rand);

			int rC = r.nextInt(1);
			if (rC == 0) {
				p.setX(0);
			} else
				p.setY(8);

		} else {
			int rand2 = r.nextInt(7) + 1;
			while (rowsAffected.contains(rand2) || rand2 % 2 != 0) {
				rand2 = r.nextInt(7) + 1;
			}
			p.setX(0);

			int rR = r.nextInt(1);
			if (rR == 0) {
				p.setY(0);
			} else
				p.setY(8);
		}
			return p;
	}

	class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
}
