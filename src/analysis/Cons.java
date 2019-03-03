package analysis;

public class Cons {
	
	ExpressionNode mInput;
	ListNode mList;

	public Cons() {}
	
	public Cons (ExpressionNode node, ListNode list) {
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
	
	public void setList(ListNode list) {
		mList = list;
	}
}
