package analysis;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import util.Token;

public class EvalTest {
	
	protected static Tokenizer mTokenizer;
	protected static Parser mParser;
	protected static Eval mEvaluator = new Eval();
	protected static ArrayList<Token> mTokens;
	
	
	private static String eval(String input) throws EvalException {
		mTokenizer = new Tokenizer(input);
		mTokens = mTokenizer.allTokens();
		mParser = new Parser(mTokens);
		ExpressionNode syntaxTree = mParser.generateSyntaxTree();
		mEvaluator.setTree(syntaxTree);
		return (mEvaluator.evaluateTree().toString());
	}
	
	
	private static void assertException(Class<? extends Throwable> cls, Executable e, String msg)
	{
		assertEquals(msg, assertThrows(cls, e).getMessage());
	}
	
	
	@Test
	void testNumbers() throws IOException, EvalException
	{
		assertEquals("3", eval("3"));
		assertEquals("-1", eval("-1"));
		assertEquals("5", eval("+5"));
	}
	
	@Test
	void testArithmetic() throws IOException, EvalException
	{
		assertEquals("2", eval("(+ 1 1)"));
		assertEquals("10", eval("(+ 1 2 3 4)"));
		assertEquals("0", eval("(+)"));
		assertEquals("3", eval("(+ 3)"));
		assertEquals("-5", eval("(- 5)"));
		assertEquals("3", eval("(- 5 2)"));
		assertEquals("1", eval("(- 5 1 3)"));
		assertEquals("1", eval("(*)"));
		assertEquals("120", eval("(* 1 2 3 4 5)"));
		assertEquals("6", eval("(/ 48 8)"));
		assertEquals("0", eval("(/ 1 2)"));
		assertException(EvalException.class, () -> eval("(/ 1 0)"), "Argument 'divisor' is 0");
		assertException(EvalException.class, () -> eval("(/ 1)"), "/ takes two arguments");
	}
	
	@Test
	void testQuote() throws IOException, EvalException
	{
		assertEquals("HI", eval("'hi"));
		assertEquals("ARTICHOKE", eval("'Artichoke"));
		assertEquals("19", eval("'19"));
		assertEquals("19", eval("(+ '9 '10)"));
		assertEquals("HI", eval("(quote hi)"));
		assertEquals("(QUOTE HI)", eval("''hi"));
		assertEquals("(1 2 3)", eval("'(1 2 3)"));
		assertEquals("(+ 1 2)", eval("'(+ 1 2)"));
	}
	
	@Test
	void testEvalList()
	{
		assertException(EvalException.class, () -> eval("(1 2 3)"), "1 is not a function name. Try a symbol instead");
		assertException(EvalException.class, () -> eval("(+ (1 2) 3)"), "1 is not a function name. Try a symbol instead");
		assertException(EvalException.class, () -> eval("(+ '(1 2) 3)"), "Expecting a number as argument to + ");
	}
	
	@Test
	void testList() throws IOException, EvalException
	{
		assertEquals("(1 2 3)", eval("(list 1 2 3)"));
		assertEquals("(MY 3 SONS)", eval("(list 'my 3 'sons)"));
		assertEquals("((+ 2 1) 3)", eval("(list '(+ 2 1) (+ 2 1))"));
	}
	
	@Test
	void testNil() throws IOException, EvalException
	{
		assertEquals("NIL", eval("()"));
		assertEquals("NIL", eval("nil"));
		assertEquals("NIL", eval("'()"));
	}
	
	@Test
	void testCarCdrCons() throws IOException, EvalException
	{
		assertEquals("(A B C D)", eval("(cons 'a '(b c d))"));
		assertEquals("(A B)", eval("(cons 'a (cons 'b nil))"));
		assertEquals("A", eval("(car '(a b c))"));
		assertEquals("(B C)", eval("(cdr '(a b c))"));
		assertEquals("C", eval("(car (cdr (cdr '(a b c d))))"));
		assertEquals("(NIL B)", eval("(cons nil (cons 'b nil))"));
		assertEquals("NIL", eval("(cdr ())"));
		assertEquals("NIL", eval("(car ())"));
		assertException(EvalException.class, () -> eval("(cdr 3)"), "3 must be a list");
		//assertException(EvalException.class, () -> eval("(cons 3 a)"), "variable A is undefined");
		assertException(EvalException.class, () -> eval("(cons 3)"), "too few arguments for CONS (CONS 3)");
	}
	
	@Test
	void testPreds() throws IOException, EvalException
	{
		assertEquals("NIL", eval("(null '(a b c))"));
		assertEquals("NIL", eval("(null 5)"));
		assertEquals("NIL", eval("(null (+ 1 2))"));
		assertEquals("T", eval("(null ())"));
		assertEquals("T", eval("(null (cdr '(a)))"));
		assertEquals("T", eval("(listp '(a b c))"));
		assertEquals("T", eval("(listp nil)"));
		assertEquals("T", eval("(listp '(+ 1 2))"));
		assertEquals("NIL", eval("(listp (+ 1 2))"));
		assertEquals("NIL", eval("(listp t)"));
		assertEquals("NIL", eval("(listp 'listp)"));
	}
	
	@Test
	void testIfAndOrNot() throws IOException, EvalException
	{
		assertEquals("3", eval("(if (listp '(a b c)) (+ 1 2) (+ 5 6))"));
		assertEquals("11", eval("(if (listp 27) (+ 1 2) (+ 5 6))"));
		assertEquals("NIL", eval("(if (listp 27) (+ 2 3))"));
		assertEquals("5", eval("(if (listp nil) (+ 2 3) x)"));
		assertEquals("5", eval("(if (listp 'x) x (+ 2 3))"));
		assertEquals("3", eval("(and t (+ 1 2))"));
		assertEquals("NIL", eval("(and nil x)"));
		assertEquals("NIL", eval("(or)"));
		assertEquals("NIL", eval("(not t)"));
		assertEquals("T", eval("(= (+ 2 2) (* 2 2))"));
	}
	
