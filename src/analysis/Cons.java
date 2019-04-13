package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Cons implements Operator {
	
	ExpressionNode mInput;
	ExpressionNode mList;

	public Cons() {}
	
	public Cons (ExpressionNode node, ExpressionNode list) {
		mInput = node;
		mList = list;
	}
	
	public ExpressionNode cons() {
		mList.getnodeList().add(0, mInput);
		return mList;
	}
	
	public void setInput(ExpressionNode node) {
		mInput = node;
	}
	
	public void setList(ExpressionNode list) {
		mList = list;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		//System.out.println("List inside cons: " + mList);
		if (mList instanceof SymbolNode && mList.getValue().equals("NIL")) {
			Token token = new Token(Type.SOE, "(");
			ListNode emptyList = new ListNode(token, new ArrayList<>());
			mList = emptyList;
		}
		mList.getnodeList().add(0, mInput);
		return mList;
	}
	
}
