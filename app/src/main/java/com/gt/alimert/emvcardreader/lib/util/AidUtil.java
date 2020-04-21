package com.gt.alimert.emvcardreader.lib.util;

import com.gt.alimert.emvcardreader.lib.enums.CardType;

import java.util.Arrays;

/**
 * @author AliMertOzdemir
 * @class AidUtil
 * @created 16.04.2020
 * @copyright © GARANTI TEKNOLOJI
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
        PIX: 010402
        AID (Application Identifier): A000000025010402
    */
    private static byte[] A000000025010402 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x25,
            (byte) 0x01,
            (byte) 0x04,
            (byte) 0x02
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
        if(Arrays.equals(A0000000041010, aid))
            return true;
        else if(Arrays.equals(A0000000043060, aid))
            return true;
        else if(Arrays.equals(A0000000031010, aid))
            return true;
        else if(Arrays.equals(A0000000032010, aid))
            return true;
        else if(Arrays.equals(A000000025010402, aid))
            return true;
        else if(Arrays.equals(A0000006723010, aid))
            return true;
        else if(Arrays.equals(A0000006723020, aid))
            return true;
        else
            return false;

    }

    public static CardType getCardBrandByAID(byte[] aid) {
        if(Arrays.equals(A0000000041010, aid))
            return CardType.MC;
        else if(Arrays.equals(A0000000043060, aid))
            return CardType.MC;
        else if(Arrays.equals(A0000000031010, aid))
            return CardType.VISA;
        else if(Arrays.equals(A0000000032010, aid))
            return CardType.VISA;
        else if(Arrays.equals(A000000025010402, aid))
            return CardType.AMEX;
        else if(Arrays.equals(A0000006723010, aid))
            return CardType.TROY;
        else if(Arrays.equals(A0000006723020, aid))
            return CardType.TROY;
        else
            return CardType.UNKNOWN;
    }
}
