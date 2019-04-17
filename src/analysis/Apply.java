package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Apply implements Operator {
	
	private ExpressionNode mFunctionOperation;
	private ExpressionNode mArguments;
	
	
	public Apply() {}
	
	public Apply(ExpressionNode func, ExpressionNode arguments) {
		mFunctionOperation = func;
		mArguments = arguments;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		Token listtkn = new Token(Type.SOE, "(");
		ArrayList<ExpressionNode> argList = new ArrayList<>();
		ExpressionNode functionSymbol = null;
		try {
			ExpressionNode funct = Eval.evaluateExpr(mFunctionOperation);
			if (funct instanceof FunctionExpression) {
				if (((FunctionExpression) funct).getFunctionName().equals("")) {
					Closure closure = (Closure) ((FunctionExpression) funct).getFunction();
					functionSymbol = closure.getFunctionBody();
				} else {
					functionSymbol = new SymbolNode(((FunctionExpression) funct).getFunctionName(), null);
				}
			} else if (funct instanceof SymbolNode) {
				functionSymbol = (SymbolNode) funct;
			} else {
				throw new EvalException("Undefined function " + funct);
			}
			argList.add(0, functionSymbol);
			ExpressionNode args = Eval.evaluateExpr(mArguments);
			ArrayList<ExpressionNode> functionArgs = args.getnodeList();
			for (ExpressionNode node : functionArgs) {
				argList.add(node);
			}
			ListNode resultExpr = new ListNode(listtkn, argList);
			return Eval.evaluateExpr(resultExpr);
		}catch(EvalException e) {
			System.out.print(e);
		}
		return null;
	}

}
