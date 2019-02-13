package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class ExpressionNode {
	
	private Token mToken;
	private ArrayList<Token> mTokenList;
	private ExpressionNode mLeftNode;
	private ExpressionNode mRightNode;
	
	
	public ExpressionNode(Token token, ExpressionNode leftnode,
			ExpressionNode rightnode) {
		mToken = token;
		mLeftNode = leftnode;
		mRightNode = rightnode;
	}

	public ExpressionNode() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Used for only number tokens
	 * @return The integer value of the number 
	 */
	public int getNumberValue() {
		return Integer.valueOf(mToken.getValue());
	
	}
	
}