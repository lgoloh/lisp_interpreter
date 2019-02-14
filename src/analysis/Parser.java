package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Parser {
	
	private ArrayList<Token> mTokens;
	private ExpressionNode mRoot;
	
	public Parser(ArrayList<Token> tokens) {
		mTokens = tokens;
	}
	
	/**
	 * Recognizes the valid syntax structure of the list of tokens
	 */
	public void generateSyntaxTree() {
		/**
		 * Create root node; 
		 * If first token is an open bracket, root node is a ListNode
		 * Else if first token is a number or symbol, root node is an AtomNode
		 */
		Token firsttoken = mTokens.get(0);
		if (firsttoken.getType() == Type.SOE) {
			Token roottoken = mTokens.get(1);
			mRoot = new ListNode(roottoken, null, null);
			for (int i = 2; i < mTokens.size(); i++) {
				Token token = mTokens.get(i);
				switch (token.getType()) {
					case NUMBER:
						AtomNode num = new AtomNode(token, null, null);
						mRoot.setLeftExpression(num);
						
				}
			}
			
		} else if (firsttoken.getType() == Type.NUMBER 
				|| firsttoken.getType() == Type.SYMBOL) {
			mRoot = new AtomNode(firsttoken, null, null);
		};
		
		}
	
	
}
