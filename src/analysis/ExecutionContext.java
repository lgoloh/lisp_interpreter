package analysis;

import java.util.Stack;

public class ExecutionContext {
	
	private Scope mFunctionScope;
	private FunctionStruct mFunctionDetails;
	
	public ExecutionContext(Scope scope, FunctionStruct function) {
		mFunctionScope = scope;
		mFunctionDetails = function;
	}

}
