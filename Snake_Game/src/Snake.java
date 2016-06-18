import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Snake {
	private ArrayList<Body> bodyParts = new ArrayList<Body>(); // contains the bodyparts of the snake
	private boolean dead = false; // determining whether it is dead or not

	public Snake(int snakeNum,int posX, int posY) { // snakeNum is for selecting which snake to create, the red snake or the green snake
		if (snakeNum == 0) {
			// if it is green snake
			bodyParts.add(new Body(new ImageIcon("images/SnakeHeadLeft.png")));

			for (int i = 0; i < 3; i++) {
				// adding body parts to the snake
				bodyParts.add(new Body(new ImageIcon("images/SnakeBody.png")));
			}
			bodyParts.get(0).setPosX(posX);
			bodyParts.get(0).setPosY(posY);
			
			for (int i = 1; i < bodyParts.size(); i++) {
				// creating distance between the body parts
				bodyParts.get(i).setPosX(bodyParts.get(i - 1).getPosX() + 30);
				bodyParts.get(i).setPosY(bodyParts.get(i-1).getPosY());
				bodyParts.get(i).setDirection(0);
			}
		} else {
			// if the sanke is red
			bodyParts.add(new Body(new ImageIcon("images/SnakeHeadLeft2.png")));

			for (int i = 0; i < 3; i++) {
				bodyParts.add(new Body(new ImageIcon("images/SnakeBody2.png")));
			}
			bodyParts.get(0).setPosX(posX);
			bodyParts.get(0).setPosY(posY);
			
			for (int i = 1; i < bodyParts.size(); i++) {
				bodyParts.get(i).setPosX(bodyParts.get(i - 1).getPosX() + 30);
				bodyParts.get(i).setPosY(bodyParts.get(i-1).getPosY());
				bodyParts.get(i).setDirection(0);
			}
		}

	}

	public ArrayList<Body> getBody() {
		return bodyParts;
	}

	public void addBody(Body body) {
		// adding a new body part to the snake
		bodyParts.add(body);
		Body previousBody = bodyParts.get(bodyParts.size()-2);
		// adding the body according the direction of the snake
		switch(previousBody.getDirection()){
		case 0:
			// the tail is moving left then add body part to the right of the tail and set direction to the left
			bodyParts.get(bodyParts.size() - 1).setPosX(bodyParts.get(bodyParts.size() - 2).getPosX() + 30);
			bodyParts.get(bodyParts.size() - 1).setPosY(bodyParts.get(bodyParts.size() - 2).getPosY());
			bodyParts.get(bodyParts.size()-1).setDirection(0);
			break;
		case 2:
			// the tail was moving left then add body part to the left of the tail and set direction to the right
			bodyParts.get(bodyParts.size() - 1).setPosX(bodyParts.get(bodyParts.size() - 2).getPosX() -30);
			bodyParts.get(bodyParts.size() - 1).setPosY(bodyParts.get(bodyParts.size() - 2).getPosY());
			bodyParts.get(bodyParts.size()-1).setDirection(2);
		case 1: 
			// the tail was moving up then add body part below the tail and set direction to up
			bodyParts.get(bodyParts.size() - 1).setPosX(bodyParts.get(bodyParts.size() - 2).getPosX());
			bodyParts.get(bodyParts.size() - 1).setPosY(bodyParts.get(bodyParts.size() - 2).getPosY()+30);
			bodyParts.get(bodyParts.size()-1).setDirection(1);
		case 3: 
			// the tail was moving down then add body part above the tail and set direction to down
			bodyParts.get(bodyParts.size() - 1).setPosX(bodyParts.get(bodyParts.size() - 2).getPosX());
			bodyParts.get(bodyParts.size() - 1).setPosY(bodyParts.get(bodyParts.size() - 2).getPosY()-30);
			bodyParts.get(bodyParts.size()-1).setDirection(3);
		}
		
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

}
