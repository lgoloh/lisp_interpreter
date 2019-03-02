package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Parser {
	
	private ArrayList<Token> mTokens;
	private ExpressionNode mSyntaxTree2 = null;
	private int mCurPosition = 0;
	
	public Parser(ArrayList<Token> tokens) {
		mTokens = tokens;
	}
	
	/**
	 * Generate S-expression AST;
	 * TODO Change AtomNodes to NumberNodes and SymbolNodes as required 
	 */
	//public ArrayList<ExpressionNode> generateSyntaxTree() {
	public ExpressionNode generateSyntaxTree() {	
		int numOfSOE = count_SOE();
		int numOfEOE = count_EOE();

		if (numOfSOE == numOfEOE) {
			while (mCurPosition < mTokens.size()) {
				Token thistoken = mTokens.get(mCurPosition);
				ExpressionNode expr = getnextExpression(thistoken);
				if (mSyntaxTree2 != null) {
					mSyntaxTree2.getnodeList().add(expr);
					} else {
					mSyntaxTree2 = expr;
					}
				mCurPosition++;
				if (thistoken.getType() == Type.EOE ||
						thistoken.getType() == Type.EOF) {
					break;
					}
				}
			} else {
			throw new InvalidInputError("Non Matching Parenthesis");
			}
		return mSyntaxTree2;
	}
	
	
	public int count_SOE() {
		int count = 0;
		for (Token token : mTokens) {
			if (token.getType() == Type.SOE) 
				count++;
		}
		return count;
	}
	
	public int count_EOE() {
		int count = 0;
		for (Token token : mTokens) {
			if (token.getType() == Type.EOE) 
				count++;
		}
		return count;
	}
	
	/**
	 * Generates List of nodes from ArrayList of tokens 
	 * @param tokens
	 * @return
	 */
	private ArrayList<ExpressionNode> nodeList(ArrayList<Token> tokens) {
		ArrayList<ExpressionNode> nodes = new ArrayList<>();
		mCurPosition+=1;
		Token tkn = tokens.get(mCurPosition);
		while (tkn.getType() != Type.EOE && tkn.getType() != Type.EOF) {
			if (tkn.getType() == Type.NUMBER) {
				int number = Integer.valueOf(tkn.getValue());
				NumberNode num = new NumberNode(number, new ArrayList<>());
				nodes.add(num);
				mCurPosition++;
				tkn = tokens.get(mCurPosition);
			} else if (tkn.getType() == Type.SYMBOL) {
				String symbol = tkn.getValue();
				SymbolNode sym = new SymbolNode(symbol, new ArrayList<>());
				nodes.add(sym);
				mCurPosition++;
				tkn = tokens.get(mCurPosition);
			} else if (tkn.getType() == Type.SOE) {
				ListNode newExpr = new ListNode(tkn, nodeList(mTokens));
				nodes.add(newExpr);
				mCurPosition++;
				tkn = tokens.get(mCurPosition);
			}
		}
		return nodes;
		
	}
	
	/**public ArrayList<Token> getNestedExpression(int i, ArrayList<Token> tokens){
		ArrayList<Token> nestedExpression = new ArrayList<>();
		Token token = tokens.get(i);
		while (token.getType() != Type.EOE) {
			nestedExpression.add(token);
			i++;
			token = tokens.get(i);
		}
		nestedExpression.add(tokens.get(i));
		return nestedExpression;
	}**/
	
	public ExpressionNode expandQuote() {
		Token listtkn = new Token(Type.SOE, "(");
		SymbolNode quoteSymbol = new SymbolNode("quote", null);
		ArrayList<ExpressionNode> argList = new ArrayList<>();
		argList.add(0, quoteSymbol);
		argList.add(1, getnextExpression(mTokens.get(mCurPosition))); 
		return new ListNode(listtkn, argList);
	}
	
	private ExpressionNode getnextExpression(Token token) {
		if (token.getType() == Type.NUMBER) {
			int number = Integer.valueOf(token.getValue());
			return new NumberNode(number, new ArrayList<>());
		} else if (token.getType() == Type.SYMBOL) {
			String symbol = token.getValue();
			return new SymbolNode(symbol, new ArrayList<>());
		} else if (token.getType() == Type.SOE) {
			return new ListNode(token, nodeList(mTokens));
		}
		return null;
	}
	
	
}
