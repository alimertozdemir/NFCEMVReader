package com.gt.alimert.emvnfclib.model;

/**
 * @author AliMertOzdemir
 * @class AipObject
 * @created 24.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class AipObject {

    private byte mFirstByte;
    private byte mSecondByte;

    public AipObject(byte firstByte, byte secondByte) {
        this.mFirstByte = firstByte;
        this.mSecondByte = secondByte;
    }

    // left most bit of mFirstByte is RFU

    public boolean isSDASupported() {
        return (mFirstByte & (byte) 0x40) > 0;
    }

    public boolean isDDASupported() {
        return (mFirstByte & (byte) 0x20) > 0;
    }

    public boolean isCardholderVerificationSupported() {
        return (mFirstByte & (byte) 0x10) > 0;
    }

    public boolean isTerminalRiskManagementToBePerformed() {
        return (mFirstByte & (byte) 0x08) > 0;
    }

    /**
     * When this bit is set to 1, Issuer Authentication using the EXTERNAL AUTHENTICATE command is supported
     */
    public boolean isIssuerAuthenticationSupported() {
        return (mFirstByte & (byte) 0x04) > 0;
    }

    public boolean isCDASupported() {
        return (mFirstByte & (byte) 0x01) > 0;
    }

    //The rest of the bits are RFU (Reserved for Future Use)

    public String getSDASupportedString(){
        if(isSDASupported()){
            return "Static Data Authentication (SDA) supported";
        }else{
            return "Static Data Authentication (SDA) not supported";
        }
    }

    public String getDDASupportedString(){
        if(isDDASupported()){
            return "Dynamic Data Authentication (DDA) supported";
        }else{
            return "Dynamic Data Authentication (DDA) not supported";
        }
    }

    public String getCardholderVerificationSupportedString(){
        if(isCardholderVerificationSupported()){
            return "Cardholder verification is supported";
        }else{
            return "Cardholder verification is not supported";
        }
    }

    public String getTerminalRiskManagementToBePerformedString() {
        if (isTerminalRiskManagementToBePerformed()) {
            return "Terminal risk management is to be performed";
        } else {
            return "Terminal risk management does not need to be performed";
        }
    }

    public String getIssuerAuthenticationIsSupportedString(){
        if (isIssuerAuthenticationSupported()) {
            return "Issuer authentication is supported";
        } else {
            return "Issuer authentication is not supported";
        }
    }

    public String getCDASupportedString() {
        if (isCDASupported()) {
            return "Combined Dynamic Data Authentication (CDA) supported";
        } else {
            return "Combined Dynamic Data Authentication (CDA) not supported";
        }
    }

    public byte[] getBytes() {
        return new byte[]{mFirstByte, mSecondByte};
    }
}
