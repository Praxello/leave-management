package com.praxello.leavemanagement.model.viewstatus;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ViewStatusResponse implements Parcelable {

    public  String Message;
    public int Responsecode;
    public  ArrayList<ViewStatusData> Data;

    protected ViewStatusResponse(Parcel in) {
        Message = in.readString();
        Responsecode = in.readInt();
        Data = in.createTypedArrayList(ViewStatusData.CREATOR);
    }

    public static final Creator<ViewStatusResponse> CREATOR = new Creator<ViewStatusResponse>() {
        @Override
        public ViewStatusResponse createFromParcel(Parcel in) {
            return new ViewStatusResponse(in);
        }

        @Override
        public ViewStatusResponse[] newArray(int size) {
            return new ViewStatusResponse[size];
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

    public ArrayList<ViewStatusData> getData() {
        return Data;
    }

    public void setData(ArrayList<ViewStatusData> data) {
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
        dest.writeTypedList(Data);
    }
}
