import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

/**
 * @author Sagnik
 * This a snake game where the user controls a snake 
 * The snake can eat different types of food which will change it's properties 
 * for example, eating a donut will make is faster, eating a lollipop will make to grow
 * each food item is worth some points
 * There is also a multiplayer mode included where the user can play against a player or an AI
 * I have also included a leaderboard where the players can compare themselves to the other players that have played this game
 */
public class Game extends JFrame implements ActionListener, MouseListener {
	private JPanel headerPanel = new JPanel(); // this is the menu present at the top 
	private JPanel helpPanel = new JPanel(); // this the help menu
	private JLabel messageLabel = new JLabel(); // this is used for sending message to users while playing the game
	// these are the various labels that are added to the top menu
	private JLabel helpLabel = new JLabel("Help"); 
	private JLabel scoreLabel1 = new JLabel("Score: 0");
	private JLabel scoreLabel2 = new JLabel("Player2: 0");
	private JLabel multiModeLabel = new JLabel("Multiplayer Mode");
	private JLabel countDownLabel = new JLabel("Time Remaining: 90s");
	private JLabel board = new JLabel("Leaderboard");
	
	private Timer updateTimer = new Timer(500, this); // for updating the messageLabel and scores during the game
	private ImageIcon SOUP = new ImageIcon("images/Soup.png");
	private ImageIcon CAKE = new ImageIcon("images/Cake.png");
	private ImageIcon DONUT = new ImageIcon("images/Donut.png");
	private ImageIcon LOLLIPOP = new ImageIcon("images/Lollipop.PNG");
	private ImageIcon DYNAMITE = new ImageIcon("images/Dynamite.png");
	private boolean help;
	private boolean multiMode;
	private Screen screen = new Screen(); // this is the game itself
	private JPanel splashPanel = new JPanel(); // shows up when the user enters mutiplayer mode
	private JPanel leaderBoard = new JPanel();
	private String userName;
	private JScrollPane scroller = new JScrollPane(leaderBoard);

