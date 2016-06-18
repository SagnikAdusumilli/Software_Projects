import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Maze extends JFrame {
	int sleepTime = 50;
	static boolean play;
	static char[][] maze;
	static JLabel[][] mazeImg;
	int locationX, locationY;
	static Stack<int[]> path = new Stack<>();
	int startX, startY;
	int endX, endY;
	JPanel mazePanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	Scanner input = new Scanner(new File("maze"));
	ImageIcon WALL = new ImageIcon("stdWall.bmp");
	ImageIcon BLANK = new ImageIcon("Black.bmp");
	ImageIcon GHOST = new ImageIcon("Ghost1.bmp");
	ImageIcon DOT = new ImageIcon("PowerUP.png");

	public Maze(int startX, int startY, int length, int width) throws FileNotFoundException {
		locationX = startX;
		locationY = startY;
		maze = new char[length][width];
		mazeImg = new JLabel[length][width];
		setTitle("Maze");
		setLayout(new BorderLayout());
		loadMaze(length, width);
		add(mazePanel, BorderLayout.CENTER);
		populateBottomPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WALL.getIconWidth() * (width+10), WALL.getIconHeight() * (length+10));
		setVisible(true);

	}

	public void populateBottomPanel() {
		bottomPanel.setOpaque(true);
		bottomPanel.setBackground(Color.BLACK);
		JButton btn = new JButton("next");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!play) {
					play = true;
				}
			}
		});
		bottomPanel.add(btn);
	}

	public void loadMaze(int height, int width) {
		mazePanel.setOpaque(true);
		mazePanel.setBackground(Color.BLACK);
		mazePanel.setLayout(new GridLayout(height, width));
		JLabel label = new JLabel();
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				mazeImg[y][x] = new JLabel();
				if (maze[y][x] == '#') {
					mazeImg[y][x].setIcon(WALL);
				} else {
					mazeImg[y][x].setIcon(BLANK);
				}
				mazePanel.add(mazeImg[y][x]);
				revalidate();
			}
		}
	}

	public void refresth() {
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				if (maze[y][x] == '#') {
					mazeImg[y][x].setIcon(WALL);
				} else if (maze[y][x] == '.') {
					if(x!=endX || y!=endY){
						mazeImg[y][x].setText("");
						mazeImg[y][x].setIcon(DOT);
					}
				} else{
					mazeImg[y][x].setIcon(null);
				}
			}
		}
		mazeImg[locationY][locationX].setIcon(GHOST);
		revalidate();
	}

	public boolean traverse(int x, int y, int endX, int endY) {
		maze[y][x] = 'X';
		locationX = x;
		locationY = y;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printMaze();
		if (y == endY && x == endX) {
			System.out.println("Done!" + " endX: " + endX + " endY " + endY);
			refresth();
			return true;
		}
		System.out.println("x: " + x + " " + "y: " + y);
		refresth();

		if (isValid(x + 1, y, 0)) { // moving right
			maze[y][x + 1] = 'X';
			refresth();
			if (traverse(x + 1, y, endX, endY)) {
				return true;
			}
		}
		if (isValid(x - 1, y, 0)) { // moving left
			maze[y][x - 1] = 'X';
			refresth();
			if (traverse(x - 1, y, endX, endY)) {
				return true;
			}
		}
		if (isValid(x, y - 1, 0)) { // moving up
			maze[y - 1][x] = 'X';
			refresth();
			if (traverse(x, y - 1, endX, endY)) {
				return true;
			}
		}
		if (isValid(x, y + 1, 0)) { // moving down
			maze[y + 1][x] = 'X';
			refresth();
			if (traverse(x, y + 1, endX, endY)) {
				return true;
			}
		}
		System.out.println("not found");
		maze[y][x] = '0';
		refresth();
		return false;
	}

	public void generateMaze() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j] = '#';
			}
		}
		Random r = new Random();

		startX = r.nextInt(2);
		if (startX != 1) {
			startX = maze[0].length - 2;
		}

		startY = r.nextInt(2);
		if (startY != 1) {
			startY = maze[0].length - 2;
		}

		createPath(startX, startY);
		countPath(startX, startY, 0);
		setlectEndPoint();

		traverse(startX, startY, endX, endY);

	}

	public void createPath(int x, int y) {
		// Scanner input = new Scanner(System.in);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// input.nextLine();
		refresth();
		maze[y][x] = '.';
		locationY = y;
		locationX = x;
		int[] points = { x, y };
		path.push(points);
		Random r = new Random();
		int dir;
		boolean foundNode = false;
		// 0 = up, 1 = right, 2 = down, 3 = right
		while (hasNext(x, y)) {
			dir = r.nextInt(4);
			System.err.println("direction: " + dir);
			switch (dir) {
			case 0: // check up
				if (isValid(x, y - 2, 1)) {
					for (int i = 0; i < 3; i++) {
						maze[y - i][x] = '.';
					}
					foundNode = true;
					locationY = y - 2;
					createPath(x, y - 2);
					return;
				}
				break;
			case 1: // check right
				if (isValid(x + 2, y, 1)) {
					for (int i = 0; i < 3; i++) {
						maze[y][x + i] = '.';
					}
					foundNode = true;
					locationX = x + 2;
					createPath(x + 2, y);
					return;
				}
				break;
			case 2: // check down
				if (isValid(x, y + 2, 1)) {

					for (int i = 0; i < 3; i++) {
						maze[y + i][x] = '.';
					}
					foundNode = true;
					locationY = y + 2;
					createPath(x, y + 2);
					return;
				}
				break;
			case 3: // check left
				if (isValid(x - 2, y, 1)) {
					for (int i = 0; i < 3; i++) {
						maze[y][x - i] = '.';
					}
					foundNode = true;
					locationX = x - 2;
					createPath(x - 2, y);
					return;
				}
			}
		}
		if (!foundNode) {
			System.out.println("X " + x + " " + " Y " + y);
			path.pop();
			if (!path.isEmpty()) {
				int[] previousPoints = path.pop();

				createPath(previousPoints[0], previousPoints[1]);
			}
		}
	}

	public void countPath(int x, int y, int count) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refresth();
		maze[y][x] = 'X';
		mazeImg[y][x].setIcon(null);
		mazeImg[y][x].setForeground(Color.WHITE);
		mazeImg[y][x].setText(String.valueOf(count));
		// locationX = x;
		// locationY = y;
		int[] points = { x, y };
		path.push(points);
		boolean foundNode = false;

		if (isValid(x + 1, y, 0)) { // moving right

			maze[y][x + 1] = 'X';
			countPath(x + 1, y, count + 1);
			foundNode = true;

		} else if (isValid(x - 1, y, 0)) { // moving left

			maze[y][x - 1] = 'X';
			countPath(x - 1, y, count + 1);
			foundNode = true;

		} else if (isValid(x, y - 1, 0)) { // moving up

			maze[y - 1][x] = 'X';
			countPath(x, y - 1, count + 1);
			foundNode = true;

		} else if (isValid(x, y + 1, 0)) { // moving down

			maze[y + 1][x] = 'X';
			countPath(x, y + 1, count + 1);
			foundNode = true;
		}

		if (!foundNode) {
			mazeImg[y][x].setForeground(Color.red);
			System.out.println("X " + x + " " + " Y " + y);
			path.pop();
			if (!path.isEmpty()) {
				int[] previousPoints = path.pop();

				countPath(previousPoints[0], previousPoints[1], count - 1);
			}
		}

	}

	public void setlectEndPoint() {
		int max = 0;
		int pointerX = 0;
		int pointerY = 0;
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				if (!mazeImg[y][x].getText().equals("")) {
					if (Integer.parseInt(mazeImg[y][x].getText()) > max) {
						max = Integer.parseInt(mazeImg[y][x].getText());
						pointerY = y;
						pointerX = x;
					}
					maze[y][x] = '.';
				}
			}
		}
		mazeImg[pointerY][pointerX].setForeground(Color.BLUE);
		endX = pointerX;
		endY = pointerY;
	}

	public static void displayPath(Stack<int[]> path) {
		for (int[] a : path) {
			for (int i = 0; i < a.length; i++) {
				System.out.print(+a[i] + " ");
			}
			System.out.print(",");
		}
		System.out.println("\n end*********");
	}

	boolean hasNext(int x, int y) {
		if (y + 2 <= maze.length - 2) {
			if (maze[y + 2][x] != '.') {
				return true;
			}
		}
		if (y - 2 >= 1) {
			if (maze[y - 2][x] != '.') {
				return true;
			}
		}
		if (x + 2 <= maze[0].length - 2) {
			if (maze[y][x + 2] != '.') {
				return true;
			}
		}
		if (x - 2 >= 1) {
			if (maze[y][x - 2] != '.') {
				return true;
			}
		}

		return false;
	}

	public void printMaze() {
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				System.out.print(maze[y][x] + " ");
			}
			System.out.println();
		}
	}

	public static boolean isValid(int x, int y, int mode) {
		if (mode == 0) {
			if (x > maze[0].length - 1 || y > maze.length - 1 || x < 0 || y < 0) {
				return false;
			}
			if (maze[y][x] == 'X' || maze[y][x] == '#') {
				return false;
			}
			System.out.println("valid");
		} else {
			if (x > maze[0].length - 2 || y > maze.length - 2 || x < 1 || y < 1) {
				return false;
			}
			if (maze[y][x] == '.') {
				return false;
			}
			System.out.println("valid");
		}
		return true;

	}

	public static void main(String[] args) throws FileNotFoundException {
		Maze maze = new Maze(0, 2, 25, 25);
		maze.generateMaze();
	}
}
