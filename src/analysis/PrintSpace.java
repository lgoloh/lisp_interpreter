package analysis;

public class PrintSpace implements Operator {

	public PrintSpace() {}
	
	
	@Override
	public ExpressionNode evaluateExpression() {
		System.out.print(" ");
		return new SymbolNode("NIL", null);
	}
}
