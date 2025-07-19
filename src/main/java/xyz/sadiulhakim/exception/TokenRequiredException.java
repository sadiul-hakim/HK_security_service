package xyz.sadiulhakim.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenRequiredException extends AuthenticationException {

    public TokenRequiredException() {
        super("Authorization Token is required to access resources.");
    }
}

