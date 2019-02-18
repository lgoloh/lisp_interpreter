package analysis;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import util.Token;
import util.Type;


public class ParserTest {
	
	protected Parser mParser1;
	protected Parser mParser2;
	protected Parser mParser3;
	protected Parser mParser4;
	protected Parser mParser5;
	protected Tokenizer mTokenizer1;
	protected Tokenizer mTokenizer2;
	protected Tokenizer mTokenizer3;
	protected Tokenizer mTokenizer4;
	protected Tokenizer mTokenizer5;
	protected String testString1;
	protected String testString2;
	protected String testString3;
	protected String testString4;
	protected String testString5;
	protected String testString6;
	protected ArrayList<Token> mTokens1;
	protected ArrayList<Token> mTokens2;
	protected ArrayList<Token> mTokens3;
	protected ArrayList<Token> mTokens4;
	protected ArrayList<Token> mTokens5;
	protected ArrayList<Token> mTokens6;
	protected ArrayList<Token> mTokens7;
	
	public ParserTest() {
		testString1 = "(+ 3 4)";
		testString2 = "(< 3 (+ 3445 4))";
		testString3 = "(<= (+ 4 5) (- 3 2))";
		testString4 = "(listp (1 2 3 4))";
		testString5 = "((- 5 5) (< 4 5) (+ 3 (+ 5 6)) 5 7 (- 3 5))";
		testString6 = "3";
		mTokenizer1 = new Tokenizer(testString1);
		mTokenizer2 = new Tokenizer(testString3);
		mTokenizer3 = new Tokenizer(testString4);
		mTokenizer4 = new Tokenizer(testString5);
		mTokenizer5 = new Tokenizer(testString6);
		mTokens1 = mTokenizer1.allTokens();
		mTokens2 = mTokenizer2.allTokens();
		mTokens4 = mTokenizer3.allTokens();
		mTokens5 = mTokenizer4.allTokens();
		mTokens6 = mTokenizer5.allTokens();
		mParser1 = new Parser(mTokens1);
		mParser2 = new Parser(mTokens2);
		mParser3 = new Parser(mTokens4);
		mParser4 = new Parser(mTokens5);
		mParser5 = new Parser(mTokens6);
	}
	
	@Test
	void assertSOECount() {
		int count = mParser4.count_SOE();
		assertEquals(6, count);
	}
	
	@Test
	void assertEOECount() {
		int count = mParser4.count_EOE();
		assertEquals(6, count);
	}
	
	@Test 
	void assetSyntaxTree() {
		ArrayList<ExpressionNode> syntaxtree = mParser1.generateSyntaxTree();
		assertEquals(syntaxtree.get(0).getToken().toString(), "{SYMBOL +}");
	}

}
