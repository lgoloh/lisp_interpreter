package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class ListOperator implements Operator {
	
	private ArrayList<ExpressionNode> mNodeList;
	
	public ListOperator() {}
	
	public ListOperator(ArrayList<ExpressionNode> nodes) {
		mNodeList = nodes;
	}
	
	@Override
	public ExpressionNode evaluateExpression() throws EvalException {
			Token token = new Token(Type.SOE, "(");
			ListNode resultList = new ListNode(token, new ArrayList<ExpressionNode>());
				if (mNodeList.size() == 1) {
					return new SymbolNode("NIL", null);
				} else if (mNodeList.size() > 1) {
					for (int i = 1; i < mNodeList.size(); i++) {
						ExpressionNode curNode = mNodeList.get(i);
						if (curNode instanceof NumberNode) {
							resultList.getnodeList().add(curNode);
						} else if (curNode instanceof ListNode) {
							ExpressionNode result = (ExpressionNode) Eval.evaluateList(curNode);
							resultList.getnodeList().add(result);	
						} else if (curNode instanceof SymbolNode) {
							resultList.getnodeList().add((ExpressionNode) Eval.evaluateSymbol(curNode));
						}
					}
				}
			return resultList;
		}
}
