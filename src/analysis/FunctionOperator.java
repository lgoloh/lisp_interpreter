package analysis;

public class FunctionOperator implements Operator {

	private SymbolNode mFunctionSymbol;
	private String mFunctionType;
	
	public FunctionOperator() {}
	
	public FunctionOperator(SymbolNode symbol, String type) {
		mFunctionSymbol = symbol;
		mFunctionType = type;
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {
		String functionsymbol = (String) mFunctionSymbol.getValue();
		Object mFunctionObject = Eval.evaluateSymbol(mFunctionSymbol);
		ExpressionNode function = new FunctionExpression(functionsymbol, mFunctionObject, mFunctionType);
		return function;
	}
}
