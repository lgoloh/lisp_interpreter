package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class ExpressionNode {
	
	private Token mToken;
	private ArrayList<ExpressionNode> mNodes;

	public ExpressionNode() {
	}
	
	public ExpressionNode(Token token, ArrayList<ExpressionNode> nodes) {
		mToken = token;
		mNodes = nodes;
	}
	
	public void setToken(Token token) {
		mToken = token;
	}
	
	public Token getToken() {
		return mToken;
	}
	
	public ArrayList<ExpressionNode> getnodeList() {
		return mNodes;
	}
	
	/**
	 * Used for only number tokens
	 * @return The integer value of the number 
	 */
	public int getNumberValue() {
		return Integer.valueOf(mToken.getValue());
	
	}
	
	/**
	 * Used only for symbol tokens
	 * @return
	 */
	public String getSymbolValue() {
		return mToken.getValue();
	}
	
	
}