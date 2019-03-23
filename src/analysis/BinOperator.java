package analysis;


public class BinOperator {
	
	protected ExpressionNode mParam1 = null;
	protected ExpressionNode mParam2 = null;
	
	public BinOperator() {}
	
	public BinOperator(ExpressionNode a, ExpressionNode b) {
		mParam1 = a;
		mParam2 = b;
	}
	
    public ExpressionNode evaluateOperation() {
		return new ExpressionNode();
	}
	
	public void setFirstParameter(ExpressionNode a) {
		mParam1 = a;
	}
	
	public void setSecondParameter(ExpressionNode b) {
		mParam2 = b;
	}	
	
}
