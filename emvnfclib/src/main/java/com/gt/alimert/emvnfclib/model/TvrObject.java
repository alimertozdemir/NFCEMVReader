package com.gt.alimert.emvnfclib.model;

import java.util.BitSet;

/**
 * @author AliMertOzdemir
 * @class TvrObject
 * @created 27.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class TvrObject {

    private BitSet bitSet;

    public TvrObject() {
        this.bitSet = new BitSet(5 * 8);
    }

    public TvrObject(byte[] data) {
        if (data.length != 5) {
            throw new IllegalArgumentException("TVR must be initialized with 5 bytes. Length=" + data.length);
        }
        this.bitSet = BitSet.valueOf(data);
    }

    public void setOfflineDataAuthenticationWasNotPerformed(boolean value) {
        bitSet.set(getBitSetIndex(1, 8), value);
    }

    public boolean offlineDataAuthenticationWasNotPerformed() {
        return bitSet.get(getBitSetIndex(1, 8));
    }

    public void sdaFailed(boolean value) {
        bitSet.set(getBitSetIndex(1, 7), value);
    }

    public boolean sdaFailed() {
        return bitSet.get(getBitSetIndex(1, 7));
    }

    public void setICCDataMissing(boolean value) {
        bitSet.set(getBitSetIndex(1, 6), value);
    }

    public boolean iccDataMissing() {
        return bitSet.get(getBitSetIndex(1, 6));
    }

    public void setCardAppearsOnTerminalExceptionFile(boolean value) {
        bitSet.set(getBitSetIndex(1, 5), value);
    }

    /**
     * There is no requirement in the EMV specification for an exception file,
     * but it is recognised that some terminals may have this capability.
     * @return
     */
    public boolean cardAppearsOnTerminalExceptionFile() {
        return bitSet.get(getBitSetIndex(1, 5));
    }

    public void setDDAFailed(boolean value) {
        bitSet.set(getBitSetIndex(1, 4), value);
    }

    public boolean ddaFailed() {
        return bitSet.get(getBitSetIndex(1, 4));
    }

    public void setCDAFailed(boolean value) {
        bitSet.set(getBitSetIndex(1, 3), value);
    }

    public boolean cdaFailed() {
        return bitSet.get(getBitSetIndex(1, 3));
    }

    //2 rightmost bits of the first byte are RFU
    //Second byte
    public void setICCAndTerminalHaveDifferentApplicationVersions(boolean value) {
        bitSet.set(getBitSetIndex(2, 8), value);
    }

    public boolean iccAndTerminalHaveDifferentApplicationVersions() {
        return bitSet.get(getBitSetIndex(2, 8));
    }

    public void setExpiredApplication(boolean value) {
        bitSet.set(getBitSetIndex(2, 7), value);
    }

    public boolean expiredApplication() {
        return bitSet.get(getBitSetIndex(2, 7));
    }

    public void setApplicationNotYetEffective(boolean value) {
        bitSet.set(getBitSetIndex(2, 6), value);
    }

    public boolean applicationNotYetEffective() {
        return bitSet.get(getBitSetIndex(2, 6));
    }

    public void setRequestedServiceNotAllowedForCardProduct(boolean value) {
        bitSet.set(getBitSetIndex(2, 5), value);
    }

    public boolean requestedServiceNotAllowedForCardProduct() {
        return bitSet.get(getBitSetIndex(2, 5));
    }

    public void setNewCard(boolean value) {
        bitSet.set(getBitSetIndex(2, 4), value);
    }

    public boolean newCard() {
        return bitSet.get(getBitSetIndex(2, 4));
    }

    //3 rightmost bits of the second byte are RFU
    //Third byte
    public void setCardholderVerificationWasNotSuccessful(boolean value) {
        bitSet.set(getBitSetIndex(3, 8), value);
    }

    public boolean cardholderVerificationWasNotSuccessful() {
        return bitSet.get(getBitSetIndex(3, 8));
    }

    public void setUnrecognisedCVM(boolean value) {
        bitSet.set(getBitSetIndex(3, 7), value);
    }

    public boolean unrecognisedCVM() {
        return bitSet.get(getBitSetIndex(3, 7));
    }

    public void setPinTryLimitExceeded(boolean value) {
        bitSet.set(getBitSetIndex(3, 6), value);
    }

    public boolean pinTryLimitExceeded() {
        return bitSet.get(getBitSetIndex(3, 6));
    }

    public void setPinEntryRequiredAndPINPadNotPresentOrNotWorking(boolean value) {
        bitSet.set(getBitSetIndex(3, 5), value);
    }

    public boolean pinEntryRequiredAndPINPadNotPresentOrNotWorking() {
        return bitSet.get(getBitSetIndex(3, 5));
    }

    public void setPinEntryRequired_PINPadPresent_ButPINWasNotEntered(boolean value) {
        bitSet.set(getBitSetIndex(3, 4), value);
    }

    public boolean pinEntryRequired_PINPadPresent_ButPINWasNotEntered() {
        return bitSet.get(getBitSetIndex(3, 4));
    }

    public void setOnlinePINEntered(boolean value) {
        bitSet.set(getBitSetIndex(3, 3), value);
    }

    public boolean onlinePINEntered() {
        return bitSet.get(getBitSetIndex(3, 3));
    }

    //2 rightmost bits of the third byte are RFU
    //Fourth byte
    public void setTransactionExceedsFloorLimit(boolean value) {
        bitSet.set(getBitSetIndex(4, 8), value);
    }

    public boolean transactionExceedsFloorLimit() {
        return bitSet.get(getBitSetIndex(4, 8));
    }

    public void setLowerConsecutiveOfflineLimitExceeded(boolean value) {
        bitSet.set(getBitSetIndex(4, 7), value);
    }

    public boolean lowerConsecutiveOfflineLimitExceeded() {
        return bitSet.get(getBitSetIndex(4, 7));
    }

    public void setUpperConsecutiveOfflineLimitExceeded(boolean value) {
        bitSet.set(getBitSetIndex(4, 6), value);
    }

    public boolean upperConsecutiveOfflineLimitExceeded() {
        return bitSet.get(getBitSetIndex(4, 6));
    }

    public void setTransactionSelectedRandomlyForOnlineProcessing(boolean value) {
        bitSet.set(getBitSetIndex(4, 5), value);
    }

    public boolean transactionSelectedRandomlyForOnlineProcessing() {
        return bitSet.get(getBitSetIndex(4, 5));
    }

    public void setMerchantForcedTransactionOnline(boolean value) {
        bitSet.set(getBitSetIndex(4, 4), value);
    }

    public boolean merchantForcedTransactionOnline() {
        return bitSet.get(getBitSetIndex(4, 4));
    }

    //3 rightmost bits of the fourth byte are RFU
    //Fifth byte
    public void setDefaultTDOLused(boolean value) {
        bitSet.set(getBitSetIndex(5, 8), value);
    }

    public boolean defaultTDOLused() {
        return bitSet.get(getBitSetIndex(5, 8));
    }

    public void setIssuerAuthenticationFailed(boolean value) {
        bitSet.set(getBitSetIndex(5, 7), value);
    }

    public boolean issuerAuthenticationFailed() {
        return bitSet.get(getBitSetIndex(5, 7));
    }

    public void setScriptProcessingFailedBeforeFinal_GENERATE_AC(boolean value) {
        bitSet.set(getBitSetIndex(5, 6), value);
    }

    public boolean scriptProcessingFailedBeforeFinal_GENERATE_AC() {
        return bitSet.get(getBitSetIndex(5, 6));
    }

    public void setScriptProcessingFailedAfterFinal_GENERATE_AC(boolean value) {
        bitSet.set(getBitSetIndex(5, 5), value);
    }

    public boolean scriptProcessingFailedAfterFinal_GENERATE_AC() {
        return bitSet.get(getBitSetIndex(5, 5));
    }

    public void reset() {
        bitSet.clear();
    }

    //4 rightmost bits of the fifth byte are RFU
    public byte[] getBytes() {
        return bitSet.toByteArray();
    }

    private int getBitSetIndex(int byteNum, int bitPos) {
        //byteNum 1 is the leftmost byte
        //bitNum 1 is the rightmost bit
        if (byteNum > 8 || byteNum < 1 || bitPos > 8 || bitPos < 1) {
            throw new IllegalArgumentException("byteNum and bitPos must be in the range 1-8. byteNum=" + byteNum + " bitPost=" + bitPos);
        }
        return (5 - byteNum) * 8 + (bitPos - 1);
    }

}
