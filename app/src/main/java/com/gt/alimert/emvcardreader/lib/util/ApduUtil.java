package com.gt.alimert.emvcardreader.lib.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

/**
 * @author AliMertOzdemir
 * @class ApduUtil
 * @created 16.04.2020
 */
public final class ApduUtil {

    private static String TAG = ApduUtil.class.getName();

    private static final byte[] PSE = "1PAY.SYS.DDF01".getBytes(); // PSE for Contact
    private static final byte[] PPSE = "2PAY.SYS.DDF01".getBytes(); // PPSE for Contactless

    private static final byte GPO_P1 = (byte) 0x00, GPO_P2 = (byte) 0x00;

    public static byte[] selectPse() {
        // Returning result
        return selectPse(PSE);
    }

    public static byte[] selectPpse() {
        // Returning result
        return selectPse(PPSE);
    }

    // PSE (Proximity Payment System Environment)
    @Nullable
    public static byte[] selectPse(@Nullable byte[] pse) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayOutputStream pseByteArrayOutputStream = null;
        try {
            pseByteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (pseByteArrayOutputStream != null) {
            try {
                pseByteArrayOutputStream.write(TlvTagConstant.SELECT);

                pseByteArrayOutputStream.write(new byte[]{
                        (byte) 0x04, // P1
                        (byte) 0x00 // P2
                });

                if (pse != null) {
                    pseByteArrayOutputStream.write(new byte[]{
                            (byte) pse.length // Lc
                    });

                    pseByteArrayOutputStream.write(pse); // Data
                }

                pseByteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                pseByteArrayOutputStream.close();

                result = pseByteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }
    // - PSE (Proximity Payment System Environment)

    @Nullable
    public static byte[] selectApplication(@NonNull byte[] aid) {
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
                byteArrayOutputStream.write(TlvTagConstant.SELECT);

                byteArrayOutputStream.write(new byte[] {
                        (byte) 0x04, // P1
                        (byte) 0x00, // P2
                        (byte) aid.length // Lc
                });

                byteArrayOutputStream.write(aid); // Data

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }

    @Nullable
    public static byte[] getProcessingOption(@NonNull byte[] pdolConstructed) {
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


                byteArrayOutputStream.write(TlvTagConstant.GET_PROCESSING_OPTIONS);

                byteArrayOutputStream.write(new byte[]{
                        GPO_P1, // P1
                        GPO_P2, // P2
                        (byte) pdolConstructed.length // Lc
                });

                byteArrayOutputStream.write(pdolConstructed); // Data

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                byteArrayOutputStream.close();

                // Temporary result
                byte[] tempResult = byteArrayOutputStream.toByteArray();
                /// - Temporary result

                if (isGpoCommand(tempResult)) {
                    result = tempResult;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }

    @Nullable
    public static byte[] getReadTlvData(byte[] tlvTag) {
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

                byteArrayOutputStream.write(TlvTagConstant.GET_DATA);

                byteArrayOutputStream.write(tlvTag);

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }

    @Nullable
    public static byte[] verifyPin(byte[] pin) {
        // Returning result
        byte[] result = null;
        // - Returning result

        pin = new byte[] { (byte)0x24, (byte)0x12, (byte)0x34, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};

        //00 20 00 80 08 24 12 34 FF FF FF FF FF

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (byteArrayOutputStream != null) {
            try {

                byteArrayOutputStream.write(TlvTagConstant.VERIFY);

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00, // P1
                        (byte) 0x80, // P2
                        (byte) pin.length // Lc
                });

                byteArrayOutputStream.write(pin); // Pin

                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }

    private static boolean isGpoCommand(@NonNull byte[] commandApdu) {
        return (commandApdu.length > 4
                && commandApdu[0] == TlvTagConstant.GET_PROCESSING_OPTIONS[0]
                && commandApdu[1] == TlvTagConstant.GET_PROCESSING_OPTIONS[1]
                && commandApdu[2] == GPO_P1
                && commandApdu[3] == GPO_P2
        );
    }
}
