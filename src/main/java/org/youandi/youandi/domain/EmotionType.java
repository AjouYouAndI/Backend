package org.youandi.youandi.domain;

public enum EmotionType {

    HORROR(Values.HORROR),
    FRIGHTEN(Values.FRIGHTEN),
    ANGRY(Values.ANGRY),
    SAD(Values.SAD),
    DEFAULT(Values.DEFAULT),
    HAPPY(Values.HAPPY),
    HATE(Values.HATE);

    private String value;

    EmotionType(String val) {
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Incorrect use of EmotionType");
        }
    }

    public static class Values {
        public static final String HORROR = "HORROR";
        public static final String FRIGHTEN = "FRIGHTEN";
        public static final String ANGRY = "ANGRY";
        public static final String SAD = "SAD";
        public static final String DEFAULT = "DEFAULT";
        public static final String HAPPY = "HAPPY";
        public static final String HATE = "HATE";
    }
}
