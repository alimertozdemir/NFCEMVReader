package com.gt.alimert.emvnfclib.data.model;

import java.util.Map;

/**
 * @author AliMertOzdemir
 * @class TransactionRequest
 * @created 28.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class TransactionRequest {

    private String cardBrand;
    private String cardType;
    private String emvData;
    private String pan;
    private String expiryDate;
    private String track2Data;
    private String transactionType;
    private String amount;
    private String transferId;
    private String currencyCode;
    private String terminalId;
    private String merchantId;
    private Map<String, String> genericField;

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getEmvData() {
        return emvData;
    }

    public void setEmvData(String emvData) {
        this.emvData = emvData;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public void setTrack2Data(String track2Data) {
        this.track2Data = track2Data;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Map<String, String> getGenericField() {
        return genericField;
    }

    public void setGenericField(Map<String, String> genericField) {
        this.genericField = genericField;
    }
}
