package analysis;

import util.Token;
import util.Type;

public class NumberNode extends ExpressionNode{

	
	public NumberNode(Token token, ExpressionNode lnode, ExpressionNode rnode) {
		super(token, lnode, rnode);
	}
}
 