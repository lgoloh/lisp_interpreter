package analysis;

import java.util.ArrayList;

public class Or implements Operator {
	
private ExpressionNode mOrExpression;
	
	public Or (ExpressionNode expr) {
		mOrExpression = expr;
	}
	
	public Or() {}
	
	public ExpressionNode evaluateExpression() throws EvalException {
		ArrayList<ExpressionNode> nodeList = mOrExpression.getnodeList();
		nodeList.remove(0);
		if (nodeList.size() > 0) {
			ExpressionNode result = new ExpressionNode();
			for (int i = 0; i < nodeList.size();) {
				result = Eval.evaluateExpr(nodeList.get(i));
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
}
