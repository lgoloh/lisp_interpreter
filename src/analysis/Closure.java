package analysis;

public class Closure {

	public Scope mEnvScope;
	
	public Closure(Scope scope) {
		mEnvScope = scope;
	}
	
	public Scope getClosureScope() {
		return mEnvScope;
	}
}
