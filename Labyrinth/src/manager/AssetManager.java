package manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class AssetManager {
	
	private int[] T1 = {1,3,4,5};
	private int[] T2 = {1,4,5,7};
	private int[] T3 = {3,4,5,7};
	private int[] T4 = {1,3,4,7};

	
	private int[] I1 = {1,4,7};
	private int[] I2 = {3,4,5};

	private int[] L1 = {4,5,7};
	private int[] L2 = {3,4,7};
	private int[] L3 = {1,3,4};
	private int[] L4 = {1,4,5};

	private int[] T1T = {1,3,4,5,-1};
	private int[] T2T = {1,4,5,7,-1};
	private int[] T3T = {3,4,5,7,-1};
	private int[] T4T = {1,3,4,7,-1};

	private int[] L1T = {4,5,7,-1};
	private int[] L2T = {3,4,7,-1};
	private int[] L3T = {1,3,4,-1};
	private int[] L4T = {1,4,5,-1};

	private int[] W = {};
	
	public static HashMap<String, int[]> toPosition;
	
	public static ImageIcon wall;
	public static ImageIcon route;
	public static ImageIcon arrow;
	public static ImageIcon player1;
	public static ImageIcon player2;
	public static ImageIcon white;
	
	public static ArrayList<ImageIcon> treasuresIcon;
	
	public static ArrayList<String> piecesNeeded;
	public AssetManager(){
		toPosition = new HashMap<>();
		toPosition.put("T1", T1);
		toPosition.put("T2", T2);
		toPosition.put("T3", T3);
		toPosition.put("T4", T4);
		toPosition.put("I1", I1);
		toPosition.put("I2", I2);
		toPosition.put("L1", L1);
		toPosition.put("L2", L2);
		toPosition.put("L3", L3);
		toPosition.put("L4", L4);
		toPosition.put("T1T", T1T);
		toPosition.put("T2T", T2T);
		toPosition.put("T3T", T3T);
		toPosition.put("T4T", T4T);
		toPosition.put("L1T", L1T);
		toPosition.put("L2T", L2T);
		toPosition.put("L3T", L3T);
		toPosition.put("L4T", L4T);
		toPosition.put("W", W);
		
		piecesNeeded = new ArrayList<>();
		for(int i = 1; i<=12;i++){
			piecesNeeded.add("I");
		}
		for(int i = 1; i<=10;i++){
			piecesNeeded.add("L");
		}
		for(int i = 1; i<=6;i++){
			piecesNeeded.add("LT");
		}
		for(int i = 1; i<=6;i++){
			piecesNeeded.add("TT");
		}
		
		
		
		BufferedImage bimageWall = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB); //create an empty buffered image
		Graphics2D bGr = bimageWall.createGraphics(); //get the graphics for the buffered image
		bGr.setColor(Color.cyan);
		bGr.fillRect(0, 0, 23, 23);
		bGr.dispose(); //dispose the graphics
		
		BufferedImage bimageRoute = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB); //create an empty buffered image
		bGr = bimageRoute.createGraphics(); //get the graphics for the buffered image
		bGr.setColor(Color.black);
		bGr.fillRect(0, 0, 23, 23);
		bGr.dispose(); //dispose the graphics
		
		BufferedImage bimageArrow = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB); //create an empty buffered image
		bGr = bimageArrow.createGraphics(); //get the graphics for the buffered image
		bGr.setColor(Color.red);
		bGr.fillRect(10, 10, 30, 30);
		bGr.dispose();
		
		BufferedImage bimagePlayer1 = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB); //create an empty buffered image
		bGr = bimagePlayer1.createGraphics(); //get the graphics for the buffered image
		bGr.setComposite(Method.makeTransparent(0.8f));
		bGr.setColor(Color.orange);
		bGr.fillRect(0, 0, 23, 23);
		bGr.setComposite(Method.makeTransparent(1));
		bGr.dispose();
		
		BufferedImage bimagePlayer2 = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB); //create an empty buffered image
		bGr = bimagePlayer2.createGraphics(); //get the graphics for the buffered image
		bGr.setComposite(Method.makeTransparent(0.8f));
		bGr.setColor(Color.pink);
		bGr.fillRect(0, 0, 23, 23);
		bGr.setComposite(Method.makeTransparent(1));
		bGr.dispose();
		
		BufferedImage bimagewhite = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB); //create an empty buffered image
		bGr = bimagewhite.createGraphics(); //get the graphics for the buffered image
		bGr.setColor(Color.white);
		bGr.fillRect(0, 0, 23, 23);
		bGr.dispose();
		
		
		wall = new ImageIcon(bimageWall);
		route = new ImageIcon(bimageRoute);
		arrow = new ImageIcon(bimageArrow);
		player1 = new ImageIcon(bimagePlayer1);
		player2 = new ImageIcon(bimagePlayer2);
		white = new ImageIcon(bimagewhite);
		
		
		treasuresIcon = new ArrayList<>();
		for(int i = 1; i<=24;i++){
			treasuresIcon.add(makeImageIcon(Integer.toString(i)));
		}
	}
	
	private static Font font = new Font("Agency FB", Font.BOLD, 20);
	
	public static ImageIcon makeImageIcon(String text){
		BufferedImage bimage = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB); //create an empty buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.setColor(Color.red);
		bGr.fillRect(0, 0, 23, 23);
		bGr.setColor(Color.white);
		bGr.setFont(font);

		bGr.drawString(text, 4, 17);
		bGr.dispose();
		
		return new ImageIcon(bimage);
	}
}
