package analysis;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import util.Token;
import util.Type;

public class EvaluationTest {
	
	protected String testString1;
	protected String testString2;
	protected Tokenizer mTokenizer;
	protected Parser mParser;
	protected Evaluator mEvaluator;
	protected ArrayList<Token> mTokens;
	
	public EvaluationTest() {
		testString1 = "(+ 12(+ 1 (- 4 5) 2))";
		testString2 = "-345";
		mTokenizer = new Tokenizer(testString1);
		mTokens = mTokenizer.allTokens();
		mParser = new Parser(mTokens);
		mEvaluator = new Evaluator(mParser.generateSyntaxTree());
	}
	
	@Test
	void assert_evaluate_number() {
		Object result = mEvaluator.evaluateTree();
		assertEquals(14, result);
	}
	
	@Test
	void assert_evaluate_list() {
		
	}
	
}
