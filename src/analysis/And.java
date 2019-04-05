package analysis;

import java.util.ArrayList;

public class And implements Operator {

	private ExpressionNode mAndExpression;
	
	public And (ExpressionNode expr) {
		mAndExpression = expr;
	}
	
	public And () {}
	
	public ExpressionNode evaluateExpression() {
		ArrayList<ExpressionNode> nodeList = mAndExpression.getnodeList();
		nodeList.remove(0);
		if (nodeList.size() > 0) {
			ExpressionNode result = Eval.evaluateExpr(nodeList.get(0));
			int i = 1;
			while (!(result instanceof SymbolNode
					&& ((SymbolNode) result).getValue().equals("NIL")) && i < nodeList.size()) {
				result = Eval.evaluateExpr(nodeList.get(i));
				i++;
			}
			return result;
		} else if (nodeList.size() == 0) {
			return new SymbolNode("T", null);
		}
		return null;
	}
}
