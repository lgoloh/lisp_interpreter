package analysis;

public class NotEqual  implements Operator {

	private ExpressionNode mParam1;
	private ExpressionNode mParam2;
	
	public NotEqual() {}
	
	public NotEqual(ExpressionNode node1, ExpressionNode node2) {
		mParam1 = node1;
		mParam2 = node2;
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {
		ExpressionNode result1 = Eval.evaluateExpr(mParam1);
		ExpressionNode result2 = Eval.evaluateExpr(mParam2);
		boolean result = result1.isEqual(result2);
		if (result == false) {
			return new SymbolNode("T", null);
		} else {
			return new SymbolNode("NIL", null);
		}
	}
	
	
}
