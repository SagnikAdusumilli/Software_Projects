import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Screen extends JPanel implements KeyListener, ActionListener {
	Timer gameTimer1 = new Timer(100, this); // controls the speed of snake1
	Timer gameTimer2 = new Timer(100, this); // controls the speed of snake2
	Timer foodTimer = new Timer(1000, this); // controls the generation of food
	Timer soupTimer1 = new Timer(5000, this); // controls the soup's effect on
												// snake1
	Timer soupTimer2 = new Timer(5000, this); // controls the soup's effect on
												// snake2
	Timer donutTimer1 = new Timer(5000, this); // controls the donut's effect on
												// snake1
	Timer donutTimer2 = new Timer(5000, this); // controls the donu's effect on
												// snake1
	Timer dynamiteTimer1 = new Timer(250, this); // controls the dynamite's
													// effect on snake1
	Timer dynamiteTimer2 = new Timer(250, this); // controls the dynamite's
													// effect on snake1
	Timer eraseTimer = new Timer(1000, this); // controls the dynamite's effect
												// on snake1
	Timer countDownTimer = new Timer(1000, this); // count down timer for
													// multiplayer mode
	Snake snake1 = new Snake(0, 651, 350); // snake that user controls
	Snake snake2 = new Snake(1, 651, 390); // snake2 is controled by the AI or
											// another user
	int score1 = 0; // score for snake1
	int score2 = 0; // score for snake2
	int countDownTime = 90; // the time limit for multiplayer mode
	public boolean multiMode; // tell's the game whether to switch to
								// multiplayer mode or not
	public boolean borders; // tell's the game to include border or not
	public boolean AI; // tell's the game to play with AI or not

	// various images needed in the game
	ImageIcon RIGHTHEAD = new ImageIcon("images/SnakeHeadRight.png");
	ImageIcon LEFTHEAD = new ImageIcon("images/SnakeHeadLeft.png");
	ImageIcon UPHEAD = new ImageIcon("images/SnakeHeadUp.png");
	ImageIcon DOWNHEAD = new ImageIcon("images/SnakeHeaddown.png");

	ImageIcon RIGHTHEAD2 = new ImageIcon("images/SnakeHeadRight2.png");
	ImageIcon LEFTHEAD2 = new ImageIcon("images/SnakeHeadLeft2.png");
	ImageIcon UPHEAD2 = new ImageIcon("images/SnakeHeadUp2.png");
	ImageIcon DOWNHEAD2 = new ImageIcon("images/SnakeHeaddown2.png");

	ImageIcon EXPLOSION = new ImageIcon("images/explosion.png");
	ImageIcon SOUP = new ImageIcon("images/Soup.png");
	ImageIcon CAKE = new ImageIcon("images/Cake.png");
	ImageIcon DONUT = new ImageIcon("images/Donut.png");
	ImageIcon LOLLIPOP = new ImageIcon("images/Lollipop.PNG");
	ImageIcon DYNAMITE = new ImageIcon("images/Dynamite.png");
	ImageIcon FADEDCAKE = new ImageIcon("images/FadedCake.png");

	ArrayList<Food> foodItems = new ArrayList<Food>(); // stores the food items
	ArrayList<Body> removeList = new ArrayList<Body>(); // includes the items
														// needed to be removed
	private String message = " ";

	public Screen() {
		setBackground(Color.BLACK);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == e.VK_P) {
			stopAllTimers(); // pause
		} else if (e.getKeyCode() == e.VK_R) {
			restart(); // restart
		} else if (e.getKeyCode() == e.VK_1) { // control's the borders feature
			if (borders) {
				borders = false;
			} else {
				borders = true;
			}
		} else {

			if (!gameTimer1.isRunning() && !snake2.isDead() && !snake1.isDead()) {
				gameTimer1.start(); // start the game once user has pressed an
									// arrow key
				foodTimer.start();
				if (multiMode) {
					countDownTimer.start(); // if playing in multipayer mode
											// start the countdown timer
				}
			}
			int dir = e.getKeyCode() - 37; // get the direction
			Body head = snake1.getBody().get(0);
			if (Math.abs(head.getDirection() - dir) != 2 || (head.getDirection() == -1)) {
				switch (dir) { // setting the direction
				case 0:
					if (head.getDirection() != 2) {
						head.setImage(LEFTHEAD);
						head.setDirection(0);
					}
					break;
				case 1:
					if (head.getDirection() != 3) {
						head.setImage(UPHEAD);
						head.setDirection(1);
					}
					break;
				case 2:
					if (head.getDirection() != -1 && head.getDirection() != 0) {
						head.setImage(RIGHTHEAD);
						head.setDirection(2);
					}
					break;
				case 3:
					if (head.getDirection() != 1) {
						head.setImage(DOWNHEAD);
						head.setDirection(3);
					}
				}
				snake1.getBody().set(0, head);
			}
		}
		if (multiMode && (e.getKeyCode() == e.VK_W || e.getKeyCode() == e.VK_A || e.getKeyCode() == e.VK_S
				|| e.getKeyCode() == e.VK_D)) { // contol's snake2
			if (!gameTimer2.isRunning() && !snake2.isDead() && !snake1.isDead() && !AI) {
				gameTimer2.start();
				foodTimer.start();
				countDownTimer.start();
			}
			Body head2 = snake2.getBody().get(0);
			if (e.getKeyCode() == e.VK_W) { // setting snake2's direction
				if (head2.getDirection() != 3) {
					head2.setImage(UPHEAD2);
					head2.setDirection(1);
				}
			} else if (e.getKeyCode() == e.VK_S) {
				if (head2.getDirection() != 1) {
					head2.setImage(DOWNHEAD2);
					head2.setDirection(3);
				}
			} else if (e.getKeyCode() == e.VK_A) {
				if (head2.getDirection() != 2) {
					head2.setImage(LEFTHEAD2);
					head2.setDirection(0);
				}
			} else if (e.getKeyCode() == e.VK_D) {
				if (head2.getDirection() != 0) {
					if (head2.getDirection() != -1) {
						head2.setImage(RIGHTHEAD2);
						head2.setDirection(2);
					}
				}
			}
			snake2.getBody().set(0, head2);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) { // control's the action for
													// various timers in the
													// game
		if (e.getSource().equals(gameTimer1)) {
			if (snake1.getBody().get(0).getDirection() != -1) {
				moveSnake(snake1); // move the snake
				collision(snake1); // show effects of collision
			}
			repaint();
		} else if (e.getSource().equals(gameTimer2)) {
			if (AI) { // if AI is playing set the direction of snake2
				if (foodItems.size() != 0) { // don't move if there is no food
												// items
					moveSnakeAI();
				}
			}
			moveSnake(snake2); // move the snake2
			collision(snake2);
			repaint();
		} else if (e.getSource().equals(soupTimer1)) {
			gameTimer1.setDelay(100);
			soupTimer1.stop();
		} else if (e.getSource().equals(soupTimer2)) {
			gameTimer2.setDelay(100);
			soupTimer2.stop();
		} else if (e.getSource().equals(donutTimer1)) {
			gameTimer1.setDelay(100);
			donutTimer1.stop();
		} else if (e.getSource().equals(donutTimer2)) {
			gameTimer2.setDelay(100);
			donutTimer2.stop();
		} else if (e.getSource().equals(dynamiteTimer1)) {
			snake1.getBody().removeAll(removeList); // remove all the body parts that are in the removeList
			dynamiteTimer1.stop();
		} else if (e.getSource().equals(dynamiteTimer2)) {
			snake2.getBody().removeAll(removeList);
			dynamiteTimer2.stop();
		}

		else if (e.getSource().equals(foodTimer)) {
			if (AI) {
				if (!gameTimer2.isRunning()) {
					gameTimer2.start(); // wake the AI snake when food has been generated
				}
			}
			// creating a random location for the objects
			int randX = new Random().nextInt(getWidth() - 120) + 60; 
			int randY = new Random().nextInt(getHeight() - 120) + 60;
			Food generatedFood = new Food(generateFood(), randX, randY);
			foodItems.add(generatedFood);
			if (generatedFood.getImage().equals(SOUP) || generatedFood.getImage().equals(CAKE)) {
				Timer deleteTimer = new Timer(1000, new ActionListener() { // every time a cake or soup is generated, start a countdown timer to remove it
					@Override
					public void actionPerformed(ActionEvent e) { 
						generatedFood.counter++;
						if (generatedFood.counter % 10 == 0) {
							foodItems.remove(generatedFood); // after 10 seconds remove the food
						} else if (generatedFood.counter >= 5) {
							for (Food food : foodItems) {
								if (food.equals(generatedFood)) {
									if (food.getImage().equals(FADEDCAKE)) {
										food.setImage(CAKE); // after 5 seconds start flashing the cake
										break;
									} else if (food.getImage().equals(CAKE)) {
										food.setImage(FADEDCAKE);
										break;
									}
								}
							}
						}

					}
				});
				deleteTimer.start();
			}

			repaint();
		} else if (e.getSource().equals(eraseTimer)) {
			message = " "; // resets the message
		} else if (e.getSource().equals(countDownTimer)) {
			countDownTime--; // at the end of the countdown decide who wins
			if (countDownTime == 0) {
				if (score1 > score2) {
					message = "Player1 Wins!";
				} else if (score1 == score2) {
					message = "It is a Tie!";
				} else {
					message = "Player 2 Wins";
				}
				snake1.setDead(true);
				snake2.setDead(true);
				stopAllTimers();
			}
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		if (borders) {
			// draw the borders if borders are present
			g2.setColor(Color.RED);
			// |
			g2.drawLine(0, 0, 0, getHeight() - 5);
			// |_
			g2.drawLine(0, getHeight() - 5, getWidth() - 5, getHeight() - 5);
			// |_|
			g2.drawLine(getWidth() - 5, getHeight() - 5, getWidth() - 5, 0);
			// complete the square
			g2.drawLine(getWidth() - 5, 0, 0, 0);

		}
		for (Food food : foodItems) { // paint the food
			food.getImage().paintIcon(this, g2, food.getPosX(), food.getPosY());
		}

		for (int i = 0; i < snake1.getBody().size(); i++) { // paint snake1
			snake1.getBody().get(i).getImage().paintIcon(this, g2, snake1.getBody().get(i).getPosX(),
					snake1.getBody().get(i).getPosY());
		}
		if (multiMode) { // paint snake2 if the game is in multiplayer mode
			for (int i = 0; i < snake2.getBody().size(); i++) {
				snake2.getBody().get(i).getImage().paintIcon(this, g2, snake2.getBody().get(i).getPosX(),
						snake2.getBody().get(i).getPosY());
			}
		}

	}

	private void moveSnake(Snake snake) { // moving the sanke


		int posX = snake.getBody().get(0).getPosX(); // store the positions of the head
		int posY = snake.getBody().get(0).getPosY();
		int posX1 = 0, posY1 = 0; // holds the positions of current body part
		int posX2 = 0, posY2 = 0; // holds the positions of body part ahead of the current
		for (int i = 1; i < snake.getBody().size(); i++) {
			if (i == 1) {
				posX1 = snake.getBody().get(i).getPosX(); 
				posY1 = snake.getBody().get(i).getPosY();
				snake.getBody().get(i).setPosX(posX); // set the postion of the body behind the head to the positon of the head
				snake.getBody().get(i).setPosY(posY);
			}

			else {
				posX2 = snake.getBody().get(i).getPosX(); // store the position of the current body
				posY2 = snake.getBody().get(i).getPosY();
				snake.getBody().get(i).setPosX(posX1); // set the position of the current body part to body part ahead
				snake.getBody().get(i).setPosY(posY1);

				posX1 = posX2; // the body part's original position will be stored for the body part behind it
				posY1 = posY2;
				// cycle continues
			}
		}
		
		// this was my inintal method to move the snake but it did not work
		// I keep this to set the directions of the body parts of the snake
		// based on the direction of the previous body part, new body parts are added
		// for example: if the tail is moving right, then add body part to the left of the tail
		int dir = snake.getBody().get(0).getDirection();
		int bufferDir = dir; 
		for (Body body : snake.getBody()) {
			bufferDir = body.getDirection();
			body.setDirection(dir);
			dir = bufferDir;
		}

		if (snake.getBody().get(0).getPosX() < -5) {
			if (!borders) {
				snake.getBody().get(0).setPosX(getWidth() - 30); // if snake reaches the width of the screen, teleport it to the beginning
			} else {
				death(snake); // if borders are present the snake will die
			}
		} else if (snake.getBody().get(0).getPosX() + 30 > getWidth() + 5) { // if snake is going past he X-origin, then teleport it to the end
			if (!borders) {
				snake.getBody().get(0).setPosX(0);
			} else {
				death(snake); // if borders are present the snake will die
			}
		}
		if (snake.getBody().get(0).getPosY() < -5) { 
			if (!borders) {
				snake.getBody().get(0).setPosY(getHeight() - 30); // if snake reaches the height of the screen, teleport it to the top
			} else {
				death(snake); // if borders are present the snake will die
			}
		} else if (snake.getBody().get(0).getPosY() + 30 > getHeight() + 5) {
			if (!borders) {
				snake.getBody().get(0).setPosY(0); // if snake is going pas the Y-origin, then teleport it to the bottom
			} else {
				death(snake); // if borders are present the snake will die
			}
		}
		snake.getBody().get(0).move(); // move the head the body will follow
	}

	private void collision(Snake snake) {
		for (Food food : foodItems) {
			if (collided(food, snake)) {
				if (food.getImage().equals(LOLLIPOP)) {
					if (snake.equals(snake1)) { // add 20 pts for eating a lollipop
						score1 += 20;
					} else {
						score2 += 20;
					}
					if (snake.equals(snake1)) { // add a new body part to the snake
						snake1.addBody(new Body(new ImageIcon("images/SnakeBody.png")));
						Body body = snake1.getBody().get(snake1.getBody().size() - 2);

					} else if (snake.equals(snake2)) {
						snake2.addBody(new Body(new ImageIcon("images/SnakeBody2.png")));
						Body body = snake2.getBody().get(snake2.getBody().size() - 2);
					}
				} else if (food.getImage().equals(DYNAMITE)) {

					if (snake.equals(snake1)) {
						if (multiMode) { // set the message for the player
							message = "Player1: Length Shortened!";
						} else {
							message = "Length Shortened";
						}
					} else {
						message = "Player2: Length Shortened!";
					}
					if (!eraseTimer.isRunning()) { // after sometime erase the message
						eraseTimer.start();
					} else {
						eraseTimer.restart();
					}
					if (!eraseTimer.isRunning()) {
						eraseTimer.start();
					} else {
						eraseTimer.restart();
					}
					if (snake.equals(snake1)) {
						int size = snake.getBody().size();
						for (int i = (size / 2) + 1; i < size; i++) {
							removeList.add(snake1.getBody().get(i));
							snake1.getBody().get(i).setImage(EXPLOSION); // show an explosion for all the body parts that will be removed
							repaint();
						}
						dynamiteTimer1.start();
						if (soupTimer1.isRunning()) { // after some time remove the explosion
							soupTimer1.stop();
						}
					} else {
						int size = snake.getBody().size();
						for (int i = (size / 2) + 1; i < size; i++) {
							removeList.add(snake2.getBody().get(i));
							snake2.getBody().get(i).setImage(EXPLOSION);
							repaint();
						}
						dynamiteTimer2.start();
						if (soupTimer2.isRunning()) {
							soupTimer2.stop();
						}
					}
				} else if (food.getImage().equals(SOUP)) {

					if (snake.equals(snake1)) {
						score1 -= 10; // remove 10 pts
						if (multiMode) {
							message = "Player1: Feeling Sleepy...ZZZZ"; // set message for the player
						} else {
							message = "Feeling Sleepy...ZZZZ";
						}
					} else {
						message = "Player2: Feeling Sleepy...ZZZZ";
						score2 -= 10;
					}

					if (!eraseTimer.isRunning()) { // remove the message after sometime
						eraseTimer.start();
					} else {
						eraseTimer.restart();
					}
					if (snake.equals(snake1)) {
						gameTimer1.setDelay(150); // slow down the snake
						if (donutTimer1.isRunning()) {
							donutTimer1.stop(); // if the snake had eaten a donut, negate the effect
						}
						if (soupTimer1.isRunning()) {
							soupTimer1.restart();
						} else {
							soupTimer1.start(); // after sometime bring the snake to normat speed
						}
					} else { // do the same for snake2
						gameTimer2.setDelay(150);
						if (donutTimer2.isRunning()) {
							donutTimer2.stop();
						}
						if (soupTimer2.isRunning()) {
							soupTimer2.restart();
						} else {
							soupTimer2.start();
						}
					}
				} else if (food.getImage().equals(DONUT)) {

					if (snake.equals(snake1)) {
						score1 += 50; // add 50 pts
						if (multiMode) { // set message for the player
							message = "Player1: Speeding Up!";
						} else {
							message = "Speeding Up!";
						}
					} else {
						message = "Player2: Speeding Up!";
						score2 += 50;
					}

					if (!eraseTimer.isRunning()) { // remove the message aftersometime
						eraseTimer.start();
					} else {
						eraseTimer.restart();
					}
					if (snake.equals(snake1)) { // speed up the snake
						gameTimer1.setDelay(50);
						if(soupTimer1.isRunning()){
							soupTimer1.stop(); // if snake had eaten soup, negate the effect
						}
						if (donutTimer1.isRunning()) { 
							donutTimer1.restart();
						} else {
							donutTimer1.start();
						}
					} else {
						if(soupTimer2.isRunning()){ // do the same for snake2
							soupTimer2.restart(); 
						}
						gameTimer2.setDelay(50);
						if (donutTimer2.isRunning()) {
							donutTimer2.restart();
						} else {
							donutTimer2.start();
						}
					}
				} else if (food.getImage().equals(CAKE) || food.getImage().equals(FADEDCAKE)) {
					if (snake.equals(snake1)) {
						score1 += 100; // add 100 pts
					} else {
						score2 += 100;
					}
				}
				foodItems.remove(food); // remove the eaten food
				break;
			}
		}
		for (Body body : snake.getBody()) { // if snake collides with itself, it will die
			if (Math.abs(body.getPosX() - snake.getBody().get(0).getPosX()) < body.getImage().getIconWidth() && 
					Math.abs(body.getPosY() - snake.getBody().get(0).getPosY()) < body.getImage().getIconHeight() &&
					!body.equals(snake.getBody().get(0)) && !body.getImage().equals(EXPLOSION)) {
				
				death(snake); 
			}
		}
	}

	private ImageIcon generateFood() { // genrates food
		int foodType = new Random().nextInt(10);

		if (foodType <= 4) { // 10% chance lollipop is 0 to 4
			return LOLLIPOP;
		} else if (foodType == 5) { // 10% chance if number = 5
			return CAKE;
		} else if (foodType == 6) { // 10% chance dynamite if number = 6
			return DYNAMITE;
		} else if (foodType <= 8) { // 20% chance soup if number is 7-8
			return SOUP;
		} else if (foodType == 9) { // 10% chance donut if number is 9
			return DONUT;
		}
		return null;
	}

	public void stopAllTimers() { // pause feature stop all the timers
		gameTimer1.stop();
		gameTimer2.stop();
		foodTimer.stop();
		soupTimer1.stop();
		soupTimer2.stop();
		donutTimer1.stop();
		donutTimer2.stop();
		dynamiteTimer1.stop();
		dynamiteTimer2.stop();
		eraseTimer.stop();
		countDownTimer.stop();
	}

	public String getMessage() {
		return message;
	}

	public int getScore1() {
		return score1;
	}

	public int getScore2() {
		return score2;
	}

	public int getCountDownTime() {
		return countDownTime;
	}

	public void restart() { // restart feature restart all timers
		stopAllTimers();
		if (multiMode) {
			countDownTime = 90;
			snake2 = new Snake(1, 651, 390);
			foodTimer.setDelay(700); // generate food faster for 2 snakes
		} else {
			foodTimer.setDelay(1000);
		}
		snake1 = new Snake(0, 651, 350);
		score1 = 0; // reset the scores
		score2 = 0;
		message = " "; // reset the message
		ArrayList<Food> removeList = new ArrayList<Food>(); // reomve all the food
		for (Food food : foodItems) {
			removeList.add(food);
		}
		foodItems.removeAll(removeList);
		repaint(); // paint the screen again
		gameTimer1.setDelay(100); // reset the speed
		gameTimer2.setDelay(100);
	}

	private boolean collided(Food food, Snake snake) { // method used to check collision with food items
		int dX = Math.abs(food.getPosX() - snake.getBody().get(0).getPosX());
		int dY = Math.abs(food.getPosY() - snake.getBody().get(0).getPosY());

		if (snake.getBody().get(0).getPosX() > food.getPosX()) {
			if (dX < food.getImage().getIconWidth()) {
				if (snake.getBody().get(0).getPosY() >= food.getPosY()) {
					if (dY < food.getImage().getIconHeight()) {
						return true;
					}
				} else {
					if (dY < snake.getBody().get(0).getImage().getIconHeight()) {
						return true;
					}
				}
			}
		} else {
			if (dX < snake.getBody().get(0).getImage().getIconWidth()) {
				if (snake.getBody().get(0).getPosY() >= food.getPosY()) {
					if (dY < food.getImage().getIconHeight()) {
						return true;
					}
				} else {
					if (dY < snake.getBody().get(0).getImage().getIconHeight()) {
						return true;
					}
				}
			}

		}

		return false;
	}

	private void death(Snake snake) {
		if (snake.equals(snake1)) {
			snake1.setDead(true); // make the snakes dead
		} else {
			snake2.setDead(true);
		}
		if (snake.equals(snake1)) { // decide who winds based on who died first
			if (multiMode) {
				message = "Player1 died, Player2 wins!";
			} else {
				message = "You Died!";
			}
		} else {
			message = "Player2 died, Player1 wins!";
		}
		stopAllTimers(); // stop the game
	}

	private void moveSnakeAI() {
		// find the nearest food
		int maxDist = (int) Math.hypot(getWidth(), getHeight());
		Body head2 = snake2.getBody().get(0);
		Body head1 = snake1.getBody().get(0);
		Food pointerFood = foodItems.get(0);
		for (Food food : foodItems) {
			if (pointerFood.getImage().equals(SOUP) && foodItems.size() != -1) {
				int counter = 0;
				while (foodItems.get(counter).getImage().equals(SOUP)) {
					if (counter < foodItems.size() - 1) {
						counter++;
					}
					if (counter == foodItems.size() - 1) {
						break;
					}
				}
				pointerFood.equals(foodItems.get(counter));
			}
			int dX2 = Math.abs(food.getPosX() - head2.getPosX());
			int dY2 = Math.abs(food.getPosY() - head2.getPosY());

			int dX1 = Math.abs(food.getPosX() - head1.getPosX());
			int dY1 = Math.abs(food.getPosY() - head1.getPosX());

			if (Math.hypot(dX2, dY2) < maxDist && Math.hypot(dX2, dY2) < Math.hypot(dX1, dY1)
					&& !food.getImage().equals(SOUP)) {
				maxDist = (int) Math.hypot(dX2, dY2);
				pointerFood = food;
			}
		}
		// deciding on the direction

		// case 1 if food is above snake
		if (pointerFood.getPosY() < head2.getPosY()) {
			// if no body part is above snake and the direction is not downwards
			if (head2.getDirection() != 3 && !checkBody(1, snake2)) {
				head2.setDirection(1);
				head2.setImage(UPHEAD2);
			} else if (pointerFood.getPosX() < head2.getPosX() && !checkBody(0, snake2)) {
				head2.setDirection(0);
				head2.setImage(LEFTHEAD2);
			} else if (pointerFood.getPosX() > head2.getPosX() && !checkBody(2, snake2)) {
				head2.setDirection(2);
				head2.setImage(RIGHTHEAD2);
			}
		}

		// case 2 if food is below

		else if (pointerFood.getPosY() > head2.getPosY()) {
			// if no body part is below snake and the direction is not upwards
			if (head2.getDirection() != 1 && !checkBody(3, snake2)) {
				head2.setDirection(3);
				head2.setImage(DOWNHEAD2);
			} else if (pointerFood.getPosX() < head2.getPosX() && !checkBody(0, snake2)) {
				head2.setDirection(0);
				head2.setImage(LEFTHEAD2);
			} else if (pointerFood.getPosX() > head2.getPosX() && !checkBody(2, snake2)) {
				head2.setDirection(2);
				head2.setImage(RIGHTHEAD2);

			}
		}

		// case 3 if the Y values are close and food is above
		if (!pointerFood.getImage().equals(SOUP)) {
			if (pointerFood.getPosY() < head2.getPosY()
					&& head2.getPosY() - pointerFood.getPosY() <= head2.getImage().getIconHeight()) {
				// if food is left of snake and and sake is not going right and
				// not
				// body part is left of snake
				if (pointerFood.getPosX() < head2.getPosX()) {
					if (head2.getDirection() != 2 && !checkBody(0, snake2)) {
						head2.setDirection(0);
						head2.setImage(LEFTHEAD2);
					}
				} else if (pointerFood.getPosX() > head2.getPosX()) {
					if (head2.getDirection() != 0 && !checkBody(2, snake2)) {
						head2.setDirection(2);
						head2.setImage(RIGHTHEAD2);
					} else if (!checkBody(1, snake2)) {
						head2.setDirection(1);
						head2.setImage(UPHEAD2);
					}
				}
			}

			// case 4 if the Y values are close and food is below

			if (pointerFood.getPosY() > head2.getPosY()
					&& pointerFood.getPosY() - head2.getPosY() <= pointerFood.getImage().getIconHeight()) {

				// if food is left of snake and snake is not going right
				if (pointerFood.getPosX() < head2.getPosX()) {
					if (head2.getDirection() != 2 && !checkBody(0, snake2)) {
						head2.setDirection(0);
						head2.setImage(LEFTHEAD2);
					}
				} else if (pointerFood.getPosX() > head2.getPosX()) {
					if (head2.getDirection() != 0 && !checkBody(2, snake2)) {
						head2.setDirection(2);
						head2.setImage(RIGHTHEAD2);
					}
				} else if (!checkBody(3, snake2)) {
					head2.setDirection(3);
					head2.setImage(DOWNHEAD2);
				}
			}
		}
		// if borders are present
		if (borders) {
			if (head2.getPosX() - 60 <= 0 && head2.getDirection() == 0) { // left
																			// border
				if (!checkBody(3, snake2) && head2.getPosY() + 60 <= getHeight()) {
					head2.setDirection(3);
					head2.setImage(DOWNHEAD2);
				} else {
					head2.setDirection(1);
					head2.setImage(UPHEAD2);
				}

			} else if (head2.getPosX() + 60 >= getWidth() && head2.getDirection() == 2) { // right
																							// border
				if (!checkBody(3, snake2) && head2.getPosY() + 60 <= getHeight()) {
					head2.setDirection(3);
					head2.setImage(DOWNHEAD2);
				} else {
					head2.setDirection(1);
					head2.setImage(UPHEAD2);
				}
			} else if (head2.getPosY() - 60 <= 0 && head2.getDirection() == 1) { // top
																					// border
				if (!checkBody(0, snake2) && head2.getPosX() - 60 >= 0) {
					head2.setDirection(0);
					head2.setImage(LEFTHEAD2);
				} else {
					head2.setDirection(2);
					head2.setImage(RIGHTHEAD2);
				}
			} else if (head2.getPosY() + 60 >= getHeight() && head2.getDirection() == 3) {// bottom
																							// border
				if (!checkBody(0, snake2) && head2.getPosX() - 60 >= 0) {
					head2.setDirection(0);
					head2.setImage(LEFTHEAD2);
				} else {
					head2.setDirection(2);
					head2.setImage(RIGHTHEAD2);
				}
			}
		}
	}

	private boolean checkBody(int direction, Snake snake) { // checks if any body part is lying before the snake's path
		Body head = snake.getBody().get(0);
		switch (direction) {
		case 0:
			for (Body body : snake.getBody()) {
				if (body.getPosX() < head.getPosX() && body.getPosY() == head.getPosY()) {
					return true;
				}
			}
			return false;
		case 1:
			for (Body body : snake.getBody()) {
				if (body.getPosY() < head.getPosY() && body.getPosX() == head.getPosX()) {
					return true;
				}
			}
			return false;

		case 2:
			for (Body body : snake.getBody()) {
				if (body.getPosX() > head.getPosX() && body.getPosY() == head.getPosY()) {
					return true;
				}
			}
			return false;
		case 3:
			for (Body body : snake.getBody()) {
				if (body.getPosY() > head.getPosY() && body.getPosX() == head.getPosX()) {
					return true;
				}
			}
			return false;
		}
		return false;

	}
}
