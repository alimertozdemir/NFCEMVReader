package com.gt.alimert.emvcardreader.lib.enums;

/**
 * @author AliMertOzdemir
 * @class CardType
 * @created 21.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public enum CardType {

    MC("MasterCard"), VISA("Visa"), AMEX("AmericanExpress"), TROY("Troy"), UNKNOWN("N/A");

    private String cardBrand;

    CardType(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardBrand() {
        return cardBrand;
    }
}
