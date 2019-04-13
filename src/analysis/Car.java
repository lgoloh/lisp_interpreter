package analysis;

import java.util.List;

public class Car implements Operator {
	
	protected ExpressionNode mList;

	public Car() {}
	
	public Car(ExpressionNode list) {
		mList = list;
	}
	
	public void setList(ExpressionNode list) {
		mList = list;
	}
	
	public ExpressionNode eval() {
		return mList.getnodeList().get(0);
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		if ((mList instanceof SymbolNode && mList.getValue().equals("NIL"))
				|| (mList instanceof ListNode && ((ListNode) mList).isEmpty())) {
			return new SymbolNode("NIL", null);
		}
		return mList.getnodeList().get(0);
	}
}
