package analysis;

import java.util.ArrayList;

import util.Token;

public class Operator {
	
	private AtomNode mOperator;
	private String[] mValidOperators = {"+", "-", "*", "/"};
	
	public Operator(AtomNode operator) {
		mOperator = operator;
	}
	
	/**
	 * Check if operator in atom node is a valid operator
	 * @return
	 */
	public boolean isValidOperator() {
		String op = mOperator.getToken().getValue();
		for (String validop : mValidOperators) {
			if (op.equals(validop)) {
				return true;
			}
		}
		return false;	
	}
	
	public int sum(int a, int b) {
		return a + b;
	}
	
	public int difference(int a, int b) {
		return a - b;
	}
	
	public int product(int a, int b) {
		return a * b;
	}
	
	public int quotient(int a, int b) {
		if (b != 0) {
			return a / b;
		} else {
		throw new IllegalArgumentException("Argument 'divisor' is 0");
		}	
	}
}
