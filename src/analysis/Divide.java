package analysis;

public class Divide extends BinOperator {
	
	public Divide() {}
	
	public Divide(int a, int b) {
		super(a, b);
	}
	
	@Override
	public int evaluateOperation() {
		if (mParam2 != 0) {
			return mParam1 / mParam2;
		} else {
		throw new IllegalArgumentException("Argument 'divisor' is 0");
		}
	}
}
