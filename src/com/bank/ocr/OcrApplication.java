package com.bank.ocr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OcrApplication {

    private static final int chunkSize = 3;
    private static final int lineLength = 27;
    private static final int noLinesPerEntry = 4;

    OcrApplication() {
    }

    Function<AccountNumber, String> getStateFunction() {
        return (accountNumber) -> {
            if (accountNumber.isValid()) {
                return AccountStatusCode.VALID.getStatusCode();
            }
            if (accountNumber.isIllegible()) {
                return AccountStatusCode.ILLEGIBLE.getStatusCode();
            }
            if (accountNumber.isInvalid()) {
                return AccountStatusCode.ERRONEOUS.getStatusCode();
            }
            return AccountStatusCode.UNKNOWN.getStatusCode();
        };
    }

    Function<AccountNumber, String> getCorrectionAwareStateFunction() {
        return (accountNumber) -> {
            if (accountNumber.isValid()) {
                return AccountStatusCode.VALID.getStatusCode();
            }
            if (accountNumber.isAmbiguous()) {
                return AccountStatusCode.AMBIGUOUS.getStatusCode();
            }

            return AccountStatusCode.ILLEGIBLE.getStatusCode();
        };
    }

    List<AccountNumber> correctAccountNumbers(List<AccountNumber> accountNumbers) {
        List<AccountNumber> correctedAccountNumbers = new ArrayList<>();

        for (AccountNumber accountNumber : accountNumbers) {
            System.out.println("Parsed account number: " + accountNumber.toString());
            System.out.println("Account number is valid: " + accountNumber.isValid());
            System.out.println("Account number is illegible: " + accountNumber.isIllegible());

            if (accountNumber.isIllegible() || accountNumber.isInvalid()) {
                Optional<AccountNumber> validAlternativeAccountNumber = accountNumber.getValidAlternativeAccountNumber();
                correctedAccountNumbers.add(validAlternativeAccountNumber.orElse(accountNumber));
            } else {
                correctedAccountNumbers.add(accountNumber);
            }
        }

        return correctedAccountNumbers;
    }

    void writeAccountNumbersToFile(String fileName, List<AccountNumber> accountNumbers, Function<AccountNumber, String> stateFunction) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (AccountNumber accountNumber : accountNumbers) {
                StringBuilder output = new StringBuilder(accountNumber.toString());

                String state = stateFunction.apply(accountNumber);

                output.append(" " + state + System.lineSeparator());
                fileWriter.write(output.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    List<AccountNumber> readAccountNumbersFromFile(String fileName) {
        List<AccountNumber> accountNumbers = new ArrayList<>();

        try (Scanner s = new Scanner(new File(fileName))) {

            List<List<String>> entries = s.findAll("(.*\\R){" + noLinesPerEntry + "}")
                    .map(matchResult -> Arrays.asList(matchResult.group().split("\\R", noLinesPerEntry))).collect(Collectors.toList());

            for (List<String> entry : entries) {
                validateEntry(entry);
                List<String> digitEntries = entry.stream().filter(et -> !et.matches("\\R")).collect(Collectors.toList());

                AccountNumber accountNumber = parseAccountNumber(digitEntries);
                accountNumbers.add(accountNumber);
            }
        } catch (FileNotFoundException | OcrException e) {
            e.printStackTrace();
        }

        return accountNumbers;
    }

    private void validateEntry(List<String> entryLines) throws OcrException {
        if (entryLines.size() != noLinesPerEntry) {
            throw new OcrException("Invalid number of lines for entry!");
        }
        if (!entryLines.get(noLinesPerEntry - 1).isBlank()) {
            throw new OcrException("Last line in entry must be blank!");
        }

    }

    private AccountNumber parseAccountNumber(List<String> digitLines) throws OcrException {
        Pattern chunkPattern = Pattern.compile("(?<=\\G.{" + chunkSize + "})");

        if (digitLines.stream().anyMatch(entry -> entry.length() != lineLength)) {
            throw new OcrException("Invalid no characters for entry line!");
        }

        List<String> digitChunks = digitLines.stream().flatMap(chunkPattern::splitAsStream).collect(Collectors.toList());
        List<StringBuilder> chunksPerDigit = getChunksPerDigit(digitChunks);

        List<Digit> digits = chunksPerDigit.stream().map(stringBuilder -> Digit.create(stringBuilder.toString())).collect(Collectors.toList());
        return AccountNumber.create(digits);
    }

    private List<StringBuilder> getChunksPerDigit(List<String> digitChunks) {
        int noDigitsPerLine = lineLength / chunkSize;
        List<StringBuilder> chunksPerDigit = new ArrayList<>();

        for (int i = 0; i < digitChunks.size(); i++) {
            int digitIndex = i % noDigitsPerLine;
            String digitChunk = digitChunks.get(i);

            if (digitIndex >= chunksPerDigit.size()) {
                StringBuilder digitBuilder = new StringBuilder(digitChunk);
                chunksPerDigit.add(digitBuilder);
            } else {
                StringBuilder existingDigitBuilder = chunksPerDigit.get(digitIndex);
                existingDigitBuilder.append(digitChunk);
            }
        }

        return chunksPerDigit;
    }



}
