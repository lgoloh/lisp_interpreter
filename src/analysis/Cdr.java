package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Cdr extends Car {
	
	public Cdr() {}
	
	public Cdr(ExpressionNode list) {
		super(list);
	}
	
	@Override
	public ExpressionNode eval() {
		ListNode restofList = new ListNode(new Token(Type.SOE, "("), new ArrayList<>());
		for (int i = 1; i < mList.getnodeList().size(); i++) {
			restofList.getnodeList().add(mList.getnodeList().get(i));
		}
		return restofList;
	}
	
	@Override
	public ExpressionNode evaluateExpression() {
		if (mList instanceof SymbolNode && mList.getValue().equals("NIL")) {
			return new SymbolNode("NIL", null);
		} else {
			//System.out.println("List in cdr: " + mList);
			ListNode restofList = new ListNode(new Token(Type.SOE, "("), new ArrayList<>());
			for (int i = 1; i < mList.getnodeList().size(); i++) {
				restofList.getnodeList().add(mList.getnodeList().get(i));
			} if (restofList.isEmpty()) {
				return new SymbolNode("NIL", null);
			}
			return restofList;
		}
	}

}
