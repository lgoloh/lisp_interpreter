package analysis;

import java.util.ArrayList;

import util.Token;

public class BinOperator {
	
	protected int mParam1;
	protected int mParam2;
	
	public BinOperator() {}
	
	public BinOperator(int a, int b) {
		mParam1 = a;
		mParam2 = b;
	}
	
	public int evaluateOperation() {
		return (mParam1 + mParam2);
	}
	
	public void setFirstParameter(int a) {
		mParam1 = a;
	}
	
	public void setSecondParameter(int b) {
		mParam2 = b;
	}	
	
}
