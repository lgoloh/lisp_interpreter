package analysis;

import java.util.ArrayList;

public class SymbolNode extends ExpressionNode {

	public SymbolNode(String symbol, ArrayList<ExpressionNode> nodeList) {
		super (symbol, nodeList);
	}
	
	@Override
	public String toString() {
		return ((String) mObject).toUpperCase();
	}
	
	@Override
	public boolean isEqual(ExpressionNode nodeb) {
		if (this.getValue().equals(nodeb.getValue())) {
			return true;
		}
		return false;
	}
}
