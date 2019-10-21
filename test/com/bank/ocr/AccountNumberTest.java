package com.bank.ocr;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountNumberTest {

    private AccountNumber createFromDigitPatterns(List<String> digitPatterns) {
        List<Digit> digitList = new ArrayList<>();
        for (String pattern : digitPatterns) {
            digitList.add(Digit.create(pattern));
        }
        return AccountNumber.create(digitList);
    }

    private AccountNumber createFromDigitPatternMultipleTimes(String pattern, int times) {
        List<String> digitPatterns = new ArrayList<>();
        for (int i = 0; i < times - 1; i++) {
            digitPatterns.add(pattern);
        }
        return createFromDigitPatterns(digitPatterns);
    }

    @Test
    void isValid() {
        AccountNumber accountNumber = createFromDigitPatternMultipleTimes(TestPatterns.patternZero.getPattern(), 9);
        assertEquals(true, accountNumber.isValid());
    }

    @Test
    void isInvalid() {
        AccountNumber accountNumber = createFromDigitPatternMultipleTimes(TestPatterns.patternNine.getPattern(), 9);
        assertEquals(true, accountNumber.isInvalid());
    }

    @Test
    void isIllegible() {
        List<String> digitPatterns = new ArrayList<>();
        digitPatterns.add(TestPatterns.patternIllegible.getPattern());
        AccountNumber accountNumber = createFromDigitPatterns(digitPatterns);
        assertEquals(true, accountNumber.isIllegible());
    }

    @Test
    void isAmbiguous() {
        AccountNumber accountNumber = createFromDigitPatternMultipleTimes(TestPatterns.patternNine.getPattern(), 9);
        assertEquals(true, accountNumber.isAmbiguous());
    }

    @Test
    void validAlternativeAccountNumber() {
        AccountNumber accountNumber = createFromDigitPatternMultipleTimes(TestPatterns.patternOne.getPattern(), 9);
        assertEquals(true, accountNumber.getValidAlternativeAccountNumber().isPresent());
    }

}