package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import util.Token;

public class Main {
	
	private static String mInput;
	
	public static void main(String[] args) throws IOException
	{	
		while (true) {
			System.out.println("CL-Lisp > ");
			mInput = read();
			if (matchParentheses(mInput)) {
				Tokenizer tokenizer = new Tokenizer(mInput);
				ArrayList<Token> tokenizedInput = tokenizer.allTokens();
				Parser parser = new Parser(tokenizedInput);
				ExpressionNode syntaxTree = parser.generateSyntaxTree();
				Evaluator evaluator = new Evaluator(syntaxTree);
				System.out.println(evaluator.evaluateTree());
				mInput = null;
				continue;
				}	
			}
		}
	
	public static String read() {
		if (mInput == null) {
			Scanner in = new Scanner(System.in);
			mInput = in.nextLine();
			}
		while (mInput != null && !(matchParentheses(mInput))) {
			Scanner in = new Scanner(System.in);
			mInput+= " " + in.nextLine();
			}
		return mInput;
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
