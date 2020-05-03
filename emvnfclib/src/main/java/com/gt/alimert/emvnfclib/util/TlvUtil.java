package com.gt.alimert.emvnfclib.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gt.alimert.emvnfclib.model.Application;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AliMertOzdemir
 * @class TlvUtil
 * @created 16.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public final class TlvUtil {

    //http://khuong.uk/Papers/EMVThesis.pdf
    //https://stackoverflow.com/questions/58299515/read-emv-data-from-mastercard-visa-debit-credit-card (APDU Comms)
    //https://www.blackhat.com/presentations/bh-usa-08/Buetler/BH_US_08_Buetler_SmartCard_APDU_Analysis_V1_0_2.pdf
    //https://stackoverflow.com/questions/58299515/read-emv-data-from-mastercard-visa-debit-credit-card
    //http://www.cs.ru.nl/~joeri/talks/rfidsec2015.pdf (Security)
    //https://neapay.com/post/read-smart-card-chip-data-with-apdu-commands-iso-7816_76.html
    //https://salmg.net/2017/09/12/intro-to-analyze-nfc-contactless-cards/
    //http://www.europeancardpaymentcooperation.eu/wp-content/uploads/2019/06/CPACE-HCE_V1.0.pdf
    //https://salmg.net/2017/09/12/intro-to-analyze-nfc-contactless-cards/ (Multi Application Card - Combi)
    //https://b2ps.com/fileadmin/pdf/cardsetdocs/Evertec_ATH-Prima_Test_Card_Set_Summary_v1.pdf
    //https://installer.id.ee/media/id2019/TD-ID1-Chip-App.pdf
    //http://mech.vub.ac.be/teaching/info/mechatronica/finished_projects_2007/Groep%201/Smartcard_files/EMV%20v4.1%20Book%203.pdf (Perfect doc)

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
}
