package analysis;

public class Subtract extends BinOperator {
	
	public Subtract() {}
	
	public Subtract(int a, int b) {
		super(a, b);
	}
	
	@Override
	public int evaluateOperation() {
		return (mParam1 - mParam2);
	}

}
