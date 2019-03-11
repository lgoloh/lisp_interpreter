package analysis;

public class Multiply extends BinOperator{
	
	public Multiply() {}
	
	public Multiply(ExpressionNode a, ExpressionNode b) {
		super(a, b);
	}
	
	@Override
	public ExpressionNode evaluateOperation() {
		return new NumberNode(((int) mParam1.getValue() * (int)mParam2.getValue()), null);
	}
	
}
