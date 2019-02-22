package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Parser {
	
	private ArrayList<Token> mTokens;
	private ExpressionNode mSyntaxTree2;
	private int mCurPosition = 0;
	
	public Parser(ArrayList<Token> tokens) {
		mTokens = tokens;
	}
	
	/**
	 * Generate S-expression AST;
	 */
	//public ArrayList<ExpressionNode> generateSyntaxTree() {
	public ExpressionNode generateSyntaxTree() {	
		int numOfSOE = count_SOE();
		int numOfEOE = count_EOE();
		
		if (numOfSOE == 0 && numOfSOE == numOfEOE) {
			mSyntaxTree2 = new AtomNode(mTokens.get(0), null);
			return mSyntaxTree2; 
		} else if (numOfSOE == 0 && numOfSOE != numOfEOE){
			throw new InvalidInputError("No Matching Opening Parenthesis");
		}
		
		if (numOfSOE == numOfEOE) {
			while (mCurPosition < mTokens.size()) {
				Token thistoken = mTokens.get(mCurPosition);
				if (thistoken.getType() == Type.SOE) {
					mSyntaxTree2 = new ListNode(thistoken, nodeList(mTokens));
					mCurPosition++;
					//Token tkn = mTokens.get(mCurPosition);
				} else if (thistoken.getType() == Type.SYMBOL || 
						thistoken.getType() == Type.NUMBER){
					AtomNode atom = new AtomNode(thistoken, null);
					mSyntaxTree2.getnodeList().add(atom);
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
			if (tkn.getType() == Type.NUMBER || tkn.getType() == Type.SYMBOL) {
				AtomNode atom = new AtomNode(tkn, null);
				nodes.add(atom);
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
