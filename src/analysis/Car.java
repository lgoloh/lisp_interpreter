package analysis;

public class Car {
	
	protected ListNode mList;

	public Car() {}
	
	public Car(ListNode list) {
		mList = list;
	}
	
	public void setList(ListNode list) {
		mList = list;
	}
	
	public ExpressionNode eval() {
		return mList.getnodeList().get(0);
	}
}
