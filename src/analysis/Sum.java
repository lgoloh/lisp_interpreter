package analysis;

public class Sum extends BinOperator {

	public Sum() {}
	
	public Sum(int a, int b) {
		super(a, b);
	}
	
	@Override
	public int evaluateOperation() {
		return (mParam1 + mParam2);
	}
	
}
