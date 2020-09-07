package com.gt.alimert.emvnfclib.util;

import com.gt.alimert.emvnfclib.enums.CardType;

import java.util.Arrays;

/**
 * @author AliMertOzdemir
 * @class AidUtil
 * @created 16.04.2020
 */
public final class AidUtil {

    /*
        Mastercard (PayPass)
        RID: A000000004
        PIX: 1010
        AID (Application Identifier): A0000000041010
     */
    private static byte[] A0000000041010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x04,
            (byte) 0x10,
            (byte) 0x10
    };

    /*
        Maestro (PayPass)
        RID: A000000004
        PIX: 3060
        AID (Application Identifier): A0000000043060
     */
    private static byte[] A0000000043060 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x04,
            (byte) 0x30,
            (byte) 0x60
    };

    /*
        Visa (PayWave)
        RID: A000000003
        PIX: 1010
        AID (Application Identifier): A0000000031010
     */
    private static byte[] A0000000031010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x03,
            (byte) 0x10,
            (byte) 0x10
    };

    /*
        Visa Electron (PayWave)
        RID: A000000003
        PIX: 2010
        AID (Application Identifier): A0000000032010
     */
    private static byte[] A0000000032010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x03,
            (byte) 0x20,
            (byte) 0x10
    };

    /*
        American Express
        RID: A000000025
        PIX: 0104
        AID (Application Identifier): A0000000250104
    */
    private static byte[] A0000000250104 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x25,
            (byte) 0x01,
            (byte) 0x04
    };

    /*
        TROY
        RID: A0000006
        PIX: 723010
        AID (Application Identifier): A0000006723010
    */
    private static byte[] A0000006723010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x06,
            (byte) 0x72,
            (byte) 0x30,
            (byte) 0x10
    };

    /*
    TROY
    RID: A0000006
    PIX: 723020
    AID (Application Identifier): A0000006723020
*/
    private static byte[] A0000006723020 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x06,
            (byte) 0x72,
            (byte) 0x30,
            (byte) 0x20
    };

    public static boolean isApprovedAID(byte[] aid) {

        byte[] shortAid = Arrays.copyOfRange(aid, 0, 7);

        if(Arrays.equals(A0000000041010, shortAid))
            return true;
        else if(Arrays.equals(A0000000043060, shortAid))
            return true;
        else if(Arrays.equals(A0000000031010, shortAid))
            return true;
        else if(Arrays.equals(A0000000032010, shortAid))
            return true;
        else if(Arrays.equals(A0000000250104, shortAid))
            return true;
        else if(Arrays.equals(A0000006723010, shortAid))
            return true;
        else if(Arrays.equals(A0000006723020, shortAid))
            return true;
        else
            return false;

    }

    public static CardType getCardBrandByAID(byte[] aid) {

        byte[] shortAid = Arrays.copyOfRange(aid, 0, 7);

        if(Arrays.equals(A0000000041010, shortAid))
            return CardType.MC;
        else if(Arrays.equals(A0000000043060, shortAid))
            return CardType.MC;
        else if(Arrays.equals(A0000000031010, shortAid))
            return CardType.VISA;
        else if(Arrays.equals(A0000000032010, shortAid))
            return CardType.VISA;
        else if(Arrays.equals(A0000000250104, shortAid))
            return CardType.AMEX;
        else if(Arrays.equals(A0000006723010, shortAid))
            return CardType.TROY;
        else if(Arrays.equals(A0000006723020, shortAid))
            return CardType.TROY;
        else
            return CardType.UNKNOWN;
    }
}
