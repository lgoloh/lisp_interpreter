package analysis;

public class Multiply extends BinOperator{
	
	public Multiply() {}
	
	public Multiply(int a, int b) {
		super(a, b);
	}
	
	@Override
	public int evaluateOperation() {
		return (mParam1 * mParam2);
	}
	
}
