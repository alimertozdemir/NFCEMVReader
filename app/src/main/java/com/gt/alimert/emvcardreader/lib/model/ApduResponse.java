package com.gt.alimert.emvcardreader.lib.model;

import java.security.AccessControlException;

/**
 * @author AliMertOzdemir
 * @class ApduResponse
 * @created 16.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class ApduResponse {

    protected int mSw1 = 0x00;
    protected int mSw2 = 0x00;

    protected byte[] mData = new byte[0];
    protected byte[] mBytes = new byte[0];

    public ApduResponse(byte[] respApdu) {
        if (respApdu.length < 2) {
            return;
        }
        if (respApdu.length > 2) {
            mData = new byte[respApdu.length - 2];
            System.arraycopy(respApdu, 0, mData, 0, respApdu.length - 2);
        }
        mSw1 = 0x00FF & respApdu[respApdu.length - 2];
        mSw2 = 0x00FF & respApdu[respApdu.length - 1];
        mBytes = respApdu;
    }

    public int getSW1() {
        return mSw1;
    }

    public int getSW2() {
        return mSw2;
    }

    public int getSW1SW2() {
        return (mSw1 << 8) | mSw2;
    }

    public byte[] getData() {
        return mData;
    }

    public byte[] toBytes() {
        return mBytes;
    }

    public void checkLengthAndStatus(int length, int sw1sw2, String message)
            throws AccessControlException {
        if (getSW1SW2() != sw1sw2 || mData.length != length) {
            throw new AccessControlException("ResponseApdu is wrong at "
                    + message);
        }
    }

    public void checkLengthAndStatus(int length, int[] sw1sw2List,
                                     String message) throws AccessControlException {
        if (mData.length != length) {
            throw new AccessControlException("ResponseApdu is wrong at "
                    + message);
        }
        for (int sw1sw2 : sw1sw2List) {
            if (getSW1SW2() == sw1sw2) {
                return; // sw1sw2 matches => return
            }
        }
        throw new AccessControlException("ResponseApdu is wrong at " + message);
    }

    public void checkStatus(int[] sw1sw2List, String message)
            throws AccessControlException {
        for (int sw1sw2 : sw1sw2List) {
            if (getSW1SW2() == sw1sw2) {
                return; // sw1sw2 matches => return
            }
        }
        throw new AccessControlException("ResponseApdu is wrong at " + message);
    }

    public void checkStatus(int sw1sw2, String message)
            throws AccessControlException {
        if (getSW1SW2() != sw1sw2) {
            throw new AccessControlException("ResponseApdu is wrong at "
                    + message);
        }
    }

    public boolean isStatus(int sw1sw2) {
        if (getSW1SW2() == sw1sw2) {
            return true;
        } else {
            return false;
        }
    }
}
