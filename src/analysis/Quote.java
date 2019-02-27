package analysis;

public class Quote {

	private static ExpressionNode mQuoteNode;
	
	public Quote() {
		
	}
	
	public Quote (ExpressionNode expr) {
		mQuoteNode = expr;
	}
	

	
	//Called is the element is a list
	public static LispList returnList() {
		LispList result = new LispList();
		for (ExpressionNode node : mQuoteNode.getnodeList()) {
			result.add(node.toString());
		}
		return result;
	}


}
