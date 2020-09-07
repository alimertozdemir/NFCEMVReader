package com.gt.alimert.emvnfclib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author AliMertOzdemir
 * @class LogMessage
 * @created 21.04.2020
 */
public class LogMessage implements Parcelable {

    private String command;
    private String request;
    private String response;

    public LogMessage(String command, String request, String response) {
        this.command = command;
        this.request = request;
        this.response = response;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    protected LogMessage(Parcel in) {
        command = in.readString();
        request = in.readString();
        response = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(command);
        dest.writeString(request);
        dest.writeString(response);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LogMessage> CREATOR = new Parcelable.Creator<LogMessage>() {
        @Override
        public LogMessage createFromParcel(Parcel in) {
            return new LogMessage(in);
        }

        @Override
        public LogMessage[] newArray(int size) {
            return new LogMessage[size];
        }
    };
}
