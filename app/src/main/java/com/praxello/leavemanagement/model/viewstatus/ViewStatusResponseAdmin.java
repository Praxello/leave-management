package com.praxello.leavemanagement.model.viewstatus;

import android.os.Parcel;
import android.os.Parcelable;

public class ViewStatusResponseAdmin implements Parcelable {

    public  String Message;
    public int Responsecode;
    public ViewStatusData Data;

    protected ViewStatusResponseAdmin(Parcel in) {
        Message = in.readString();
        Responsecode = in.readInt();
        Data = in.readParcelable(ViewStatusData.class.getClassLoader());
    }

    public static final Creator<ViewStatusResponseAdmin> CREATOR = new Creator<ViewStatusResponseAdmin>() {
        @Override
        public ViewStatusResponseAdmin createFromParcel(Parcel in) {
            return new ViewStatusResponseAdmin(in);
        }

        @Override
        public ViewStatusResponseAdmin[] newArray(int size) {
            return new ViewStatusResponseAdmin[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(int responsecode) {
        Responsecode = responsecode;
    }

    public ViewStatusData getData() {
        return Data;
    }

    public void setData(ViewStatusData data) {
        Data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeInt(Responsecode);
        dest.writeParcelable(Data, flags);
    }
}
