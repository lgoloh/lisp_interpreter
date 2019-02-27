package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class ExpressionNode {
	
	protected Token mToken;
	protected Object mObject;
	protected ArrayList<ExpressionNode> mNodes;

	public ExpressionNode() {
	}
	
	//instead of taking a token, it should take the value of the symbol or the number 
	public ExpressionNode(Object object, ArrayList<ExpressionNode> nodes) {
		mObject = object;
		mNodes = nodes;
	}
	
	public void setValue(Object object) {
		mObject = object;
	}
	
	public Object getValue() {
		return mObject;
	}
	
	public ArrayList<ExpressionNode> getnodeList() {
		return mNodes;
	}
	
	
	@Override
	public String toString() {
		return null;};
	
}