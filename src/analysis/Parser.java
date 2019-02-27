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
				if (thistoken.getType() == Type.SOE) {
					if (mSyntaxTree2 != null) {
						mSyntaxTree2.getnodeList().add(new ListNode(thistoken, nodeList(mTokens)));
					} else if (mSyntaxTree2 == null) {
						mSyntaxTree2 = new ListNode(thistoken, nodeList(mTokens));
					}
					mCurPosition++;
				} else if (thistoken.getType() == Type.SYMBOL) {
					String symbol = mTokens.get(mCurPosition).getValue();
					SymbolNode sym = new SymbolNode(symbol, new ArrayList<>());
					if (mSyntaxTree2 != null) {
						mSyntaxTree2.getnodeList().add(sym);
					} else if (mSyntaxTree2 == null) {
						mSyntaxTree2 = sym;
					}
					mCurPosition++;
				} else if (thistoken.getType() == Type.NUMBER) {
					int number = Integer.valueOf(mTokens.get(0).getValue());
					NumberNode num = new NumberNode(number, new ArrayList<>());
					if (mSyntaxTree2 != null) {
						mSyntaxTree2.getnodeList().add(num);
					} else if (mSyntaxTree2 == null) {
						mSyntaxTree2 = num;
					}
					mCurPosition++;
				} else if (thistoken.getType() == Type.EOE ||
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
	public ArrayList<ExpressionNode> nodeList(ArrayList<Token> tokens) {
		ArrayList<ExpressionNode> nodes = new ArrayList<>();
		mCurPosition+=1;
		Token tkn = tokens.get(mCurPosition);
		while (tkn.getType() != Type.EOE && tkn.getType() != Type.EOF) {
			int prevIndex = mCurPosition-1;
			if (tkn.getType() == Type.NUMBER) {
				int number = Integer.valueOf(tkn.getValue());
				if (nodes.get(prevIndex).getValue() == "'") {
					NumberNode num = new NumberNode(number, new ArrayList<>());
					nodes.get(prevIndex).getnodeList().add(num);
				} else {
					NumberNode num = new NumberNode(number, new ArrayList<>());
					nodes.add(num);
				}
				mCurPosition++;
				tkn = tokens.get(mCurPosition);
			} else if (tkn.getType() == Type.SYMBOL) {
				String symbol = tkn.getValue();
				if (nodes.get(prevIndex).getValue() == "'") {
					SymbolNode sym = new SymbolNode(symbol, new ArrayList<>());
					nodes.get(prevIndex).getnodeList().add(sym);
				} else {
					SymbolNode sym = new SymbolNode(symbol, new ArrayList<>());
					nodes.add(sym);
				}
				mCurPosition++;
				tkn = tokens.get(mCurPosition);
			} else if (tkn.getType() == Type.SOE) {
				if (nodes.get(prevIndex).getValue() == "'") {
					ListNode newExpr = new ListNode(tkn, nodeList(mTokens));
					nodes.get(prevIndex).getnodeList().add(newExpr);
				} else {
					ListNode newExpr = new ListNode(tkn, nodeList(mTokens));
					nodes.add(newExpr);
				}
				mCurPosition++;
				tkn = tokens.get(mCurPosition);
			}
		}
		return nodes;
		
	}
	
	public ArrayList<Token> getNestedExpression(int i, ArrayList<Token> tokens){
		ArrayList<Token> nestedExpression = new ArrayList<>();
		Token token = tokens.get(i);
		while (token.getType() != Type.EOE) {
			nestedExpression.add(token);
			i++;
			token = tokens.get(i);
		}
		nestedExpression.add(tokens.get(i));
		return nestedExpression;
	}
	
	
}
