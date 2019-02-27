package analysis;

import java.util.ArrayList;

import util.Token;

public class ListNode extends ExpressionNode {
	
	public ListNode(Token token, ArrayList<ExpressionNode> nodeList) {
		super(token, nodeList);
	}
	
	@Override
	public String toString() {
		String resString = mToken.getValue();
		for (ExpressionNode node : mNodes) {
			resString+= " " + node.toString();
		}
		return resString;
	}
}
