package com.game.packman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener, ActionListener {

	private Timer gameTimer = new Timer(225, this);
	private Timer animateTimer = new Timer(50, this);
	private Timer powerUpTimer = new Timer(1000, this); // ghosts turn blue for 10 seconds once pacman has eaten a powerup
	private Timer cherryTimer = new Timer(5000, this);
	private Timer ghost1Timer = new Timer(225, this); // individual ghost timers were created to control individual speeds
	private Timer ghost2Timer = new Timer(225, this);
	private Timer ghost3Timer = new Timer(225, this);

	private final ImageIcon WALL = new ImageIcon("images/StdWall.bmp");
	private final ImageIcon FOOD = new ImageIcon("images/StdFood.bmp");
	private final ImageIcon BLANK = new ImageIcon("images/BLACK.bmp");
	private final ImageIcon DOOR = new ImageIcon("images/BLACK.bmp");
	private final ImageIcon SKULL = new ImageIcon("images/SKULL.bmp");
	private final ImageIcon POWERUP = new ImageIcon("images/PowerUp.png");
	private final ImageIcon BLUEGHOST = new ImageIcon("images/BlueGhost.bmp");
	private final ImageIcon CHERRY = new ImageIcon("images/Cherry.bmp");
	private final ImageIcon GHOST1 = new ImageIcon("images/Ghost1.bmp");
	private final ImageIcon GHOST2 = new ImageIcon("images/Ghost2.bmp");
	private final ImageIcon GHOST3 = new ImageIcon("images/Ghost3.bmp");
	private final ImageIcon WHITEGHOST = new ImageIcon("images/whiteGhost.png");
	private JLabel[][] cell = new JLabel[25][27];
	private char[][] maze = new char[25][27]; // char's from text file

	private PacMan pacMan;
	private Ghost[] ghost = new Ghost[3];
	private int collidedNum;
	private int pellets = 0;
	private int score = 0;
	private Clip chompClip;
	private Clip backgroundClip;
	private int bufferDirection; // this is created to remember the direction which the user wants to go to
	public int getScore() {
		return score;
	}

	private boolean hardMode = false;

	public boolean isHardMode() {
		return hardMode;
	}

	public void setHardMode(boolean hardMode) {
		this.hardMode = hardMode;
	}

	private int moveCounter = 0;
	private int pStep;
	private int powerTime;
	JLabel scoreLabel = new JLabel("Score: " + score);
	int pauseCounter = 1;

	public Board() throws IOException {

		// create the pacMan and the ghosts

		
		loadBoard();
	}

	public void loadBoard() throws IOException {
		score = 0;
		removeAll();
		repaint();
		setLayout(new GridLayout(25, 27));
		setBackground(Color.BLACK);
		pacMan = new PacMan(0, 0, 0, 0, false, new ImageIcon("images/PacLeftClosed.bmp"));
		pacMan.setDirection(1);

		ghost[0] = new Ghost(0, 0, 0, 0, true, GHOST1);
		ghost[1] = new Ghost(0, 0, 0, 0, true, GHOST2);
		ghost[2] = new Ghost(0, 0, 0, 0, true, GHOST3);

		int r = 0;
		
		Scanner	input = new Scanner(new File("maze.txt"));
		while ( input.hasNext()) {
			maze[r] = input.nextLine().toCharArray();
			for (int c = 0; c < maze[r].length; c++) {
				
				cell[r][c] = new JLabel(); // creates a new pictures for each
				// cell
				if (maze[r][c] == 'W') {
					if (isHardMode()) {
						cell[r][c].setIcon(FOOD);
					} else {
						cell[r][c].setIcon(WALL);
					}
				} else if (maze[r][c] == 'F') {
					cell[r][c].setIcon(FOOD);
					pellets++;
				} else if (maze[r][c] == 'S') {
					cell[r][c].setIcon(POWERUP);
				} else if (maze[r][c] == 'P') {
					cell[r][c].setIcon(pacMan.getImage());
					pacMan.setRow(r);
					pacMan.setColumn(c);
				} else if (maze[r][c] == '0' || maze[r][c] == '1' || maze[r][c] == '2') {
					int gNum = Character.getNumericValue(maze[r][c]);
					cell[r][c].setIcon(ghost[gNum].getImage());
					ghost[gNum].setRow(r);
					ghost[gNum].setColumn(c);
				}
				
				else if (maze[r][c] == 'D') {
					cell[r][c].setIcon(DOOR);
				}
				
				add(cell[r][c]);
				revalidate();
			}
			r++;
		}
		input.close();
		try {
			backgroundClip = AudioSystem.getClip();
			backgroundClip.open(AudioSystem.getAudioInputStream(new File("sounds/b.wav")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 80) {
			if (pauseCounter % 2 == 0&&!pacMan.isDead()) {
				stopAllTimers();
			} else if(!pacMan.isDead()) {
				startAllTimers();
			}
			pauseCounter++;
		} else if (e.getKeyCode() == 82) {
			restart();
		}else if(e.getKeyCode()== KeyEvent.VK_1){
			if(ghost1Timer.isRunning()&&ghost2Timer.isRunning()&&ghost3Timer.isRunning()){
				
				ghost1Timer.stop();
				ghost2Timer.stop();
				ghost3Timer.stop();
			}else{
				ghost1Timer.start();
				ghost2Timer.start();
				ghost3Timer.start();
			}
			
		}else {
			if (!gameTimer.isRunning() & !pacMan.isDead()) {
				gameTimer.start();
				cherryTimer.start();
				ghost1Timer.start();
				ghost2Timer.start();
				ghost3Timer.start();
			}

			if (!pacMan.isDead()) {
				int direction = e.getKeyCode() - 37;
				bufferDirection = direction; // sets the buffer direction
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == gameTimer) {
			// sets pacman's direction to bufferdirection 
			if (pacMan.getDirection()!=bufferDirection){
				if (bufferDirection == 0 &&  maze[pacMan.getRow()][pacMan.getColumn() - 1] != 'W' && !pacMan.isDead()) {
					pacMan.setDirection(0); // moving left
					pacMan.setImage(0,1);
					cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getImage());
				} else if (bufferDirection == 1 && maze[pacMan.getRow() - 1][pacMan.getColumn()] != 'W' && !pacMan.isDead()) {
					pacMan.setDirection(1); // moving up
					pacMan.setImage(1,1);
					cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getImage());
				} else if (bufferDirection == 2 && maze[pacMan.getRow()][pacMan.getColumn() + 1] != 'W' && !pacMan.isDead()) {
					pacMan.setDirection(2);// moving right
					pacMan.setImage(2,1);
					cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getImage());
				} else if (bufferDirection == 3 && maze[pacMan.getRow() + 1][pacMan.getColumn()] != 'W' && !pacMan.isDead()) {
					pacMan.setDirection(3); // moving down
					pacMan.setImage(3,1);
					cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getImage());
				}
			}
			performMove(pacMan);
			// if the game is finished display the message "You Won!" and plays the victory sound
			if (checkFinished()) {
				// checkFinishied checks whether there is any food left on the board
				System.out.println("hello");
				removeAll();
				repaint();
				JLabel label = new JLabel();
				label.setFont(new Font(null, Font.BOLD, 32));
				label.setText("You Won!");
				label.setForeground(Color.WHITE);
				setLayout(new GridBagLayout());
				GridBagConstraints gc = new GridBagConstraints();
				gc.anchor = GridBagConstraints.BASELINE;
				gc.gridy = 0;
				add(label, gc);
				revalidate();
				stopAllTimers();
				backgroundClip.stop();
				play("sounds/interm.wav"); // the play(fileName) method plays an audio clip
			}

		} else if (e.getSource() == animateTimer) {
			animatePacMan(pStep);

			pStep++;

			if (pStep == 2) {
				pStep = 0;
			}
		} else if (e.getSource() == powerUpTimer) {
			powerTime++;
			if (powerTime >= 6) {
				for (int i = 0; i < 3; i++) {
					switch (i) {
					case 0:
						if (ghost[i].getImage().equals(BLUEGHOST)) {
							ghost[i].setImage(WHITEGHOST);
						} else if (ghost[i].getImage().equals(WHITEGHOST)) {
							ghost[i].setImage(BLUEGHOST);
						}
						cell[ghost[i].getRow()][ghost[i].getColumn()].setIcon(ghost[i].getImage());
						break;
					case 1:
						if (ghost[i].getImage().equals(BLUEGHOST)) {
							ghost[i].setImage(WHITEGHOST);
						} else if (ghost[i].getImage().equals(WHITEGHOST)) {
							ghost[i].setImage(BLUEGHOST);
						}
						cell[ghost[i].getRow()][ghost[i].getColumn()].setIcon(ghost[i].getImage());
						break;
					case 2:
						if (ghost[i].getImage().equals(BLUEGHOST)) {
							ghost[i].setImage(WHITEGHOST);
						} else if (ghost[i].getImage().equals(WHITEGHOST)) {
							ghost[i].setImage(BLUEGHOST);
						}
						cell[ghost[i].getRow()][ghost[i].getColumn()].setIcon(ghost[i].getImage());
						break;
					}
				}
			}
			if (powerTime == 10) {
				convertGhosts(3);
				powerUpTimer.stop();
			}
		} else if (e.getSource() == cherryTimer) {
			// cherry timer
			int randomRow;
			int randomColumn;
			do {
				// spawns a cheery on the section of board where there is no food
				randomRow = new Random().nextInt(23) + 1;
				randomColumn = new Random().nextInt(25) + 1;
			} while (maze[randomRow][randomColumn] != 'E');
            
			cell[randomRow][randomColumn].setIcon(CHERRY);
			maze[randomRow][randomColumn] = 'C';
			cherryTimer.stop();
		}
		//A ghost timer slows down a ghost turns blue
		else if (e.getSource() == ghost1Timer) {
			if (ghost[0].getImage().equals(BLUEGHOST) || ghost[0].getImage().equals(WHITEGHOST)) {
				ghost1Timer.setDelay(290);
			} else {
				ghost1Timer.setDelay(225);
			}
			moveGhost(ghost[0]);
		} else if (e.getSource() == ghost2Timer) {
			if (ghost[1].getImage().equals(BLUEGHOST) || ghost[1].getImage().equals(WHITEGHOST)) {
				ghost2Timer.setDelay(290);
			} else {
				ghost2Timer.setDelay(225);
			}
			moveGhost(ghost[1]);
		} else if (e.getSource() == ghost3Timer) {
			if (ghost[2].getImage().equals(BLUEGHOST) || ghost[2].getImage().equals(WHITEGHOST)) {
				ghost3Timer.setDelay(290);
			} else {
				ghost3Timer.setDelay(225);
			}
			moveGhost(ghost[2]);

		}
	}

	private void animatePacMan(int step) {
		// changes made: only made the animate timer do the animation, and not
		// draw blanks
		if (step == 0) {

			pacMan.setImage(pacMan.getDirection(), 1);
			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getImage());
			animateTimer.setDelay(100);
		} else if (step == 1) {
			// draw mouth closed
			pacMan.setImage(pacMan.getDirection(), 0);
			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getImage());
		}

	}

	private void performMove(Mover mover) {

		if (mover.getColumn() == 0) {
			mover.setColumn(25);
			cell[12][0].setIcon(BLANK);
		} else if (mover.getColumn() == 26) {
			mover.setColumn(1);
			cell[12][26].setIcon(BLANK);
		}

		if (maze[mover.getNextRow()][mover.getNextColumn()] != 'W') { // controls
			// pacman's
			// movements
			if (mover.equals(pacMan)) {
				// System.out.println(pacMan.getRow()+" "+pacMan.getColumn());
				animateTimer.start();
				if (maze[mover.getRow()][mover.getColumn()] == 'S') {
					score += 50;
					if (powerUpTimer.isRunning()) {
						powerUpTimer.restart();
						powerTime = 0;
					} else {
						powerUpTimer.start();
						powerTime = 0;
					}
					for (Ghost g : ghost) {
						g.setImage(BLUEGHOST);
						cell[g.getRow()][g.getColumn()].setIcon(g.getImage());
					}
				} else if (maze[mover.getRow()][mover.getColumn()] == 'C') {
					play("sounds/fruiteat.wav");
					score += 100;
					cherryTimer.start();
				} else if (maze[mover.getRow()][mover.getColumn()] == 'F') {
					score += 10;
				}
				maze[mover.getRow()][mover.getColumn()] = 'E';
				cell[mover.getRow()][mover.getColumn()].setIcon(BLANK);

			} else {
				if (maze[mover.getRow()][mover.getColumn()] == 'F') {
					cell[mover.getRow()][mover.getColumn()].setIcon(FOOD);

				} else if (maze[mover.getRow()][mover.getColumn()] == 'S') {
					cell[mover.getRow()][mover.getColumn()].setIcon(POWERUP);
				} else if (maze[mover.getRow()][mover.getColumn()] == 'C') {
					cell[mover.getRow()][mover.getColumn()].setIcon(CHERRY);
				} else { // controls the ghosts movements
					cell[mover.getRow()][mover.getColumn()].setIcon(BLANK);
				}

			}
			// after making the current tile black you can move the piece
			mover.move();
			cell[mover.getRow()][mover.getColumn()].setIcon(mover.getImage());

			if (collided()) {
				if (!ghost[collidedNum].getImage().equals(BLUEGHOST)
						&& !ghost[collidedNum].getImage().equals(WHITEGHOST)&& maze[ghost[collidedNum].getRow()][ghost[collidedNum].getColumn()] !='S') {
					death();
				} else {
					score += 200;
					play("sounds/GHOSTEATEN.wav");
					ghost[collidedNum].setDead(true);
					cell[ghost[collidedNum].getRow()][ghost[collidedNum].getColumn()].setIcon(BLANK);
					ghost[collidedNum].setColumn(13);
					ghost[collidedNum].setRow(13);

					convertGhosts(collidedNum);
				}
			}

		}

	}

	public boolean collided() {
		for (int i = 0; i < ghost.length; i++) {
			if (ghost[i].getColumn() == pacMan.getColumn() && ghost[i].getRow() == pacMan.getRow()) {
				collidedNum = i;
				return true;
			}
		}
		return false;
	}

	public void death() {
		backgroundClip.stop();
		play("sounds/killed.wav");
		pacMan.setDead(true);
		stopGame();
	}

	private void stopGame() {
		if (pacMan.isDead()) { 
			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);

			stopAllTimers();
		}
	}

	private void convertGhosts(int ghostNum) {
	// when a ghosts is eaten, the ghost is converted back to its original color or when the time runs our all the ghosts turn back to their original colors
		switch (ghostNum) {
		case 0:
			ghost[ghostNum].setImage(GHOST1);
			cell[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn()].setIcon(ghost[ghostNum].getImage());
			break;
		case 1:
			ghost[ghostNum].setImage(GHOST2);
			cell[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn()].setIcon(ghost[ghostNum].getImage());
			break;
		case 2:
			ghost[ghostNum].setImage(GHOST3);
			cell[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn()].setIcon(ghost[ghostNum].getImage());
			break;
		case 3:
			ghost[0].setImage(GHOST1);
			ghost[1].setImage(GHOST2);
			ghost[2].setImage(GHOST3);
			for (Ghost g : ghost) {
				cell[g.getRow()][g.getColumn()].setIcon(g.getImage());
			}
			break;
		}
	}

	private void moveGhost(Ghost g) {
		moveCounter++;
		int dir = 0;
		if (g.getImage().equals(GHOST1)) {
			// green sets the the initial direction for the green ghost
			if (moveCounter == 1) {
				do {
					dir = (int) (Math.random() * 4);
				} while (Math.abs(g.getDirection() - dir) == 2);
				g.setDirection(dir);
			}
		} else if (g.getImage().equals(GHOST2)) {
			// yellow tries to locate pacMan
			if (g.getNextColumn() < 27 && g.getNextRow() < 27 && g.getNextColumn() >= 0 && g.getNextRow() >= 0) {
				if (g.getColumn() > pacMan.getColumn() && g.getDirection() == 2) {
					if (pacMan.getRow() > g.getRow()) {
						g.setDirection(3);
					} else {
						g.setDirection(1);
					}
				} else if (g.getColumn() < pacMan.getColumn() && g.getDirection() == 0) {
					if (pacMan.getRow() > g.getRow()) {
						g.setDirection(3);
					} else {
						g.setDirection(1);
					}
				}

				if (g.getRow() > pacMan.getColumn() && g.getDirection() == 2) {
					if (pacMan.getRow() > g.getRow()) {
						g.setDirection(3);
					} else {
						g.setDirection(1);
					}
				} else if (g.getColumn() < pacMan.getColumn() && g.getDirection() == 0) {
					if (pacMan.getRow() > g.getRow()) {
						g.setDirection(3);
					} else {
						g.setDirection(1);
					}
				}
			}
		}
		// }
		else {
			// red mimics the direction of pac man
			g.setDirection(pacMan.getDirection());
		}

		if (g.isDead())

		{
			if (g.getImage().equals(GHOST1)) {
				// the red ghost will more right
				g.setDirection(2);
			} else if (g.getImage().equals(GHOST2)) {
				// the yellow ghost will move up
				g.setDirection(1);
			} else if (g.getImage().equals(GHOST3)) {
				// the green ghost will move left
				g.setDirection(0);
			}
		}

		if (g.getColumn() == 13) { 
			// go up if you reach column 13
			g.setDirection(1);
		} else if (g.getColumn() == pacMan.getColumn())

		{
			if (!g.getImage().equals(BLUEGHOST) && !g.getImage().equals(WHITEGHOST)) {
				if (g.getRow() < pacMan.getRow()) {
					g.setDirection(3);
				} else {
					g.setDirection(1);
				}
			} else {
				if (g.getRow() < pacMan.getRow()) {
					g.setDirection(1);
				} else {
					g.setDirection(3);
				}
			}
		} else if (g.getRow() == pacMan.getRow()) {
			if (!g.getImage().equals(BLUEGHOST) && !g.getImage().equals(WHITEGHOST)) {
				if (g.getColumn() < pacMan.getColumn()) {
					g.setDirection(2);
				} else {
					g.setDirection(0);
				}
			} else {
				if (g.getColumn() < pacMan.getColumn()) {
					g.setDirection(0);
				} else {
					g.setDirection(2);
				}
			}
		}
		if (g.getNextColumn() < 27 && g.getRow() < 27 && g.getNextColumn() >= 0 && g.getNextRow() >= 0) {

			if (maze[g.getNextRow()][g.getNextColumn()] == 'W') {
				do {
					dir = (int) (Math.random() * 4);
				} while (Math.abs(g.getDirection() - dir) == 2);
				g.setDirection(dir);

			}
		}
		if (g.getRow() == 9 && g.getColumn() == 13) {
			g.setDead(false);
			if (g.getDirection() == 3) {
				do {
					dir = (int) (Math.random() * 4);
				} while (Math.abs(g.getDirection() - dir) == 2);
				g.setDirection(dir);
			}
		}

		performMove(g);
	}

	// public void createRandomDirection() {
	// for (Ghost g : ghost) {
	// int dir = 0;
	// do {
	// dir = (int) (Math.random() * 4);
	// } while (Math.abs(g.getDirection() - dir) == 2);
	// g.setDirection(dir);
	// }
	// }

	public void stopAllTimers() {
		gameTimer.stop();
		animateTimer.stop();
		cherryTimer.stop();
		powerUpTimer.stop();
		ghost1Timer.stop();
		ghost2Timer.stop();
		ghost3Timer.stop();
	}

	public void startAllTimers() {
		gameTimer.start();
		animateTimer.start();
		cherryTimer.start();
		powerUpTimer.start();
		ghost1Timer.start();
		ghost2Timer.start();
		ghost3Timer.start();
	}

	public boolean checkFinished() {
		// Returns false if it finds food else return true
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 27; j++) {
				if (maze[i][j] == 'F') {
					return false;
				}
			}
		}
		return true;
	}

	public static void play(String filename) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
			clip.flush();
			clip.start();
		} catch (Exception exc) {
			System.out.println("error");
		}
	}
	public void restart(){
		backgroundClip.stop();
		score = 0;
		removeAll();
		stopAllTimers();
		repaint();
		try {
			loadBoard();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		revalidate();
	}

}
