package analysis;

public class Terpri implements Operator {

	
	public Terpri() {}
	
	
	@Override
	public ExpressionNode evaluateExpression() {
		System.out.print("\n");
		return new SymbolNode("NIL", null);
	}
}
