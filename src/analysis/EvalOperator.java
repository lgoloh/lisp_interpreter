package analysis;


public class EvalOperator implements Operator{

	private ExpressionNode mExpression;
	
	public EvalOperator() {
		mExpression = null;
	}
	
	public void setExpression(ExpressionNode expr) {
		mExpression = expr;
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {
		//System.out.println("EvalOperator arg: " + mExpression);
		return Eval.evaluateExpr(mExpression);
	}
}
