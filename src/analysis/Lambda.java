package analysis;

import java.util.ArrayList;
import java.util.HashMap;

public class Lambda implements Operator{

	private ExpressionNode mLambdaStruct;
	private ArrayList<ExpressionNode> mArguments;
	private Scope mCurrentScope = Eval.getCurrentScope();
	
	public Lambda(ExpressionNode lambda, ArrayList<ExpressionNode> args) {
		mLambdaStruct = lambda;
		mArguments = args;
	}
	
	
	public void initVars() throws EvalException {
		//Get the list of argument symbols in the lambda listnode
		ArrayList<ExpressionNode> argSymbols = mLambdaStruct.getnodeList().get(1).getnodeList();
		HashMap<String, Object> variables = new HashMap<String, Object>();
		if (argSymbols.size() == mArguments.size()) {
			for (int i = 0; i < mArguments.size(); i++) {
				ExpressionNode value = Eval.evaluateExpr(mArguments.get(i));
				String varname = (String)argSymbols.get(i).getValue();
				variables.put(varname, value);
			}
		} else if (argSymbols.size() < mArguments.size()) {
			throw new EvalException("too many arguments given to LAMBDA");
		} else if (argSymbols.size() > mArguments.size()) {
			throw new EvalException("too few arguments given to LAMBDA");
		}
		mCurrentScope.setVariableHash(variables);
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {
		ExpressionNode result = new ExpressionNode();
		if (mLambdaStruct.getnodeList().size() > 2) {
			initVars();
			for (int i = 2; i < mLambdaStruct.getnodeList().size(); i++) {
				result = Eval.evaluateExpr(mLambdaStruct.getnodeList().get(i));
			}
		} else if ((mLambdaStruct.getnodeList().size() == 2)) {
			result = new SymbolNode("NIL", null);
		} else {
			throw new EvalException("lambda-list for NIL is missing");
		}
		return result;
	}
}
