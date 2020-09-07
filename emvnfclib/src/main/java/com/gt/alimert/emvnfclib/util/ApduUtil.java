package com.gt.alimert.emvnfclib.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * @author AliMertOzdemir
 * @class ApduUtil
 * @created 16.04.2020
 */
public final class ApduUtil {

    private static String TAG = ApduUtil.class.getName();

    private static final byte[] PSE = "1PAY.SYS.DDF01".getBytes(); // PSE for Contact
    private static final byte[] PPSE = "2PAY.SYS.DDF01".getBytes(); // PPSE for Contactless

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
                        (byte) 0x00, // P1
                        (byte) 0x00, // P2
                        (byte) (pdolConstructed.length + 2)// Lc
                });

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x83,
                        (byte) pdolConstructed.length
                });

                byteArrayOutputStream.write(pdolConstructed); // Data

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
    public static byte[] generateAC(@NonNull byte[] cdolConstructed) {
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


                byteArrayOutputStream.write(TlvTagConstant.GENERATE_AC);

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x80, // P1
                        (byte) 0x00, // P2
                        (byte) cdolConstructed.length // Lc
                });

                byteArrayOutputStream.write(cdolConstructed); // Data

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
    public static byte[] getChallange() {
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

                byteArrayOutputStream.write(TlvTagConstant.GET_CHALLANGE);

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00, // P1
                        (byte) 0x00  // P2
                        // Lc not present
                });

                // Data not present

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
        return verifyPin(pin, null, false);
    }

    @Nullable
    public static byte[] verifyPin(byte[] pin, byte[]unNumber, boolean isEncrypted) {
        // Returning result
        byte[] result = null;
        // - Returning result

        //pin = new byte[] { (byte)0x24, (byte)0x12, (byte)0x34, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};

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

                if(isEncrypted) {

                    byteArrayOutputStream.write(new byte[]{
                            (byte) 0x00, // P1
                            (byte) 0x88, // P2
                            (byte) pin.length // Lc
                    });
                } else {

                    byteArrayOutputStream.write(new byte[]{
                            (byte) 0x00, // P1
                            (byte) 0x80, // P2
                            (byte) 0x08,  // Lc
                            (byte) (0x20 | pin.length * 2) // Control Field
                    });

                    byteArrayOutputStream.write(pin); // Pin

                    byte[] pinFiller = new byte[7 - pin.length];

                    Arrays.fill(pinFiller, 0, pinFiller.length, (byte) 0xFF); //Filler bytes

                    byteArrayOutputStream.write(pinFiller);
                }

                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }
}
