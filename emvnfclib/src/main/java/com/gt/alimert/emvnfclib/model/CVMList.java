package com.gt.alimert.emvnfclib.model;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author AliMertOzdemir
 * @class CVMList
 * @created 27.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class CVMList {
    private LinkedList<CVRule> cvRules = new LinkedList<CVRule>();
    private byte[] amountField;
    private byte[] secondAmountField;

    public CVMList(byte[] data){

        if(data.length < 8 ){
            throw new IllegalArgumentException("Length is less than 8. Length=" + data.length);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        amountField = new byte[4];
        secondAmountField = new byte[4];
        bis.read(amountField, 0, amountField.length);
        bis.read(secondAmountField, 0, secondAmountField.length);
        if(bis.available() % 2 != 0 ){
            throw new IllegalStateException("CMVRules data is not a multiple of 2. Length=" + data.length);
        }
        while(bis.available() > 0){
            byte[] tmp = new byte[2];
            bis.read(tmp, 0, tmp.length);
            cvRules.add(new CVRule(tmp[0], tmp[1], amountField, secondAmountField));
        }
    }

    public List<CVRule> getRules() {
        return Collections.unmodifiableList(cvRules);
    }
}
