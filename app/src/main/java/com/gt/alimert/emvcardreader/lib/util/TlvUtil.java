package com.gt.alimert.emvcardreader.lib.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gt.alimert.emvcardreader.lib.model.Application;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AliMertOzdemir
 * @class TlvUtil
 * @created 16.04.2020
 */
public final class TlvUtil {

    //http://khuong.uk/Papers/EMVThesis.pdf
    //https://www.blackhat.com/presentations/bh-usa-08/Buetler/BH_US_08_Buetler_SmartCard_APDU_Analysis_V1_0_2.pdf
    //https://stackoverflow.com/questions/58299515/read-emv-data-from-mastercard-visa-debit-credit-card
    //http://www.cs.ru.nl/~joeri/talks/rfidsec2015.pdf
    //https://neapay.com/post/read-smart-card-chip-data-with-apdu-commands-iso-7816_76.html
    //https://salmg.net/2017/09/12/intro-to-analyze-nfc-contactless-cards/
    //http://www.europeancardpaymentcooperation.eu/wp-content/uploads/2019/06/CPACE-HCE_V1.0.pdf
    //https://salmg.net/2017/09/12/intro-to-analyze-nfc-contactless-cards/ (Multi Application Card - Combi)
    //https://b2ps.com/fileadmin/pdf/cardsetdocs/Evertec_ATH-Prima_Test_Card_Set_Summary_v1.pdf

    private static final String TAG = TlvUtil.class.getName();

    @Nullable
    public static byte[] getTlvByTag(@NonNull byte[] dataBytes, @NonNull byte[] tlvTag) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(dataBytes);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (byteArrayInputStream != null) {
            if (byteArrayInputStream.available() < 2) {
                try {
                    throw new Exception("Cannot preform TLV byte array stream actions, available bytes < 2; Length is " + byteArrayInputStream.available());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                int i = 0, resultSize;

                byte[] tlvTagLength = new byte[tlvTag.length];

                while (byteArrayInputStream.read() != -1) {
                    i += 1;

                    if (i >= tlvTag.length) {
                        try {
                            tlvTagLength = Arrays.copyOfRange(dataBytes, i - tlvTag.length, i);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    if (Arrays.equals(tlvTag, tlvTagLength)) {
                        resultSize = byteArrayInputStream.read();

                        if (resultSize > byteArrayInputStream.available()) {
                            continue;
                        }

                        if (resultSize != -1) {
                            byte[] resultRes = new byte[resultSize];

                            if (byteArrayInputStream.read(resultRes, 0, resultSize) != 0) {
                                String checkResponse = HexUtil.bytesToHexadecimal(dataBytes), checkResult = HexUtil.bytesToHexadecimal(resultRes);

                                if (checkResponse != null && checkResult != null && checkResponse.contains(checkResult)) {
                                    result = resultRes;
                                }
                            }
                        }
                    }
                }
            }

            try {
                byteArrayInputStream.close();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }

    public static List<Application> getApplicationList(byte[] data) {

        List<Application> appList = new ArrayList<>();

        data = TlvUtil.getTlvByTag(data, TlvTagConstant.FCI_TLV_TAG);

        for (byte[] app : getMultipleTlv(data, TlvTagConstant.APP_TLV_TAG)) {
            Application application = new Application();

            byte[] aid = getTlvByTag(app, TlvTagConstant.AID_TLV_TAG);
            if(aid != null)
                application.setAid(aid);

            byte[] appLabel = getTlvByTag(app, TlvTagConstant.APPLICATION_LABEL_TLV_TAG);
            if(appLabel != null)
                application.setAppLabel(HexUtil.bytesToAscii(appLabel));

            byte[] appPriorityInd = getTlvByTag(app, TlvTagConstant.APP_PRIORITY_IND_TLV_TAG);
            if(appPriorityInd != null) {
                String appPriorityIndStr = HexUtil.bytesToHexadecimal(appPriorityInd);
                if(appPriorityIndStr != null)
                    application.setPriority(Integer.parseInt(appPriorityIndStr));
            }

            appList.add(application);
        }

        return appList;
    }

    private static List<byte[]> getMultipleTlv(byte[] data, byte[] tlvTag) {

        List<byte[]> appList = new ArrayList<>();

        int startIndex = 0;
        int endIndex = data.length;

        while (endIndex > startIndex) {
            data = Arrays.copyOfRange(data, startIndex, data.length);
            byte[] tlvData = getTlvByTag(data, tlvTag);
            if(tlvData != null) {
                appList.add(tlvData);
                startIndex += tlvData.length + 1 + tlvTag.length;
            } else {
                return appList;
            }
        }

        return appList;
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
