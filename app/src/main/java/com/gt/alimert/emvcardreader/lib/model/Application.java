package com.gt.alimert.emvcardreader.lib.model;

/**
 * @author AliMertOzdemir
 * @class Application
 * @created 21.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class Application {

    private byte[] aid;
    private String appLabel;
    private int priority;

    public byte[] getAid() {
        return aid;
    }

    public void setAid(byte[] aid) {
        this.aid = aid;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
