package com.gt.alimert.emvnfclib.model;

/**
 * @author AliMertOzdemir
 * @class EmvParam
 * @created 24.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class EmvParam {

    private byte[] transactionCurrencyCode = new byte[] {(byte) 0x09, (byte) 0x49};
    private byte[] applicationInterchangeProfile;
    private byte[] dedicatedFileName;
    // TODO: !!! Check TVR Value !!!
    private byte[] terminalVerificationResults = new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x00};
    private byte[] transactionDate;
    private byte[] transactionType;
    private byte[] amountAuthorized;
    private byte[] amountOther = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    private byte[] applicationVersionNumber = new byte[]{(byte) 0x00, (byte) 0x02};
    private byte[] issuerApplicationData;
    private byte[] terminalCountryCode = new byte[]{(byte) 0x07, (byte) 0x92};
    private byte[] interfaceDeviceSerialNumber;
    private byte[] applicationCryptogram;
    private byte[] cryptogramInformationData;
    // TODO: !!! Check Terminal Capabilities Value !!!
    private byte[] terminalCapabilities = new byte[]{(byte) 0x00, (byte) 0x08, (byte) 0x08};
    // TODO: !!! Check CVM Results !!!
    private byte[] cvmResults = new byte[]{(byte) 0x42, (byte) 0x03, (byte) 0x00};
    private byte[] terminalType = new byte[]{(byte) 0x21};
    private byte[] applicationTransactionCounter;
    private byte[] unpredictableNumber;
    private byte[] transactionSequenceCounter;
    private byte[] panSequenceNumber;

    public byte[] getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(byte[] transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public byte[] getApplicationInterchangeProfile() {
        return applicationInterchangeProfile;
    }

    public void setApplicationInterchangeProfile(byte[] applicationInterchangeProfile) {
        this.applicationInterchangeProfile = applicationInterchangeProfile;
    }

    public byte[] getDedicatedFileName() {
        return dedicatedFileName;
    }

    public void setDedicatedFileName(byte[] dedicatedFileName) {
        this.dedicatedFileName = dedicatedFileName;
    }

    public byte[] getTerminalVerificationResults() {
        return terminalVerificationResults;
    }

    public void setTerminalVerificationResults(byte[] terminalVerificationResults) {
        this.terminalVerificationResults = terminalVerificationResults;
    }

    public byte[] getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(byte[] transactionDate) {
        this.transactionDate = transactionDate;
    }

    public byte[] getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(byte[] transactionType) {
        this.transactionType = transactionType;
    }

    public byte[] getAmountAuthorized() {
        return amountAuthorized;
    }

    public void setAmountAuthorized(byte[] amountAuthorized) {
        this.amountAuthorized = amountAuthorized;
    }

    public byte[] getAmountOther() {
        return amountOther;
    }

    public void setAmountOther(byte[] amountOther) {
        this.amountOther = amountOther;
    }

    public byte[] getApplicationVersionNumber() {
        return applicationVersionNumber;
    }

    public void setApplicationVersionNumber(byte[] applicationVersionNumber) {
        this.applicationVersionNumber = applicationVersionNumber;
    }

    public byte[] getIssuerApplicationData() {
        return issuerApplicationData;
    }

    public void setIssuerApplicationData(byte[] issuerApplicationData) {
        this.issuerApplicationData = issuerApplicationData;
    }

    public byte[] getTerminalCountryCode() {
        return terminalCountryCode;
    }

    public void setTerminalCountryCode(byte[] terminalCountryCode) {
        this.terminalCountryCode = terminalCountryCode;
    }

    public byte[] getInterfaceDeviceSerialNumber() {
        return interfaceDeviceSerialNumber;
    }

    public void setInterfaceDeviceSerialNumber(byte[] interfaceDeviceSerialNumber) {
        this.interfaceDeviceSerialNumber = interfaceDeviceSerialNumber;
    }

    public byte[] getApplicationCryptogram() {
        return applicationCryptogram;
    }

    public void setApplicationCryptogram(byte[] applicationCryptogram) {
        this.applicationCryptogram = applicationCryptogram;
    }

    public byte[] getCryptogramInformationData() {
        return cryptogramInformationData;
    }

    public void setCryptogramInformationData(byte[] cryptogramInformationData) {
        this.cryptogramInformationData = cryptogramInformationData;
    }

    public byte[] getTerminalCapabilities() {
        return terminalCapabilities;
    }

    public void setTerminalCapabilities(byte[] terminalCapabilities) {
        this.terminalCapabilities = terminalCapabilities;
    }

    public byte[] getCvmResults() {
        return cvmResults;
    }

    public void setCvmResults(byte[] cvmResults) {
        this.cvmResults = cvmResults;
    }

    public byte[] getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(byte[] terminalType) {
        this.terminalType = terminalType;
    }

    public byte[] getApplicationTransactionCounter() {
        return applicationTransactionCounter;
    }

    public void setApplicationTransactionCounter(byte[] applicationTransactionCounter) {
        this.applicationTransactionCounter = applicationTransactionCounter;
    }

    public byte[] getUnpredictableNumber() {
        return unpredictableNumber;
    }

    public void setUnpredictableNumber(byte[] unpredictableNumber) {
        this.unpredictableNumber = unpredictableNumber;
    }

    public byte[] getTransactionSequenceCounter() {
        return transactionSequenceCounter;
    }

    public void setTransactionSequenceCounter(byte[] transactionSequenceCounter) {
        this.transactionSequenceCounter = transactionSequenceCounter;
    }

    public byte[] getPanSequenceNumber() {
        return panSequenceNumber;
    }

    public void setPanSequenceNumber(byte[] panSequenceNumber) {
        this.panSequenceNumber = panSequenceNumber;
    }
}


