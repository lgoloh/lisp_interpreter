package analysis;

import java.util.ArrayList;

public class FunctionStruct {
	
	private int mParamCount;
	private ArrayList<SymbolNode> mParameters = new ArrayList<>();
	//private ExpressionNode mFunctionBody;
	private ArrayList<ExpressionNode> mFunctionBodyForms;
	private Closure mClosure;
	
	public FunctionStruct(int count, ArrayList<ExpressionNode> body) {
		mParamCount = count;
		mFunctionBodyForms = body;
		mClosure = null;
	}
	
	public int getParamCount() {
		return mParamCount;
	}
	
	public ArrayList<ExpressionNode> getFunctionBody() {
		return mFunctionBodyForms;
	}
	
	public ArrayList<SymbolNode> getParamList() {
		return mParameters;
	}
	
	public void addParam(SymbolNode param) {
		mParameters.add(param);
	}
	
	public void addClosure(Closure closure) {
		mClosure = closure;
	}
	
	public Closure getClosure() {
		return mClosure;
	}
	
}
