package com.game.packman;
import java.io.IOException;

public class PacManGame {
	/* Added features:
	 * Ghosts turn blue when pacMan eats power pellets, and they flash white when they turn back. They also move slower when the turn white
	 * Once eaten, they will spawn in the central area of the maze
	 * Music: when pacman dies, there is background music, sound effects when pacman eats different kinds of food and a victory music when pacman wins. 
	 * Modified the controls, pacman will remember the key the user has pressed and will turn in that direction when possible
	 * Added AI to the ghosts: yellow can locate pacman, red mimics the direction of pacman, green keeps moving in one direction until it hits a wall
	 * All the ghosts chase pacman when are either in the same column or the same row
	 * The blue ghosts run away from pacman when they are in the same column or the same row
	 * Added help menu, pause button and restart button
	 * Added a scoring system and also an insanity mode where all the walls turn into food, but only some are edible
	 * A cherry will appear 5 seconds after the previous cherry has been eaten.
	 * Animation has been modified. Now it only consists of two steps and there is no further blinking
	 * The users win the game if he or she has eaten all the food pellets on the board
	 */
	public static void main(String[] args) throws IOException {
		new PacManGUI();
	}
}
