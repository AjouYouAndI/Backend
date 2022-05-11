package org.youandi.youandi.advice;

public class CAccountExistedException extends RuntimeException{
    public CAccountExistedException(String msg, Throwable t) {
        super(msg, t);
    }
    public CAccountExistedException(String msg) {
        super(msg);
    }

    public CAccountExistedException() {
        super();
    }
}
