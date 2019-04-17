package analysis;

public class FunctionExpression extends ExpressionNode {

	private String mSymbol;
	private Object mFunction;
	private String mFunctionType;
	
	public FunctionExpression(String symbol, Object object, String functionType) {
		mSymbol = symbol;
		mFunction = object;
		mFunctionType = functionType;
	}
	
	public Object getFunction() {
		return mFunction;
	}
	
	public String getFunctionName() {
		return mSymbol;
	}
	
	@Override
	public String toString() {
		switch(mFunctionType) {
		case "Builtin":
			return ("<" + "Built-In Function " +  mSymbol + ">");
		case "UserDefined":
			return ("<" + "User-Defined Function " +  mSymbol + ">");
		case "Anonymous":
			return ("<"+ "Anonymous Function " + mFunction + ">");
		}
		return null;
	}
}
