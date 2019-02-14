package analysis;

import util.Token;

public class AtomNode extends ExpressionNode{

	public AtomNode(Token token, ExpressionNode lnode, 
			ExpressionNode rnode) {
		super(token, lnode, rnode);
	}
}
 