package xyz.sadiulhakim.exception;

import java.io.Serial;

public class UnsupportedActivityException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -4998390560011309742L;

	public UnsupportedActivityException(String msg) {
		super(msg);
	}
}
