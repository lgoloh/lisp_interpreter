package analysis;

import java.util.ArrayList;
import java.util.Stack;

import util.Token;
import util.Type;

public class Eval {

	
	private ExpressionNode mSyntaxTree;
	private static String[] mValidOperators = {"+", "-", "*", "/", "'", "QUOTE", 
			"LIST", "CONS", "CAR", "CDR", "LISTP", "NULL", "IF", 
			"AND", "OR", "<", ">", "<=", ">=", "=", "/=", "DEFUN", "LET", "SETQ", 
			"EQUAL", "DO", "PROGN", "TERPRI", "PRINC", "QUIT", "NOT", "APPLY", "FUNCTION"};
	
	private static String[] mVerySpecialOperators = {"NIL", "T"};
	private static String[] mBuiltinOperators = {"+", "-", "*", "/", "'", "LIST", "CONS", 
			"CAR", "CDR", "LISTP", "AND", "OR", "<", ">", "<=", ">=", "=", "/=", "DEFUN", 
			"SETQ", "EQUAL", "DO", "PRINC", "QUIT", "NOT", "APPLY", "FUNCTION"};
	private static ArrayList<String> mUserDefined = new ArrayList<>();
	
	//The Global Scope 
	private static Scope mGlobalScope = new Scope("Global");
	//Scope everything is currently operating in.
	private static Scope mCurScope = mGlobalScope;
	
	
	public Eval() {
	}
	
	public Eval(ExpressionNode tree) {
		mSyntaxTree = tree;
	}
	
	public void setTree(ExpressionNode tree) {
		mSyntaxTree = tree;
	}
	
	public static ExpressionNode evaluateNumber(ExpressionNode node) {
		return node;
	}
	
	public static Object evaluateSymbol(ExpressionNode head) {
		String operator = (String) head.getValue();
		//try {
			//if (isValidOperator(operator, mValidOperators)) {
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
					case "LIST":
						return new ListOperator();
					case "QUOTE":
						Quote quote = new Quote();
						return quote;
					case "CONS":
						Cons cons = new Cons();
						return cons;
					case "CAR":
						return new Car();
					case "CDR":
						return new Cdr();
					case "LISTP":
						return new ListP();
					case "NULL":
						return head;
					case "IF":
						return head;
					case "AND":
						return new And();
					case "OR":
						return new Or();
					case "DEFUN":
						return new Defun();
					case "=":
						return new Equal();
					case "EQUAL"	:
						return new Equal();
					case "SETQ":
						return new Setq();
					case "LET":
						return new Let();
					case "DO":
						return new Do();
					case "PROGN":
						return new Progn();
					case "PRINC":
						return head;
					case "QUIT":	
						return head;
					case "NOT":
						return new Not();
					case "FUNCTION":
						return new FunctionOperator();
					case "T":
						return head;
					case "NIL":
						return head;
					case "APPLY":
						return new Apply();
				}
				return isDefined((SymbolNode) head);
			