	@Test
	void testDefun() throws IOException, EvalException
	{
		assertEquals("OUR-THIRD", eval("(defun our-third (x) (car (cdr (cdr x))))"));
		assertEquals("C", eval("(our-third '(a b c d))"));
		assertEquals("SUM-GREATER", eval("(defun sum-greater (x y z) (> (+ x y) z))"));
		assertEquals("T", eval("(sum-greater 1 4 3)"));
	}
	
	@Test
	void testRecursion() throws IOException, EvalException
	{
		assertEquals("OUR-MEMBER", eval("(defun our-member (obj lst)" +
	                                  "  (if (null lst)" +
				                            "    nil" +
	                                  "    (if (equal (car lst) obj)" +
				                            "        lst" +
				                            "        (our-member obj (cdr lst)))))"));
		assertEquals("(B C)", eval("(our-member 'b '(a b c))"));
		assertEquals("NIL", eval("(our-member 'z '(a b c))"));
	}
	
	@Test
	void testLet() throws IOException, EvalException
	{
		assertEquals("3", eval("(let ((x 1) (y 2)) (+ x y))"));
		assertEquals("NIL", eval("(let ())"));
		assertEquals("5", eval("(let ((x 5)) x)"));
		assertEquals("8", eval("(let ((x 5) (y 3)) (+ x y))"));
		assertEquals("(A)", eval("(let (tail ( b 'a)) (cons b tail))"));
		assertEquals("(A)", eval("(let ((tail) (b 'a)) (cons b tail))"));
		assertEquals("2", eval("(let ((a 5) (b 3)) (let ((a b) (b a)) (- b a)))"));
		assertException(EvalException.class, () -> eval("(let ((x 1) (y (+ x 1))) y)"), "variable X is undefined");
	}
	
	@Test
	void testSetq() throws IOException, EvalException
	{
		assertEquals("1", eval("(setq a 1)"));
		assertEquals("1", eval("a"));
		assertEquals("3", eval("(setq b 2 c 3)"));
		assertEquals("2", eval("b"));
		assertEquals("3", eval("c"));
		assertEquals("5", eval("(setq d 4 e (+ d 1))"));
		assertEquals("4", eval("d"));
		assertEquals("5", eval("e"));
		assertEquals("7", eval("(setq d 7)"));
		assertEquals("7", eval("d"));
		assertEquals("5", eval("e"));
		assertEquals("10", eval("(let ((a 10)) a)"));
		assertEquals("1", eval("a"));
		assertEquals("15", eval("(let ((a 11)) (setq a (+ a 4)) a)"));
		assertEquals("1", eval("a"));
		assertEquals("17", eval("(let ((a 17)) (setq f 19) a)"));
		assertEquals("1", eval("a"));
		assertEquals("19", eval("f"));
		assertEquals("(1 2 3 7 5 19)", eval("(list a b c d e f)"));
	}
	
	@Test
	void testDo() throws IOException, EvalException
	{
		assertEquals("NIL", eval("(do () (t))"));
		assertEquals("5", eval("(do ((x 1 (+ x 1))) ((>= x 5) x))"));
		assertEquals("6", eval("(do ((x 1 (+ x 1))) ((> x 5) x))"));
		assertEquals("10", eval("(setq a 10)"));
		assertEquals("DONE", eval("(do ((x 1 (+ x 1))) ((> x 5) 'done) (setq a (+ 2 a)))"));
		assertEquals("20", eval("a"));
		assertEquals("SNOC", eval("(defun snoc (xs x) (if (null xs) (list x) (cons (car xs) (snoc (cdr xs) x))))"));
		assertEquals("REVERSE", eval("(defun reverse (xs) (if (null xs) nil (snoc (reverse (cdr xs)) (car xs))))"));
		assertEquals("(1 4 9 16)", eval("(do ((lst '(1 2 3 4) (cdr lst)) (res nil (cons (* (car lst) (car lst)) res))) ((null lst) (reverse res)))"));
		assertEquals("NIL", eval("(do ((a 0)) (t))"));
		assertEquals("20", eval("a")); // checking that scopes are clean
	}
	
	@Test
	void testApply() throws IOException, EvalException
	{
		assertEquals("6", eval("(apply #'+ '(1 2 3))"));
		assertEquals("6", eval("(apply '+ '(1 2 3))"));
		assertEquals("7", eval("(apply #'(lambda (x y) (+ x (* 2 y))) '(3 2))"));
	}
	
	@Test
	void testMap() throws IOException, EvalException
	{
		assertEquals("MAPCAR", eval("(defun mapcar (f xs) (if (null xs) nil (cons (apply f (list (car xs))) (mapcar f (cdr xs)))))"));
		assertEquals("1+", eval("(defun 1+ (x) (+ x 1))"));
		assertEquals("SQUARE", eval("(defun square (x) (* x x))"));
		assertEquals("(2 3 4)", eval("(mapcar #'1+ '(1 2 3))"));
		assertEquals("(1 4 9)", eval("(mapcar #'square '(1 2 3))"));
		assertEquals("COMPOSE", eval("(defun compose (f1 f2) (defun c (x) (apply f1 (list (apply f2 (list x))))) (function c))"));
		assertEquals("(2 5 10)", eval("(mapcar (compose #'1+ #'square) '(1 2 3))"));
		assertEquals("(4 9 16)", eval("(mapcar (compose #'square #'1+) '(1 2 3))"));
	}
	
	@Test
	void testEval() throws IOException, EvalException {
		
	}
}
