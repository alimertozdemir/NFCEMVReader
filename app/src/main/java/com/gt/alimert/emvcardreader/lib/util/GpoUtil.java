package com.gt.alimert.emvcardreader.lib.util;

import android.util.Log;

import androidx.annotation.Nullable;

import com.gt.alimert.emvcardreader.lib.model.TlvObject;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author AliMertOzdemir
 * @class GpoUtil
 * @created 17.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public final class GpoUtil {

    private static final String TAG = GpoUtil.class.getName();

    @Nullable
    public static byte[] generatePdol(@Nullable byte[] pdol) {
        // Returning result
        byte[] result = null;
        // - Returning result

        int pdolLength = 0;

        List<TlvObject> tlvObjectArrayList = new ArrayList<>();

        if (pdol != null) {
            for (int i = 0; i < pdol.length; i++) {
                int goNext = i;

                byte[] tlvTag = {
                        pdol[goNext++]
                };

                if ((tlvTag[0] & 0x1F) == 0x1F) {
                    tlvTag = new byte[]{
                            tlvTag[0], pdol[goNext++]
                    };
                }

                TlvObject tlvObj = new TlvObject(tlvTag, pdol[goNext]);
                tlvObjectArrayList.add(tlvObj);

                i += tlvObj.getTlvTag().length;
            }

            for (TlvObject tlvObject : tlvObjectArrayList) {
                pdolLength += tlvObject.getTlvTagLength();
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(new byte[]{
                    (byte) 0x83,
                    (byte) pdolLength
            });

            if (pdol != null) {
                for (TlvObject tlvObject : tlvObjectArrayList) {
                    byte[] generatePdolResult = new byte[tlvObject.getTlvTagLength()];

                    byte[] resultValue = null;

                    Date transactionDate = new Date();

                    // TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)
                    if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TTQ_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> TTQ (Terminal Transaction Qualifiers); " + "9F66" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        byte[] data = new byte[4];

                        data[0] |= 1 << 5; // Contactless EMV mode supported (bit index (in the example: "5") <= 7)

                        resultValue = Arrays.copyOf(data, data.length);

                        resultValue = new byte[]{
                                (byte) 0x27,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00
                        };
                    }
                    // - TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)

                    // Amount, Authorised (Numeric); 9F02; 6 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.AMOUNT_AUTHORISED_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> Amount, Authorised (Numeric); " + "9F02" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        //resultValue = new byte[tlvObject.getTlvTagLength()];

                        resultValue = new byte[]{
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x01
                        };
                    }
                    // - Amount, Authorised (Numeric); 9F02; 6 Byte(s)

                    // Amount, Other (Numeric); 9F03; 6 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.AMOUNT_OTHER_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> Amount, Other (Numeric); " + "9F03" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        resultValue = new byte[tlvObject.getTlvTagLength()];

                        /*resultValue = new byte[]{
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00
                        };*/
                    }
                    // - Amount, Other (Numeric); 9F03; 6 Byte(s)

                    // Terminal Country Code; 9F1A; 2 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TERMINAL_COUNTRY_CODE_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> Terminal Country Code; " + "9F1A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        resultValue = new byte[]{
                                (byte) 0x07,
                                (byte) 0x92
                        };

                        // https://en.wikipedia.org/wiki/ISO_3166-1

                        // Example: Turkey: 792 (Hexadecimal representation: 0792); Reference: https://en.wikipedia.org/wiki/ISO_3166-1
                    }
                    // - Terminal Country Code; 9F1A; 2 Byte(s)

                    // Transaction Currency Code; 5F2A, 2 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TRANSACTION_CURRENCY_CODE_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> Transaction Currency Code; " + "5F2A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        resultValue = new byte[]{
                                (byte) 0x09,
                                (byte) 0x40
                        };

                        // https://en.wikipedia.org/wiki/ISO_4217

                        // Example: Turkish Lira (TRY; Turkish Lira): 949 (Hexadecimal representation: 0949)
                    }
                    // - Transaction Currency Code; 5F2A, 2 Byte(s)

                    // TVR (Transaction Verification Results); 95; 5 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TVR_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> TVR (Transaction Verification Results); " + "95" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        resultValue = new byte[tlvObject.getTlvTagLength()];

                        /*resultValue = new byte[]{
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x01
                        };*/
                    }
                    // - TVR (Transaction Verification Results); 95; 5 Byte(s)

                    // Transaction Date; 9A, 3 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TRANSACTION_DATE_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> Transaction Date; " + "9A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        SimpleDateFormat simpleDateFormat = null;
                        try {
                            simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault()); // Format: Year, Month in year, Day in month
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
                    }
                    // - Transaction Date; 9A, 3 Byte(s)

                    // Transaction Type; 9C, 1 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TRANSACTION_TYPE_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> Transaction Type; " + "9C" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        resultValue = new byte[]{
                                (byte) 0x00
                        };
                    }
                    // - Transaction Type; 9C, 1 Byte(s)

                    // Transaction Time; 9F21; 3 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TRANSACTION_TIME_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> Transaction Date; " + "9F21" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        // "SimpleDateFormat" Reference: https://developer.android.com/reference/java/text/SimpleDateFormat.html
                        SimpleDateFormat simpleDateFormat = null;
                        try {
                            simpleDateFormat = new SimpleDateFormat("HHmmss", Locale.getDefault()); // Format: Hour in day (0-23), Minute in hour, Second in minute
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
                    }
                    // - Transaction Time; 9F21; 3 Byte(s)

                    // Terminal Type; 9F35, 1 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.TERMINAL_TYPE)) {
                        Log.d(TAG, "Generate PDOL -> Terminal Type; " + "9F35" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        resultValue = new byte[]{
                                (byte) 0x21,
                        };
                    }
                    // - Terminal Type; 9F35, 1 Byte(s)

                    // UN (Unpredictable Number); 9F37, 1 or 4 Byte(s)
                    else if (Arrays.equals(tlvObject.getTlvTag(), TlvTagConstant.UN_TLV_TAG)) {
                        Log.d(TAG, "Generate PDOL -> UN (Unpredictable Number); " + "9F37" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                        // Generate random unpredictable number
                        SecureRandom unSecureRandom = null;
                        try {
                            unSecureRandom = new SecureRandom();
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                        if (unSecureRandom != null) {
                            try {
                                unSecureRandom.nextBytes(generatePdolResult);
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                        // - Generate random unpredictable number
                    }
                    // - UN (Unpredictable Number); 9F37, 1 or 4 Byte(s)

                    if (resultValue != null) {
                        try {
                            System.arraycopy(resultValue, 0, generatePdolResult, 0, Math.min(resultValue.length, generatePdolResult.length));
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    byteArrayOutputStream.write(generatePdolResult); // Data
                }
            }

            byteArrayOutputStream.close();

            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return result;
    }
}