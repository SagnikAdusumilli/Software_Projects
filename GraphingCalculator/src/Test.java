import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JFrame;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Test {
	private static TextArea infoDisplay;
	private static String decimalFormat = "[0-9.]*";
	private static final String piSymbol = "\u03C0";

	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");

		JFXPanel panel = new JFXPanel();
		panel.setScene(createScene());
		
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.setSize(600, 600);
//		frame.setVisible(true);
		
		 /* conditions for interning *: number*(, number*root, number*(any trig function), number*pi
		  *  )*( pi*number 
		  */
		
//		String test = "25(21";
		String test = "-";
		
//		System.out.println(Double.valueOf(test));
		
		System.out.println(test.matches("-?\\d*\\.?\\d+(E-?\\d+)?"));
		
		Analyzer a = new Analyzer();
		ArrayList<String> list = new ArrayList<>();
		String[] values = {"5","^","-1"};
		
		for(int i=0; i<values.length; i++){
			list.add(values[i]);
		}
		
		try {
			System.out.println(a.calculator(a.evaluate(list), 1, true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Scene createScene() {
		Text text = new Text("Hello World");
		text.setFont(new Font(24));
		text.setEffect(new Reflection());

		Button btn = new Button("click me");

		// using lambda expressions
		btn.setOnAction(event -> infoDisplay.setText("Button was clicked"));

		BorderPane pane = new BorderPane();
		pane.setCenter(createInfoDisplay());
		pane.setBottom(btn);

		Scene scene = new Scene(pane);

		return scene;
	}

	public static BorderPane createInfoDisplay() {
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(5, 5, 5, 5));

		infoDisplay = new TextArea();
		infoDisplay.setMinSize(300, 300);
		infoDisplay.setText("Information regarding the function will be displayed here");
		borderPane.setCenter(infoDisplay);

		return borderPane;
	}
}
