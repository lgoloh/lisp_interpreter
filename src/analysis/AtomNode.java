package analysis;

import java.util.ArrayList;

import util.Token;

public class AtomNode extends ExpressionNode{

	public AtomNode(Token token, ArrayList<ExpressionNode> nodeList) {
		super(token, nodeList);
	}
}
 