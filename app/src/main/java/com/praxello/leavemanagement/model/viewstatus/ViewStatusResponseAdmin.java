package com.praxello.leavemanagement.model.viewstatus;

public class ViewStatusResponseAdmin {

    public  String Message;
    public int Responsecode;
    public ViewStatusData Data;

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
}
