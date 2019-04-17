package analysis;

import java.util.ArrayList;
import java.util.HashMap;

public class Let implements Operator{

	private ArrayList<ExpressionNode> mNodeList;
	private Scope mCurrentScope = Eval.getCurrentScope();
	
	public Let() {};
	
	public Let(ArrayList<ExpressionNode> nodes) {
		mNodeList = nodes;
	}
	
	public ExpressionNode evaluateExpression() throws EvalException {
		try {
			if (mNodeList.size() >= 2) {
				ExpressionNode result = new ExpressionNode();
				ListNode variableSpecs = (ListNode)mNodeList.get(1);
				//sets the variables
				letvariableInitializer(variableSpecs);
				//System.out.println("let Scope " + mCurrentScope.getVariables());
				if (mNodeList.size() > 2) {
					for (int i = 2; i < mNodeList.size(); i++) {
						result = Eval.evaluateExpr(mNodeList.get(i));
					}
				} else {
					return new SymbolNode("NIL", null);
				}
				return result;
			} else {
				throw new EvalException("Too few parameters for special operator LET " + mNodeList);
			}
		}catch(EvalException e) {
			throw e;
			//System.out.println(e);
		}
		
	}
	
	private void letvariableInitializer(ListNode varlist) throws EvalException {
		try {
			HashMap<String, Object> variables = new HashMap<String, Object>();
			ArrayList<ExpressionNode> variableSpecs = varlist.getnodeList();
			for (ExpressionNode node : variableSpecs) {
				if (node instanceof ListNode) {
					ArrayList<ExpressionNode> vars = node.getnodeList();
					if (vars.size() == 2 && vars.get(0) instanceof SymbolNode) {
						ExpressionNode value = Eval.evaluateExpr(vars.get(1));
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
			mCurrentScope.setVariableHash(variables);
			//System.out.println("variables inside variable init " + mCurrentScope.getVariables());
		} catch(EvalException e) {
			throw e;
			//System.out.println(e);
		}
	}
}
