package analysis;

import java.util.ArrayList;

import util.Token;

public class ListNode extends ExpressionNode {
	
	public ListNode(Token token, ArrayList<ExpressionNode> nodeList) {
		super(token, nodeList);
	}
	
	@Override
	public String toString() {
		String resString = mNodes.get(0).toString();
		for (int i = 1; i < mNodes.size(); i++) {	
			resString+= " " + mNodes.get(i).toString();
		}
		return "(" + resString + ")";
	}
}
