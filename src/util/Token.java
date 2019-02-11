package util;

public class Token {
	
	private Type mType;
	private String mValue;
	
	public Token(Type type, String val) {
		mType = type;
		mValue = val;
	}
	
	@Override
	public String toString() {
		return "{" + mType + " " + mValue + "}";
	}
	
	public Type getType() {
		return mType;
	}
	
	public String getValue() {
		return mValue;
	}
	
}

