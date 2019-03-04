package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Cdr extends Car{
	
	public Cdr() {}
	
	public Cdr(ListNode list) {
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

}
