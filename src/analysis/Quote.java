package analysis;


public class Quote {

	private static ExpressionNode mQuoteNode;
	
	public Quote() {
		
	}
	
	public Quote (ExpressionNode expr) {
		mQuoteNode = expr;
	}
	
	public void setExpression(ExpressionNode listnode) {
		mQuoteNode = listnode;
	}
	
	public ExpressionNode returnData() {
		if (mQuoteNode.getnodeList().get(1) instanceof SymbolNode) {
			return (SymbolNode) mQuoteNode.getnodeList().get(1);
		} else if (mQuoteNode.getnodeList().get(1) instanceof NumberNode) {
			return (NumberNode) mQuoteNode.getnodeList().get(1);
		} else if (mQuoteNode.getnodeList().get(1) instanceof ListNode) {
			return (ListNode) mQuoteNode.getnodeList().get(1);
		}
		return null;
	}

}