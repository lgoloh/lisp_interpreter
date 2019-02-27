package analysis;

import java.util.ArrayList;

public class Quote {

	private static SymbolNode mQuoteNode;
	
	public Quote() {
		
	}
	
	public Quote (SymbolNode expr) {
		mQuoteNode = expr;
	}

	public static SymbolNode returnSymbol() {
		return (SymbolNode) mQuoteNode.getnodeList().get(0);
	}
	
	
	public static NumberNode returnNumber() {
		return (NumberNode) mQuoteNode.getnodeList().get(0);
	}
	
	public static ArrayList<ExpressionNode> returnList() {
		return mQuoteNode.getnodeList();
	}


}