package analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import util.Token;
import util.Type;

public class Evaluator {
	
	private ExpressionNode mSyntaxTree;
	private static String[] mValidOperators = {"+", "-", "*", "/"};
	
	public Evaluator(ExpressionNode tree) {
		mSyntaxTree = tree;
	}
	
	/**
	 * Evaluates a number node 
	 * @param node
	 * @return
	 */
	public int evaluateNumber(ExpressionNode node) {
		//String value = node.getToken().getValue();
		return (Integer.valueOf(node.getToken().getValue()));
	}
	
	/**
	 * Evaluates a Symbol; Currently only works for mathematical operations 
	 * @param head
	 * @return
	 */
	public BinOperator evaluateSymbol(ExpressionNode head) {
		Token operator = head.getToken();
		if (isValidOperator(operator)) {
			switch(operator.getValue()) {
				case "+":
					return new Sum();
				case "-":
					return new Subtract();
				case "*":
					return new Multiply();
				case "/":
					return new Divide();
			}
		} else {
			throw new InvalidInputError(head.getSymbolValue() + " " + "is not a Valid Symbol");
		}
		return null;
	}
	
	/**
	 * Evaluates a single ListNode
	 * @param listnode
	 * @return
	 */
	public int evaluateList(ExpressionNode listnode) {
		ArrayList<ExpressionNode> nodes = listnode.getnodeList();
		Queue<Integer> argQueue = new LinkedList<Integer>();
		//ArrayList<Integer> intArguments = new ArrayList<>();
		ExpressionNode head = nodes.get(0);
		///if head is an AtomNode and the token is type symbol
		if (head instanceof AtomNode && head.getToken().getType() == Type.SYMBOL) {
			// if evaluating the symbol does not return null
			if (evaluateSymbol(head) != null) {
				BinOperator operation = evaluateSymbol(head);
				for (int i = 1; i < nodes.size(); i++) {
					ExpressionNode curNode = nodes.get(i);
					if ((curNode instanceof AtomNode) && 
							(curNode.getToken().getType() == Type.NUMBER)) {
						argQueue.add(evaluateNumber(curNode));
					} else if (curNode instanceof ListNode) {
						argQueue.add(evaluateList(curNode));
					}
				} while (argQueue.size() != 1) {
					operation.setFirstParameter(argQueue.remove());
					operation.setSecondParameter(argQueue.remove());
					int result = operation.evaluateOperation();
					argQueue.add(result);
				}
				return (argQueue.remove());
			}
		} else {
			throw new InvalidInputError(head.getSymbolValue() + " " + "is not a Valid Symbol");
		}
		return 0;
	}
	
	
	/**
	 * Check if operator in atom node is a valid operator
	 * @return
	 */
	public static boolean isValidOperator(Token token) {
		String op = token.getValue();
		for (String validop : mValidOperators) {
			if (op.equals(validop)) {
				return true;
			}
		}
		return false;	
	}
	
	/**
	 * Evaluates the entire Syntax Tree by evoking the appropriate methods
	 * @return
	 */
	public int evaluateTree() {
		if (mSyntaxTree instanceof ListNode) {
			System.out.println(evaluateList(mSyntaxTree));
			return evaluateList(mSyntaxTree);
		} else if (mSyntaxTree instanceof AtomNode && mSyntaxTree.getToken().getType() == Type.NUMBER) {
			System.out.println(evaluateNumber(mSyntaxTree));
			return evaluateNumber(mSyntaxTree);
		} else {
			throw new InvalidInputError("Symbol has no value");
		}
	}
	
	
	
}
