package com.bank.ocr;

public enum AccountStatusCode {

    AMBIGUOUS("AMB"),
    ILLEGIBLE("ILL"),
    ERRONEOUS("ERR"),
    UNKNOWN("???"),
    VALID("");

    private final String statusCode;

    AccountStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
