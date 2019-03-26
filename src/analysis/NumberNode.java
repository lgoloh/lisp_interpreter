package analysis;

import java.util.ArrayList;

public class NumberNode extends ExpressionNode {
	
	public NumberNode(int number, ArrayList<ExpressionNode> nodeList) {
		super(number, nodeList);
	}
	
	@Override
	public String toString() {
		return String.valueOf((int) mObject);
	}
	
	@Override
	public boolean isEqual(ExpressionNode nodeb) {
		if (this.getValue() == nodeb.getValue()) {
			return true;
		}
		return false;
	}
}
