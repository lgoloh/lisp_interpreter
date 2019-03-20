package analysis;

import java.util.ArrayList;
import java.util.HashMap;

public class Scope {
	
	private HashMap<SymbolNode, Object> mScopeVariables;
	private String mScopeName;
	private Scope mParentScope;
	
	public Scope(String name) {
		mScopeName = name;
		mScopeVariables = new HashMap<>();
		mParentScope = null;
	}
	
	public void addVariable(SymbolNode var, ExpressionNode value) {
		mScopeVariables.put(var, value);
	}
	
	public Object lookupVar(SymbolNode variable) {
		Object value = null;
		try {
			if (mScopeVariables.containsKey(variable)) {
				value = mScopeVariables.get(variable);
			} else {
				throw new EvalException(variable + " is undefined");
			}
		} catch(EvalException e) {
			System.out.println(e);
		}
		return value;
	}
	
	public String getScope() {
		return mScopeName;
	}
	
	public HashMap<SymbolNode, Object> getVariables() {
		return mScopeVariables;
	}
	
	public void setParentScope(Scope scope) {
		mParentScope = scope;
	}
	
	public Scope getParentScope() {
		return mParentScope;
	}
	
}
