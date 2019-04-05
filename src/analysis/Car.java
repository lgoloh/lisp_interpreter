package analysis;

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
		return mList.getnodeList().get(0);
	}
}
