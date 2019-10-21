package com.bank.ocr;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DigitTest {

    private static final List<String> oneToNine = new ArrayList<>();

    @BeforeAll
    public static void setupAll() {
        oneToNine.add(TestPatterns.patternZero.getPattern());
        oneToNine.add(TestPatterns.patternOne.getPattern());
        oneToNine.add(TestPatterns.patternTwo.getPattern());
        oneToNine.add(TestPatterns.patternThree.getPattern());
        oneToNine.add(TestPatterns.patternFour.getPattern());
        oneToNine.add(TestPatterns.patternFive.getPattern());
        oneToNine.add(TestPatterns.patternSix.getPattern());
        oneToNine.add(TestPatterns.patternSeven.getPattern());
        oneToNine.add(TestPatterns.patternEight.getPattern());
        oneToNine.add(TestPatterns.patternNine.getPattern());
    }

    @Test
    void numericValuesZeroToNine() {
        for (int i = 0; i < oneToNine.size(); i++) {
            Digit digit = Digit.create(oneToNine.get(i));
            assertEquals(i, digit.getNumericValue());
        }
    }

    @Test
    void patternIsIllegible() {
        Digit digit = Digit.create(TestPatterns.patternIllegible.getPattern());
        assertTrue(digit.isIllegible());
    }

}