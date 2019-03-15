package analysis;

import java.util.ArrayList;
import java.util.HashMap;

public class Scope {
	
	private HashMap<SymbolNode, ExpressionNode> mScopeVariables;
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
	
	public ExpressionNode lookupVar(SymbolNode variable) {
		ExpressionNode value = new ExpressionNode();
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
	
	public HashMap<SymbolNode, ExpressionNode> getVariables() {
		return mScopeVariables;
	}
	
	public void setParentScope(Scope scope) {
		mParentScope = scope;
	}
	
	public Scope getParentScope() {
		return mParentScope;
	}
	
}
