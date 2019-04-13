package analysis;

import java.util.ArrayList;
import java.util.Stack;

import util.Token;
import util.Type;

public class Eval {

	
	private ExpressionNode mSyntaxTree;
	private static String[] mValidOperators = {"+", "-", "*", "/", "'", "QUOTE", 
			"LIST", "CONS", "CAR", "CDR", "LISTP", "NULL", "IF", 
			"AND", "OR", "<", ">", "<=", ">=", "=", "/=", "DEFUN", "LET", "SETQ", "EVAL", "PRINT-SPACE",
			"EQUAL", "DO", "PROGN", "TERPRI", "PRINC", "QUIT", "NOT", "APPLY", "FUNCTION", "LAMBDA"};
	
	private static String[] mVerySpecialOperators = {"NIL", "T"};
	private static String[] mBuiltinOperators = {"+", "-", "*", "/", "'", "LIST", "CONS", 
			"CAR", "CDR", "LISTP", "AND", "OR", "<", ">", "<=", ">=", "=", "/=", "DEFUN", "EVAL", 
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
	
	public static Object evaluateSymbol(ExpressionNode head) throws EvalException {
		String operator = (String) head.getValue();
		try {
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
						return new Princ();
					case "QUIT":	
						return head;
					case "NOT":
						return new Not();
					case "FUNCTION":
						return new FunctionOperator();
					case "APPLY":
						return new Apply();
					case "EVAL":
						return new EvalOperator();
					case "T":
						return head;
					case "NIL":
						return head;
					case "PRINT-SPACE":
						return new PrintSpace();
					case "TERPRI":
						return new Terpri();
				} 		
				return isDefined((SymbolNode) head);
			
		} catch(EvalException e) {
			throw e;
		}
	}
	
	
	private static Object isDefined(SymbolNode function) throws EvalException {
		Object value = mCurScope.lookup(function);
		try {
			if (value == null) {
				throw new EvalException("variable " + function + " is undefined");
			} else {
				return value;
			}
		}catch(EvalException e) {
			throw e;
		}

	}
	
	
	public static ExpressionNode evaluateList(ExpressionNode listnode) throws EvalException {
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
					
					//CONS
					else if (operation instanceof Cons) {
						if (nodes.size() == 3) {
							try {
								//Checks to make sure list is a ListNode Object
								//ExpressionNode data = getDataList(nodes.get(2));
								ExpressionNode data = evaluateExpr(nodes.get(2));
								ExpressionNode newValue = new ExpressionNode();
								if (data instanceof ListNode
										|| (data instanceof SymbolNode && data.getValue().equals("NIL"))) {
									if (nodes.get(1) instanceof NumberNode) {
										newValue = evaluateNumber(nodes.get(1));
									} else if (nodes.get(1) instanceof ListNode) {
										newValue = evaluateList(nodes.get(1));
									} else if (nodes.get(1) instanceof SymbolNode) {
										newValue = checkSymbol(nodes.get(1));
									}
									((Cons) operation).setInput(newValue);
									((Cons) operation).setList(data);
									return ((Cons) operation).evaluateExpression();
								} else {
									throw new EvalException(nodes.get(2) + " " + "must be a list");
								}
							}catch(EvalException e) {
								throw e;
							}
						} else if (nodes.size() < 3){
							throw new EvalException("too few arguments for CONS " + listnode);
						} else if (nodes.size() > 3) {
							throw new EvalException("too many arguments for CONS " + listnode);
						}
						//return null;
					}
					
					else if (operation instanceof Car) {
						try {
							//ExpressionNode data = getDataList(nodes.get(1));
							ExpressionNode data = evaluateExpr(nodes.get(1));
							if (data instanceof ListNode
									|| (data instanceof SymbolNode && data.getValue().equals("NIL"))) {
								((Car) operation).setList(data);
								return ((Car) operation).evaluateExpression();
							} else {
								throw new EvalException(nodes.get(1) + " " + "must be a list");
							}
						}catch (EvalException e) {
							throw e;
						}
					}
					
					else if (operation instanceof Cdr) {
						try {
							//ExpressionNode data = getDataList(nodes.get(1));
							ExpressionNode data = evaluateExpr(nodes.get(1));
							if (data instanceof ListNode
									|| (data instanceof SymbolNode && data.getValue().equals("NIL"))) {
								((Cdr) operation).setList(data);
								return ((Cdr) operation).evaluateExpression();
							} else {
								throw new EvalException(nodes.get(1) + " " + "must be a list");
							}
						}catch (EvalException e) {
							throw e;
						}
					} 
					//LISTP
					else if (operation instanceof ListP) {
						if (nodes.size() == 2) {
							//ListNode data = (ListNode) getDataList(nodes.get(1));
							operation = new ListP(nodes.get(1));
							return ((ListP) operation).evaluateExpression();
						} else if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to LISTP " + listnode);
						} else if (nodes.size() < 2){
							throw new EvalException("too few arguments given to LISTP " + listnode);
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
						int paramcount = nodes.size() - 1;
						if (paramcount == ((FunctionStruct) operation).getParamCount()) {
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
							//System.out.println("Functions vars: " + functionscope.getVariables());
							ExecutionContext env = new ExecutionContext(functionscope, ((FunctionStruct) operation).getFunctionBody());
							mCurScope = env.getFunctionScope();
							ArrayList<ExpressionNode> functionBody = env.getFunctionBody();
							//System.out.println("inside function call, function body: " +functionBody);
							//System.out.println("Current scope vars: "+ mCurScope.getVariables());
							Progn implicitProgn = new Progn(functionBody);
							ExpressionNode result = implicitProgn.evaluateExpression();
							//System.out.println(result + " is result of implicit progn in defun");
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
						nodes.remove(0);
						operation = new Progn(nodes); 
						return ((Progn) operation).evaluateExpression();
					}
					
					//PRINC
					else if (operation instanceof Princ) {
						if (nodes.size() == 2) {
							((Princ) operation).setExpression(nodes.get(1));
							ExpressionNode result = ((Princ) operation).evaluateExpression();
							return result;
						} else {
							throw new EvalException("too many arguments given to " + operation);
						}
					}
					
					//PRINT-SPACE
					else if (operation instanceof PrintSpace){
						return ((PrintSpace) operation).evaluateExpression();
					}
					
					else if (operation instanceof Terpri) {
						return ((Terpri) operation).evaluateExpression();
					} 
						
					//QUIT
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("QUIT")) {
						Main.endLoop();
						System.exit(0);
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
					
					//EVAL
					else if (operation instanceof EvalOperator) {
						System.out.println("Eval input: " + nodes);
						if (nodes.size() == 2) {
							if (nodes.get(1) instanceof ListNode) {
								ExpressionNode arg = evaluateExpr(nodes.get(1));
								((EvalOperator) operation).setExpression(arg);
							} else {
								((EvalOperator) operation).setExpression(nodes.get(1));
							}
							return ((EvalOperator) operation).evaluateExpression();
						} else if (nodes.size() < 2){
							throw new EvalException("too few arguments given to " + nodes.get(0));
						} else if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to " + nodes.get(0));
						}
					}
					
				} else {
					throw new EvalException("undefined function " + head);
				}
				
			} else if (nodes.size() < 1){
				return new SymbolNode("NIL", null);
			} else if (!(nodes.get(0) instanceof SymbolNode)) {
				throw new EvalException(nodes.get(0) + " is not a function name. Try a symbol instead");
			} 
		}catch(EvalException e) {
			//System.out.print(e);
			throw e;
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
	
	
	public static ExpressionNode evaluateExpr(ExpressionNode node) throws EvalException {
		ExpressionNode result = new ExpressionNode();
		if (node instanceof NumberNode) {
			result = evaluateNumber(node);
		} else if (node instanceof ListNode) {
			//System.out.println("expr: " + node);
			result = evaluateList(node);
		} else if (node instanceof SymbolNode) {
			result = checkSymbol(node);
		}
		return result;	
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
	 * @throws Exception 
	 */
	private static ExpressionNode evaluateMath(BinOperator operation, SymbolNode op, ArrayList<ExpressionNode> nodes)
			throws EvalException, ClassCastException {
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
						tempStack.push((ExpressionNode) checkSymbol(curNode));
					}
				} 
				argStack = reverseStack(tempStack); 
				if  (argStack.size() == 1 && operation instanceof Divide) {
					throw new EvalException("/ takes two arguments");
				}else if (argStack.size() >= 1) {
					return evalStack(operation, argStack);
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
			throw new EvalException("Expecting a number as argument to " + op + " "); 	
		} catch (EvalException e) {
			throw e;
		}
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
		if (arguments.size() == 1) {
			if (operation instanceof Sum || operation instanceof Subtract) {
				operation.setFirstParameter(new NumberNode(0, null));
				operation.setSecondParameter(arguments.pop());
			} else if (operation instanceof Multiply) {
				operation.setFirstParameter(new NumberNode(1, null));
				operation.setSecondParameter(arguments.pop());
			}
			ExpressionNode result = operation.evaluateOperation();
			arguments.push(result);
		} else if (arguments.size() > 1) {
			while (arguments.size() != 1) {
				operation.setFirstParameter(arguments.pop());
				operation.setSecondParameter(arguments.pop());
				ExpressionNode result = operation.evaluateOperation();
				arguments.push(result);
			}
		}
		return arguments.pop();
	}
	
	
	private static ExpressionNode checkSymbol(ExpressionNode symbol) throws EvalException {
		try {
				Object result = null;
				if (isValidOperator((String) symbol.getValue(), mVerySpecialOperators)) {
					return (ExpressionNode) evaluateSymbol(symbol);
				} else {
					result = isDefined((SymbolNode) symbol);
					if (result instanceof ExpressionNode) {
						return (ExpressionNode) result;
					} else {
						throw new EvalException("variable " + symbol + " has no value");
					}
				}
			} catch(EvalException e) {
			throw e;
		}
		
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	//Special Operators
	
	/**
	 * Evaluates null
	 * @param arg
	 * @return
	 * @throws EvalException 
	 */
	private static ExpressionNode evaluateNull(ExpressionNode arg) throws EvalException {
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
	 * @throws EvalException 
	 */
	private static ExpressionNode evaluateIf(ArrayList<ExpressionNode> nodeList) throws EvalException {
		ExpressionNode result = new ExpressionNode();
		result = evaluateExpr(nodeList.get(0));
		if (result instanceof SymbolNode
				&& ((SymbolNode) result).getValue().equals("NIL") && nodeList.size() == 3) {
			return evaluateExpr(nodeList.get(2));
		} else if (result instanceof SymbolNode
				&& ((SymbolNode) result).getValue().equals("NIL") && nodeList.size() == 2) {
			return result;
		} else {
			//System.out.println("If result: " + nodeList.get(1));
			return evaluateExpr(nodeList.get(1));
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public Object evaluateTree() throws EvalException {
		try {
			if (mSyntaxTree instanceof ListNode) {
				return evaluateList(mSyntaxTree);
			} else if (mSyntaxTree instanceof NumberNode) {
				return evaluateNumber((NumberNode) mSyntaxTree);
			} else if (mSyntaxTree instanceof SymbolNode) {
				return checkSymbol(mSyntaxTree);
			}
		}catch(EvalException e) {
			throw e;
		}
		return null;
	}
}
