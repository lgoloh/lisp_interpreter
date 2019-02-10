package analysis;

import util.Token;
import util.Type;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Tokenizer {
	
	private int mCurPosition;
	private Token mCurToken;
	private String mInputString;
	
	public Tokenizer(String inputText) {
		mInputString = inputText;
		mCurPosition = 0;
		mCurToken = null;
	}
	
	public Token getNextToken(String text) {
		char character = 0;
		
		if (mCurPosition < text.length()) {
			 character = text.charAt(mCurPosition);
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
			return recognizeNumberToken(text.substring(mCurPosition, text.length()-1));
		}
		
		if (Character.isLetter(character) || Character.isDigit(character) 
				|| isSpecialSymbol(character)) {
			return recognizeSymbolToken(text.substring(mCurPosition, text.length()-1));
		}
		
		return null;
	}
	
	/**
	 * Recognizes a number token.
	 * @param text; starts with either a digit, a + or a - sign.
	 * @return a token object
	 */
	private Token recognizeNumberToken(String text) {
		char character = text.charAt(0);
		mCurPosition++;
		String value = Character.toString(character);
		
		//if the length of the text is greater than 1, that is 
		if (text.length() > 1) {
			for (int i = 1; i < text.length();) {
				character = text.charAt(i);
				if (!(Character.isWhitespace(character))) {
					if (Character.isDigit(character)) {
						value+=character;
						i++;
						mCurPosition++;
					}
				}
			}
			return new Token(Type.NUMBER, value);
		} else if (text.length() == 1 && Character.isDigit(character)){
			return new Token(Type.NUMBER, value);
		}
		return null;
	}
	
	/**
	 * Recognizes a symbol token.
	 * @param text starts with either a letter, a digit, or a special symbol
	 * @return a token object
	 */
	private Token recognizeSymbolToken(String text) {
		char character = text.charAt(0);
		mCurPosition++;
		String value = Character.toString(character);
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
 
}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	