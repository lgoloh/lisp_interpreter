package util;

import java.util.regex.Pattern;

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
