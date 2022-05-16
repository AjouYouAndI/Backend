package org.youandi.youandi.advice;

public class CPostNotExistedException extends RuntimeException{
    public CPostNotExistedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CPostNotExistedException(String msg) {
        super(msg);
    }

    public CPostNotExistedException() {
        super();
    }
}
