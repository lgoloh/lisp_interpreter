package analysis;

import java.util.ArrayList;

import util.Token;

public class ListNode extends ExpressionNode {
	
	public ListNode(Token token, ArrayList<ExpressionNode> nodeList) {
		super(token, nodeList);
	}
}
