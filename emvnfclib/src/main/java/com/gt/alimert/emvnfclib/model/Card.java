package com.gt.alimert.emvnfclib.model;

import com.gt.alimert.emvnfclib.enums.CardType;

import java.util.List;

/**
 * @author AliMertOzdemir
 * @class Card
 * @created 21.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class Card {

    private String pan;
    private String expireDate;
    private CardType cardType;
    private String track2;
    private String emvData;
    private List<LogMessage> logMessages;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getEmvData() {
        return emvData;
    }

    public void setEmvData(String emvData) {
        this.emvData = emvData;
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public void setLogMessages(List<LogMessage> logMessages) {
        this.logMessages = logMessages;
    }
}
