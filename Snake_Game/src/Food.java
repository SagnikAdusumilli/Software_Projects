import javax.swing.ImageIcon;

public class Food {
	private ImageIcon image; // for displaying the different types of food
	public int counter = 0; // public should be responsibly used

	private int posX, posY; // for drawing them onto the screen and detecting
							// collision

	public Food(ImageIcon image, int posX, int posY) {
		this.image = image;
		this.posX = posX;
		this.posY = posY;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
}
