package com.bank.ocr;

public enum TestPatterns {

    patternZero(" _ | ||_|"),
    patternOne("     |  |"),
    patternTwo(" _  _||_ "),
    patternThree(" _  _| _|"),
    patternFour("   |_|  |"),
    patternFive(" _ |_  _|"),
    patternSix(" _ |_ |_|"),
    patternSeven(" _   |  |"),
    patternEight(" _ |_||_|"),
    patternNine(" _ |_| _|"),
    patternOneTwoThree("    _  _ \\n  | _| _|\\n  ||_  _|\\n\\n"),
    patternIllegible("_   |  %");


    private final String pattern;

    TestPatterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
