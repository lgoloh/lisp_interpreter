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
		System.out.println("Scope variables: " + mScopeVariables);
		//System.out.println(mScopeVariables.containsKey(var));
	}
	
	public Object lookup(SymbolNode variable) {
		Object value = null;
		//System.out.println(variable);
		//System.out.println(mScopeVariables);
		//System.out.println("inside: "+ mScopeVariables.containsKey(variable.getValue()));
		if (mScopeVariables.containsKey(variable.getValue())) {
			value = mScopeVariables.get(variable.getValue());
			mScope = this;
		} else if (mParentScope != null){
			//System.out.println("inside scope lookup");
			value = mParentScope.lookup(variable);
			mScope = mParentScope;
		}
		//System.out.println("inside scope lookup: " + value);
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
