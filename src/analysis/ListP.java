package analysis;

public class ListP implements Operator {

	private ListNode mList;
	
	public ListP() {}
	
	public ListP(ListNode node) {
		mList = node;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		if (mList != null) {
			return new SymbolNode("T", null);
		}
		return new SymbolNode("NIL", null);
	}
}
