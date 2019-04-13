package analysis;

import java.util.ArrayList;
import java.util.HashMap;

public class Do implements Operator {

	private ArrayList<ExpressionNode> mDoExpression;
	private Scope mCurScope = Eval.getCurrentScope();
	
	
	public Do () {}
	
	public Do(ArrayList<ExpressionNode> doexpr) {
		mDoExpression = doexpr;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		try {
			ArrayList<ExpressionNode> variableSpecs = mDoExpression.get(1).getnodeList();
			ArrayList<ExpressionNode> secondArgList = mDoExpression.get(2).getnodeList();
			ArrayList<ExpressionNode> afterIterExpressions = new ArrayList<ExpressionNode>();
			ExpressionNode endtest = secondArgList.get(0);
			//secondArgList.remove(0);
			for (int i = 1; i < secondArgList.size(); i++) {
				afterIterExpressions.add(secondArgList.get(i));
			}
			//ArrayList<ExpressionNode> afterIterExpressions = secondArgList;
			HashMap<String, Object> stepforms = new HashMap<String, Object>();
			ArrayList<ExpressionNode> bodyexpressions = new ArrayList<ExpressionNode>();
			ExpressionNode result = null;
			if (mDoExpression.size() > 3) {
				for (int i = 3; i < mDoExpression.size(); i++)
				bodyexpressions.add(mDoExpression.get(i));
			}
			for (ExpressionNode node : variableSpecs) {
				if (node instanceof ListNode) {
					initializeVars((ListNode) node);
				} else {
					throw new EvalException("Invalid variable initializer " + node);
				}
			}
			while (!(isEndIteration(endtest))) {
				//if the list of body expressions is not empty
				if (!(bodyexpressions.isEmpty())) {
					for (ExpressionNode expr : bodyexpressions) {
						Eval.evaluateExpr(expr);
					}
					stepforms = incrementVar(variableSpecs);
					mCurScope.setVariableHash(stepforms);
				} else {
					stepforms = incrementVar(variableSpecs);
					mCurScope.setVariableHash(stepforms);
				}
			}
			if (afterIterExpressions.size() > 0) {
				for (ExpressionNode node : afterIterExpressions) {
					result = Eval.evaluateExpr(node);
				}
			} else {
				result = new SymbolNode("NIL", null);
			}
			return result;
		} catch(EvalException e) {
			System.out.print(e);
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
				ExpressionNode value = Eval.evaluateExpr(spec.get(1));
				mCurScope.addVariable(varname, value);
			} else {
				throw new EvalException("Invalid variable initializer " + variableSpec);
			}
		} catch(EvalException e) {
			System.out.print(e);
		}	 
	}
	
	/**
	 * Evaluates the step-form of a variable
	 * @param varname
	 * @param updateexpr
	 * @throws EvalException 
	 */
	private HashMap<String, Object> incrementVar(ArrayList<ExpressionNode> varspecs) throws EvalException {
		HashMap<String, Object> stepforms = new HashMap<String, Object>();
		for (int i = 0; i < varspecs.size(); i++) {
			ExpressionNode node = varspecs.get(i);
			if (node.getnodeList().size() == 3) {
				String varname = (String) node.getnodeList().get(0).getValue();
				stepforms.put(varname, Eval.evaluateExpr(node.getnodeList().get(2)));
			}
		}
		return stepforms;
	}
	
	/**
	 * Tests the end iteration expression
	 * @param endtest
	 * @return
	 * @throws EvalException 
	 */
	private boolean isEndIteration(ExpressionNode endtest) throws EvalException {
		ExpressionNode result = Eval.evaluateExpr(endtest);
		if (result.isEqual(new SymbolNode("T", null))) {
			return true;
		}
		return false;
	}
}
