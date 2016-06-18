package com.game.packman;

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
import java.awt.peer.MenuPeer;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

public class PacManGUI extends JFrame implements ActionListener {
	JPanel menuPanel = new JPanel();
	JLabel scoreLabel = new JLabel("Score: ");
	JLabel helpLabel = new JLabel("Help");
	JPanel helpPanel = new JPanel();
	Board board = new Board();
	Timer scoreTimer = new Timer(5, this);
    JLabel hardMode = new JLabel("Insane Mode");
	// creates a new board all uses keyListeners from Board
	public PacManGUI() throws IOException {
		setSize(600, 600);
		setTitle("PacMan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		populateMenuPanel();
		add(menuPanel, BorderLayout.SOUTH);
		addKeyListener(board);
		add(board, BorderLayout.CENTER);
		setVisible(true);
		populateHelpPanel();
		
		scoreTimer.start();

	}

	public void populateMenuPanel() {
		menuPanel.setOpaque(true);
		menuPanel.setBackground(Color.BLACK);
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		scoreLabel.setForeground(Color.white);
		menuPanel.add(helpLabel, gc);
		helpLabel.setForeground(Color.white);
		helpLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (helpLabel.getText().equals("Help")) {
					remove(board);
					board.stopAllTimers();
					add(helpPanel, BorderLayout.CENTER);
					repaint();
					helpLabel.setText("Back");
				} else {
					remove(helpPanel);
					helpLabel.setText("Help");
					add(board, BorderLayout.CENTER);
					repaint();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		gc.insets = new Insets(0, 100, 0, 0);
		menuPanel.add(scoreLabel, gc);
		hardMode.setForeground(Color.WHITE);
		hardMode.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(!board.isHardMode()){
					board.setHardMode(true);
				}else{
					board.setHardMode(false);
				}
					board.restart();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuPanel.add(hardMode, gc);
	}

	public void populateHelpPanel() {
		helpPanel.setBackground(Color.BLACK);
		helpPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		//// first row/////
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.BASELINE;
		gc.insets = new Insets(3, 3, 3, 3);
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
		helpPanel.add(pauseLabel,gc);
		//// 4th row/////
		gc.gridx = 0;
		gc.gridy++;
		JLabel rule1 = new JLabel("Try to avoid these ghosts: ");
		rule1.setForeground(Color.WHITE);
		helpPanel.add(rule1, gc);

		gc.gridx++;
		JLabel ghost1Label = new JLabel();
		gc.anchor = GridBagConstraints.LINE_END;
		ghost1Label.setIcon(new ImageIcon("images/Ghost1.bmp"));

		helpPanel.add(ghost1Label, gc);

		gc.gridx++;

		JLabel ghostLabel1 = new JLabel();
		gc.anchor = GridBagConstraints.LINE_START;
		ghostLabel1.setIcon(new ImageIcon("images/Ghost2.bmp"));

		helpPanel.add(ghostLabel1, gc);

		gc.gridx++;

		JLabel ghostLabel2 = new JLabel();
		ghostLabel2.setIcon(new ImageIcon("images/Ghost3.bmp"));

		helpPanel.add(ghostLabel2, gc);

		//////// 5th row ////////
		gc.gridy++;
		gc.gridx = 0;
		JLabel rule2 = new JLabel("You can eat ghosts when they are blue");
		rule2.setForeground(Color.WHITE);

		helpPanel.add(rule2, gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_END;
		JLabel blueGhost = new JLabel();
		blueGhost.setIcon(new ImageIcon("images/BlueGhost.bmp"));

		helpPanel.add(blueGhost, gc);

		///////// 6th row ////////
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 0;
		gc.gridy++;
		JLabel rule3 = new JLabel("Eat a power pellet to turn the ghosts blue:");
		rule3.setForeground(Color.WHITE);
		helpPanel.add(rule3, gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_END;
		JLabel powerLabel = new JLabel();
		powerLabel.setIcon(new ImageIcon("images/PowerUp.png"));

		helpPanel.add(powerLabel, gc);

		////////// 7th row ///////
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.BASELINE;
		JLabel scoreChart = new JLabel();
		scoreChart.setForeground(Color.WHITE);
		scoreChart.setFont(new Font(null, Font.BOLD, 16));
		scoreChart.setText("Score Chart");
		helpPanel.add(scoreChart, gc);

		//////////// 8th row ////////

		gc.gridy++;
		JLabel food = new JLabel();
		food.setIcon(new ImageIcon("images/StdFood.bmp"));
//		 gc.insets = new Insets(0, 0, 5, 0);
		helpPanel.add(food, gc);

		food.setText("= 10 points");
		food.setForeground(Color.WHITE);
		//////////// 9th row ///////

		gc.gridy++;
		JLabel powerPoints = new JLabel();
		powerPoints.setIcon(new ImageIcon("images/PowerUp.png"));
		helpPanel.add(powerPoints, gc);

		powerPoints.setText("= 50 points");
		powerPoints.setForeground(Color.WHITE);

		//////////// 10th row ////////
		gc.gridy++;
		JLabel ghostPoints = new JLabel();
		ghostPoints.setIcon(new ImageIcon("images/BlueGhost.bmp"));
		helpPanel.add(ghostPoints, gc);

		ghostPoints.setText("= 200 points");
		ghostPoints.setForeground(Color.WHITE);

		/////////// 11th row //////////
		gc.gridy++;
		
		JLabel cherryLabel = new JLabel();
		cherryLabel.setIcon(new ImageIcon("images/Cherry.bmp"));
		helpPanel.add(cherryLabel, gc);
		
		cherryLabel.setText("= 100 points");
		cherryLabel.setForeground(Color.WHITE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		scoreLabel.setText("Score: "+board.getScore()); 
		
	}

}