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
		//System.out.println(mCurPosition);
		
		//if the mCurPosition is less than the length of the string
		//and is not a whitespace character 
		if (mCurPosition == mInputString.length()) {
			mCurPosition++;
			return new Token(Type.EOF, null); 
		} else if (mCurPosition < mInputString.length()) {
			 character = nextAfterWhitespace(mInputString.charAt(mCurPosition));
			 
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
			 //System.out.println("TTrue");
			if (Character.isDigit(character) || character == '+' || character == '-') {
				//System.out.println("TTrue");
				mCurToken =  recognizeNumberToken(mInputString.substring(mCurPosition));
				if (mCurToken != null) {
					return mCurToken;
				} else {
					mCurToken = recognizeSymbolToken(mInputString.substring(mCurPosition));
					return mCurToken;
				}	
			}
			//System.out.println("FTrue");
			if (Character.isLetter(character) || Character.isDigit(character) 
					|| isSpecialSymbol(character)) {
				//System.out.println("TestTrue");
				mCurToken = recognizeSymbolToken(mInputString.substring(mCurPosition));
				return mCurToken;
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
		//System.out.println("True");
		String value = Character.toString(character);
		if (mInputString.length() == 1 && Character.isDigit(character)) {
			mCurPosition++;
			return new Token(Type.NUMBER, value);
		} else if (mInputString.length() == 1 && (character == '+' || character == '-')) {
			return null;
		} else {
			//System.out.println("True");
			mCurPosition++;
			char curChar = character;
			for (int i = 1; i < mInputString.length();) {
				char prevChar = curChar;
				//System.out.println(prevChar);
				curChar = mInputString.charAt(i);
				//System.out.println(curChar);
				if (!(Character.isWhitespace(curChar)) && Character.isDigit(curChar)) {
					value+=curChar;
					//System.out.println(value);
					i++;
					//System.out.println(i);
					mCurPosition++;
					//System.out.println(mCurPosition); 
					}
				else if (Character.isDigit(prevChar)){
					return new Token(Type.NUMBER, value); } 
				else {
					mCurPosition--;
					break;
				}
			} if (Character.isDigit(curChar)) {
				return new Token(Type.NUMBER, value);}
		}
		//System.out.println("isnull");
		//System.out.println(mCurPosition);
		return null;
	}
	
	/**
	 * Recognizes a symbol token.
	 * @param mInputString starts with either a letter, a digit, or a special symbol
	 * @return a token object
	 */
	private Token recognizeSymbolToken(String mInputString) {
		char character = mInputString.charAt(0);
		//System.out.println("ishere");
		mCurPosition++;
		String value = Character.toString(character);
		//System.out.println("Test");
		if (mInputString.length() == 1) {
			return new Token(Type.SYMBOL, value);
		} else {
			//System.out.println(mInputString.length());
			for (int i = 1; i < mInputString.length();) {
				character = mInputString.charAt(i);
				//System.out.println(character);
				if (!(Character.isWhitespace(character)) && 
						(Character.isLetter(character) || Character.isDigit(character) 
						|| isSpecialSymbol(character))) {
					value+=character;
					//System.out.println(value);
					i++;
					//System.out.println(i);
					mCurPosition++;
					//System.out.println(mCurPosition);
				} else {
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
		//System.out.println("STrue");
		char[] specialSymbols = {'+', '-', '*', '/', '@', '$', '%', '^', 
				'&', '_', '=', '<', '>', '~', '.'};
		for (char ch : specialSymbols) {
			if (c == ch) {
				//System.out.println("AnsIsTrue");
				return true;
			} 
		}	
		return false;
	}
	
	/**
	 * Moves the index to the next position 
	 * Assigns a new c if the current character is a Whitespace character
	 */
	private char nextAfterWhitespace(char c) {
		if (Character.isWhitespace(c)) {
			mCurPosition++;
			//System.out.println(mCurPosition);
			return mInputString.charAt(mCurPosition);
		} else {
			//System.out.println("True");
			//System.out.println(c);
			return c;
		}
	}
	
	/**
	 * Returns an ArrayList of tokens
	 * @return
	 */
	public ArrayList<Token> allTokens() {
		ArrayList<Token> allTokens = new ArrayList<>();
		Token token = this.getNextToken();
		while (token.getType() != Type.EOF) {
			//System.out.println("Testing");
			allTokens.add(token);
			
			token = this.getNextToken();
			//System.out.println(token.toString());
		}
		allTokens.add(token);
		//for (Token t : allTokens) {
		//	System.out.println(t.toString());
		//}
		return allTokens;
	}
 
}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	