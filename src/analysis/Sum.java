package analysis;

public class Sum extends BinOperator {

	public Sum() {}
	
	public Sum(ExpressionNode a, ExpressionNode b) {
		super(a, b);
	}
	
	@Override
	public ExpressionNode evaluateOperation() {
		return new NumberNode(((int) mParam1.getValue() + (int) mParam2.getValue()), null);
	}
	
}
