package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import util.Token;

public class Main {
	
	private static String mInput;
	private static boolean mRunner = true;
	
	public static void main(String[] args) throws IOException, EvalException
	{	
		Tokenizer tokenizer = new Tokenizer();
		Parser parser = new Parser();
		Eval evaluator = new Eval();
		//Evaluator evaluator = new Evaluator();
		//Scanner scanner = new Scanner(System.in);
		while (mRunner) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("CL-Lisp > ");
			mInput = read(scanner);
			if (matchParentheses(mInput)) {
				tokenizer.setInput(mInput);
				ArrayList<Token> tokenizedInput = tokenizer.allTokens();
				parser.setTokenList(tokenizedInput);
				ExpressionNode syntaxTree = parser.generateSyntaxTree();
				evaluator.setTree(syntaxTree);
				try {
					System.out.println(evaluator.evaluateTree());
				}catch(EvalException e) {
					System.out.println(e);
				}
				
				mInput = null;
				continue;
				}	
			}
		}
	
	public static String read(Scanner scanner) {
		if (mInput == null) {
			mInput = scanner.nextLine();
			}
		while (mInput != null && !(matchParentheses(mInput))) {
			mInput+= " " + scanner.nextLine();
			}
		return mInput;
		}
	
	public static void endLoop() {
		mRunner = false;
	}
		
	public static boolean matchParentheses(String input) {
		Stack parenStack = new Stack();
		if (input == null) {
			return false;
		}
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '(') {
				parenStack.push(input.charAt(i));
			} else if (input.charAt(i) == ')' && !(parenStack.isEmpty())) {
				parenStack.pop();
			}
		}
		if (parenStack.isEmpty()) {
			return true;
		}
		return false;
	}
	

}
