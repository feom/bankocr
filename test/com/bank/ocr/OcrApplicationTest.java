package com.bank.ocr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class OcrApplicationTest {

    private OcrApplication ocrApplication;

    @BeforeEach
    void setUp() {
        ocrApplication = new OcrApplication();
    }

    @AfterEach
    void tearDown() {
    }

    private List<AccountNumber> readAccountNumbersFromFile() {
        return ocrApplication.readAccountNumbersFromFile("accounts.txt");
    }

    @Test
    void readAccountNumbers() {
        List<AccountNumber> accountNumbers = readAccountNumbersFromFile();

    }

    @Test
    void writeAccountNumbers() {
        List<AccountNumber> accountNumbers = readAccountNumbersFromFile();
        ocrApplication.writeAccountNumbersToFile("accounts_output.txt", accountNumbers, ocrApplication.getStateFunction());

    }

    @Test
    void correctAccountNumbers() {
        List<AccountNumber> accountNumbers = readAccountNumbersFromFile();
        List<AccountNumber> correctedAccountNumbers = ocrApplication.correctAccountNumbers(accountNumbers);
        ocrApplication.writeAccountNumbersToFile("accounts_output_corrected.txt", correctedAccountNumbers, ocrApplication.getCorrectionAwareStateFunction());
    }

}