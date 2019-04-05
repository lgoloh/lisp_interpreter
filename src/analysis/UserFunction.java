package analysis;

import java.util.ArrayList;

public class UserFunction implements Operator {
	
	private FunctionStruct mFunctionStruct;
	private Scope mCurScope = Eval.getCurrentScope();
	private ArrayList<ExpressionNode> mNodeList;
	
	public UserFunction() {}
	
	public UserFunction(FunctionStruct struct, ArrayList<ExpressionNode> nodes) {
		mFunctionStruct = struct;
		mNodeList = nodes;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		int paramcount = mNodeList.size() - 1;
		if (paramcount == ((FunctionStruct) mFunctionStruct).getParamCount()) {
			//the tempScope;
			Scope tempScope = mCurScope;
			
			System.out.println("Test function call ");
			SymbolNode head = (SymbolNode) mNodeList.get(0);
			Scope functionscope = new Scope((String) head.getValue());
			//change from GlobalScope to CurScope
			if (((FunctionStruct) mFunctionStruct).getClosure() != null) {
				Scope sClosure = ((FunctionStruct) mFunctionStruct).getClosure().getClosureScope();
				functionscope.setParentScope(sClosure);
			} else {
				functionscope.setParentScope(tempScope);
			}
			ArrayList<SymbolNode> paramlist = ((FunctionStruct) mFunctionStruct).getParamList();
			int i = 1;
			for (SymbolNode variable : paramlist) {
				functionscope.addVariable((String) variable.getValue(), Eval.evaluateExpr(mNodeList.get(i)));
				//System.out.println("Scope variables: " + functionscope.getVariables());
				i++;
			}
			ExecutionContext env = new ExecutionContext(functionscope, ((FunctionStruct) mFunctionStruct).getFunctionBody());
			mCurScope = env.getFunctionScope();
			System.out.println("Closure: " + mCurScope.getParentScope().getVariables());
			ExpressionNode functionBody = env.getFunctionBody();
			ExpressionNode result = Eval.evaluateList(functionBody);
			mCurScope = tempScope;
			return result;
		}
		return null;
	}
}
