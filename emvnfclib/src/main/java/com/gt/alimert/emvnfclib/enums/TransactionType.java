package com.gt.alimert.emvnfclib.enums;

/**
 * @author AliMertOzdemir
 * @class TransactionType
 * @created 25.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public enum TransactionType {

    SALE("00"), VOID("34"), REFUND("20"), SEARCH("40");

    private String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
