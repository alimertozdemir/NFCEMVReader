package com.gt.alimert.emvnfclib.data.model;

import java.util.Map;

/**
 * @author AliMertOzdemir
 * @class TransactionResponse
 * @created 28.04.2020
 */
public class TransactionResponse {

    private String responseCode;
    private String transferId;
    private Map<String, String> genericField;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public Map<String, String> getGenericField() {
        return genericField;
    }

    public void setGenericField(Map<String, String> genericField) {
        this.genericField = genericField;
    }
}
