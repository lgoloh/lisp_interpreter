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
	
	public Tokenizer(String inputmInputString) {
		mInputString = inputmInputString;
		mCurPosition = 0;
		mCurToken = null;
	}
	
	public Token getNextToken() {
		char character = 0;
		
		if (mCurPosition < mInputString.length()) {
			 character = mInputString.charAt(mCurPosition);
		} else if (mCurPosition == mInputString.length()) {
			return new Token(Type.EOF, null);
		}
		
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
			mCurToken =  recognizeNumberToken(mInputString.substring(mCurPosition, mInputString.length()-1));
			return mCurToken;
		}
		
		if (Character.isLetter(character) || Character.isDigit(character) 
				|| isSpecialSymbol(character)) {
			mCurToken = recognizeSymbolToken(mInputString.substring(mCurPosition, mInputString.length()-1));
			return mCurToken;
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
		System.out.println("True");
		String value = Character.toString(character);
		if (mInputString.length() == 1 && Character.isDigit(character)) {
			mCurPosition++;
			return new Token(Type.NUMBER, value);
		} else if (mInputString.length() == 1 && (character == '+' || character == '-')) {
			System.out.println("True");
			return null;
		} else {
			mCurPosition++;
			System.out.println("True");
			for (int i = 1; i < mInputString.length();) {
				character = mInputString.charAt(i);
				if (!(Character.isWhitespace(character))) {
					if (Character.isDigit(character)) {
						value+=character;
						i++;
						mCurPosition++;
					}
				} else if (Character.isDigit(character)){
					return new Token(Type.NUMBER, value);
				}
			}
			
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
				if (!(Character.isWhitespace(character))) {
					value+=character;
					i++;
					mCurPosition++;
				} else {
					return new Token(Type.SYMBOL, value);
				}
			}
		}
		return null;  
	}
	
	/**
	 * Checks if character is a special symbol
	 * @param c
	 * @return boolean value
	 */
	private boolean isSpecialSymbol(char c) {
		char[] specialSymbols = {'+', '-', '*', '/', '@', '$', '%', '^', 
				'&', '_', '=', '<', '>', '~', '.'};
		return Arrays.asList(specialSymbols).contains(c);
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
		for (Token t : allTokens) {
			System.out.println(t.toString());
		}
		return allTokens;
	}
 
}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	