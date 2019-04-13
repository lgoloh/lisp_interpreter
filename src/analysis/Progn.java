package analysis;

import java.util.ArrayList;

public class Progn implements Operator{

	
	private ArrayList<ExpressionNode> mNodeList;
	//private Scope mCurrentScope = Eval.getCurrentScope();
	
	public Progn() {};
	
	public Progn(ArrayList<ExpressionNode> nodes) {
		mNodeList = nodes;
	}
	
	public ExpressionNode evaluateExpression() throws EvalException {
		//System.out.println("Progn nodes: " + mNodeList);
		//System.out.println(mNodeList.size());
		ExpressionNode result = new SymbolNode("NIL", null);
		for (int i = 0; i < mNodeList.size(); i++) {
			result = Eval.evaluateExpr(mNodeList.get(i));
		}
		return result;
	}
}
