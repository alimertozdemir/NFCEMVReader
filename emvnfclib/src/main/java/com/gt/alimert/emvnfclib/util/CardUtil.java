package com.gt.alimert.emvnfclib.util;

import com.gt.alimert.emvnfclib.model.Card;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AliMertOzdemir
 * @class CardUtil
 * @created 21.04.2020
 */
public final class CardUtil {

    private static final Pattern TRACK2_EQUIVALENT_PATTERN = Pattern.compile("([0-9]{1,19})D([0-9]{4})([0-9]{3})?(.*)");

    private CardUtil() {}

    public static Card getCardInfoFromTrack2(byte[] track2) {
        Card card = new Card();

        String track2Data = HexUtil.bytesToHexadecimal(track2);
        String cardNumber = "", expireDate = "";

        if(track2Data != null) {
            Matcher matcher = TRACK2_EQUIVALENT_PATTERN.matcher(track2Data);
            if(matcher.find()) {
                cardNumber = matcher.group(1);
                expireDate = matcher.group(2);
            }
        }

        card.setTrack2(track2Data);
        card.setPan(cardNumber);
        card.setExpireDate(expireDate);
        return card;
    }

}
