package com.bank.ocr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountNumber {

    private List<Digit> digits;

    private AccountNumber(List<Digit> digits) {
        this.digits = digits;
    }

    static AccountNumber create(List<Digit> digits) {
        return new AccountNumber(digits);
    }

    private int calculateChecksum() {
        List<Digit> digitsReversed = new ArrayList<>(digits);
        Collections.reverse(digitsReversed);

        int index = 1;
        int checksum = 0;

        for (Digit digit : digitsReversed) {
            int numericValue = digit.getNumericValue();
            checksum += index * numericValue;
            index++;
        }

        checksum %= 11;

        return checksum;
    }

    boolean isValid() {
        if (isIllegible()) {
            return false;
        }
        int checksum = calculateChecksum();
        return checksum == 0;
    }

    boolean isInvalid() {
        return !isValid();
    }

    boolean isIllegible() {
        return digits.stream().anyMatch(Digit::isIllegible);
    }

    boolean isAmbiguous() {
        List<AccountNumber> validAlternativeAccountNumbers = generateValidAlternativeAccountNumbers();
        return validAlternativeAccountNumbers.size() > 1;
    }

    /**
     * For each digit, create permutations by adding or removing one single underscore or pipe character at a time.
     * Create a new account number for each permutation.
     */
    private List<AccountNumber> generateAlternativeAccountNumbers() {
        List<AccountNumber> alternativeAccountNumbers = new ArrayList<>();

        for (Digit digit : digits) {
            List<Digit> permutationsUnderscoreAdded = digit.generateValidPermutations(" ", "_");
            List<Digit> permutationsPipeAdded = digit.generateValidPermutations(" ", "|");
            List<Digit> permutationsUnderscoreRemoved = digit.generateValidPermutations("_", " ");
            List<Digit> permutationsPipeRemoved = digit.generateValidPermutations("|", " ");

            List<Digit> allPermutations = new ArrayList<>();
            allPermutations.addAll(permutationsUnderscoreAdded);
            allPermutations.addAll(permutationsPipeAdded);
            allPermutations.addAll(permutationsUnderscoreRemoved);
            allPermutations.addAll(permutationsPipeRemoved);

            alternativeAccountNumbers.addAll(generateAlternativeAccountNumbersForPermutations(allPermutations, digit));
        }
        return alternativeAccountNumbers;
    }

    private List<AccountNumber> generateAlternativeAccountNumbersForPermutations(List<Digit> permutations, Digit inputDigit) {
        List<AccountNumber> alternativeAccountNumbers = new ArrayList<>();

        for (Digit newDigit : permutations) {
            List<Digit> newDigits = new ArrayList<>(digits);
            int targetIndex = digits.indexOf(inputDigit);
            newDigits.remove(targetIndex);
            newDigits.add(targetIndex, newDigit);
            alternativeAccountNumbers.add(create(newDigits));
        }

        return alternativeAccountNumbers;
    }

    Optional<AccountNumber> getValidAlternativeAccountNumber() {
        List<AccountNumber> validAlternativeAccountNumbers = generateValidAlternativeAccountNumbers();

        if (validAlternativeAccountNumbers.size() == 1) {
            System.out.println("Found unique alternative account number: " + validAlternativeAccountNumbers.get(0).toString());
            return Optional.ofNullable(validAlternativeAccountNumbers.get(0));
        }
        return Optional.empty();
    }

    private List<AccountNumber> generateValidAlternativeAccountNumbers() {
        List<AccountNumber> alternativeAccountNumbers = generateAlternativeAccountNumbers();
        return alternativeAccountNumbers.stream().filter(AccountNumber::isValid).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return digits.stream().map(Digit::toString).collect(Collectors.joining());
    }
}
