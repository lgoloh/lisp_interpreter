package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Parser {
	
	private ArrayList<Token> mTokens;
	
	public Parser(ArrayList<Token> tokens) {
		mTokens = tokens;
	}
	
	private void isExpression(ArrayList<Token> tokens) {
		switch(token.getType()) {
		case NUMBER:
			break;
		case SOE:
			
		}
	}
	
	
	/**
	 * Recognizes the valid syntax structure of the list of tokens
	 */
	public void recognizeSyntax() {}
	
	
}
