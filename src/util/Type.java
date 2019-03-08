package util;


public enum Type {
	SOE ("SOE"),
	EOE ("EOE"),
	EOF ("EOF"),
	NUMBER ("Number"),
	SYMBOL ("Symbol");
	
	private final String typeValue;
	
	private Type (String value) {
		this.typeValue = value;
	}
	
	String getTypeValue() {
		return typeValue;
	}
}
