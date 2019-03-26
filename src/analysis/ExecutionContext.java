package analysis;

public class ExecutionContext {
	
	private Scope mFunctionScope;
	private ExpressionNode mFunctionBody;
	private ExpressionNode mReturnValue;
	
	public ExecutionContext() {}
	
	public ExecutionContext(Scope scope, ExpressionNode function) {
		mFunctionScope = scope;
		mFunctionBody = function;
	}
	
	public Scope getFunctionScope() {
		return mFunctionScope;
	}
	
	public ExpressionNode getFunctionBody() {
		return mFunctionBody;
	}
	
	public void setReturnValue(ExpressionNode res) {
		mReturnValue = res;
	}
	
	public ExpressionNode getReurnValue() {
		return mReturnValue;
	}
	

}
