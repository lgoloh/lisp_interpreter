package analysis;

import java.util.ArrayList;

public class Do {

	private ArrayList<ExpressionNode> mDoExpression;
	private Scope mDoScope;
	
	
	public Do () {}
	
	private Do(ArrayList<ExpressionNode> doexpr, Scope scope) {
		mDoExpression = doexpr;
		mDoScope = scope;
	}
	
	public ExpressionNode evaluateDo(ArrayList<ExpressionNode> dolist) {
		//Set variables
		ArrayList<ExpressionNode> variableSpecs = dolist.get(1).getnodeList();
		for (ExpressionNode node : variableSpecs) {
			if (node instanceof ListNode) {
				
			}
		}
		return null;
	}
	
	/**
	 * Takes a single variable specification and initializes the value  
	 * @param variableSpec
	 */
	private void initializeVars(ListNode variableSpec) {
		ArrayList<ExpressionNode> spec = variableSpec.getnodeList();
		if (spec.get(0) instanceof SymbolNode) {
			String varname = (String) spec.get(0).getValue();
			//ExpressionNode 
		}
	}
}
