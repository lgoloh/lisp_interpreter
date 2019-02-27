package analysis;


public class Quote {

	private static SymbolNode mQuoteNode;
	
	public Quote() {
		
	}
	
	public Quote (SymbolNode expr) {
		mQuoteNode = expr;
	}
	
	public ExpressionNode returnData() {
		if (mQuoteNode.getnodeList().get(0) instanceof SymbolNode) {
			return (SymbolNode) mQuoteNode.getnodeList().get(0);
		} else if (mQuoteNode.getnodeList().get(0) instanceof NumberNode) {
			return (NumberNode) mQuoteNode.getnodeList().get(0);
		} else if (mQuoteNode.getnodeList().get(0) instanceof ListNode) {
			return (ListNode) mQuoteNode.getnodeList().get(0);
		}
		return null;
	}

}