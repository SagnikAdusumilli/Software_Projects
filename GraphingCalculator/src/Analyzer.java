import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * 
 * @author Sagnik This class takes in a String containing operations in infix
 *         notations and gives out a String in prefix notations
 */
public class Analyzer {
	
	// stores the raking of the operators
	private HashMap<String, Integer> rankings = new HashMap<>();
	
	// stores the operations that require single operands
	private ArrayList<String> singleOperationFunction = new ArrayList<>();
	
	// stores the place that it rounds the number to
	private  final int PLACES = 2;
	
	// stores the decimal format
	private final String DECIMAL_FORM = "-?\\d*\\.?\\d+(E-?\\d+)?";
	
	/**
	 * insert the rankings and store the single operand operators
	 */
	public Analyzer() {
		
		// inserting the rankings
		rankings.put("sin", 1);
		rankings.put("cos", 1);
		rankings.put("tan", 1);
		rankings.put("root", 1);
		rankings.put("^", 2);
		rankings.put("/", 3);
		rankings.put("*", 3);
		rankings.put("+", 4);
		rankings.put("-", 4);
		rankings.put("(", 5);
		
		// storing the single operand operators
		singleOperationFunction.add("sin");
		singleOperationFunction.add("cos");
		singleOperationFunction.add("tan");
		singleOperationFunction.add("root");

	}
	
	/**
	 *  takes in a list containing operations in infix notation gives out a containing operations infix notation 
	 * @param input
	 * @return
	 */
	public ArrayList<String> evaluate(ArrayList<String> input) {
		
		// holds the operations
		Stack<String> operations = new Stack<>();
		// this list will hold the operations in infix notations
		ArrayList<String> output = new ArrayList<>();

		for (String token : input) {
			
			// if the token is not a decimal and not a variable, it must a operator
			if (!token.matches(DECIMAL_FORM) && !token.matches("x")) {
				
				// if the stack it emplty or if the token is (
				if (operations.isEmpty() || token.equals("(")) {
					
					// push the operator into the stack
					operations.push(token);

				} else if (token.equals(")")) { // if the ) is found

					while (operations.size() > 0 && !operations.peek().equals("(")) {
						
						// push all the operations inside the bracket into the output list
						output.add(operations.pop());
					}
					// remove the (
					operations.pop();

				} else if (rankings.get(operations.peek()) > rankings.get(token)) {
					
					// if the current taken has higher rank, push it into the stack
					operations.push(token);

				} else if (rankings.get(operations.peek()) <= rankings.get(token)) {
					
					// if the current taken has lower rank, remove all the higher ranking operations before pushing it in
					while (operations.size() > 0 && rankings.get(operations.peek()) <= rankings.get(token)) {
						output.add(operations.pop());
					}
					operations.push(token);

				}
			} else {
				// if the token is a number or x then add it the output list
				output.add(token);
			}

		}
		
		// put all the remaining operation into the output list
		while (operations.size() > 0) {
			output.add(operations.pop());
		}
		// return the output list
		return output;
	}
	
	/**
	 *  takes a list in prefix notation and gives the result
	 *  Throws exception if certain operations are impossible
	 * @param input
	 * @param value
	 * @param radians
	 * @return
	 * @throws Exception
	 */
	public double calculator (ArrayList<String> input, double value, boolean radians) throws Exception  {
		
		// marks the itmes that need to be removed from the list
		ArrayList<String> marked = new ArrayList<String>();
		
		// holds the result of the operations
		double result = 0;
		
		// operands
		double operand1;
		double operand2;

		// setting x to value given 
		if (input.contains("x")) {
			input.set(input.indexOf("x"), String.valueOf(value));
		}

		for (int i = 0; i < input.size() - 1; i++) {

			/*
			 * the current element should be a number if the next element is not
			 * sin or cos, then the next element should be a number the element
			 * after that should be a operator
			 */
			
			if (input.get(i).matches(DECIMAL_FORM) && singleOperationFunction.contains(input.get(i + 1))) {
				
				// set the operand 
				operand1 = Double.valueOf(input.get(i));

				switch (input.get(i + 1)) {
				// perform operation accordingly 
				case "sin":
					// check the mode 
					if (radians) {
						result = Math.sin(operand1);
					} else {
						result = Math.sin((Math.PI/180)*operand1);
					}
					break;
				case "cos":
					// check the mode 
					if (radians) {
						result = Math.cos(operand1);
					} else {
						result = Math.cos((Math.PI/180)*operand1);
					}
					break;
				case "tan":
					// check the mode 
					if (radians) {
						result = Math.tan(operand1);
					} else {
						result = Math.tan((Math.PI/180)*operand1);
					}
					break;
				case "root":
						result = Math.sqrt(operand1);
						if(Double.isNaN(result)){
							throw new Exception();
						}
					break;
				}
				
				// mark the operator
				marked.add(input.get(i + 1));
				// put the result into the list
				input.set(i, "" + result);

			} else if (i < input.size() - 2 && input.get(i).matches(DECIMAL_FORM) && input.get(i + 1).matches(DECIMAL_FORM) && !input.get(i + 2).matches(DECIMAL_FORM)&& !singleOperationFunction.contains(input.get(i + 2))) {
				// the there is a double operator in between 2 numbers
				
				// set the operands 
				operand1 = Double.valueOf(input.get(i));
				operand2 = Double.valueOf(input.get(i + 1));

				switch (input.get(i + 2)) {
				// perform operation accordingly
				case "^":
					result = Math.pow(Math.abs(operand1), operand2);
					if(operand1<0){
						result*= -1;
					}
					break;
				case "*":
					result = operand1 * operand2;
					break;
				case "/":
					result = operand1 / operand2;
					break;
				case "+":
					result = operand1 + operand2;
					break;
				case "-":
					result = operand1 - operand2;
					break;

				}
				
				// mark the operator and the second number
				marked.add(input.get(i + 1));
				marked.add(input.get(i + 2));
				
				// put the result into the list
				input.set(i, "" + result);
			}
		}

		for (String s : marked) {
			
			// remove all the marked items
			input.remove(s);
		}
		if (input.size() == 1) {
			// if only one value is left return the value
			return Double.valueOf(input.get(0));
		} else {
			// repeat process till only one value is left
			return calculator(input, value, radians);
		}
	}
}
