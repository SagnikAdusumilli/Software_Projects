import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class FunctionSplitterTest {

	private HashMap<String, Integer> rankings = new HashMap<>();
	private ArrayList<String> singleOperationFunction = new ArrayList<>();

	private String decimalForm = "-?[0-9.]*";

	public FunctionSplitterTest() {

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

		singleOperationFunction.add("sin");
		singleOperationFunction.add("cos");
		singleOperationFunction.add("tan");
		singleOperationFunction.add("root");

	}

	public ArrayList<String> evaluate(String input) {

		Stack<String> operations = new Stack<>();
		ArrayList<String> output = new ArrayList<>();
		String token = "";
		String decimalForm = "[0-9.]*";

		for (int i = 0; i < input.length(); i++) {

			token = String.valueOf(input.charAt(i));

			if (!token.matches(decimalForm) && !token.equals("x")) {

				if (token.equals("s")) {
					token = "sin";
					i += 2;

				} else if (token.equals("c")) {
					token = "cos";
					i += 2;

				} else if (token.equals("t")) {
					token = "tan";
					i += 2;
				} else if (token.equals("r")) {
					token = "root";
					i += 3;
				}

				if (operations.isEmpty() || token.equals("(")) {

					operations.push(token);

				} else if (token.equals(")")) {

					while (operations.size() > 0 && !operations.peek().equals("(")) {
						output.add(operations.pop());
					}
					operations.pop();

				} else if (rankings.get(operations.peek()) > rankings.get(token)) {

					operations.push(token);

				} else if (rankings.get(operations.peek()) <= rankings.get(token)) {

					while (operations.size() > 0 && rankings.get(operations.peek()) <= rankings.get(token)) {
						output.add(operations.pop());
					}
					operations.push(token);
				}

			} else {
				while (i + 1 < input.length() && !token.equals("x")
						&& String.valueOf(input.charAt(i + 1)).matches("[0-9]")) {
					i++;
					token += String.valueOf(input.charAt(i));
				}

				output.add(token);
			}

		}

		while (operations.size() > 0) {
			output.add(operations.pop());
		}
		System.out.println(output);
		return output;
	}

	public double calculator(ArrayList<String> input) {

		ArrayList<String> marked = new ArrayList<String>();

		double result = 0;
		double operand1;
		double operand2;

		for (int i = 0; i < input.size() - 1; i++) {

			/*
			 * the current element should be a number if the next element is not
			 * sin or cos, then the next element should be a number the element
			 * after that should be a operator
			 */

			if (input.get(i).matches(decimalForm) && singleOperationFunction.contains(input.get(i + 1))) {

				operand1 = Double.valueOf(input.get(i));

				switch (input.get(i + 1)) {
				case "sin":
					result = Math.sin(operand1);
					break;
				case "cos":
					result = Math.sin(operand1);
					break;
				case "tan":
					result = Math.tan(operand1);
					break;
				case "root":
					result = Math.sqrt(operand1);
					break;
				}

				marked.add(input.get(i + 1));
				input.set(i, "" + result);

			} else if (i < input.size() - 2 && input.get(i).matches(decimalForm)
					&& input.get(i + 1).matches(decimalForm) && !input.get(i + 2).matches(decimalForm)) {

				operand1 = Double.valueOf(input.get(i));
				operand2 = Double.valueOf(input.get(i + 1));

				switch (input.get(i + 2)) {
				case "^":
					result = Math.pow(operand1, operand2);
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

				marked.add(input.get(i + 1));
				marked.add(input.get(i + 2));
				input.set(i, "" + result);
			}
		}

		for (String s : marked) {
			input.remove(s);
		}
		// System.out.println(input);

		if (input.size() == 1) {
			return Double.valueOf(input.get(0));
		} else {
			return calculator(input);
		}
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		FunctionSplitterTest splitter = new FunctionSplitterTest();
		String in = input.nextLine();
		while (!in.equals("e")) {

			ArrayList<String> function = splitter.evaluate(in);

			ArrayList<String> list = new ArrayList<>(function);

			System.out.println("calculating value for x=5");
			
			for(int i=0; i<list.size(); i++){
				if(list.get(i).equals("x")){
					list.set(i, "5");
				}
			}
			System.out.println(list);

			System.out.println("value " + splitter.calculator(list));

			in = input.nextLine();
		}
	}
}