package analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import util.Token;
import util.Type;

public class Evaluator {
	
	private ExpressionNode mSyntaxTree;
	private static String[] mValidOperators = {"+", "-", "*", "/", "'", "QUOTE", 
			"LIST", "CONS", "CAR", "CDR", "LISTP", "NIL", "T", "NULL", "IF", 
			"AND", "OR", "<", ">", "<=", ">=", "=", "/=", "DEFUN", "LET", "SETQ", "EQUAL"};
	//The Global Scope 
	private Scope mGlobalScope = new Scope("Global");
	private ExecutionContext mGlobalExecutionContext = new ExecutionContext(); 
	
	//Scope everything is currently operating in.
	private Scope mCurScope = mGlobalScope;
	
	private Stack<ExecutionContext> mExecutionStack = new Stack<ExecutionContext>();
	
	public Evaluator() {
		//mGlobalExecutionContext = new ExecutionContext(mGlobalScope, mSyntaxTree);
		//mExecutionStack.push(mGlobalExecutionContext);
	}
	
	public Evaluator(ExpressionNode tree) {
		mSyntaxTree = tree;
		//mGlobalExecutionContext = new ExecutionContext(mGlobalScope, mSyntaxTree);
		//mExecutionStack.push(mGlobalExecutionContext);
	}
	