		/**} catch(EvalException e) {
			System.out.println(e);
		} **/
	}
	
	
	private static Object isDefined(SymbolNode function) {
		Object value = mCurScope.lookup(function);
		try {
			if (value == null) {
				throw new EvalException(function + " is undefined");
			} else {
				return value;
			}
		}catch(EvalException e) {
			System.out.print(e);
		}
		return null;

	}
	
	
	public static ExpressionNode evaluateList(ExpressionNode listnode) {
		ArrayList<ExpressionNode> nodes = listnode.getnodeList();
		try {
			if (nodes.size() >= 1 && nodes.get(0) instanceof SymbolNode) {
				ExpressionNode head = nodes.get(0);
				if (isValidOperator((String) head.getValue(), mValidOperators) || mUserDefined.contains((String) head.getValue()))
				
				{
					Object operation = evaluateSymbol((SymbolNode) head);
					//If the operation is a binary operation (+, -, /, *)
					if (operation instanceof BinOperator) {		
						return evaluateMath((BinOperator) operation, (SymbolNode) head, nodes);
					} 
					
					//QUOTE
					else if (operation instanceof Quote) {
						((Quote) operation).setExpression(listnode);
						Quote quote = (Quote) operation;
						ExpressionNode data = (ExpressionNode) quote.returnData();
						return data;
					} 
					
					//LIST
					else if (operation instanceof ListOperator) {
						operation = new ListOperator(nodes);
						ExpressionNode result = ((ListOperator) operation).evaluateExpression();
						return result;
					} 
					
					else if (operation instanceof Cons && nodes.size() == 3) {
						try {
							//Checks to make sure list is a ListNode Object
							ExpressionNode data = getDataList(nodes.get(2));
							ExpressionNode newValue = new ExpressionNode();
							if (data != null) {
								if (nodes.get(1) instanceof NumberNode) {
									newValue = evaluateNumber(nodes.get(1));
								} else if (nodes.get(1) instanceof ListNode) {
									newValue = evaluateList(nodes.get(1));
								} else if (nodes.get(1) instanceof SymbolNode) {
									newValue = (ExpressionNode) evaluateSymbol(nodes.get(1));
								}
								((Cons) operation).setInput(newValue);
								((Cons) operation).setList(data);
								return ((Cons) operation).evaluateExpression();
							} else {
								throw new EvalException(data + " " + "must be a list");
							}
						}catch(EvalException e) {
							System.out.println(e);
						}
						return null;
					}
					
					else if (operation instanceof Car) {
						try {
							ExpressionNode data = getDataList(nodes.get(1));
							if (data != null) {
								((Car) operation).setList(data);
								//System.out.println("Test car");
								return ((Car) operation).evaluateExpression();
							} else {
								throw new EvalException(nodes.get(1) + " " + "must be a list");
							}
						}catch (EvalException e) {
							System.out.println(e);
						}
					}
					
					else if (operation instanceof Cdr) {
						try {
							//System.out.println("List in cdr: " + nodes.get(1)); 
							ExpressionNode data = getDataList(nodes.get(1));
							//System.out.println("List in cdr: " + data); 
							if (data != null) {
								((Cdr) operation).setList(data);
								//System.out.println("Test car");
								return ((Cdr) operation).evaluateExpression();
							} else {
								throw new EvalException(nodes.get(1) + " " + "must be a list");
							}
						}catch (EvalException e) {
							System.out.println(e);
						}
					} 
					//LISTP
					else if (operation instanceof ListP) {
						if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to LISTP " + listnode);
						} else {
							ListNode data = (ListNode) getDataList(nodes.get(1));
							operation = new ListP(data);
							return ((ListP) operation).evaluateExpression();
						}	
						
					}
					//NULL
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("NULL")) {
						//System.out.println("Eval null");
						if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to NULL " + listnode);
						} else {
							//System.out.println("Eval null 2");
							return evaluateNull(nodes.get(1));
						}
					}
					//IF
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
					
					//AND		
					else if (operation instanceof And) {
						operation = new And(listnode);
						return ((And) operation).evaluateExpression();
					}
					
					//OR
					else if (operation instanceof Or) {
						operation = new Or(listnode);
						return ((Or) operation).evaluateExpression();
					}
					
					//DEFUN
					else if (operation instanceof Defun) {
						String functionname = (String) nodes.get(1).getValue();
						mUserDefined.add(functionname);
						operation = new Defun(nodes);
						return ((Defun) operation).evaluateExpression();
						
					}
					//handles user defined functions
					else if (operation instanceof FunctionStruct) {
						//Scope tempScope = mCurScope;
						int paramcount = nodes.size() - 1;
						if (paramcount == ((FunctionStruct) operation).getParamCount()) {
							//the tempScope;
							Scope tempScope = mCurScope;
							Scope functionscope = new Scope((String) head.getValue());
							if (((FunctionStruct) operation).getClosure() != null) {
								Scope sClosure = ((FunctionStruct) operation).getClosure().getClosureScope();
								functionscope.setParentScope(sClosure);
							} else {
								functionscope.setParentScope(tempScope);
							}
							ArrayList<SymbolNode> paramlist = ((FunctionStruct) operation).getParamList();
							int i = 1;
							for (SymbolNode variable : paramlist) {
								functionscope.addVariable((String) variable.getValue(), evaluateExpr(nodes.get(i)));
								i++;
							}
							ExecutionContext env = new ExecutionContext(functionscope, ((FunctionStruct) operation).getFunctionBody());
							mCurScope = env.getFunctionScope();
							//System.out.println("Closure: " + mCurScope.getParentScope().getVariables());
							ExpressionNode functionBody = env.getFunctionBody();
							ExpressionNode result = evaluateList(functionBody);
							mCurScope = tempScope;
							return result;
						} else if (paramcount < ((FunctionStruct) operation).getParamCount()) {
							throw new EvalException("too few arguments given to " + nodes.get(0)); 
						} else if (paramcount > ((FunctionStruct) operation).getParamCount()) {
							throw new EvalException("too many arguments given to " + nodes.get(0)); 
						} 
					}
					
					//EQUAL/=
					else if (operation instanceof Equal) {
						if (nodes.size() == 3) {
							operation = new Equal(nodes.get(1), nodes.get(2));
							return ((Equal) operation).evaluateExpression();
						} else if (nodes.size() < 3){
							throw new EvalException("too few arguments given to " + nodes.get(0));
						} else if (nodes.size() > 3) {
							throw new EvalException("too many arguments given to " + nodes.get(0));
						}
						
					}
					
					//SETQ
					else if (operation instanceof Setq) {
						operation = new Setq(nodes);
						return ((Setq) operation).evaluateExpression();
					}
					
					//LET
					else if (operation instanceof Let) {
						Scope tempScope = mCurScope;
						mCurScope = new Scope("let");
						mCurScope.setParentScope(tempScope);
						operation = new Let(nodes);
						ExpressionNode result = ((Let) operation).evaluateExpression();
						//System.out.println("Result of let " + result);
						mCurScope = tempScope;
						return result;
					}
					
					//DO
					else if (operation instanceof Do) {
						Scope tempScope = mCurScope;
						mCurScope = new Scope("do");
						mCurScope.setParentScope(tempScope);
						operation = new Do(nodes); 
						ExpressionNode result = ((Do) operation).evaluateExpression();
						mCurScope = tempScope;
						return result;
					}
					
					//PROGN
					else if (operation instanceof Progn) {
						operation = new Progn(nodes); 
						return ((Progn) operation).evaluateExpression();
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("PRINC")) {
						
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("TERPRI")) {
						
					}
					
					//QUIT
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("QUIT")) {
						Main.endLoop();
					}
					
					//NOT
					else if (operation instanceof Not) {
						operation = new Not(listnode);
						return ((Not) operation).evaluateExpression();
					}
					
					//FUNCTION
					else if (operation instanceof FunctionOperator) {
						String functionName = (String) ((SymbolNode) nodes.get(1)).getValue();
						String functionType = null;
						if (isValidOperator(functionName, mBuiltinOperators)) {
							functionType = "Builtin";
						} else if (mUserDefined.contains(functionName)) {
							functionType = "UserDefined";
						} else {
							throw new EvalException("Undefined function " + nodes.get(1));
						}
						operation = new FunctionOperator((SymbolNode) nodes.get(1), functionType);
						return ((FunctionOperator) operation).evaluateExpression();
					}
					
					//APPLY
					else if (operation instanceof Apply) {
						operation = new Apply(nodes.get(1), nodes.get(2));
						ExpressionNode result = ((Apply) operation).evaluateExpression();
						return result;
					}
					
				} else if (isValidOperator((String) head.getValue(), mVerySpecialOperators) ) {
					throw new EvalException("undefined function " + head);
				} else {
					throw new EvalException(head + " is undefined");
				}
				
			} else if (nodes.size() < 1){
				return new SymbolNode("NIL", null);
			} else if (!(nodes.get(0) instanceof SymbolNode)) {
				throw new EvalException(nodes.get(0) + " is not a function name. Try a symbol instead");
			} 
		}catch(EvalException e) {
			System.out.print(e);
		}
		return new NumberNode(0, null);
	}
	
	
	///Accessing the Scopes
	public static Scope getCurrentScope() {
		return mCurScope;
	}
	
	public static Scope getGlobalScope() {
		return mGlobalScope;
	}
	
	
	public static ExpressionNode evaluateExpr(ExpressionNode node) {
		ExpressionNode result = new ExpressionNode();
		if (node instanceof NumberNode) {
			result = evaluateNumber(node);
		} else if (node instanceof ListNode) {
			//System.out.println("expr: " + node);
			result = evaluateList(node);
		} else if (node instanceof SymbolNode) {
			result = (ExpressionNode) evaluateSymbol(node);
		}
		return result;	
	}
	
	/**
	 * TODO Extensively test this
	 * @param list
	 * @return
	 */
	public static ExpressionNode getDataList(ExpressionNode list) {
		ExpressionNode data = new ExpressionNode();
		if (list instanceof ListNode && !(((ListNode) list).isEmpty())) {
			data = evaluateList(list);
		} 
		
		else if (list instanceof ListNode && ((ListNode) list).isEmpty()) {
			//data = list;
			data = new SymbolNode("NIL", null);
		} 
		
		else if (list instanceof SymbolNode) {
			ExpressionNode result = (ExpressionNode) evaluateSymbol(list);
			if (result.getValue().equals("NIL")) {
				Token token = new Token(Type.SOE, "(");
				ListNode emptyList = new ListNode(token, new ArrayList<>());
				data = emptyList;
			} else if (result instanceof ListNode) {
				data = result;
			} 
		}
		return data;	
	}
	
	
	public static boolean isValidOperator(String token, String[] operatorList) {
		for (String validop : operatorList) {
			if (token.equals(validop)) {
				return true;
			}
		}
		return false;	
	}
	
	
	/**
	 * Evaluates Math operations
	 * TODO handles division by 1 number 
	 * @param operation
	 * @param nodes
	 * @return
	 */
	private static ExpressionNode evaluateMath(BinOperator operation, SymbolNode op, ArrayList<ExpressionNode> nodes) {
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
			System.out.print(e);
		}
		//System.out.println("Is here2");
		return new NumberNode(0, null);		
	}
	
	
	public static <T> Stack<T> reverseStack(Stack<T> stack) {
		Stack<T> finalStack = new Stack<T>();
		while (!(stack.isEmpty())) {
			finalStack.push(stack.pop());
		}
		return finalStack;
	}
	
	
	private static ExpressionNode evalStack(BinOperator operation, Stack<ExpressionNode> arguments) {
		while (arguments.size() != 1) {
			operation.setFirstParameter(arguments.pop());
			operation.setSecondParameter(arguments.pop());
			ExpressionNode result = operation.evaluateOperation();
			arguments.push(result);
		}
		return arguments.pop();
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	//Special Operators
	
	/**
	 * Evaluates null
	 * @param arg
	 * @return
	 */
	private static ExpressionNode evaluateNull(ExpressionNode arg) {
		if (arg instanceof SymbolNode) {
			ExpressionNode res = (ExpressionNode) evaluateSymbol(arg);
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
	private static ExpressionNode evaluateIf(ArrayList<ExpressionNode> nodeList) {
		ExpressionNode result = new ExpressionNode();
		result = evaluateExpr(nodeList.get(0));
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
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public Object evaluateTree() {
		if (mSyntaxTree instanceof ListNode) {
			return evaluateList(mSyntaxTree);
		} else if (mSyntaxTree instanceof NumberNode) {
			return evaluateNumber((NumberNode) mSyntaxTree);
		} else if (mSyntaxTree instanceof SymbolNode) {
			return evaluateSymbol((SymbolNode) mSyntaxTree);
		}
		return mSyntaxTree;
	}
}
