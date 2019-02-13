package analysis;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import util.Token;
import util.Type;

class TokenizerTest {
	
	protected Tokenizer mTokenizer;
	//Sample String
	protected String testString1;
	protected String testString2;
	protected String testString3;
	protected String testString4;
	protected String testString5;
	protected String testString6;
	protected String testString7;
	protected String testString8;
	
	
	public TokenizerTest() {
		testString1 = "(+ 3 4)";
		testString2 = "(< 3445 4)";
		testString3 = "(<= 5 5478)";
		testString4 = "+ght&";
		testString5 = "-";
		testString6 = "x";
		testString7 = "/78gh";
		testString8 = "list";
		mTokenizer = new Tokenizer(testString6);
	}
	

	@Test
	void assertNextToken() {
		Token token = mTokenizer.getNextToken();
		//Token token2 = mTokenizer.getNextToken();
		//Token token3 = mTokenizer.getNextToken();
		//System.out.println(token3.toString());
		//Token token4 = mTokenizer.getNextToken();
		//Token token5 = mTokenizer.getNextToken();
		//Token token6 = mTokenizer.getNextToken();
		assertEquals(token.toString(), "{SYMBOL x}");
	}
	
	@Test
	void assertAllTokens() {
		ArrayList<Token> tokenList = mTokenizer.allTokens();
		assertEquals(tokenList.get(0).toString(), "{SYMBOL x}");
	}

}