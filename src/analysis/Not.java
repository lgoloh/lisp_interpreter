package analysis;

import java.util.ArrayList;

public class Not implements Operator {

	private ExpressionNode mNotExpression;
	
	public Not() {}
	
	public Not(ExpressionNode notexpression) {
		mNotExpression = notexpression;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		ArrayList<ExpressionNode> mNodeList = mNotExpression.getnodeList();
		try {
			if (mNodeList.size() == 2) {
				ExpressionNode result = Eval.evaluateExpr(mNodeList.get(1));
				if (!(result.isEqual(new SymbolNode("NIL", null)))) {
					return new SymbolNode("NIL", null);
				} else {
					return new SymbolNode("T", null);
				}
			} else if (mNodeList.size() == 1) {
				throw new EvalException("too few arguments given to not " + mNotExpression);
			} else if (mNodeList.size() > 2) {
				throw new EvalException("too many arguments given to not " + mNotExpression);
			}
		} catch(EvalException e) {
			System.out.println(e);
		}
		return null;
	}
}
