package analysis;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Main {
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("CL-Lisp >");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		if (matchParentheses(input)) {
			 
		}
		
		
	}
	
	
	public static boolean matchParentheses(String input) {
		Stack parenStack = new Stack();
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
