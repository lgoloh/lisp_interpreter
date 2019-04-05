package analysis;

public class Equal implements Operator {
	
	private ExpressionNode mParam1;
	private ExpressionNode mParam2;
	
	public Equal() {}
	
	public Equal(ExpressionNode node1, ExpressionNode node2) {
		mParam1 = node1;
		mParam2 = node2;
	}
	
	public ExpressionNode evaluateExpression() {
		ExpressionNode result1 = Eval.evaluateExpr(mParam1);
		ExpressionNode result2 = Eval.evaluateExpr(mParam2);
		boolean result = result1.isEqual(result2);
		if (result == true) {
			return new SymbolNode("T", null);
		} else {
			return new SymbolNode("NIL", null);
		}
		
	}
}
