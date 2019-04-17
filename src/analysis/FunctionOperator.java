package analysis;

public class FunctionOperator implements Operator {

	private String mFunctionSymbol;
	private String mFunctionType;
	private Object mFunctionObject;
	
	public FunctionOperator() {}
	
	public FunctionOperator(String symbol, Object object, String type) {
		mFunctionSymbol = symbol;
		mFunctionType = type;
		mFunctionObject = object;
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {		
		ExpressionNode function = new FunctionExpression(mFunctionSymbol, mFunctionObject, mFunctionType);
		return function;
	}
}
