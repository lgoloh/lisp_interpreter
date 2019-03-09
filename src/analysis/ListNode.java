package analysis;

import java.util.ArrayList;

import util.Token;

public class ListNode extends ExpressionNode {
	
	public ListNode(Token token, ArrayList<ExpressionNode> nodeList) {
		super(token, nodeList);
	}
	
	@Override
	public String toString() {
		if (mNodes.size() >= 1) {
			String resString = mNodes.get(0).toString();
			for (int i = 1; i < mNodes.size(); i++) {	
				resString+= " " + mNodes.get(i).toString();
			}
			return "(" + resString + ")";
		} else {
			return "(" + ")";
		}	
	}
	
	public boolean isEmpty() {
		if (mNodes.size() == 0) {
			return true;
		}
		return false;
	}
}
