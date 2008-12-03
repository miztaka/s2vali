package jp.honestyworks.s2vali;

public class InvalidConventionRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5971803416263404921L;

	public InvalidConventionRuntimeException(String string) {
		super(string);
	}

	public InvalidConventionRuntimeException(String string, Throwable e) {
		super(string, e);
	}

}
