package com.gt.alimert.emvnfclib.util;

import android.util.Log;

import com.gt.alimert.emvnfclib.model.EmvParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author AliMertOzdemir
 * @class EmvUtil
 * @created 23.04.2020
 */
public final class EmvUtil {

    private static final String TAG = EmvUtil.class.getName();

    public static byte[] generateEmvData(EmvParam emvParam) {

        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (byteArrayOutputStream != null) {
            try {

                byte[] data = setTlvDataByTag(emvParam.getTransactionCurrencyCode(), TlvTagConstant.TRANSACTION_CURRENCY_CODE_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getApplicationInterchangeProfile(), TlvTagConstant.AIP_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getDedicatedFileName(), TlvTagConstant.DEDICATED_FILE_NAME_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getTerminalVerificationResults(), TlvTagConstant.TVR_TLV_TAG); // -- OK --
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(getTransactionDate(), TlvTagConstant.TRANSACTION_DATE_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getTransactionType(), TlvTagConstant.TRANSACTION_TYPE_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getAmountAuthorized(), TlvTagConstant.AMOUNT_AUTHORISED_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getAmountOther(), TlvTagConstant.AMOUNT_OTHER_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getApplicationVersionNumber(), TlvTagConstant.APPLICATION_VERSION_NUMBER_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getIssuerApplicationData(), TlvTagConstant.ISSUER_APPLICATION_DATA_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getTerminalCountryCode(), TlvTagConstant.TERMINAL_COUNTRY_CODE_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getInterfaceDeviceSerialNumber(), TlvTagConstant.IFD_SERIAL_NUMBER_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getApplicationCryptogram(), TlvTagConstant.APPLICATION_CRYPTOGRAM_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getCryptogramInformationData(), TlvTagConstant.CRYPTOGRAM_INFORMATION_DATA_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getTerminalCapabilities(), TlvTagConstant.TERMINAL_CAPABILITIES_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getCvmResults(), TlvTagConstant.CVM_RESULTS_TLV_TAG); // -- OK --
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getTerminalType(), TlvTagConstant.TERMINAL_TYPE); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getApplicationTransactionCounter(), TlvTagConstant.ATC_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(getUnpredictableNumber(), TlvTagConstant.UNPREDICTABLE_NUMBER_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getTransactionSequenceCounter(), TlvTagConstant.TRANSACTION_SEQUENCE_COUNTER_TLV_TAG); // OK
                byteArrayOutputStream.write(data);
                data = setTlvDataByTag(emvParam.getPanSequenceNumber(), TlvTagConstant.PAN_SEQUENCE_NUMBER_TLV_TAG); // OK
                byteArrayOutputStream.write(data);

                byteArrayOutputStream.close();

                // Temporary result
                byte[] tempResult = byteArrayOutputStream.toByteArray();
                // - Temporary result

                result = tempResult;

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;

    }

    private static byte[] setTlvDataByTag(byte[] data, byte[] tlvTag) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if(data != null && tlvTag != null) {

            outputStream.write(tlvTag);

            outputStream.write(new byte[]{
                    (byte) data.length
            });

            outputStream.write(data);
        }

        return outputStream.toByteArray();
    }

    private static byte[] getTransactionDate() {

        byte [] resultValue = null;

        Date transactionDate = new Date();

        SimpleDateFormat simpleDateFormat = null;
        try {
            // Format: Year, Month in year, Day in month
            simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (simpleDateFormat != null) {
            String dateFormat = null;
            try {
                dateFormat = simpleDateFormat.format(transactionDate);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (dateFormat != null) {
                resultValue = HexUtil.hexadecimalToBytes(dateFormat);
            }
        }

        return resultValue;
    }

    private static byte[] getUnpredictableNumber() {

        byte[] result = new byte[4];
        // Generate random unpredictable number
        SecureRandom unSecureRandom = null;
        try {
            unSecureRandom = new SecureRandom();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (unSecureRandom != null) {
            unSecureRandom.nextBytes(result);
        }
        // - Generate random unpredictable number

        return result;
    }

    // List of fields that should be send inside Bit 55 on request
    // 5F2A - Transaction Currency Code - Indicates the currency code of the transaction according to ISO 4217
    // 82   - Application Interchange Profile - Indicates the capabilities of the card to support specific functions in the application
    // 84   - Dedicated File (DF) Name
    // 95   - Terminal Verification Results (TVR) - Status of the different functions as seen from the terminal
    // 9A   - Transaction Date - Local date that the transaction was authorised
    // 9C   - Transaction Type - Indicates the type of financial transaction, represented by the first two digits of ISO 8583:1987 Processing Code
    // 9F02 - Amount, Authorised (Numeric) - Authorised amount of the transaction (excluding adjustments)
    // 9F03 - Amount, Other (Numeric) - Secondary amount associated with the transaction representing a cashback amount
    // 9F09 - Application Version Number
    // 9F10 - Issuer Application Data - Contains proprietary application data for transmission to the issuer in an online transaction
    // 9F1A - Terminal Country Code - Indicates the country of the terminal, represented according to ISO 3166
    // 9F1E - Interface Device (IFD) Serial Number
    // 9F26 - Application Cryptogram - Cryptogram returned by the ICC in response of the GENERATE AC command
    // 9F27 - Cryptogram Information Data - Indicates the type of cryptogram and the actions to be performed by the terminal
    // 9F33 - Terminal Capabilities
    // 9F34 - Cardholder Verification Method (CVM) Results
    // 9F35 - Terminal Type
    // 9F36 - Application Transaction Counter (ATC) - Counter maintained by the application in the ICC (incrementing the ATC is managed by the ICC)
    // 9F37 - Unpredictable Number - Value to provide variability and uniqueness to the generation of a cryptogram
    // 9F41 - Transaction Sequence Counter
    // 5F34 - Application PAN Sequence Number - Identifies and differentiates cards with the same PAN


    // READ ON MASTER CARD :
    // 5F2A - // Hardcoded
    // 82   - 2000
    // 84   - 325041592E5359532E4444463031
    // 95   -
    // 9A   - // Hardcoded
    // 9C   - // Hardcoded
    // 9F02 - // From Amount Left padding 12
    // 9F03 - // Hardcoded
    // 9F09 - // Hardcoded new byte[]{0x00, 0x01});
    // 9F10 - 06011103A000000F030000002500000000250003AFC776
    // 9F1A - // Hardcoded new byte[]{0x07, (byte) 0x92}
    // 9F1E - // mDataManager.getTerminalInfo().getTerminalId().getBytes()
    // 9F26 - B4DD72A5956C1131
    // 9F27 - 80
    // 9F33 - // mTerminal.getTerminalCapability().getBytes() - 000808
    // 9F34 - 00
    // 9F35 - // Hardcoded - new byte[]{0x21}
    // 9F36 - 07D7
    // 9F37 - // Tdol une setlenmeli Ddol unden alınmalı
    // 9F41 - // Stan or 00 00 00 01 - new byte[]{0x00, 0x00, 0x00, (byte) 0x01}
    // 5F34 - // From Card 00


    // 5A - 4824914751960012
    // 57 - 4824914751960012D21042010000003202900F
}
