package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import util.Token;
import util.Type;

public class Evaluator {
	
	private ExpressionNode mSyntaxTree;
	private static String[] mValidOperators = {"+", "-", "*", "/", "'", "QUOTE", 
			"LIST", "CONS", "CAR", "CDR", "LISTP", "NIL", "T", "NULL", "IF", 
			"AND", "OR", "<", ">", "<=", ">=", "=", "/=", "DEFUN", "LET", "SETQ", 
			"EQUAL", "DO", "PROGN", "TERPRI", "PRINC", "QUIT", "NOT", "APPLY", "FUNCTION"};
	
	private static String[] mSpecialOperators = {"NIL", "T", "IF", "QUOTE" };
	
	//The Global Scope 
	private Scope mGlobalScope = new Scope("Global");
	
	//Scope everything is currently operating in.
	private Scope mCurScope = mGlobalScope;
	
	public Evaluator() {
	}
	
	public Evaluator(ExpressionNode tree) {
		mSyntaxTree = tree;
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
						System.out.println(head);
						return head;
					case "LISTP":
						return new ListP();
					case "NULL":
						return head;
					case "IF":
						return head;
					case "AND":
						return head;
						//return new And();
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
					case "DO":
						return head;
					case "PROGN":
						return head;
					case "PRINC":
						return head;
					case "QUIT":	
						return head;
					case "NOT":
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
		System.out.println(head);
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
				//System.out.println(head);
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
						if (paramcount == ((FunctionStruct) operation).getParamCount()) {
							Scope functionscope = new Scope((String) head.getValue());
							//change from GlobalScope to CurScope
							functionscope.setParentScope(mCurScope);
							ArrayList<SymbolNode> paramlist = ((FunctionStruct) operation).getParamList();
							int i = 1;
							for (SymbolNode variable : paramlist) {
								functionscope.addVariable((String) variable.getValue(), evaluateExpr(nodes.get(i)));
								//System.out.println("Scope variables: " + functionscope.getVariables());
								i++;
							}
							ExecutionContext env = new ExecutionContext(functionscope, ((FunctionStruct) operation).getFunctionBody());
							mCurScope = env.getFunctionScope();
							//System.out.println("CurScope: " + mCurScope.getScope());
							ExpressionNode functionBody = env.getFunctionBody();
							ExpressionNode result = evaluateList(functionBody);
							mCurScope = mCurScope.getParentScope();
							return result;
						}
					}
					
