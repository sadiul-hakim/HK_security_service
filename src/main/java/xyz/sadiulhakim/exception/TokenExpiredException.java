package xyz.sadiulhakim.exception;

import java.io.Serial;

public class TokenExpiredException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1327197789919358869L;

	public TokenExpiredException(String msg) {
		super(msg);
	}
}
