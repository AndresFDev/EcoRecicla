package com.example.ecorecicla.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private String messageTitle;
    private String messageBody;
    private String messageImage;

    public Message(String messageTitle, String messageBody, String messageImage) {
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;
        this.messageImage = messageImage;
    }

    protected Message(Parcel in) {
        messageTitle = in.readString();
        messageBody = in.readString();
        messageImage = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getMessageTitle() {
        return messageTitle;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getMessageImage() {
        return messageImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(messageTitle);
        parcel.writeString(messageBody);
        parcel.writeString(messageImage);
    }
}