					else if (operation instanceof SymbolNode 
							&& (((SymbolNode) operation).getValue().equals("=") 
									|| ((SymbolNode) operation).getValue().equals("EQUAL"))) {
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
									//System.out.print("setq value: " + value);
									//if the value is not found, put it in the global variable
									if (value == null) {
										mGlobalScope.addVariable(varname, nvalue);
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
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("LET")) {
						//Check that the variable initializer list is valid
						if (nodes.size() >= 2) {
							ExpressionNode result = new ExpressionNode();
							Scope tempScope = mCurScope;
							mCurScope = new Scope("let");
							mCurScope.setParentScope(tempScope);
							ListNode variableSpecs = (ListNode)nodes.get(1);
							//sets the variables
							letvariableInitializer(variableSpecs);
							if (nodes.size() > 2) {
								for (int i = 2; i < nodes.size(); i++) {
									result = evaluateExpr(nodes.get(i));
								}
							} else {
								return new SymbolNode("NIL", null);
							}
							mCurScope = tempScope;
							return result;
						} else {
							throw new EvalException("Too few parameters for special operator LET " + nodes);
						}
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("DO")) {
						return evaluateDo(nodes);
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("PROGN")) {
						ExpressionNode result = new SymbolNode("NIL", null);
						for (int i = 1; i < nodes.size(); i++) {
							result = evaluateExpr(nodes.get(i));
						}
						return result;
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("PRINC")) {
						
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("TERPRI")) {
						
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("QUIT")) {
						Main.endLoop();
					}
					
					else if (operation instanceof SymbolNode 
							&& ((SymbolNode) operation).getValue().equals("NOT")) {
						if (nodes.size() == 2) {
							ExpressionNode result = evaluateExpr(nodes.get(1));
							if (!(result.isEqual(new SymbolNode("NIL", null)))) {
								return new SymbolNode("NIL", null);
							} else {
								return new SymbolNode("T", null);
							}
						} else if (nodes.size() == 1) {
							throw new EvalException("too few arguments given to not " + listnode);
						} else if (nodes.size() > 2) {
							throw new EvalException("too many arguments given to not " + listnode);
						}
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
				//System.out.println("Test car");
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
			//System.out.println("Is in cdr");
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
					newValue = evaluateNumber(input);
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
			//System.out.println("getdatalist result: " + result);
			if (result.getValue().equals("NIL")) {
				Token token = new Token(Type.SOE, "(");
				ListNode emptyList = new ListNode(token, new ArrayList<>());
				data = emptyList;
			} else if (result instanceof ListNode) {
				//System.out.println("This result: " + result);
				data = result;
			}
		}
		if (data instanceof ListNode) {
			//System.out.println("This data: " + data);
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
		return new SymbolNode("NIL", null);
	}
	
	/**
	 * Evaluates null
	 * @param arg
	 * @return
	 */
	private ExpressionNode evaluateNull(ExpressionNode arg) {
		if (arg instanceof SymbolNode) {
			ExpressionNode res = (ExpressionNode) evaluateSymbol(arg);
			//System.out.println("Result of null: " + res);
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
		//System.out.println(nodeList.get(0));
		result = evaluateExpr(nodeList.get(0));
		//System.out.println("If result: "+ result);
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
	public ExpressionNode evaluateExpr(ExpressionNode node) {
		//System.out.println("Scope here: " + mCurScope.getScope());
		//System.out.println("Scope variables here: " + mCurScope.getVariables());
		ExpressionNode result = new ExpressionNode();
		if (node instanceof NumberNode) {
			result = evaluateNumber(node);
		} else if (node instanceof ListNode) {
			//System.out.println("expression node: " + node);
			result = evaluateList(node);
		} else if (node instanceof SymbolNode) {
			result = (ExpressionNode) evaluateSymbol(node);
		}
		//System.out.println("expression result: " + result);
		return result;	
	}
	
	
	
	private void letvariableInitializer(ListNode varlist) {
		try {
			HashMap<String, Object> variables = new HashMap<String, Object>();
			ArrayList<ExpressionNode> variableSpecs = varlist.getnodeList();
			for (ExpressionNode node : variableSpecs) {
				if (node instanceof ListNode) {
					ArrayList<ExpressionNode> vars = node.getnodeList();
					if (vars.size() == 2 && vars.get(0) instanceof SymbolNode) {
						ExpressionNode value = evaluateExpr(vars.get(1));
						String varname = (String)vars.get(0).getValue();
						variables.put(varname, value);
					} else if (vars.size() > 2) {
						throw new EvalException("Illegal variable specification " + node);
					} else if (vars.size() < 2 && vars.get(0) instanceof SymbolNode) {
						//System.out.println("Inside variable init " + node);
						ExpressionNode value = new SymbolNode("NIL", null);
						String varname = (String)node.getnodeList().get(0).getValue();
						variables.put(varname, value);
					}
				} else if (node instanceof SymbolNode) {
					ExpressionNode value = new SymbolNode("NIL", null);
					String varname = (String)node.getValue();
					variables.put(varname, value);
				} else if (node instanceof NumberNode) {
					throw new EvalException("Illegal variable specification " + node);
				}
			}
			mCurScope.setVariableHash(variables);
			System.out.println("variables inside variable init " + mCurScope.getVariables());
		} catch(EvalException e) {
			System.out.println(e);
		}
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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * All Do functions 
	 * @param dolist
	 * @return
	 */
	
	public ExpressionNode evaluateDo(ArrayList<ExpressionNode> dolist) {
		//setting up scopes
		Scope tempScope = mCurScope;
		mCurScope = new Scope("do");
		mCurScope.setParentScope(tempScope);
		//Set variables
		try {
			ArrayList<ExpressionNode> variableSpecs = dolist.get(1).getnodeList();
			ArrayList<ExpressionNode> afterIterExpressions = dolist.get(2).getnodeList();
			//Stores the step-forms of the variable
			HashMap<String, Object> stepforms = new HashMap<String, Object>();
			ExpressionNode endtest = afterIterExpressions.get(0);
			System.out.println("EndTest: " + endtest);
			ExpressionNode bodyexpressions = null;
			ExpressionNode result = null;
			if (dolist.size() > 3) {
				bodyexpressions = dolist.get(3);
			}
			for (ExpressionNode node : variableSpecs) {
				if (node instanceof ListNode) {
					initializeVars((ListNode) node);
				} else {
					throw new EvalException("Invalid variable initializer " + node);
				}
			}
			while (!(isEndIteration(endtest))) {
				if (bodyexpressions != null) {
					//System.out.println("Is it here?");
					//after evaluation of body (what to do with the result of the eval?)
					evaluateExpr(bodyexpressions);
					//update result is added to the list of updates
					stepforms = incrementVar(variableSpecs);
					mCurScope.setVariableHash(stepforms);
				} else {
					stepforms = incrementVar(variableSpecs);
					mCurScope.setVariableHash(stepforms);
				}
			}
			for (ExpressionNode node : afterIterExpressions) {
				System.out.println("Is it here?");
				result = evaluateExpr(node);
				
			}
			mCurScope = tempScope;
			return result;
		} catch(EvalException e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * Takes a single variable specification and initializes the value  
	 * @param variableSpec
	 */
	private void initializeVars(ListNode variableSpec) {
		try {
			ArrayList<ExpressionNode> spec = variableSpec.getnodeList();
			if (spec.get(0) instanceof SymbolNode) {
				String varname = (String) spec.get(0).getValue();
				ExpressionNode value = evaluateExpr(spec.get(1));
				mCurScope.addVariable(varname, value);
			} else {
				throw new EvalException("Invalid variable initializer " + variableSpec);
			}
		} catch(EvalException e) {
			System.out.println(e);
		}	 
	}
	
	/**
	 * Evaluates the step-form of a variable
	 * @param varname
	 * @param updateexpr
	 */
	private HashMap<String, Object> incrementVar(ArrayList<ExpressionNode> varspecs) {
		HashMap<String, Object> stepforms = new HashMap<String, Object>();
		for (ExpressionNode node : varspecs) {
			String varname = (String) node.getnodeList().get(0).getValue();
			stepforms.put(varname, evaluateExpr(node.getnodeList().get(2)));
		}
		return stepforms;
	}
	
	/**
	 * Tests the end iteration expression
	 * @param endtest
	 * @return
	 */
	private boolean isEndIteration(ExpressionNode endtest) {
		ExpressionNode result = evaluateExpr(endtest);
		if (result.isEqual(new SymbolNode("T", null))) {
			return true;
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
