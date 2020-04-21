package com.gt.alimert.emvcardreader.lib.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gt.alimert.emvcardreader.lib.model.AflObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AliMertOzdemir
 * @class AflUtil
 * @created 17.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public final class AflUtil {

    private static final String TAG = AflUtil.class.getName();

    @Nullable
    public static List<AflObject> getAflDataRecords(@NonNull byte[] aflData) {
        // Returning result
        List<AflObject> result = null;
        // - Returning result

        Log.d(TAG, "AFL Data Length: " + aflData.length);

        if (aflData.length < 4) { // At least 4 bytes length needed to go ahead
            try {
                throw new Exception("Cannot preform AFL data byte array actions, available bytes < 4; Length is " + aflData.length);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            result = new ArrayList<>();

            for (int i = 0; i < aflData.length / 4; i++) {
                int firstRecordNumber = aflData[4 * i + 1], lastRecordNumber = aflData[4 * i + 2]; // First record number & final record number

                while (firstRecordNumber <= lastRecordNumber) {
                    AflObject aflObject = new AflObject();
                    aflObject.setSfi(aflData[4 * i] >> 3); // SFI (Short File Identifier)
                    aflObject.setRecordNumber(firstRecordNumber);

                    byte[] cReadRecord = null;

                    ByteArrayOutputStream readRecordByteArrayOutputStream = null;
                    try {
                        readRecordByteArrayOutputStream = new ByteArrayOutputStream();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                    if (readRecordByteArrayOutputStream != null) {
                        try {
                            readRecordByteArrayOutputStream.write(TlvTagConstant.READ_RECORD);

                            readRecordByteArrayOutputStream.write(new byte[]{
                                    (byte) aflObject.getRecordNumber(),
                                    (byte) ((aflObject.getSfi() << 0x03) | 0x04),
                            });

                            readRecordByteArrayOutputStream.write(new byte[]{
                                    (byte) 0x00 // Le
                            });

                            readRecordByteArrayOutputStream.close();

                            cReadRecord = readRecordByteArrayOutputStream.toByteArray();
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    if (cReadRecord != null) {
                        aflObject.setReadCommand(cReadRecord);
                    }

                    result.add(aflObject);

                    firstRecordNumber++;
                }
            }
        }

        return result;
    }
}
