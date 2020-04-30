package com.gt.alimert.emvcardreader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gt.alimert.emvnfclib.enums.TransactionType;

/**
 * @author AliMertOzdemir
 * @class TransactionAmount
 * @created 30.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class TransactionAmount implements Parcelable {

    private String amount;
    private TransactionType transactionType;

    public TransactionAmount(String amount, TransactionType transactionType) {
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amount);
        dest.writeInt(this.transactionType == null ? -1 : this.transactionType.ordinal());
    }

    protected TransactionAmount(Parcel in) {
        this.amount = in.readString();
        int tmpTransactionType = in.readInt();
        this.transactionType = tmpTransactionType == -1 ? null : TransactionType.values()[tmpTransactionType];
    }

    public static final Creator<TransactionAmount> CREATOR = new Creator<TransactionAmount>() {
        @Override
        public TransactionAmount createFromParcel(Parcel source) {
            return new TransactionAmount(source);
        }

        @Override
        public TransactionAmount[] newArray(int size) {
            return new TransactionAmount[size];
        }
    };
}
