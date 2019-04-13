package analysis;

public class ListP implements Operator {

	private ExpressionNode mList;
	
	public ListP() {}
	
	public ListP(ExpressionNode node) {
		mList = node;
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {
		ExpressionNode result = Eval.evaluateExpr(mList);
		if (result instanceof ListNode) {
			return new SymbolNode("T", null);
		}
		return new SymbolNode("NIL", null);
	}
}
