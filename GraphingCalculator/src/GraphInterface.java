import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This is the interface that user uses to control the graph
 * 
 * @author Sagnik
 *
 */
public class GraphInterface extends JFXPanel implements EventHandler<KeyEvent> {

	// these buttons can be can be used to construct the function
	private Button[][] calculatorBtns;

	// this the text area that displays information about the function
	private TextArea infoDisplay;

	// this is the graph that the user will control
	private Graph graph;

	// this is String that holds the funtion that the user creates
	private String function;

	// this the textfield where user can type in the function
	private TextField functionInputFeild;

	// radio buttons that control of the mode (radians or degree)
	private RadioButton degreeBtn;
	private RadioButton radianBtn;

	// the symbol for pi will be used often
	private final String PI = "\u03C0";

	// analyzer is used to split and calculate the value of the function entered
	// by the user
	private Analyzer analyzer;

	// holds the split function containing the order in prefix notations
	private ArrayList<String> functionList;

	// holds the values for the increasing and decreasing intervals
	private ArrayList<String> increaseInterval = new ArrayList<>();
	private ArrayList<String> decreaseInterval = new ArrayList<>();

	// controls the y-interval
	TextField startY;
	TextField endY;

	// controls the x-interval
	TextField startX;
	TextField endX;

	// controls the scales
	TextField yScale;
	TextField xScale;

	// the y-intercept point
	Point yIntercept = new Point(0, 0);

	/**
	 * Initializes the global variables and creates the GUI
	 * 
	 * @param graph
	 * @param analyzer
	 * @param width
	 * @param height
	 */
	public GraphInterface(Graph graph, Analyzer analyzer, int width, int height) {
		// Initializing the graph and the analyzer
		this.graph = graph;
		this.analyzer = analyzer;

		// setting the dimension for the controller
		setMinimumSize(new Dimension(width, height));

		// this is the main layout that contains all the GUI component
		VBox layout = new VBox();
		// adding key listener to the layout
		layout.setOnKeyPressed(this);

		// sublayout contains all the GUI component in an horizontally arranged
		// manner
		HBox subLayout = new HBox();
		subLayout.setAlignment(Pos.CENTER);
		subLayout.getChildren().addAll(createGraphController(), createFunctionController(), createInfoDisplay(),
				movementController());

		layout.getChildren().addAll(functionInputInterface(), subLayout);

		// adding the main layout to the panel
		Scene mainScene = new Scene(layout);
		setScene(mainScene);

	}

