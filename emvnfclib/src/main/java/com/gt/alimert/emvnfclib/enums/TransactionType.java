package com.gt.alimert.emvnfclib.enums;

/**
 * @author AliMertOzdemir
 * @class TransactionType
 * @created 25.04.2020
 */
public enum TransactionType {

    SALES("00"), VOID("34"), REFUND("20"), SEARCH("40");

    private String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
