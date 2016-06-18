import javax.swing.ImageIcon;

public class Body {
private ImageIcon image;
private int posX, posY;
private int velY= 0;
private int velX= 0;
private int dir = -1;
public Body( ImageIcon image){
	this.image = image;
}
public void setDirection(int dir){
	 switch(dir){
	 case 0:
	 velX = -30;
	 velY = 0;
	 break;
	 case 1:
    velY = -30;
    velX = 0;
    break;
	 case 2:
	 velX = 30;
	 velY = 0;
	 break;
	 case 3:
	 velY = 30;
	 velX = 0;
	 }
	 this.dir = dir;
}

public int getDirection(){
	 return dir;
}
public void move(){
	 posX += velX;
	 posY += velY;
}

public int getPosY() {
	// TODO Auto-generated method stub
	return posY;
}

public int getPosX(){
	return posX;
}
public ImageIcon getImage() {
	return image;
}
public void setPosX(int posX) {
	this.posX = posX;
}
public void setPosY(int posY) {
	this.posY = posY;
}

public void setImage(ImageIcon image) {
	this.image = image;
}
}
