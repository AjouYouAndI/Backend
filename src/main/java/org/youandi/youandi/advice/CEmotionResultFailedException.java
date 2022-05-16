package org.youandi.youandi.advice;

public class CEmotionResultFailedException extends RuntimeException{
    public CEmotionResultFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CEmotionResultFailedException(String msg) {
        super(msg);
    }

    public CEmotionResultFailedException() {
        super();
    }
}
