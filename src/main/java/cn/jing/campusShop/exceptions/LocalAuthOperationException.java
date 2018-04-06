package cn.jing.campusShop.exceptions;

public class LocalAuthOperationException extends RuntimeException {

	private static final long serialVersionUID = 7556321348050349366L;

	public LocalAuthOperationException(String msg) {
		super(msg);
	}
}
