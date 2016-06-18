import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

/**
 * The main widow of the program
 * @author Sagnik
 *
 */
public class MainInterface extends JFrame {

	// the graph will display the function
	private Graph graph;

	// the UI to interact with the graph
	private GraphInterface graphInterface;

	// analzyer to read the function
	private Analyzer analyzer;

	public MainInterface() {
		// setting title
		setTitle("Graphing Calculator");

		// setting dimensions
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		// setting exit operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// setting the layout
		setLayout(new BorderLayout());

		// getting the screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		// adding graph
		graph = new Graph(width, height / 2);

		// creating the graph
		HBox layout = new HBox(graph);
		layout.setStyle("-fx-background-color: white");

		// creating the panel to hold the graph
		JFXPanel graphPanel = new JFXPanel();

		// Inserting graph into the panel
		Scene scene = new Scene(layout);
		graphPanel.setScene(scene);

		// adding graph panel the frame
		add(graphPanel, BorderLayout.CENTER);

		// adding the graph controller
		analyzer = new Analyzer();
		graphInterface = new GraphInterface(graph, analyzer, width, height / 2);
		add(graphInterface, BorderLayout.SOUTH);

		setVisible(true);

	}

	public static void main(String[] args) {
		new MainInterface();
	}
}
