package analysis;

import java.util.ArrayList;

public class ExecutionContext {
	
	private Scope mFunctionScope;
	private ArrayList<ExpressionNode> mFunctionBody;
	private ExpressionNode mReturnValue;
	
	public ExecutionContext() {}
	
	public ExecutionContext(Scope scope, ArrayList<ExpressionNode> function) {
		mFunctionScope = scope;
		mFunctionBody = function;
	}
	
	public Scope getFunctionScope() {
		return mFunctionScope;
	}
	
	public ArrayList<ExpressionNode> getFunctionBody() {
		return mFunctionBody;
	}
	
	public void setReturnValue(ExpressionNode res) {
		mReturnValue = res;
	}
	
	public ExpressionNode getReurnValue() {
		return mReturnValue;
	}
	

}
