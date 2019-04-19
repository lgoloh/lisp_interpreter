package analysis;

import java.util.ArrayList;

public class Defun implements Operator {
	
	private ArrayList<ExpressionNode> mFunctionDetails;
	
	public Defun() {}
	
	public Defun(ArrayList<ExpressionNode> function) {
		mFunctionDetails = function;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		Scope currentScope = Eval.getCurrentScope();
		Scope globalScope = Eval.getGlobalScope();
		String funcname = (String) ((SymbolNode) mFunctionDetails.get(1)).getValue();
		ArrayList<ExpressionNode> paramlist = mFunctionDetails.get(2).getnodeList();
		int paramcount = paramlist.size();
		ArrayList<ExpressionNode> bodyForms = new ArrayList<ExpressionNode>();
		for (int i = 3; i < mFunctionDetails.size(); i++) {
			bodyForms.add(mFunctionDetails.get(i));
		}
		ExpressionNode body = mFunctionDetails.get(3);
		FunctionStruct function = new FunctionStruct(paramcount, bodyForms);
		for (ExpressionNode variable : paramlist) {
			function.addParam((SymbolNode) variable);
		}
		Closure closure = setClosure(currentScope, globalScope);
		if (closure != null) {
			function.addClosure(closure);
		}
		globalScope.addVariable(funcname, function);
		return mFunctionDetails.get(1);
		
	}
	
	private Closure setClosure(Scope cur, Scope global) {
		if (!(cur.getScope().equals("Global"))) {
			return new Closure(cur);
		} else {
			return null;
		}
	}
	
}
