package analysis;

import java.util.ArrayList;

import util.Token;

public class ListNode extends ExpressionNode {
	
	public ListNode(Token token, ArrayList<ExpressionNode> nodeList) {
		super(token, nodeList);
	}
	
	@Override
	public String toString() {
		System.out.println(mNodes.size());
		String resString = mNodes.get(0).getValue().toString();
		for (int i = 1; i < mNodes.size(); i++) {
			
			resString+= " " + mNodes.get(i).toString();
			//System.out.println(resString);
		}
		return "(" + resString + ")";
	}
}
