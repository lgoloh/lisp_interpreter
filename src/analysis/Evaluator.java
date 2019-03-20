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
			"list", "cons", "car", "cdr", "listp", "nil", "t", "null", "if", 
			"and", "or", "<", ">", "<=", ">=", "=", "/=", "defun"};
	//The Global Scope 
	private static final Scope mGlobalScope = new Scope("Global");
	
	public Evaluator(ExpressionNode tree) {
		mSyntaxTree = tree;
	}
	
	/**
	 * Evaluates a number node 
	 * TODO Use NumberNode not ExpressionNode
	 * @param node
	 * @return
	 */
	public ExpressionNode evaluateNumber(ExpressionNode node) {
		return node;
	}
	
	/**
	 * Evaluates a Symbol; Currently only works for mathematical operations 
	 * TODO Use SymbolNode not ExpressionNode
	 * @param head
	 * @return
	 */
	public Object evaluateSymbol(ExpressionNode head) {
		String operator = (String) head.getValue();
		try {
			if (isValidOperator(operator)) {
				if (operator.equals("<") || operator.equals(">") 
						|| operator.equals(">=") || operator.equals("<=")) {
					return new ComparisonOperator(operator);	
				} 
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
						return head;
					case "car":
						return new Car();
					case "cdr":
						return new Cdr();
					case "t":
						return head;
					case "listp":
						return new ListP();
					case "null":
						return head;
					case "if":
						return head;
					case "and":
						return head;
					case "or":
						return head;
					case "defun":
						return head;
				}
			//checking for user-defined functions in the global scope 
			//returns the FunctionStruct of the function	
			} else if (mGlobalScope.getVariables().containsKey(head)){
				return mGlobalScope.getVariables().get(head);
			} else {
				throw new EvalException(head.getValue() + " " + "is not a Valid Symbol");
			}
			
		} catch(EvalException e) {
			System.out.println(e);
			}

		return null;
	}
	
	/**
	 * Evaluates a single ListNode
	 * Should take a Scope object that 
	 * @param listnode
	 * @return
	 */
	public ExpressionNode evaluateList(ExpressionNode listnode) {
		ArrayList<ExpressionNode> nodes = listnode.getnodeList();
		try {
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
					
					else if (operation instanceof ListP) {
						if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to LISTP " + listnode);
						} else {
							return evaluateListP(nodes.get(1));
						}	
						
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("null")) {
						if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to NULL " + listnode);
						} else {
							return evaluateNull(nodes.get(1));
						}
					}
					
					else if (operation instanceof SymbolNode
							&& ((SymbolNode) operation).getValue().equals("if")) {
						if (nodes.size() == 3 || nodes.size() == 4) {
							nodes.remove(0);
							ArrayList<ExpressionNode> argList = new ArrayList<>();
							argList.addAll(nodes);
							return evaluateIf(argList);
						} else if (nodes.size() < 3){
							throw new EvalException("too few arguments for special operator IF " + listnode);
						} else if (nodes.size() > 4) {
							throw new EvalException("too many arguments for special operator IF " + listnode);
						}
					}
					
					else if (operation instanceof SymbolNode
							&& ((SymbolNode) operation).getValue().equals("and")) {
						nodes.remove(0);
						ArrayList<ExpressionNode> argList = new ArrayList<>();
						argList.addAll(nodes);
						return evaluateAnd(argList);
					}
					
					else if (operation instanceof SymbolNode
							&& ((SymbolNode) operation).getValue().equals("or")) {
						nodes.remove(0);
						ArrayList<ExpressionNode> argList = new ArrayList<>();
						argList.addAll(nodes);
						return evaluateOr(argList);
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("defun")) {
						SymbolNode funcname = (SymbolNode) nodes.get(1);
						int paramcount = nodes.get(2).getnodeList().size();
						ExpressionNode body = nodes.get(3);
						FunctionStruct function = new FunctionStruct(paramcount, body);
						mGlobalScope.getVariables().put(funcname, function);
						return funcname;
					}
					//handles user defined functions
					else if (operation instanceof FunctionStruct) {
						int paramcount = nodes.size() - 1;
						//if the number of parameters passed to the function 
						//is equal to the number of parameters in the FunctionStruct
						if (paramcount == ((FunctionStruct) operation).getParamCount()) {
							
						}
					}
				} 
				//the empty list
			} else {
				return new SymbolNode("nil", null);
			}
		}catch(EvalException e) {
			System.out.println(e);
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
	private ExpressionNode evaluateMath(BinOperator operation, SymbolNode op, ArrayList<ExpressionNode> nodes) {
		Stack<ExpressionNode> argStack = new Stack<ExpressionNode>();
		Stack<ExpressionNode> tempStack = new Stack<ExpressionNode>();
		try {
			if (nodes.size() > 1) {
				for (int i = 1; i < nodes.size(); i++) {
					ExpressionNode curNode = nodes.get(i);
					if (curNode instanceof NumberNode) {
						tempStack.push(curNode);
					} else if (curNode instanceof ListNode) {
						tempStack.push(evaluateList(curNode));
					}
				} 
				argStack = reverseStack(tempStack); 
				while (argStack.size() != 1) {
					operation.setFirstParameter(argStack.pop());
					operation.setSecondParameter(argStack.pop());
					ExpressionNode result = operation.evaluateOperation();
					argStack.push(result);
				}return argStack.pop();
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
	
	//private ExpressionNode evaluateComparison(String operator, ExpressionNode first, ExpressionNode second) {
	
	
	/**
	 * Evaluates Car
	 * @param car
	 * @param list
	 * @return
	 */
	private ExpressionNode evaluateCar(Car car, ExpressionNode list) {
		try {
			ListNode data = getDataList(list);
			if (data != null) {
				car.setList(data);
				return car.eval();
			} else {
				throw new EvalException(list + " " + "must be a list");
			}
		}catch (EvalException e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	/**
	 * Evaluates Cdr
	 * @param cdr
	 * @param list
	 * @return
	 */
	private ExpressionNode evaluateCdr(Cdr cdr, ExpressionNode list) {
		try {
			ListNode data = getDataList(list);
			if (data != null) {
				cdr.setList(data);
				return cdr.eval();
			} else {
				throw new EvalException(list + " " + "must be a list");
			}
		}catch (EvalException e) {
			
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
	private ExpressionNode evaluateCons(Cons cons, ExpressionNode input, ExpressionNode list) {
		try {
			//Checks to make sure list is a ListNode Object
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
	 * Returns ListNode that is a Datalist
	 * If evaluating the listNode returns a list 
	 * @param list
	 * @return
	 */
	private ListNode getDataList(ExpressionNode list) {
		ExpressionNode data = new ExpressionNode();
		if (list instanceof ListNode && !(((ListNode) list).isEmpty())) {
			data = evaluateList(list);
		} else if (list instanceof ListNode && ((ListNode) list).isEmpty()) {
			data = list;
		} else if (list instanceof SymbolNode && list.getValue().equals("nil")) {
			Token token = new Token(Type.SOE, "(");
			ListNode emptyList = new ListNode(token, new ArrayList<>());
			data = emptyList;
		}
		if (data instanceof ListNode) {
			return (ListNode) data;
		}
		return null;
	}
	
	
	/**
	 * Evaluates the list function.
	 * @param nodes
	 * @return
	 */
	private ExpressionNode evaluateListOperator(ArrayList<ExpressionNode> nodes) {
		Token token = new Token(Type.SOE, "(");
		ListNode resultList = new ListNode(token, new ArrayList<ExpressionNode>());
		try {
			if (nodes.size() == 1) {
				return new SymbolNode("nil", null);
			} else if (nodes.size() > 1) {
				for (int i = 1; i < nodes.size(); i++) {
					ExpressionNode curNode = nodes.get(i);
					//If the expression is a number
					if (curNode instanceof NumberNode) {
						resultList.getnodeList().add(curNode);
					//if the expression is a ListNode 
					} else if (curNode instanceof ListNode) {
						ExpressionNode result = (ExpressionNode) evaluateList(curNode);
						resultList.getnodeList().add(result);	
					//If the expression is a SymbolNode, throw an evaluation exception
					} else if (curNode instanceof SymbolNode) {
						throw new EvalException("variable" + " " +curNode.getValue() + " " + "has no value");
					}
				}
			}
		} catch(EvalException e) {
			System.out.println(e);
		}	
		return resultList;
	}
	
	/**
	 * Evaluates Listp
	 * @param list
	 * @return
	 */
	private ExpressionNode evaluateListP(ExpressionNode list) {
		ListNode data = getDataList(list);
		if (data != null) {
			return new SymbolNode("T", null);
		}
		return new SymbolNode("nil", null);
	}
	
	/**
	 * Evaluates null
	 * @param arg
	 * @return
	 */
	private ExpressionNode evaluateNull(ExpressionNode arg) {
		if ((arg instanceof SymbolNode && ((SymbolNode) arg).getValue().equals("nil"))
				|| arg instanceof ListNode && ((ListNode) arg).isEmpty()) {
			return new SymbolNode("T", null);
		} else if (arg instanceof ListNode) {
			ExpressionNode data = evaluateList(arg);
			if (data instanceof ListNode && ((ListNode) data).isEmpty()) {
				return new SymbolNode("T", null);
			}
		}
		return new SymbolNode("nil", null);
	}
	
	/**
	 * Evaluate If
	 * @param nodeList
	 * @return
	 */
	private ExpressionNode evaluateIf(ArrayList<ExpressionNode> nodeList) {
		ExpressionNode result = new ExpressionNode();
		result = evaluateExpr(nodeList.get(0));
		if (result instanceof SymbolNode
				&& ((SymbolNode) result).getValue().equals("nil") && nodeList.size() == 3) {
			return evaluateExpr(nodeList.get(2));
		} else if (result instanceof SymbolNode
				&& ((SymbolNode) result).getValue().equals("nil") && nodeList.size() == 2) {
			return result;
		} else {
			return evaluateExpr(nodeList.get(1));
		}
	}
	
	/**
	 * Evaluates and
	 * @param nodeList
	 * @return
	 */
	private ExpressionNode evaluateAnd(ArrayList<ExpressionNode> nodeList) {
		if (nodeList.size() > 0) {
			ExpressionNode result = evaluateExpr(nodeList.get(0));
			int i = 1;
			while (!(result instanceof SymbolNode
					&& ((SymbolNode) result).getValue().equals("nil")) && i < nodeList.size()) {
				result = evaluateExpr(nodeList.get(i));
				i++;
			}
			return result;
		} else if (nodeList.size() == 0) {
			return new SymbolNode("t", null);
		}
		return null;
	}
	
	
	private ExpressionNode evaluateOr(ArrayList<ExpressionNode> nodeList) {
		if (nodeList.size() > 0) {
			ExpressionNode result = new ExpressionNode();
			for (int i = 0; i < nodeList.size();) {
				result = evaluateExpr(nodeList.get(i));
				if (!(result instanceof SymbolNode
					&& ((SymbolNode) result).getValue().equals("nil"))) {
					return result;
				} else {
					i++;
				}
			}
		} else if (nodeList.size() == 0) {
			return new SymbolNode("nil", null);
		}
		return null;
	}
	
	/**
	 * Evaluates a ListNode or NumberNode
	 * @param node
	 * @return
	 */
	private ExpressionNode evaluateExpr(ExpressionNode node) {
		ExpressionNode result = new ExpressionNode();
		if (node instanceof NumberNode) {
			result = evaluateNumber(node);
		} else if (node instanceof ListNode) {
			result = evaluateList(node);
		} else if (node instanceof SymbolNode) {
			result = (ExpressionNode) evaluateSymbol(node);
		}
		return result;	
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
