package analysis;

public class Princ implements Operator {

	private ExpressionNode mObject;
	
	public Princ() {
		mObject = null;
	}
	
	public void setExpression(ExpressionNode object) {
		mObject = object;
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {
		ExpressionNode result = Eval.evaluateExpr(mObject);
		System.out.print(result);
		//System.out.println();
		return new SymbolNode("NIL", null);
	}
}
