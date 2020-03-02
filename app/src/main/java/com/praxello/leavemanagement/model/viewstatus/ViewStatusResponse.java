package com.praxello.leavemanagement.model.viewstatus;

import java.util.ArrayList;

public class ViewStatusResponse {

    public  String Message;
    public int Responsecode;
    public  ArrayList<ViewStatusData> Data;

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
}
