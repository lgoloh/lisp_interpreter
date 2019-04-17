package analysis;

public class Divide extends BinOperator {
	
	public Divide() {}
	
	public Divide(ExpressionNode a, ExpressionNode b) {
		super(a, b);
	}
	
	@Override
	public ExpressionNode evaluateOperation() throws EvalException {
		try {
			if ((int)mParam2.getValue() != 0 && mParam2 != null) {
				return new NumberNode(((int)mParam1.getValue() / (int) mParam2.getValue()), null);
			} else {
				throw new EvalException("Argument 'divisor' is 0");
			}
		}catch(EvalException e) {
			throw e;
			//System.out.println(e);
		}
		//return new NumberNode(0, null);
	}
}
