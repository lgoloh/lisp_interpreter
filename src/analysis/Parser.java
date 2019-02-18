package analysis;

import java.util.ArrayList;

import util.Token;
import util.Type;

public class Parser {
	
	private ArrayList<Token> mTokens;
	private ArrayList<ExpressionNode> mSyntaxTree;
	private int mCurPosition = 0;
	
	public Parser(ArrayList<Token> tokens) {
		mTokens = tokens;
	}
	
	/**
	 * Generate S-expression AST; to be traversed by Inorder traversal
	 */
	public ArrayList<ExpressionNode> generateSyntaxTree() {
		int numOfSOE = count_SOE();
		int numOfEOE = count_EOE();
		
		if (numOfSOE == 0 && numOfSOE == numOfEOE) {
			mSyntaxTree = new ArrayList<>();
		} else if (numOfSOE == 0 && numOfSOE != numOfEOE){
			throw new InvalidInputError("No Matching Opening Parenthesis");
		}
		
		if (numOfSOE == numOfEOE) {
			//System.out.println(mTokens.size());
			while (mCurPosition < mTokens.size()) {
				//System.out.println(mCurPosition);
				Token thistoken = mTokens.get(mCurPosition);
				if (thistoken.getType() == Type.SOE) {
					///ArrayList<Token> expr = getNestedExpression(mCurPosition, mTokens);
					mSyntaxTree = nodeList(mTokens);
					System.out.println(mSyntaxTree.toString());
					System.out.println("isithere?");
					mCurPosition++;
					Token tkn = mTokens.get(mCurPosition);
					System.out.println(tkn.toString());
					System.out.println(mCurPosition);
				} else if (thistoken.getType() == Type.SYMBOL || 
						thistoken.getType() == Type.NUMBER){
					AtomNode atom = new AtomNode(thistoken, null);
					mSyntaxTree.add(atom);
					mCurPosition++;
				} else if (thistoken.getType() == Type.EOE ||
						thistoken.getType() == Type.EOF) {
					System.out.println("blahblah");
					break;
				}
			}
		} else {
			throw new InvalidInputError("Non Matching Parenthesis");
		}
		System.out.println(mSyntaxTree.toString());
		//System.out.println("outoffunction");
		return mSyntaxTree;
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
		///int i = 1;
		mCurPosition+=1;
		Token tkn = tokens.get(mCurPosition);
		System.out.println(tkn.toString());
		System.out.println("test2" + mCurPosition);
		while (tkn.getType() != Type.EOE && tkn.getType() != Type.EOF) {
			if (tkn.getType() == Type.NUMBER || tkn.getType() == Type.SYMBOL) {
				System.out.println(tkn.toString());
				AtomNode atom = new AtomNode(tkn, null);
				nodes.add(atom);
				//i++;
				mCurPosition++;
				//System.out.println(mCurPosition);
				tkn = tokens.get(mCurPosition);
				//System.out.println(tkn.toString());
				//System.out.println("outofwhileloop");
			} else if (tkn.getType() == Type.SOE) {
				//System.out.println("nestedexpression");
				//System.out.println(mCurPosition);
				//ArrayList<Token> nestedExpression = getNestedExpression(mCurPosition, tokens);
				//System.out.println(nestedExpression.toString());
				ListNode newExpr = new ListNode(tkn, nodeList(mTokens));
				ArrayList<ExpressionNode> res = newExpr.getnodeList();
				//System.out.println(res.toString());
				for (ExpressionNode node : res) {
					System.out.println(node.getToken().toString());
				}
				nodes.add(newExpr);
				mCurPosition++;
				System.out.println(mCurPosition);
				System.out.println(tokens);
				tkn = tokens.get(mCurPosition);
				System.out.println(tkn.toString());
				System.out.println("thistest" + mCurPosition);
				System.out.println(nodes.toString());
			}
		}
		System.out.println("outofwhileloop");
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
		//System.out.println(i);
		return nestedExpression;
	}
	
	
}
