package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class ExpressionNode {
	
	private Token mToken;
	private ExpressionNode mLeftNode;
	private ExpressionNode mRightNode;
	
	public ExpressionNode() {
	}
	
	public ExpressionNode(Token token, ExpressionNode leftnode,
			ExpressionNode rightnode) {
		mToken = token;
		mLeftNode = leftnode;
		mRightNode = rightnode;
	}
	
	public void setLeftExpression(ExpressionNode node) {
		mLeftNode = node;
	}
	
	public void setRightExpression(ExpressionNode node) {
		mRightNode = node;
	}
	
	public void setToken(Token token) {
		mToken = token;
	}
	
	public ExpressionNode getLeftExpression() {
		return mLeftNode;
	}
	
	public ExpressionNode getRightExpression() {
		return mRightNode;
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