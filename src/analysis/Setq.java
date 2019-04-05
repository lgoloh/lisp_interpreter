package analysis;

import java.util.ArrayList;

public class Setq implements Operator {
	
	private ArrayList<ExpressionNode> mNodeList;
	
	public Setq() {};
	
	public Setq(ArrayList<ExpressionNode> nodes) {
		mNodeList = nodes;
	}
	
	public ExpressionNode evaluateExpression() {
		ExpressionNode nvalue = new ExpressionNode();
		Scope currentScope = Eval.getCurrentScope();
		Scope globalScope = Eval.getGlobalScope();
		try {
			if (mNodeList.size()%2 == 1) {
				int j = 2;
				for (int i = 1; i< mNodeList.size()-1; i+=2) {
					if (mNodeList.get(i) instanceof SymbolNode) {
						String varname = (String) ((SymbolNode) mNodeList.get(i)).getValue();
						SymbolNode variable = (SymbolNode) mNodeList.get(i);
						System.out.println("Current Scope in Setq: " + currentScope.getScope());
						ExpressionNode value = (ExpressionNode)currentScope.lookup(variable);
						nvalue = Eval.evaluateExpr(mNodeList.get(j));
						if (value == null) {
							globalScope.addVariable(varname, nvalue);
						} else {
							Scope scope = currentScope.getOtherScope();
							scope.getVariables().replace(varname, nvalue);
						}
					}
					j+=2;
				}

			} else if (mNodeList.size()%2 == 0){
				throw new EvalException("setq has odd arguments");
			}
		} catch(EvalException e) {
			System.out.println(e);
		}
		 
		return nvalue;
	}
}
