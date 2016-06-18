package manager;

import java.awt.AlphaComposite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Method {
	private static Random r = new Random();

	public static boolean contains(int[] array, int num){
		for(int n: array){ 
			if(num==n){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	public static int getRandomInt(int start, int end){
		return r.nextInt(end-start+1)+start;
	}


	public static String getRandom(ArrayList<String> pieceNeeded){
		String text;
		
		text = pieceNeeded.get(r.nextInt(pieceNeeded.size()));
		pieceNeeded.remove(text);		

		if(text=="L"){
			text+=Integer.toString(r.nextInt(4)+1);
		}else if(text=="TT"||text=="LT"){
			text = Character.toString(text.charAt(0)) + Integer.toString(r.nextInt(4)+1) + Character.toString(text.charAt(1));
		}else if(text=="I"){
			text+=Integer.toString(r.nextInt(2)+1);
		}

		if(pieceNeeded.size()==0){
			pieceNeeded.add(text);
		}

		return text;
	}

	public static AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER; //set the type
		return (AlphaComposite.getInstance(type,alpha)); //return the alphaComposite based on the float value and the type
	}
}
