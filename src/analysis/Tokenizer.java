package analysis;

import util.Token;
import util.Type;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.regex.Pattern;

public class Tokenizer {
	
	private int mCurPosition;
	private Token mCurToken;
	private String mInputString;
	
	public Tokenizer(String inputString) {
		mInputString = inputString;
		mCurPosition = 0;
		mCurToken = null;
	}
	
	public Token getNextToken() {
		char character = 0;
		if (mCurPosition == mInputString.length()) {
			mCurPosition++;
			return new Token(Type.EOF, null); 
		} else if (mCurPosition < mInputString.length()) {
			 character = nextAfterWhitespace(mInputString);
			 System.out.println(character);
			 
			 switch(character) {    
				case '(':
					mCurPosition+=1;
					mCurToken = new Token(Type.SOE, Character.toString(character));
					return mCurToken;
					
				case ')':
					mCurPosition+=1;
					mCurToken = new Token(Type.EOE, Character.toString(character));
					return mCurToken;
				
				}
			if (Character.isDigit(character) || character == '+' || character == '-') {
				mCurToken =  recognizeNumberToken(mInputString.substring(mCurPosition));
				if (mCurToken != null) {
					return mCurToken;
				} else {
					System.out.println("Test");
					mCurToken = recognizeSymbolToken(mInputString.substring(mCurPosition));
					return mCurToken;
				}	
			}
			if (Character.isLetter(character) || Character.isDigit(character) 
					|| isSpecialSymbol(character)) {
				mCurToken = recognizeSymbolToken(mInputString.substring(mCurPosition));
				return mCurToken;
			}
			
			if (Character.isWhitespace(character)) {
				mCurPosition+=1;
			}
		} 
		
		return null;
	}
	
	/**
	 * Recognizes a number token.
	 * @param mInputString; starts with either a digit, a + or a - sign.
	 * @return a token object
	 */
	private Token recognizeNumberToken(String mInputString) {
		char character = mInputString.charAt(0);
		String value = Character.toString(character);
		if (mInputString.length() == 1 && Character.isDigit(character)) {
			mCurPosition++;
			return new Token(Type.NUMBER, value);
		} else if (mInputString.length() == 1 && (character == '+' || character == '-')) {
			return null;
		} else {
			mCurPosition++;
			char curChar = character;
			for (int i = 1; i < mInputString.length();) {
				char prevChar = curChar;
				curChar = mInputString.charAt(i);
				if (!(Character.isWhitespace(curChar)) && Character.isDigit(curChar)) {
					value+=curChar;
					i++;
					mCurPosition++;
					}
				else if (Character.isDigit(prevChar) && 
						(Character.isWhitespace(curChar) || (curChar == '(' || curChar == ')'))){
					return new Token(Type.NUMBER, value); } 
				else {
					mCurPosition--;
					break;
				}
			} if (Character.isDigit(curChar)) {
				return new Token(Type.NUMBER, value);}
		}
		return null;
	}
	
	/**
	 * Recognizes a symbol token.
	 * @param mInputString starts with either a letter, a digit, or a special symbol
	 * @return a token object
	 */
	private Token recognizeSymbolToken(String mInputString) {
		char character = mInputString.charAt(0);
		mCurPosition++;
		String value = Character.toString(character);
		if (mInputString.length() == 1) {
			return new Token(Type.SYMBOL, value);
		} else {
			for (int i = 1; i < mInputString.length();) {
				character = mInputString.charAt(i);
				System.out.println(character);
				if (!(Character.isWhitespace(character)) && 
						(Character.isLetter(character) || Character.isDigit(character) 
						|| isSpecialSymbol(character))) {
					value+=character;
					i++;
					mCurPosition++;
				} else if (Character.isWhitespace(character)){
					System.out.println("This value" + value);
					return new Token(Type.SYMBOL, value);
				} 
			} 
			return new Token(Type.SYMBOL, value);
		}
	}
	
	/**
	 * Checks if character is a special symbol
	 * @param c
	 * @return boolean value
	 */
	private boolean isSpecialSymbol(char c) {
		char[] specialSymbols = {'+', '-', '*', '/', '@', '$', '%', '^', 
				'&', '_', '=', '<', '>', '~', '.'};
		for (char ch : specialSymbols) {
			if (c == ch) {
				return true;
			} 
		}	
		return false;
	}
	
	/**
	 * Moves the index to the next position 
	 * Assigns a new c if the current character is a Whitespace character
	 */
	private char nextAfterWhitespace(String str) {
		while (Character.isWhitespace(str.charAt(mCurPosition))) {
			mCurPosition++;
		}
		return mInputString.charAt(mCurPosition);
	}
	
	/**
	 * Returns an ArrayList of tokens
	 * @return
	 */
	public ArrayList<Token> allTokens() {
		ArrayList<Token> allTokens = new ArrayList<>();
		Token token = this.getNextToken();
		while (token.getType() != Type.EOF) {
			System.out.println("Testing");
			allTokens.add(token);
			
			token = this.getNextToken();
			System.out.println(token.toString());
		}
		allTokens.add(token);
		/**for (Token t : allTokens) {
			System.out.println(t.toString());
		}**/
		return allTokens;
	}
 
}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	