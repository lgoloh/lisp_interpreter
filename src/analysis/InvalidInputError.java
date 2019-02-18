package analysis;

public class InvalidInputError extends RuntimeException {
	
	public InvalidInputError(String errorMessage) {
		super(errorMessage);
	}
	
}
