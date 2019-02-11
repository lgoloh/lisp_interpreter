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
	
	
	public TokenizerTest() {
		testString1 = "(+ 3 4)";
		testString2 = "(< 34 4)";
		testString3 = "(<= 5 54)";
		mTokenizer = new Tokenizer(testString1);
	}
	

	@Test
	void assertNextToken() {
		Token token = mTokenizer.getNextToken();
		Token token2 = mTokenizer.getNextToken();
		Token token3 = mTokenizer.getNextToken();
		//System.out.println(token3.toString());
		assertEquals(token3.toString(), "{NUMBER 3}");
	}
	
	@Test
	void assertAllTokens() {
		ArrayList<Token> tokenList = mTokenizer.allTokens();
		assertEquals(tokenList.get(0).toString(), "{SOE (}");
	}

}