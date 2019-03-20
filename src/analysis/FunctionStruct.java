package analysis;

import java.util.ArrayList;

public class FunctionStruct {
	
	private int mParamCount;
	private ArrayList<SymbolNode> mParameters = new ArrayList<>();
	private ExpressionNode mFunctionBody;
	
	public FunctionStruct(int count, ExpressionNode body) {
		mParamCount = count;
		mFunctionBody = body;
	}
	
	public int getParamCount() {
		return mParamCount;
	}
	
	public ExpressionNode getFunctionBody() {
		return mFunctionBody;
	}
	
	public ArrayList<SymbolNode> getParamList() {
		return mParameters;
	}
	
	public void addParam(SymbolNode param) {
		mParameters.add(param);
	}
	
}
