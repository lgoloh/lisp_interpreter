package analysis;

public class FunctionStruct {
	
	private int mParamCount;
	private ExpressionNode mFunctionBody;
	
	public FunctionStruct(int count, ExpressionNode body) {
		mParamCount = count;
		mFunctionBody = body;
	}
	
	public int getParamCount() {
		return mParamCount;
	}
	
	public ExpressionNode getFunctionBody() {
		return mFunctionBody;
	}
}
