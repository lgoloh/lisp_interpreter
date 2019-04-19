package analysis;


import java.util.HashMap;

public class Scope {
	
	private HashMap<String, Object> mScopeVariables = new HashMap<>();
	private String mScopeName;
	private Scope mParentScope = null;
	//use for setq
	private Scope mScope = null;
	
	public Scope(String name) {
		mScopeName = name;
	}
	
	public void addVariable(String var, Object value) {
		mScopeVariables.put(var, value);
		//System.out.println( mScopeName + " Scope variables: " + mScopeVariables);
	}
	
	public Object lookup(SymbolNode variable) {
		Object value = null;
		if (mScopeVariables.containsKey(variable.getValue())) {
			value = mScopeVariables.get(variable.getValue());
			mScope = this;
		} else if (mParentScope != null){
			value = mParentScope.lookup(variable);
			mScope = mParentScope;
		}
		return value;
	}
	
	public String getScope() {
		return mScopeName;
	}
	
	public HashMap<String, Object> getVariables() {
		return mScopeVariables;
	}
	
	public void setParentScope(Scope scope) {
		mParentScope = scope;
	}
	
	public Scope getParentScope() {
		return mParentScope;
	}
	
	public Scope getOtherScope() {
		return mScope;
	}
	
	public void setVariableHash(HashMap<String, Object> hash) {
		mScopeVariables = hash;
	}
	
}
