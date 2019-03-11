package analysis;

public class Subtract extends BinOperator {
	
	public Subtract() {}
	
	public Subtract(ExpressionNode a, ExpressionNode b) {
		super(a, b);
	}
	
	@Override
	public ExpressionNode evaluateOperation() {
		return new NumberNode(((int) mParam1.getValue() - (int) mParam2.getValue()), null);
	}

}
