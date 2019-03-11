package analysis;

public class ComparisonOperator extends BinOperator {
	
	protected String mOperator;
	
	public ComparisonOperator(ExpressionNode a, ExpressionNode b, String op) {
		super(a, b);
		mOperator = op;
	}
	
	public ComparisonOperator(String op) {
		mOperator = op;
	}
	
	@Override
	public ExpressionNode evaluateOperation() {
		int first = 0;
		int second = 0;
		try {
			if (mParam1 instanceof NumberNode && mParam2 instanceof NumberNode) {
				first = (int) mParam1.getValue();
				second = (int) mParam2.getValue();
				switch (mOperator) {
				case "<":
					if (first < second) {
						return new SymbolNode("t", null);
					} 
					return new SymbolNode("nil", null);
				case ">":
					if (first > second) {
						return new SymbolNode("t", null);
					}
					return new SymbolNode("nil", null);
				case "<=":
					if (first <= second) {
						return new SymbolNode("t", null);
					}
					return new SymbolNode("nil", null);
				case ">=":
					if (first >= second) {
						return new SymbolNode("t", null);
					}
					return new SymbolNode("nil", null);
					}
			} else if (!(mParam1 instanceof NumberNode)){
				throw new EvalException(mParam1 + " " + "is not a number");
			} else if (!(mParam2 instanceof NumberNode)) {
				throw new EvalException(mParam2 + " " + "is not a number");
			}
			
		} catch(EvalException e) {
			System.out.println(e);
		}
		return new NumberNode(0, null);
		
	}
	
}