	public Game() {
		setTitle("Snake Game");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		populateHeaderPanel(); // adding elements to the top menu
		add(headerPanel, BorderLayout.NORTH);
		add(screen, BorderLayout.CENTER);
		addKeyListener(screen);
		setVisible(true);
		messageLabel.setText(screen.getMessage());
		updateTimer.start();
		populateHelpPanel(); // adding elements to the help menu
		JLabel next = new JLabel("Got it!");
		next.setFont(new Font(null, Font.BOLD, 16));
		next.setBackground(Color.BLACK);
		next.setForeground(Color.WHITE);
		add(helpPanel, BorderLayout.CENTER);
		JPanel panel = new JPanel(); // add a lablel at the bottom so the user can click past the help menu which shows up at the beginning 
		panel.setOpaque(true);
		panel.setBackground(Color.BLACK);
		panel.add(next);
		add(panel, BorderLayout.SOUTH);
		next.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				next.setForeground(Color.WHITE);

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				next.setForeground(Color.YELLOW);

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				remove(helpPanel);
				remove(panel);
				repaint();
			}
		});
		populateSplashPanel(); // decorate the splash panel
	}

	private void populateHeaderPanel() {
		headerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		headerPanel.setOpaque(true);
		headerPanel.setBackground(Color.BLACK);

		///// column 1 ////////
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 100);

		helpLabel.setFont(new Font(null, Font.BOLD, 16));
		helpLabel.setForeground(Color.WHITE);
		helpLabel.addMouseListener(this);
		headerPanel.add(helpLabel, gc);

		//////// column 1 ////////
		gc.gridx = 1;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 100);

		board.setFont(new Font(null, Font.BOLD, 16));
		board.setForeground(Color.WHITE);
		board.addMouseListener(this);
		headerPanel.add(board, gc);

		//////// column 2 ///////////
		gc.gridx = 2;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.BASELINE;
		messageLabel.setFont(new Font(null, Font.BOLD, 24));
		messageLabel.setForeground(Color.YELLOW);
		headerPanel.add(messageLabel, gc);
		////// row 1/////
		gc.gridy = 0;
		gc.gridx = 2;
		gc.anchor = GridBagConstraints.BASELINE;
		multiModeLabel.setFont(new Font(null, Font.BOLD, 16));
		multiModeLabel.setForeground(Color.WHITE);
		multiModeLabel.addMouseListener(this);
		headerPanel.add(multiModeLabel, gc);

		///////// column 3 //////////
		gc.gridx = 3;
		scoreLabel1.setFont(new Font(null, Font.BOLD, 16));
		scoreLabel1.setForeground(Color.WHITE);
		headerPanel.add(scoreLabel1, gc);

	}

	private void populateHelpPanel() {
		helpPanel.setOpaque(true);
		helpPanel.setBackground(Color.BLACK);
		helpPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		//// first row/////
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.BASELINE;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel label = new JLabel();
		label.setForeground(Color.WHITE);
		label.setFont(new Font(null, Font.BOLD, 16));
		label.setText("Rules");
		helpPanel.add(label, gc);

		///// 2nd row///////
		gc.gridy++;
		JLabel controls = new JLabel("Use arrow keys to change direction");
		gc.anchor = GridBagConstraints.LINE_START;
		controls.setForeground(Color.WHITE);
		helpPanel.add(controls, gc);
		////// 3rd row////////
		gc.gridy++;
		gc.gridx = 0;
		JLabel pauseLabel = new JLabel("Press 'P' to pause and 'R' to restart");
		pauseLabel.setForeground(Color.WHITE);
		helpPanel.add(pauseLabel, gc);
		//// 4th row/////
		gc.gridx = 0;
		gc.gridy++;
		JLabel rule1 = new JLabel("Eating a dynamite will half your length ");
		rule1.setForeground(Color.WHITE);
		helpPanel.add(rule1, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		JLabel dynamiteLabel = new JLabel();
		gc.anchor = GridBagConstraints.LINE_END;
		dynamiteLabel.setIcon(DYNAMITE);

		helpPanel.add(dynamiteLabel, gc);

		////// 5th row /////
		gc.gridx = 0;
		gc.gridy++;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel rule2 = new JLabel("Eating a donut will increase your speed");
		rule2.setForeground(Color.WHITE);
		gc.anchor = GridBagConstraints.LINE_START;
		helpPanel.add(rule2, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		JLabel donutLabel = new JLabel();
		gc.anchor = GridBagConstraints.LINE_END;
		donutLabel.setIcon(DONUT);

		helpPanel.add(donutLabel, gc);

		/////// 6th row//////
		gc.gridx = 0;
		gc.gridy++;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel rule3 = new JLabel("Drinking soup will decrese your speed");
		rule3.setForeground(Color.WHITE);
		gc.anchor = GridBagConstraints.LINE_START;
		helpPanel.add(rule3, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		JLabel soupLabel = new JLabel();
		gc.anchor = GridBagConstraints.LINE_END;
		soupLabel.setIcon(SOUP);

		helpPanel.add(soupLabel, gc);

		////////// 7th row////////////
		gc.gridx = 0;
		gc.gridy++;
		gc.insets = new Insets(5, 5, 5, 5);
		JLabel borderLabel = new JLabel("Press 1 to include/exclude borders");
		borderLabel.setForeground(Color.WHITE);
		gc.anchor = GridBagConstraints.LINE_START;
		helpPanel.add(borderLabel, gc);

		////////// 8th row ///////
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.BASELINE;
		JLabel scoreChart = new JLabel();
		scoreChart.setForeground(Color.WHITE);
		scoreChart.setFont(new Font(null, Font.BOLD, 16));
		scoreChart.setText("Score Chart");
		helpPanel.add(scoreChart, gc);

		//////////// 9th row ////////

		gc.gridy++;
		JLabel food = new JLabel();
		food.setIcon(LOLLIPOP);
		helpPanel.add(food, gc);

		food.setText("= 20 points");
		food.setForeground(Color.WHITE);
		//////////// 10 row ///////

		gc.gridy++;
		JLabel powerPoints = new JLabel();
		powerPoints.setIcon(DONUT);
		helpPanel.add(powerPoints, gc);

		powerPoints.setText("= 50 points");
		powerPoints.setForeground(Color.WHITE);
		//////// 11th row ///////
		gc.gridy++;
		JLabel cakePoints = new JLabel();
		cakePoints.setIcon(CAKE);
		helpPanel.add(cakePoints, gc);

		cakePoints.setText("= 100 points");
		cakePoints.setForeground(Color.WHITE);
		//////// 12th row ////////
		gc.gridy++;
		JLabel SoupPoints = new JLabel();
		SoupPoints.setIcon(SOUP);
		helpPanel.add(SoupPoints, gc);

		SoupPoints.setText("= -10 points");
		SoupPoints.setForeground(Color.WHITE);

	}

	public void populateSplashPanel() { // adding elements for the splash panel
		splashPanel.setLayout(new GridBagLayout());
		splashPanel.setOpaque(true);
		splashPanel.setBackground(Color.BLACK);

		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridy = 0;

		gc2.gridy++;
		JLabel AILabel = new JLabel("Player vs Player");
		AILabel.setFont(new Font(null, Font.BOLD, 16));
		AILabel.setForeground(Color.WHITE);
		gc2.insets = new Insets(0, 0, 20, 0);
		splashPanel.add(AILabel, gc2);
		AILabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				AILabel.setForeground(Color.WHITE);

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				AILabel.setForeground(Color.YELLOW);

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!screen.AI) {
					AILabel.setText("Player vs AI");
					screen.AI = true;
					screen.restart();
				} else {
					AILabel.setText("Player vs Player");
					screen.AI = false;
					screen.restart();
				}

			}
		});

		gc2.gridy++;
		JLabel snake2Label = new JLabel("Use WASD keys to control the red snake (Only for Player vs Player)");
		snake2Label.setFont(new Font(null, Font.BOLD, 16));
		snake2Label.setForeground(Color.WHITE);
		splashPanel.add(snake2Label, gc2);

		gc2.gridy++;
		gc2.insets = new Insets(20, 0, 0, 0);
		JLabel next = new JLabel("Continue");
		next.setFont(new Font(null, Font.BOLD, 16));
		next.setForeground(Color.WHITE);
		splashPanel.add(next, gc2);
		next.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				next.setForeground(Color.WHITE);

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				next.setForeground(Color.YELLOW);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				remove(splashPanel);
				add(screen, BorderLayout.CENTER);
				repaint();
			}
		});
	}

	public void populateLeaderBoard() {
		leaderBoard.setLayout(new BorderLayout());
		leaderBoard.setOpaque(true);
		leaderBoard.setBackground(Color.BLACK);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		panel.setOpaque(true);
		panel.setBackground(Color.BLACK);
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 50, 20, 0);
		JLabel titleLabel = new JLabel("Leader Board"); // adding title 
		titleLabel.setFont(new Font(null, Font.BOLD, 20));
		titleLabel.setForeground(Color.WHITE);
		panel.add(titleLabel, gc);

		try {
			List<String> players = Files.readAllLines(Paths.get("Names")); // reading data from the files
			List<String> scores = Files.readAllLines(Paths.get("Scores"));

			for (int i = 0; i < players.size(); i++) {
				// displaying the data on the leaderboard
				gc.insets = new Insets(0, 0, 0, 0);
				gc.gridx = 0;
				gc.gridy = i + 1;
				gc.anchor = GridBagConstraints.LINE_START;
				JLabel player = new JLabel((i + 1) + ". " + players.get(i));
				player.setFont(new Font(null, Font.BOLD, 18));
				player.setForeground(Color.WHITE);
				panel.add(player, gc);

				gc.gridx = 1;
				gc.anchor = GridBagConstraints.LINE_END;
				JLabel score = new JLabel(scores.get(i));
				score.setFont(new Font(null, Font.BOLD, 18));
				score.setForeground(Color.WHITE);
				panel.add(score, gc);
			}
			leaderBoard.add(panel, BorderLayout.NORTH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(updateTimer)) {
			messageLabel.setText(screen.getMessage()); // updating the message
			if (!multiMode) { // updating the scores
				scoreLabel1.setText("Score: " + screen.getScore1());
			} else {
				scoreLabel1.setText("Player1: " + screen.getScore1());
				scoreLabel2.setText("Player2: " + screen.getScore2());
				countDownLabel.setText("Time remaining: " + screen.getCountDownTime() + "s");
			}
			if (screen.snake1.isDead() && !screen.multiMode) {// if the snake dies, prompt the user the enter name and add it to the leaderboard
				updateTimer.stop();
				try {
					// ask user for input
					userName = JOptionPane.showInputDialog(null, "Enter your name to add it to the leaderboard",
							"Add you score", JOptionPane.PLAIN_MESSAGE);
					while (userName.equals("")) {
						// if the user enters blank then ask again
						userName = JOptionPane.showInputDialog(null, "You must enter a name to sumbit your score",
								"Add you score", JOptionPane.PLAIN_MESSAGE);
					}
				} catch (NullPointerException ex) {
					// if the user presses cancel , then catch exception
					System.out.println("User did not enter name");
					updateTimer.start();
				}
				if (userName != null) { // adding the entered name to the files
					try {
						List<String> players = Files.readAllLines(Paths.get("Names")); // reading all the names from the files
						List<String> scores = Files.readAllLines(Paths.get("Scores")); // reading all the scores from the files
						players.add(userName); // adding entered name to the list of name
						scores.add(String.valueOf(screen.getScore1()));	// adding score to the list of scores		
						sortScores(scores, players); // sort the scores
						Files.write(Paths.get("Names"), players); // adding sorted lists the files
						Files.write(Paths.get("Scores"), scores);
						userName = null;
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Your score could not be added!");
						e1.printStackTrace();
					}
				}
				screen.snake1.setDead(false); // revive the snake, so the conditions is not entered again
			}
		}

	}

	public static void main(String[] args) {
		new Game(); 
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(helpLabel)) {
			if (!help) {
				remove(screen);
				helpLabel.setText("Back");
				repaint();
				add(helpPanel, BorderLayout.CENTER);
				revalidate();
				help = true;
				screen.stopAllTimers();
			} else {
				remove(helpPanel);
				helpLabel.setText("Help");
				repaint();
				add(screen, BorderLayout.CENTER);
				revalidate();
				help = false;
			}
		} else if (e.getSource().equals(multiModeLabel)) {
			if (!multiMode) {
				screen.multiMode = true;
				screen.AI = false;
				screen.restart();
				multiModeLabel.setText("Single player Mode");
				multiMode = true;
				GridBagConstraints gc = new GridBagConstraints();
				scoreLabel1.setText("Player 1: 0");
				scoreLabel1.setForeground(Color.green);

				gc.gridx = 4;
				gc.gridy = 0;
				gc.insets = new Insets(0, 0, 0, 100);
				scoreLabel2.setForeground(Color.red);
				scoreLabel2.setFont(new Font(null, Font.BOLD, 16));
				headerPanel.add(scoreLabel2, gc);

				gc.gridx++;
				countDownLabel.setForeground(Color.WHITE);
				countDownLabel.setFont(new Font(null, Font.BOLD, 16));
				headerPanel.add(countDownLabel, gc);

				gc.gridx = 3;
				gc.gridy = 1;
				gc.anchor = GridBagConstraints.BASELINE;
				headerPanel.add(messageLabel, gc);

				remove(screen);
				add(splashPanel, BorderLayout.CENTER);
				repaint();
			} else {
				headerPanel.remove(scoreLabel2);
				headerPanel.remove(countDownLabel);
				headerPanel.repaint();
				scoreLabel1.setForeground(Color.white);
				screen.multiMode = false;
				screen.AI = false;
				multiMode = false;
				screen.restart();
				multiModeLabel.setText("Multiplayer Mode");
				GridBagConstraints gc = new GridBagConstraints();
				gc.gridx = 1;
				gc.gridy = 1;
				gc.anchor = GridBagConstraints.BASELINE;
				headerPanel.add(messageLabel, gc);
				remove(splashPanel);
				add(screen, BorderLayout.CENTER);
				screen.restart();
				repaint();
			}
		} else if (e.getSource().equals(board)) {
			if (board.getText().equals("Leaderboard")) {
				remove(screen);
				remove(scroller);
				board.setText("Back");
				messageLabel.setText(" ");
				leaderBoard.removeAll();
				populateLeaderBoard();
				leaderBoard.repaint();
				leaderBoard.revalidate();
				add(scroller, BorderLayout.CENTER);
				repaint();
				revalidate();
			} else {
				board.setText("Leaderboard");
				updateTimer.start();
				remove(scroller);
				repaint();
				add(screen, BorderLayout.CENTER);
				revalidate();
			}

		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource().equals(helpLabel)) {
			helpLabel.setForeground(Color.YELLOW);
		} else if (e.getSource().equals(multiModeLabel)) {
			multiModeLabel.setForeground(Color.YELLOW);
		} else {
			board.setForeground(Color.YELLOW);
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource().equals(helpLabel)) {
			helpLabel.setForeground(Color.white);
		} else if (e.getSource().equals(multiModeLabel)) {
			multiModeLabel.setForeground(Color.white);
		} else {
			board.setForeground(Color.white);
		}

	}

	public static void sortScores(List<String> scores, List<String> players) {
		for (int i = 0; i < scores.size(); i++) {
			// the default max would is the current position
			int maxNum = Integer.parseInt(scores.get(i));
			for (int j = i + 1; j < scores.size(); j++) {
				int num = Integer.parseInt(scores.get(j));
				if (num > maxNum) {
					// if a new max is found, then switch position with the new max
					int scorePointer = Integer.parseInt(scores.get(i)); // store the current maxScore
					String playerPointer = players.get(i); // store the current name

					scores.set(i, scores.get(j)); // set current max score to new maxScore
					scores.set(j, String.valueOf(scorePointer)); // set the current name to new name

					players.set(i, players.get(j)); // set the new maxScore to the stored score
					players.set(j, playerPointer); // set the new name to the stored name
				}
			}
		}
	}
}
