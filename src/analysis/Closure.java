package analysis;



public class Closure {

	private Scope mEnvScope;
	private ExpressionNode mFunctionBody;
	
	public Closure(Scope scope) {
		mEnvScope = scope;
	}
	
	public Scope getClosureScope() {
		return mEnvScope;
	}
	
	public void setFunctionBody(ExpressionNode body) {
		mFunctionBody = body;
	}
	
	public ExpressionNode getFunctionBody() {
		return mFunctionBody;
	}
	
	@Override
	public String toString() {
		return mFunctionBody.toString();
	}
}
