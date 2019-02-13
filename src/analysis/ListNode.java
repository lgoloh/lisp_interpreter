package analysis;

import util.Token;

public class ListNode extends ExpressionNode {
	
	public ListNode(Token token, ExpressionNode lnode, 
			ExpressionNode rnode) {
		super(token, lnode, rnode);
	}
}
