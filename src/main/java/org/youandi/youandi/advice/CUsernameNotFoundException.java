package org.youandi.youandi.advice;

public class CUsernameNotFoundException extends RuntimeException{
    public CUsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
    public CUsernameNotFoundException(String msg) {
        super(msg);
    }

    public CUsernameNotFoundException() {
        super();
    }
}
