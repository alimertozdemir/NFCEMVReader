package com.gt.alimert.emvnfclib.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author AliMertOzdemir
 * @class HexUtil
 * @created 16.04.2020
 */
public final class HexUtil {

    private static final String TAG = HexUtil.class.getName();

    public static String getSpaces(int length) {
        StringBuilder buf = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            buf.append(" ");
        }

        return buf.toString();
    }

    public static byte[] addLeftSpaceZeros(String data, int length) {
        String result = String.format("%" + length + "s", data).replace(' ', '0');
        return hexadecimalToBytes(result);
    }

    public static byte[] removeLeftSpeceZerosFromString(String data) {
        String result = data.replaceFirst("^0+(?!$)", "").replaceFirst("\\s+","").trim();
        return hexadecimalToBytes(result);
    }

    @Nullable
    public static String bytesToHexadecimal(@NonNull byte[] bytesIn) {
        // Returning result
        String result = null;
        // - Returning result

        StringBuilder stringBuilder = null;
        try {
            stringBuilder = new StringBuilder();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (stringBuilder != null) {
            for (byte byteOut : bytesIn) {
                try {
                    stringBuilder.append(String.format("%02X", byteOut));
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }

            try {
                result = stringBuilder.toString();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }

    @Nullable
    public static byte[] hexadecimalToBytes(@NonNull String hexadecimal) {
        // Returning result
        byte[] result = null;
        // - Returning result

        try {
            result = new byte[hexadecimal.length() / 2];
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (result != null) {
            for (int i = 0; i < hexadecimal.length(); i += 2) {
                try {
                    result[i / 2] = (byte) ((Character.digit(hexadecimal.charAt(i), 16) << 4) + Character.digit(hexadecimal.charAt(i + 1), 16));
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }

        return result;
    }

    @Nullable
    public static String hexadecimalToAscii(@NonNull String hexadecimal) {
        // Returning result
        String result = null;
        // - Returning result

        StringBuilder stringBuilder = null;
        try {
            stringBuilder = new StringBuilder();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (stringBuilder != null) {
            for (int i = 0; i < hexadecimal.length(); i += 2) {
                try {
                    stringBuilder.append((char) Integer.parseInt(hexadecimal.substring(i, i + 2), 16));
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }

            try {
                result = stringBuilder.toString();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return result;
    }

    @Nullable
    public static String bytesToAscii(@NonNull byte[] bytesIn) {
        // Returning result
        String result = null;
        // - Returning result

        String hexadecimal = bytesToHexadecimal(bytesIn);

        if (hexadecimal != null) {
            result = hexadecimalToAscii(hexadecimal);
        }

        return result;
    }
}