	/**
	 * 
	 * this is the text field in which user enters the function
	 * 
	 * @return
	 */
	public HBox functionInputInterface() {
		// contains the textfield
		HBox hLayout = new HBox();

		// Aligning the text feild
		hLayout.setAlignment(Pos.CENTER);

		// creating padding
		hLayout.setPadding(new Insets(5, 5, 5, 5));

		// setting spacing between the elements
		hLayout.setSpacing(5);

		// creating the function input field and the graph
		Label functionLabel = new Label("f(x): ");
		functionInputFeild = new TextField();
		functionInputFeild.setPromptText("Enter the function here.");
		functionInputFeild.setMinWidth(600);

		// this button will clear the field
		Button clearBtn = new Button("clear");

		// adding functionality to the button
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// clearing the input field
				functionInputFeild.setText("");
				functionInputFeild.requestFocus();

				// clearing the graph
				graph.clear();

			}
		});

		// adding all the components to the layout
		hLayout.getChildren().addAll(functionLabel,functionInputFeild, clearBtn);

		// returns the finished layout
		return hLayout;

	}

	/**
	 * creates the various componets that allow the user the control the
	 * appearance of the graph
	 * 
	 * @return
	 */
	public VBox createGraphController() {

		// the main layout that contains all the controller components
		VBox controllerLayout = new VBox();
		controllerLayout.setSpacing(5);
		controllerLayout.setPadding(new Insets(20, 20, 10, 0));

		// creating the y-scale controller
		HBox yScaleLayout = new HBox();

		// label
		Label yScaleLabel = new Label("Y-scale: ");

		// button the decrease the y-scale
		Button decreaseYScaleBtn = new Button("-");

		// textfeild that controls the y-scale
		yScale = new TextField("" + graph.getyScale());
		yScale.setPrefColumnCount(4);

		// button to increase the y-scale
		Button increaseYScaleBtn = new Button("+");

		// adding the components to the y-scale controller
		yScaleLayout.getChildren().addAll(yScaleLabel, decreaseYScaleBtn, yScale, increaseYScaleBtn);

		// creating the x-scale controller
		HBox xScaleLayout = new HBox();

		// label
		Label xScaleLabel = new Label("X-scale: ");

		// button the decrease the x-scale
		Button decreaseXScaleBtn = new Button("-");

		// textfeild that controls the x-scale
		xScale = new TextField("" + graph.getxScale());
		xScale.setPrefColumnCount(4);

		// button the increase the x-scale
		Button increaseXScaleBtn = new Button("+");

		// adding all the components to the x-scale controller
		xScaleLayout.getChildren().addAll(xScaleLabel, decreaseXScaleBtn, xScale, increaseXScaleBtn);

		// adding functionality to the x-scale buttons
		increaseXScaleBtn.setOnAction(
				event -> setScales(Double.valueOf(xScale.getText()) + 1, Double.valueOf(yScale.getText())));
		decreaseXScaleBtn.setOnAction(
				event -> setScales(Double.valueOf(xScale.getText()) - 1, Double.valueOf(yScale.getText())));

		// adding functionality to the y-scale buttons
		increaseYScaleBtn.setOnAction(
				event -> setScales(Double.valueOf(xScale.getText()), Double.valueOf(yScale.getText()) + 1));
		decreaseYScaleBtn.setOnAction(
				event -> setScales(Double.valueOf(xScale.getText()), Double.valueOf(yScale.getText()) - 1));

		// creating the y-interval controller
		VBox yIntervalLayout = new VBox();
		yIntervalLayout.setSpacing(5);
		yIntervalLayout.setPadding(new Insets(10, 0, 10, 0));

		// added spacing to the label to align properly
		Label titleY = new Label("        Y-interval");

		// layout to control the starting value
		HBox startYLayout = new HBox();

		// label to marking the starting point
		Label startYLabel = new Label("From: ");

		// button to decrease the starting point
		Button decreaseStartY = new Button("-");

		// field that displays the starting point
		startY = new TextField("" + graph.getMinY());
		startY.setPrefColumnCount(4);

		// button to increase the starting point
		Button increaseStartY = new Button("+");

		// adding all the components to the starting controller
		startYLayout.getChildren().addAll(startYLabel, decreaseStartY, startY, increaseStartY);

		// layout to control the end value
		HBox endYLayout = new HBox();

		// added spacing to the label to align properly
		Label endYLabel = new Label("    To: ");

		// button to decrease the starting point
		Button decreaseEndY = new Button("-");

		// field that displays the starting point
		endY = new TextField("" + graph.getMaxY());
		endY.setPrefColumnCount(4);

		// button to decrease the starting point
		Button increaseEndY = new Button("+");

		// adding all the components to the end controller
		endYLayout.getChildren().addAll(endYLabel, decreaseEndY, endY, increaseEndY);

		// adding the title, starting controller and ending controller to the
		// main y-interval controller
		yIntervalLayout.getChildren().addAll(titleY, startYLayout, endYLayout);

		// creating the x-interval controller
		VBox xIntervalLayout = new VBox();
		xIntervalLayout.setSpacing(5);
		xIntervalLayout.setPadding(new Insets(10, 0, 10, 0));

		// added space for alignment
		Label titleX = new Label("        X-interval");

		// layout to control the starting value
		HBox startXLayout = new HBox();

		// added spacing to the label to align properly
		Label startXLabel = new Label("From: ");

		// button to decrease the starting point
		Button decreaseStartX = new Button("-");

		// displays the starting point
		startX = new TextField("" + graph.getMinX());
		startX.setPrefColumnCount(4);

		// button to increase the starting point
		Button increaseStartX = new Button("+");

		// adding all the components to the starting controller
		startXLayout.getChildren().addAll(startXLabel, decreaseStartX, startX, increaseStartX);

		// creating the x-interval controller
		HBox endXLayout = new HBox();

		// added space for alignment
		Label endXLabel = new Label("    To: ");

		// button to decrease the ending point
		Button decreaseEndX = new Button("-");

		// displays the ending point
		endX = new TextField("" + graph.getMaxX());
		endX.setPrefColumnCount(4);

		// button to decrease the ending point
		Button increaseEndX = new Button("+");

		// adding all the components to the x-ending controller
		endXLayout.getChildren().addAll(endXLabel, decreaseEndX, endX, increaseEndX);

		// adding both the intervals controller to main x-controller
		xIntervalLayout.getChildren().addAll(titleX, startXLayout, endXLayout);

		// adding functionality to the to x-controller buttons
		increaseStartX.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()) + 1,
				Double.valueOf(endX.getText()), Double.valueOf(startY.getText()), Double.valueOf(endY.getText())));

		decreaseStartX.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()) - 1,
				Double.valueOf(endX.getText()), Double.valueOf(startY.getText()), Double.valueOf(endY.getText())));

		decreaseEndX.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()),
				Double.valueOf(endX.getText()) - 1, Double.valueOf(startY.getText()), Double.valueOf(endY.getText())));

		increaseEndX.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()),
				Double.valueOf(endX.getText()) + 1, Double.valueOf(startY.getText()), Double.valueOf(endY.getText())));

		// adding functionality to the to y-controller buttons
		increaseStartY.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()), Double.valueOf(endX.getText()),
				Double.valueOf(startY.getText()) + 1, Double.valueOf(endY.getText())));

		decreaseStartY.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()), Double.valueOf(endX.getText()),
				Double.valueOf(startY.getText()) - 1, Double.valueOf(endY.getText())));

		decreaseEndY.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()), Double.valueOf(endX.getText()),
				Double.valueOf(startY.getText()), Double.valueOf(endY.getText()) - 1));

		increaseEndY.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()), Double.valueOf(endX.getText()),
				Double.valueOf(startY.getText()), Double.valueOf(endY.getText()) + 1));

		// add everything to the final layout
		controllerLayout.getChildren().addAll(yScaleLayout, xScaleLayout, yIntervalLayout, xIntervalLayout);
		return controllerLayout;

	}

	/**
	 * creates arrow keys to shift the graph creates the magnifying button to
	 * zoom in and zoom out
	 * 
	 * @return
	 */
	public VBox movementController() {

		// main layout that contains all the components
		VBox mainLayout = new VBox();

		// setting the padding
		mainLayout.setPadding(new Insets(0, 0, 0, 100));

		// creating the side arrows
		Button rightBtn = new Button("\u2192");
		Button leftBtn = new Button("\u2190");

		// this layout will contain all the side arrow buttons
		HBox horizontalShiftLayout = new HBox();

		// setting the spacing between the elements
		horizontalShiftLayout.setSpacing(20);

		// setting the padding
		horizontalShiftLayout.setPadding(new Insets(2, 0, 2, 0));

		// setting the alignment
		horizontalShiftLayout.setAlignment(Pos.CENTER);

		// adding the buttons to the layout
		horizontalShiftLayout.getChildren().addAll(leftBtn, rightBtn);

		// creating the up and down arrows
		Button upBtn = new Button("\u2191");

		// layout that holds the arrow button
		HBox upBtnLayout = new HBox(upBtn);
		upBtnLayout.setAlignment(Pos.CENTER);

		Button downBtn = new Button("\u2193");
		// layout that holds the arrow button
		HBox downBtnLayout = new HBox(downBtn);
		downBtnLayout.setAlignment(Pos.CENTER);

		// adding functionality to the buttons
		upBtn.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()), Double.valueOf(endX.getText()),
				Double.valueOf(startY.getText()) + graph.getyScale(),
				Double.valueOf(endY.getText()) + graph.getyScale()));

		downBtn.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()), Double.valueOf(endX.getText()),
				Double.valueOf(startY.getText()) - graph.getyScale(),
				Double.valueOf(endY.getText()) - graph.getyScale()));

		leftBtn.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()) - graph.getxScale(),
				Double.valueOf(endX.getText()) - graph.getxScale(), Double.valueOf(startY.getText()),
				Double.valueOf(endY.getText())));

		rightBtn.setOnAction(event -> shiftGraph(Double.valueOf(startX.getText()) + graph.getxScale(),
				Double.valueOf(endX.getText()) + graph.getxScale(), Double.valueOf(startY.getText()),
				Double.valueOf(endY.getText())));

		// layout that holds the zoom buttons
		HBox zoomLayout = new HBox();

		// setting the padding
		zoomLayout.setPadding(new Insets(10, 0, 0, 0));

		// setting the alignment
		zoomLayout.setAlignment(Pos.CENTER);

		// this button will make the graph zoom-in
		Button zoomInBtn = new Button("+");
		zoomInBtn.setOnAction(event -> zoom(true));

		// this button will make the graph zoom-out
		Button zoomOutBtn = new Button("-");
		zoomOutBtn.setOnAction(event -> zoom(false));

		// adding the zoom buttons to the zoom layout
		zoomLayout.getChildren().addAll(zoomInBtn, zoomOutBtn);

		// adding all the sub layout to the main layout
		mainLayout.getChildren().addAll(upBtnLayout, horizontalShiftLayout, downBtnLayout, zoomLayout);

		// return the main layout
		return mainLayout;
	}

	/**
	 * this is the calculator like interface that the user can use to form the
	 * function
	 * 
	 * @return
	 */
	public VBox createFunctionController() {

		// the main layout contains all the main components
		VBox mainLayout = new VBox();
		mainLayout.setSpacing(5);

		// raido buttons layout holds that buttons to control the mode (degree
		// or radians)
		HBox radioBtnLayout = new HBox();

		// setting the spacing
		radioBtnLayout.setSpacing(5);
		// setting the padding
		radioBtnLayout.setPadding(new Insets(0, 0, 0, 10));

		// creating the group to Degree and Radian radio button
		ToggleGroup group = new ToggleGroup();

		// creating the degree button
		degreeBtn = new RadioButton("Degree");
		degreeBtn.setSelected(true);
		degreeBtn.setToggleGroup(group);
		radioBtnLayout.getChildren().add(degreeBtn);

		// creating the radian button
		radianBtn = new RadioButton("Radian");
		radianBtn.setToggleGroup(group);
		radioBtnLayout.getChildren().add(radianBtn);

		// grid layouts holds the calculator buttons (values entered in cols and
		// rows)
		GridPane grid = new GridPane();

		// setting the padding
		grid.setPadding(new Insets(10, 10, 10, 10));

		// setting the spacing
		grid.setHgap(5);
		grid.setVgap(5);

		// adding functionality so that the user can enter values in the
		// function when the focus is on the grid
		grid.setOnKeyTyped(event -> functionInputFeild.appendText(event.getCharacter()));

		// creating the 8x3 btn array
		calculatorBtns = new Button[8][3];

		// creating sin button
		calculatorBtns[0][0] = new Button("sin");
		calculatorBtns[0][0].setOnAction(event -> functionInputFeild.appendText("sin("));

		// creating cos button
		calculatorBtns[0][1] = new Button("cos");
		calculatorBtns[0][1].setOnAction(event -> functionInputFeild.appendText("cos("));

		// creating trig button
		calculatorBtns[0][2] = new Button("tan");
		calculatorBtns[0][2].setOnAction(event -> functionInputFeild.appendText("tan("));

		// creating the power button
		calculatorBtns[1][0] = new Button("^");
		calculatorBtns[1][0].setOnAction(event -> functionInputFeild.appendText("^"));

		// creating the power root button
		calculatorBtns[1][1] = new Button("\u221A");
		calculatorBtns[1][1].setOnAction(event -> functionInputFeild.appendText("root("));

		// creating the * button
		calculatorBtns[1][2] = new Button("*");
		calculatorBtns[1][2].setOnAction(event -> functionInputFeild.appendText("*"));

		// creating the / button
		calculatorBtns[2][0] = new Button("/");
		calculatorBtns[2][0].setOnAction(event -> functionInputFeild.appendText("/"));

		// creating the + button
		calculatorBtns[2][1] = new Button("+");
		calculatorBtns[2][1].setOnAction(event -> functionInputFeild.appendText("+"));

		// creating the - button
		calculatorBtns[2][2] = new Button("-");
		calculatorBtns[2][2].setOnAction(event -> functionInputFeild.appendText("-"));

		// creating the pi button
		calculatorBtns[3][0] = new Button(PI);
		calculatorBtns[3][0].setOnAction(event -> functionInputFeild.appendText(PI));

		// creating the ( button
		calculatorBtns[3][1] = new Button("(");
		calculatorBtns[3][1].setOnAction(event -> functionInputFeild.appendText("("));

		// creating the ) button
		calculatorBtns[3][2] = new Button(")");
		calculatorBtns[3][2].setOnAction(event -> functionInputFeild.appendText(")"));

		// creating the number pads
		for (int y = 4; y < 7; y++) {
			for (int x = 0; x < 3; x++) {
				int number = (11 - (4 + 3 * (y - 4)) + x);

				// labeling the buttons
				calculatorBtns[y][x] = new Button("" + number);
				// adding functionality to the buttons
				calculatorBtns[y][x].setOnAction(event -> functionInputFeild.appendText(number + ""));
			}
		}

		// creating the 0 button
		calculatorBtns[7][0] = new Button("0 ");
		calculatorBtns[7][0].setOnAction(event -> functionInputFeild.appendText("0"));

		// creating the . button
		calculatorBtns[7][1] = new Button(".");
		calculatorBtns[7][1].setOnAction(event -> functionInputFeild.appendText("."));

		// creating the go button

		calculatorBtns[7][2] = new Button("\uD835\uDC65");
		calculatorBtns[7][2].setOnAction(event -> functionInputFeild.appendText("x"));

		// adding all the buttons to grid
		for (int rows = 0; rows < 8; rows++) {

			for (int cols = 0; cols < 3; cols++) {

				calculatorBtns[rows][cols].setMaxWidth(Double.MAX_VALUE);
				calculatorBtns[rows][cols].setPrefWidth(50);
				grid.add(calculatorBtns[rows][cols], cols, rows);
			}
		}

		// adding the radio buttons and grid buttons to the main layout
		mainLayout.getChildren().addAll(radioBtnLayout, grid);

		// returning the main layout
		return mainLayout;

	}

	/**
	 * creates the text area that displays information about the funtion
	 * 
	 * @return
	 */
	public BorderPane createInfoDisplay() {
		// holds the display
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(5, 5, 5, 5));

		// displays the information
		infoDisplay = new TextArea();

		// giving message to the user
		infoDisplay.setPromptText("Press Enter to draw funtion");

		// making the display scrollable
		infoDisplay.wrapTextProperty();

		// adding display to the layout
		borderPane.setCenter(infoDisplay);

		// returning the layout
		return borderPane;
	}

	// sends the function to the analyzer and then draws it onto the graph
	public void sendFunction() {

		// clear any existing function
		graph.clear();

		// get the text from the input field
		function = functionInputFeild.getText();

		// format the function so it can be split successfully
		checkFunction();

		// breaks the function down by infix notation
		ArrayList<String> list = new ArrayList<String>();

		// holds each part of the function
		String token = "";

		// keeps track whether a number was found
		boolean enteredNumberLoop = false;

		for (int i = 0; i < function.length(); i++) {

			// if the first number is -ve, start the token with -
			if ((i == 0||(i>0&&String.valueOf(function.charAt(i-1)).matches("[^0-9]"))) && String.valueOf(function.charAt(i)).equals("-")) {
				token = "-";
				i++;
			} else {
				// reset the token
				token = "";
			}
			enteredNumberLoop = false;

			// while the current character is a digit or a period, then it is a
			// part of a decimal number
			while ((String.valueOf(function.charAt(i)).matches("[0-9]")
					|| String.valueOf(function.charAt(i)).matches("\\."))) {

				// mark that the number loop was entered to put the number to
				// gether
				enteredNumberLoop = true;

				// add the current charater to the decimal number
				token += String.valueOf(function.charAt(i));

				// increment the character count
				i++;

				// if the end of function is reached
				if (i == function.length()) {
					// add number to list
					list.add(token);

					// pass list to analyzer and get the prefix notation
					functionList = analyzer.evaluate(list);

					// draw the function
					drawFunction();
					return;
				}
			}

			if (enteredNumberLoop) {
				// if the token entered number loop add token
				list.add(token);
			}

			// if the token is not a number or a period
			if ((!String.valueOf(function.charAt(i)).matches("[0-9]")
					&& !String.valueOf(function.charAt(i)).matches("\\."))) {

				switch (String.valueOf(function.charAt(i))) {
				// check for possibilities of token
				case "s":
					token = "sin";
					i += 2;
					break;
				case "r":
					token = "root";
					i += 3;
					break;
				case "t":
					token = "tan";
					i += 2;
					break;
				case "c":
					token = "cos";
					i += 2;
					break;
				default:
					// by default it should be a operation
					token = String.valueOf(function.charAt(i));
				}
				// add token to the list
				list.add(token);
			}

		}

		// get the function in infix notation
		functionList = analyzer.evaluate(list);

		// draw the function
		drawFunction();

	}

	/**
	 * draws the function based on the min and max value of the graph
	 */
	public void drawFunction() {

		// don't draw anything if the function is empty
		if (functionInputFeild.getText().equals("")) {
			return;
		}
		// clear all the previous data
		increaseInterval.clear();
		decreaseInterval.clear();
		infoDisplay.clear();

		// markers to point the differt stages of an interval
		boolean intervalStart = false;
		boolean middleInterval = false;
		boolean endInterval = false;

		// Points to mark the interval
		Point start = new Point(0, 0);
		Point middle = new Point(0, 0);
		Point end = new Point(0, 0);

		for (double x = graph.getMinX(); x <= graph.getMaxX(); x += 0.001) {

			try {

				// the y point to be drawn on the graph
				double y = analyzer.calculator(createCopy(functionList), x, radianBtn.isSelected());

				// draw the point on the graph
				graph.drawPoint(x, y);

				if (!intervalStart) {

					// if the interval did not start, marking the staring
					start.setX(x);
					start.setY(y);
					intervalStart = true;

				} else if (!middleInterval && !endInterval) {

					// marking the middle point next
					middle.setX(x);
					middle.setY(y);
					middleInterval = true;

				} else if (!endInterval) {

					// lastly marking the end point of the curve
					end.setX(x);
					end.setY(y);
					endInterval = true;

				} else if (intervalStart && middleInterval && endInterval) {

					// keep moving the middle and end points till the end of
					// curve is reached
					middle.setX(end.getX());
					middle.setY(end.getY());

					// setting the end point to the current
					end.setX(x);
					end.setY(y);

					// concave down end of increasing interval
					if (middle.getY() > start.getY() && end.getY() < middle.getY()) {

						increaseInterval.add(start + " to " + middle);

						// reset the interval
						intervalStart = false;
						endInterval = false;
						middleInterval = false;

					} else if (middle.getY() < start.getY() && end.getY() > middle.getY()) { // concave up end of decreasing interval

						decreaseInterval.add(start + " to " + middle);

						// reset the interval
						intervalStart = false;
						endInterval = false;
						middleInterval = false;
					}

				}

			} catch (Exception e) {
				// cannot draw points
				// not printing anything to make progress faster
			}

		}
		
		// if there is x-axis find the y-intercept
		if (graph.getMinX() <= 0 && graph.getMaxX() >= 0) {

			yIntercept.setX(0);
			try {
				// displaying the y-intercept
				yIntercept.setY(analyzer.calculator(createCopy(functionList), 0, radianBtn.isSelected()));
				infoDisplay.appendText("\n found y-intercpet at: " + yIntercept);
			} catch (Exception e) {}
		}

		// if the the interval was a straight line, then check the the 2 end points
		if (increaseInterval.size() == 0 && (end.getY() > start.getY())) {
			increaseInterval.add(start + " to " + end);
	 	}

		// display all the increasing intervals
		if (increaseInterval.size() > 0) {
			infoDisplay.appendText("\n increasing intervals: " + increaseInterval);
		}

		// if the the interval was a straight line, then check the the 2 end
		// points
		if (decreaseInterval.size() == 0 && (end.getY() < start.getY())) {
			decreaseInterval.add(start + " to " + end);
		}

		// display all the decreasing intervals
		if (decreaseInterval.size() > 0) {
			infoDisplay.appendText("\n decreasing intervals " + decreaseInterval);
		}

	}

	/**
	 * This method checks the function and insert * where it is required and
	 * converts pi symbol to a number
	 * 
	 * @param list
	 */
	public void checkFunction() {

		// inserting * between x and single operation function
		function = function.replaceAll("(x)([a-z])", "$1*$2");

		// inserting * for number and (
		function = function.replaceAll("(\\d+)(\\()", "$1*$2");

		// Interning * between coefficient
		function = function.replaceAll("(\\d+)([a-z])", "$1*$2");

		// Interning * between number and pi symbol
		function = function.replaceAll("(\\d+)(" + PI + ")", "$1*$2");

		// inserting * for pi and operations or x
		function = function.replaceAll("(" + PI + ")([a-z])", "$1*$2");

		// inserting * for pi and operation or x if they are in reverse order
		function = function.replaceAll("([a-z])(" + PI + ")", "$1*$2");

		// inserting * between )(
		function = function.replaceAll("(\\))(\\()", "$1*$2");

		// inserting * between ) single operation function
		function = function.replaceAll("(\\))([a-z])", "$1*$2");

		// replace all the pi with Math.PI
		function = function.replaceAll(PI, "" + Math.PI);

		// if the function starts with -( then insert 1 in the middle
		if (String.valueOf(function.charAt(0)).equals("-")
				&& (String.valueOf(function.charAt(1)).equals("x") || String.valueOf(function.charAt(1)).equals("("))) {

			StringBuilder temp = new StringBuilder(function);
			temp.insert(1, '1');
			temp.insert(2, "*");

			function = temp.toString();
		}
	}

	/**
	 * returns a deep copy of an arrayList
	 * 
	 * @param list
	 * @return
	 */
	public ArrayList<String> createCopy(ArrayList<String> list) {

		ArrayList<String> copy = new ArrayList<String>();

		// copying each element of the list to the copy list
		for (String s : list) {
			copy.add(s);
		}

		// returning the copied list
		return copy;
	}

	/**
	 * shifts the graph and re-draws it
	 * 
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 */
	public void shiftGraph(double minX, double maxX, double minY, double maxY) {

		// setting the new intervals
		graph.setMinX(minX);
		graph.setMaxX(maxX);

		// setting the y-intervals
		graph.setMinY(minY);
		graph.setMaxY(maxY);

		// updating the information display for x-interval
		startX.setText("" + minX);
		endX.setText("" + maxX);

		// updating the information display for y-interval
		startY.setText("" + minY);
		endY.setText("" + maxY);

		// clear graph and re-draw
		graph.clear();
		drawFunction();

	}

	/**
	 * changes the scale of the graph and re-draws it
	 * 
	 * @param xScale
	 * @param yScale
	 */
	public void setScales(double xScale, double yScale) {

		if (xScale > 0) {
			// sets the new scale if the entry is valid
			this.xScale.setText("" + xScale);
			graph.setxScale(xScale);
		} else {
			// keeps the old scale if the entry is invalid
			this.xScale.setText("" + graph.getxScale());
		}

		if (yScale > 0) {
			// sets the new scale if the entry is valid
			this.yScale.setText("" + yScale);
			graph.setyScale(yScale);
		} else {
			// keeps the old scale if the entry is invalid
			this.yScale.setText("" + graph.getyScale());
		}

		// clear and re-draw the graph
		graph.clear();
		drawFunction();
	}

	/**
	 * zooming the graph
	 * 
	 * @param in
	 */
	public void zoom(boolean in) {

		if (in) {
			/**
			 * zooming in by increasing the scale
			 */
			graph.setScaleX(graph.getScaleX() + 0.1);
			graph.setScaleY(graph.getScaleY() + 0.1);
		} else {
			/**
			 * zooming out by decreasing the scale
			 */
			graph.setScaleX(graph.getScaleX() - 0.1);
			graph.setScaleY(graph.getScaleY() - 0.1);
		}

	}

	@Override
	public void handle(KeyEvent event) {
		// TODO Auto-generated method stub

		// if the user presses enter the graph is drawn
		if (event.getCode().equals(KeyCode.ENTER)) {

			// setting the scale based on the scale fields
			setScales(Double.valueOf(xScale.getText()), Double.valueOf(yScale.getText()));

			// setting the intervals based on the interval fields
			shiftGraph(Double.valueOf(startX.getText()), Double.valueOf(endX.getText()),
					Double.valueOf(startY.getText()), Double.valueOf(endY.getText()));

			// drawing the function if the function field is not empty
			if (!functionInputFeild.getText().equals("")) {
				sendFunction();
			}

		}

	}

}