	public void setTree(ExpressionNode tree) {
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
					case "LIST":
						return new ListOperator();
					case "QUOTE":
						//returns the actual Quote function that has the execution function
						Quote quote = new Quote();
						return quote;
					case "CONS":
						Cons cons = new Cons();
						return cons;
					case "NIL":
						return head;
					case "CAR":
						return new Car();
					case "CDR":
						return new Cdr();
					case "T":
						return head;
					case "LISTP":
						return new ListP();
					case "NULL":
						return head;
					case "IF":
						return head;
					case "AND":
						return head;
					case "OR":
						return head;
					case "DEFUN":
						return head;
					case "=":
						return head;
					case "EQUAL"	:
						return head;
					case "SETQ":
						return head;
					case "LET":
						return head;
				}
			//checking for user-defined functions in the global scope 
			//returns the FunctionStruct of the function	
			} else {
				Object value = mCurScope.lookup((SymbolNode) head);
				if (value == null) {
					throw new EvalException(head + " is undefined");
				} else {
					return value;
				}
				//return mCurScope.lookup((SymbolNode) head);
			}
		} catch(EvalException e) {
			System.out.println(e);
		}
		//System.out.println(mGlobalScope.getVariables());
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
				System.out.println(head);
				//System.out.println(mGlobalScope.getVariables());
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
							&& ((SymbolNode) operation).getValue().equals("NULL")) {
						System.out.println("Eval null");
						if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to NULL " + listnode);
						} else {
							System.out.println("Eval null 2");
							return evaluateNull(nodes.get(1));
						}
					}
					
					else if (operation instanceof SymbolNode
							&& ((SymbolNode) operation).getValue().equals("IF")) {
						if (nodes.size() == 3 || nodes.size() == 4) {
							ArrayList<ExpressionNode> argList = new ArrayList<>();
							for (int i = 1; i < nodes.size(); i++) {
								argList.add(nodes.get(i));
							}
							return evaluateIf(argList);
						} else if (nodes.size() < 3){
							throw new EvalException("too few arguments for special operator IF " + listnode);
						} else if (nodes.size() > 4) {
							throw new EvalException("too many arguments for special operator IF " + listnode);
						}
					}
					
					else if (operation instanceof SymbolNode
							&& ((SymbolNode) operation).getValue().equals("AND")) {
						nodes.remove(0);
						ArrayList<ExpressionNode> argList = new ArrayList<>();
						argList.addAll(nodes);
						return evaluateAnd(argList);
					}
					
					else if (operation instanceof SymbolNode
							&& ((SymbolNode) operation).getValue().equals("OR")) {
						nodes.remove(0);
						ArrayList<ExpressionNode> argList = new ArrayList<>();
						argList.addAll(nodes);
						return evaluateOr(argList);
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("DEFUN")) {
						String funcname = (String) ((SymbolNode) nodes.get(1)).getValue();
						ArrayList<ExpressionNode> paramlist = nodes.get(2).getnodeList();
						int paramcount = paramlist.size();
						ExpressionNode body = nodes.get(3);
						FunctionStruct function = new FunctionStruct(paramcount, body);
						for (ExpressionNode variable : paramlist) {
							function.addParam((SymbolNode) variable);
						}
						mGlobalScope.addVariable(funcname, function);
						return nodes.get(1);
						
					}
					//handles user defined functions
					else if (operation instanceof FunctionStruct) {
						int paramcount = nodes.size() - 1;
						//System.out.println("Body Check: " + ((FunctionStruct) operation).getFunctionBody());
						//System.out.println("Execution is here");
						//if the number of parameters passed to the function 
						//is equal to the number of parameters in the FunctionStruct
						//Initialize new scope, parent set to GlobalScope
						if (paramcount == ((FunctionStruct) operation).getParamCount()) {
							Scope functionscope = new Scope((String) head.getValue());
							//change from GlobalScope to CurScope
							functionscope.setParentScope(mCurScope);
							ArrayList<SymbolNode> paramlist = ((FunctionStruct) operation).getParamList();
							int i = 1;
							for (SymbolNode variable : paramlist) {
								//function scope will be the contain the variable name 
								//and the result of evaluating the value of the arguments
								//check for variables defined within the function this function is called in if applicable
								functionscope.addVariable((String) variable.getValue(), evaluateExpr(nodes.get(i)));
								//System.out.println("Scope variables: " + functionscope.getVariables());
								i++;
							}
							//The execution context which takes the function's Scope and Function body
							ExecutionContext env = new ExecutionContext(functionscope, ((FunctionStruct) operation).getFunctionBody());
							//put execution context on stack
							mExecutionStack.push(env);
							//System.out.println("Exec stac" + mExecutionStack);
							return executeFunctions();
						}
					}
					
					else if (operation instanceof SymbolNode 
							&& (((SymbolNode) operation).getValue().equals("=") || ((SymbolNode) operation).getValue().equals("EQUAL"))) {
						return evaluateEquality(nodes.get(1), nodes.get(2));
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("SETQ")) {
						ExpressionNode nvalue = new ExpressionNode();
						if (nodes.size()%2 == 1) {
							int j = 2;
							for (int i = 1; i<nodes.size()-1; i+=2) {
								if (nodes.get(i) instanceof SymbolNode) {
									String varname = (String) ((SymbolNode) nodes.get(i)).getValue();
									SymbolNode variable = (SymbolNode) nodes.get(i);
									ExpressionNode value = (ExpressionNode) mCurScope.lookup(variable);
									nvalue = evaluateExpr(nodes.get(j));
									if (value == null) {
										mCurScope.addVariable(varname, nvalue);
									} else {
										Scope scope = mCurScope.getOtherScope();
										scope.getVariables().replace(varname, nvalue);
									}
								}
								j+=2;
							}

						} else if (nodes.size()%2 == 0){
							throw new EvalException("setq has odd arguments");
						} 
						return nvalue;
					}
				} 
				//the empty list
			} else {
				return new SymbolNode("NIL", null);
			}
		}catch(EvalException e) {
			System.out.println(e);
		}
		//System.out.println("Is here1");
		return new NumberNode(0, null);
	} 
	
	
	
	/**
	 * Executes a specific function
	 * Sets the mCurScope to the scope of the function
	 * Executes the function body using mCurScope as the scope 
	 * @param context
	 * @return
	 */
	private ExpressionNode executeFunction(ExecutionContext context) {
		mCurScope = context.getFunctionScope();
		//System.out.println("CurScope: " + mCurScope.getScope());
		ExpressionNode functionBody = context.getFunctionBody();
		ExpressionNode result = evaluateList(functionBody);
		mExecutionStack.pop();
		return result;
	}
	
	
	private ExpressionNode executeFunctions() {
		//System.out.println("Size of execution contec = " + mExecutionStack.size());
		//System.out.println("Exec stack: " + mExecutionStack);
		//peek instead of pop
		ExecutionContext curcontext = mExecutionStack.peek();
		ExpressionNode result = executeFunction(curcontext);
		mCurScope = mGlobalScope;
		System.out.println("Exec stack: " + mExecutionStack);
		return result;
	}

	
	/**
	 * Evaluates Math operations
	 * TODO handles division by 1 number 
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
					} else if (curNode instanceof SymbolNode) {
						tempStack.push((ExpressionNode) evaluateSymbol(curNode));
					}
				} 
				argStack = reverseStack(tempStack); 
				if (argStack.size() > 1) {
					return evalStack(operation, argStack);
				} else if (argStack.size() == 1 && operation instanceof Divide) {
					throw new EvalException("/ takes two arguments");
				}
				
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
		//System.out.println("Is here2");
		return new NumberNode(0, null);		
	}
	
	
	
	private ExpressionNode evalStack(BinOperator operation, Stack<ExpressionNode> arguments) {
		while (arguments.size() != 1) {
			operation.setFirstParameter(arguments.pop());
			operation.setSecondParameter(arguments.pop());
			ExpressionNode result = operation.evaluateOperation();
			arguments.push(result);
		}
		return arguments.pop();
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
				System.out.println("Test car");
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
			System.out.println("Is in cdr");
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
	 * Does not handle symbols(variables) (Does now)
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
					newValue = (ExpressionNode) evaluateSymbol(input);
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
		} else if (list instanceof SymbolNode) {
			ExpressionNode result = (ExpressionNode) evaluateSymbol(list);
			if (result.getValue().equals("NIL")) {
				Token token = new Token(Type.SOE, "(");
				ListNode emptyList = new ListNode(token, new ArrayList<>());
				data = emptyList;
			} else if (result instanceof ListNode) {
				System.out.println("This result: " + result);
				data = result;
			}
		}
		if (data instanceof ListNode) {
			System.out.println("This data: " + data);
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
			if (nodes.size() == 1) {
				return new SymbolNode("NIL", null);
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
						resultList.getnodeList().add((ExpressionNode) evaluateSymbol(curNode));
						//throw new EvalException("variable" + " " +curNode.getValue() + " " + "has no value");
					}
				}
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
		if (arg instanceof SymbolNode) {
			ExpressionNode res = (ExpressionNode) evaluateSymbol(arg);
			System.out.println("Result of null: " + res);
			if (res.getValue().equals("NIL") || res.getnodeList().isEmpty())
				return new SymbolNode("T", null);
			else
				return new SymbolNode("NIL", null); 
		}if (arg instanceof ListNode && ((ListNode) arg).isEmpty()) {
			return new SymbolNode("T", null);
		} else if (arg instanceof ListNode) {
			ExpressionNode data = evaluateList(arg);
			if (data instanceof ListNode && ((ListNode) data).isEmpty()) {
				return new SymbolNode("T", null);
			}
		}
		return new SymbolNode("NIL", null);
	}
	
	/**
	 * Evaluate If
	 * @param nodeList
	 * @return
	 */
	private ExpressionNode evaluateIf(ArrayList<ExpressionNode> nodeList) {
		ExpressionNode result = new ExpressionNode();
		System.out.println(nodeList.get(0));
		result = evaluateExpr(nodeList.get(0));
		System.out.println("If result: "+ result);
		if (result instanceof SymbolNode
				&& ((SymbolNode) result).getValue().equals("NIL") && nodeList.size() == 3) {
			return evaluateExpr(nodeList.get(2));
		} else if (result instanceof SymbolNode
				&& ((SymbolNode) result).getValue().equals("NIL") && nodeList.size() == 2) {
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
					&& ((SymbolNode) result).getValue().equals("NIL")) && i < nodeList.size()) {
				result = evaluateExpr(nodeList.get(i));
				i++;
			}
			return result;
		} else if (nodeList.size() == 0) {
			return new SymbolNode("T", null);
		}
		return null;
	}
	
	
	private ExpressionNode evaluateOr(ArrayList<ExpressionNode> nodeList) {
		if (nodeList.size() > 0) {
			ExpressionNode result = new ExpressionNode();
			for (int i = 0; i < nodeList.size();) {
				result = evaluateExpr(nodeList.get(i));
				if (!(result instanceof SymbolNode
					&& ((SymbolNode) result).getValue().equals("NIL"))) {
					return result;
				} else {
					i++;
				}
			}
		} else if (nodeList.size() == 0) {
			return new SymbolNode("NIL", null);
		}
		return null;
	}
	
	
	/**
	 * Evaluates Equality
	 * @param param1
	 * @param param2
	 * @return
	 */
	
	private ExpressionNode evaluateEquality(ExpressionNode param1, ExpressionNode param2) {
		ExpressionNode result1 = evaluateExpr(param1);
		ExpressionNode result2 = evaluateExpr(param2);
		boolean result = result1.isEqual(result2);
		if (result == true) {
			return new SymbolNode("T", null);
		} else {
			return new SymbolNode("NIL", null);
		}
		
	}
	
	/**
	 * Evaluates a ListNode or NumberNode
	 * @param node
	 * @return
	 */
	private ExpressionNode evaluateExpr(ExpressionNode node) {
		//System.out.println("Scope here: " + mCurScope.getScope());
		//System.out.println("Scope variables here: " + mCurScope.getVariables());
		ExpressionNode result = new ExpressionNode();
		if (node instanceof NumberNode) {
			result = evaluateNumber(node);
		} else if (node instanceof ListNode) {
			System.out.println("expression node: " + node);
			result = evaluateList(node);
		} else if (node instanceof SymbolNode) {
			result = (ExpressionNode) evaluateSymbol(node);
		}
		System.out.println("expression result: " + result);
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
