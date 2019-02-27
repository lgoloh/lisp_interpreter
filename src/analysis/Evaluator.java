package analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import util.Token;
import util.Type;

public class Evaluator {
	
	private ExpressionNode mSyntaxTree;
	private static String[] mValidOperators = {"+", "-", "*", "/", "'", "quote", 
			"list", "cons", "car", "cdr", "listp"};
	
	public Evaluator(ExpressionNode tree) {
		mSyntaxTree = tree;
	}
	
	/**
	 * Evaluates a number node 
	 * TODO Use NumberNode not ExpressionNode
	 * @param node
	 * @return
	 */
	public int evaluateNumber(NumberNode node) {
		return (int) node.getValue();
	}
	
	/**
	 * Evaluates a Symbol; Currently only works for mathematical operations 
	 * TODO Use SymbolNode not ExpressionNode
	 * @param head
	 * @return
	 */
	public Object evaluateSymbol(SymbolNode head) {
		String operator = (String) head.getValue();
		if (isValidOperator(operator)) {
			switch(operator) {
				case "+":
					return new Sum();
				case "-":
					return new Subtract();
				case "*":
					return new Multiply();
				case "/":
					return new Divide();
				//returns the Quote operation object if ' or quote is used	
				case "'"	:
					//expands the quote operator to use the symbol quote
					Token listtkn = new Token(Type.SOE, "(");
					SymbolNode quoteSymbol = new SymbolNode("quote", null);
					head.getnodeList().add(0, quoteSymbol);
					ExpressionNode fullQuote = new ListNode(listtkn, head.getnodeList());
					return  evaluateList(fullQuote);
				case "quote":
					//returns the actual Quote function that has the execution function
					Quote quote = new Quote();
					return quote;
			}
		} else {
			throw new InvalidInputError(head.getValue() + " " + "is not a Valid Symbol");
		}
		return null;
	}
	
	/**
	 * Evaluates a single ListNode
	 * @param listnode
	 * @return
	 */
	public Object evaluateList(ExpressionNode listnode) {
		ArrayList<ExpressionNode> nodes = listnode.getnodeList();
		ExpressionNode head = nodes.get(0);
		if (head instanceof SymbolNode) {
			//Returns an operation object after the Symbol is evaluated
			Object operation = evaluateSymbol((SymbolNode) head);
			//If the operation is a binary operation (+, -, /, *)
			if (operation instanceof BinOperator) {		
				return evaluateMath((BinOperator) operation, nodes);
			} else if (operation instanceof Quote) {
				((Quote) operation).setExpression(listnode);
				Quote quote = (Quote) operation;
				ExpressionNode data = (ExpressionNode) quote.returnData();
				return data.toString();
			}
		} else {
			throw new InvalidInputError(head.getValue() + " " + "is not a Valid Symbol");
			}
			
		return 0;
	} 
	
	
	/**
	 * Evaluates Math operations
	 * TODO handles division by 1 number 
	 * TODO use NumberNodes and SymbolNodes instead of AtomNodes
	 * @param operation
	 * @param nodes
	 * @return
	 */
	public int evaluateMath(BinOperator operation, ArrayList<ExpressionNode> nodes) {
		Stack<Integer> argStack = new Stack<Integer>();
		Stack<Integer> tempStack = new Stack<Integer>();
		if (nodes.size() > 1) {
			for (int i = 1; i < nodes.size(); i++) {
				ExpressionNode curNode = nodes.get(i);
				if (curNode instanceof NumberNode) {
					tempStack.push(evaluateNumber((NumberNode) curNode));
				} else if (curNode instanceof ListNode) {
					tempStack.push((Integer) evaluateList(curNode));
				}
			} 
			argStack = reverseStack(tempStack); 
			while (argStack.size() != 1) {
				operation.setFirstParameter(argStack.pop());
				operation.setSecondParameter(argStack.pop());
				int result = operation.evaluateOperation();
				argStack.push(result);
			}return (argStack.pop());
		} else if (nodes.size() == 1) {
			if (operation instanceof Sum) {
				return 0;
				} else if (operation instanceof Multiply) {
				return 1;
				} else {
				throw new InvalidInputError(operation.getClass() + " " + "has too few arguments");
				}
			}
		return 0;	
	}
	
	/**
	 * Returns an ExpressionNode which could either be a NumberNode, SymbolNode or a ListNode
	 * @param quote
	 * @param arg -> SymbolNode
	 * @return
	 */
	public ExpressionNode evaluateQuote(Quote quote, SymbolNode arg) {
		return quote.returnData();
	}
	
	
	
	

	public static <T> Stack<T> reverseStack(Stack<T> stack) {
		Stack<T> finalStack = new Stack<T>();
		while (!(stack.isEmpty())) {
			finalStack.push(stack.pop());
		}
		return finalStack;
	}
	
	
	/**
	 * Check if operator in atom node is a valid operator
	 * @return
	 */
	public static boolean isValidOperator(String token) {
		for (String validop : mValidOperators) {
			if (token.equals(validop)) {
				return true;
			}
		}
		return false;	
	}
	
	
	/**
	 * Evaluates the entire Syntax Tree by evoking the appropriate methods
	 * @return
	 */
	public Object evaluateTree() {
		if (mSyntaxTree instanceof ListNode) {
			//System.out.println(evaluateList(mSyntaxTree));
			return evaluateList(mSyntaxTree);
		} else if (mSyntaxTree instanceof NumberNode) {
			//System.out.println(evaluateNumber(mSyntaxTree));
			return evaluateNumber((NumberNode) mSyntaxTree);
		} else if (mSyntaxTree instanceof SymbolNode) {
			return evaluateSymbol((SymbolNode) mSyntaxTree);
			//System.out.println("Test");
			//if (isValidOperator())
			//throw new InvalidInputError("Symbol has no value");
		}
		return mSyntaxTree;
	}
	
}
