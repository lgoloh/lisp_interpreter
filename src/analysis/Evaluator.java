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
			"list", "cons", "car", "cdr", "listp", "nil", "T"};
	
	public Evaluator(ExpressionNode tree) {
		mSyntaxTree = tree;
	}
	
	/**
	 * Evaluates a number node 
	 * TODO Use NumberNode not ExpressionNode
	 * @param node
	 * @return
	 */
	public int evaluateNumber(ExpressionNode node) {
		return (int) node.getValue();
	}
	
	/**
	 * Evaluates a Symbol; Currently only works for mathematical operations 
	 * TODO Use SymbolNode not ExpressionNode
	 * @param head
	 * @return
	 */
	public Object evaluateSymbol(ExpressionNode head) {
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
				case "list":
					return new ListOperator();
				case "quote":
					//returns the actual Quote function that has the execution function
					Quote quote = new Quote();
					return quote;
					
				case "cons":
					Cons cons = new Cons();
					return cons;
				case "nil":
					Token token = new Token(Type.SOE, "(");
					ListNode emptyList = new ListNode(token, new ArrayList<>());
					return emptyList;
				case "car":
					return new Car();
				case "cdr":
					return new Cdr();
				case "T":
					break;
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
	public ExpressionNode evaluateList(ExpressionNode listnode) {
		ArrayList<ExpressionNode> nodes = listnode.getnodeList();
		if (nodes.size() >= 1) {
			ExpressionNode head = nodes.get(0);
			if (head instanceof SymbolNode) {
				//Returns an operation object after the Symbol is evaluated
				Object operation = evaluateSymbol((SymbolNode) head);
				//If the operation is a binary operation (+, -, /, *)
				if (operation instanceof BinOperator) {		
					return evaluateMath((BinOperator) operation, (SymbolNode) head, nodes);
				} 
				
				else if (operation instanceof Quote) {
					((Quote) operation).setExpression(listnode);
					Quote quote = (Quote) operation;
					ExpressionNode data = (ExpressionNode) quote.returnData();
					return data;
				} 
				
				else if (operation instanceof ListOperator) {
					return evaluateListOperator(nodes);
				} 
				
				else if (operation instanceof Cons && nodes.size() == 3) {
					return evaluateCons((Cons) operation, nodes.get(1), nodes.get(2));
				}
				
				else if (operation instanceof Car) {
					return evaluateCar((Car) operation, nodes.get(1));
				}
				
				else if (operation instanceof Cdr) {
					return evaluateCdr((Cdr) operation, nodes.get(1));
				}
			} else {
				throw new InvalidInputError(head.getValue() + " " + "is not a Valid Symbol");
				}
		} else {
			return new SymbolNode("nil", null);
		}	
		return new NumberNode(0, null);
	} 

	
	/**
	 * Evaluates Math operations
	 * TODO handles division by 1 number 
	 * TODO use NumberNodes and SymbolNodes instead of AtomNodes
	 * @param operation
	 * @param nodes
	 * @return
	 */
	public ExpressionNode evaluateMath(BinOperator operation, SymbolNode op, ArrayList<ExpressionNode> nodes) {
		Stack<Integer> argStack = new Stack<Integer>();
		Stack<Integer> tempStack = new Stack<Integer>();
		try {
			if (nodes.size() > 1) {
				for (int i = 1; i < nodes.size(); i++) {
					ExpressionNode curNode = nodes.get(i);
					if (curNode instanceof NumberNode) {
						tempStack.push(evaluateNumber((NumberNode) curNode));
					} else if (curNode instanceof ListNode) {
						tempStack.push((Integer) (evaluateList(curNode)).getValue());
					}
				} 
				argStack = reverseStack(tempStack); 
				while (argStack.size() != 1) {
					operation.setFirstParameter(argStack.pop());
					operation.setSecondParameter(argStack.pop());
					int result = operation.evaluateOperation();
					argStack.push(result);
				}return (new NumberNode(argStack.pop(), null));
			} else if (nodes.size() == 1) {
				if (operation instanceof Sum) {
					return new NumberNode(0, null);
					} else if (operation instanceof Multiply) {
					return new NumberNode(1, null);
					} else {
					throw new EvalException(op + " " + "has too few arguments");
					}
				}		
		} catch (ClassCastException e) {
			System.out.print("Expecting a number as argument to " + op + " ");
		} catch (Exception e) {
			System.out.println(e);
		}
		return new NumberNode(0, null);		
	}
	
	
	/**
	 * Evaluates Car
	 * @param car
	 * @param list
	 * @return
	 */
	public ExpressionNode evaluateCar(Car car, ExpressionNode list) {
		try {
			ListNode data = getDataList(list);
			if (data != null) {
				car.setList(data);
				return car.eval();
			}
		}catch (Exception e) {
			
		}
		return null;
	}
	
	
	/**
	 * Evaluates Cdr
	 * @param cdr
	 * @param list
	 * @return
	 */
	public ExpressionNode evaluateCdr(Cdr cdr, ExpressionNode list) {
		try {
			ListNode data = getDataList(list);
			if (data != null) {
				cdr.setList(data);
				return cdr.eval();
			}
		}catch (Exception e) {
			
		}
		return null;
	}
	
	
	/**
	 * Evaluates Cons
	 * Does not handle symbols(variables)
	 * @param cons
	 * @param input
	 * @param list
	 * @return
	 */
	public ExpressionNode evaluateCons(Cons cons, ExpressionNode input, ExpressionNode list) {
		try {
			ListNode data = getDataList(list);
			ExpressionNode newValue = new ExpressionNode();
			if (data != null) {
				if (input instanceof NumberNode) {
					newValue.setValue(evaluateNumber(input));
					newValue.setNodeList();
				} else if (input instanceof ListNode) {
					newValue = evaluateList(input);
				} else if (input instanceof SymbolNode) {
					throw new EvalException(input + " " + "has no value");
				}
				cons.setInput(newValue);
				cons.setList(data);
				return cons.cons();
			} else {
				throw new EvalException(list + " " + "must be a list");
			}
		}catch(EvalException e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * Returns ListNode that is a datalist
	 * If evaluating the listNode returns a list 
	 * @param list
	 * @return
	 */
	private ListNode getDataList(ExpressionNode list) {
		ExpressionNode data = new ExpressionNode();
		if (list instanceof ListNode) {
			data = evaluateList(list);
		} else if (list instanceof SymbolNode && list.getValue().equals("nil")) {
			data = (ExpressionNode) evaluateSymbol(list);
		}
		if (data instanceof ListNode) {
			return (ListNode) data;
		}
		return null;
	}
	
	
	/**
	 * Evaluates the list function. Doesn't handle symbols
	 * @param nodes
	 * @return
	 */
	public ExpressionNode evaluateListOperator(ArrayList<ExpressionNode> nodes) {
		Token token = new Token(Type.SOE, "(");
		ListNode resultList = new ListNode(token, new ArrayList<ExpressionNode>());
		if (nodes.size() == 1) {
			return new SymbolNode("nil", null);
		} else if (nodes.size() > 1) {
			for (int i = 1; i < nodes.size(); i++) {
				ExpressionNode curNode = nodes.get(i);
				if (curNode instanceof NumberNode) {
					resultList.getnodeList().add(new NumberNode(evaluateNumber(curNode), null));
				} else if (curNode instanceof ListNode) {
					ExpressionNode result = (ExpressionNode) evaluateList(curNode);
					resultList.getnodeList().add(result);	
				}
			}
		}
		return resultList;
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
