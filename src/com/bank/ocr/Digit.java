package com.bank.ocr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Digit {

    private String pattern;

    private static final Map<String, Character> DIGIT_PATTERNS = new HashMap<>();
    private static final Character ILLEGIBLE_DIGIT = '?';

    static {
        DIGIT_PATTERNS.put(" _ | ||_|", '0');
        DIGIT_PATTERNS.put("     |  |", '1');
        DIGIT_PATTERNS.put(" _  _||_ ", '2');
        DIGIT_PATTERNS.put(" _  _| _|", '3');
        DIGIT_PATTERNS.put("   |_|  |", '4');
        DIGIT_PATTERNS.put(" _ |_  _|", '5');
        DIGIT_PATTERNS.put(" _ |_ |_|", '6');
        DIGIT_PATTERNS.put(" _   |  |", '7');
        DIGIT_PATTERNS.put(" _ |_||_|", '8');
        DIGIT_PATTERNS.put(" _ |_| _|", '9');
    }

    private Digit(String pattern) {
        this.pattern = pattern;
    }

    static Digit create(String pattern) {
        return new Digit(pattern);
    }

    private Character getAsCharacter() {
        return DIGIT_PATTERNS.getOrDefault(pattern, ILLEGIBLE_DIGIT);
    }

    boolean isIllegible() {
        return getAsCharacter().equals(ILLEGIBLE_DIGIT);
    }

    boolean isValid() {
        return !isIllegible();
    }

    int getNumericValue() {
        return Character.getNumericValue(getAsCharacter());
    }

    @Override
    public String toString() {
        return getAsCharacter().toString();
    }

    List<Digit> generateValidPermutations(String search, String replacement) {
        List<Digit> permutations = new ArrayList<>();
        List<Integer> indicesOfSearchString = getIndicesOfString(search);

        for (Integer index : indicesOfSearchString) {
            Digit digit = generatePermutation(index, replacement);
            if (digit.isValid()) {
                permutations.add(digit);
                //System.out.println("with " + replacement + " valid permutation for digit " + getAsCharacter() + ": " + sb + ". As character: " + digit.getAsCharacter());
            }
        }

        return permutations;
    }

    private Digit generatePermutation(Integer index, String replacement) {
        StringBuilder sb = new StringBuilder(pattern);
        sb.replace(index, index + 1, replacement);
        return create(sb.toString());
    }

    private List<Integer> getIndicesOfString(String input) {
        List<Integer> indices = new ArrayList<>();
        int index = pattern.indexOf(input);

        while (index >= 0) {
            indices.add(index);
            index = pattern.indexOf(input, index + 1);
        }

        return indices;
    }


}
