package com.gt.alimert.emvcardreader.util;

import java.math.BigDecimal;

/**
 * @author AliMertOzdemir
 * @class FormatUtils
 * @created 30.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public final class FormatUtils {

    public static String getHumanReadableAmount(String amount) {
        String cleanAmount = amount.replaceAll("[^0-9]", "");
        BigDecimal parsed = new BigDecimal(cleanAmount).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = parsed.toString().replace(".", ",");
        return  formatted;
    }
}
