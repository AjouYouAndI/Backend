package org.youandi.youandi.advice;

public class CRefreshTokenException extends RuntimeException{
    public CRefreshTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public CRefreshTokenException(String msg) {
        super(msg);
    }

    public CRefreshTokenException() {
        super();
    }
}
