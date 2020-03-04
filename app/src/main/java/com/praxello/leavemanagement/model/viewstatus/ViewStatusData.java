package com.praxello.leavemanagement.model.viewstatus;

import android.os.Parcel;
import android.os.Parcelable;

public class ViewStatusData implements Parcelable {
    private int leave_id;
    private int user_id;
    private String leave_request;
    private String leave_type;
    private String leave_reason;
    private String start_date;
    private String end_date;
    private String leave_status;
    private String afname;
    private String alname;
    private String firstName;
    private String lastName;

    public ViewStatusData(int leave_id, int user_id, String leave_request, String leave_type, String leave_reason, String start_date, String end_date, String leave_status, String afname, String alname, String firstName, String lastName) {
        this.leave_id = leave_id;
        this.user_id = user_id;
        this.leave_request = leave_request;
        this.leave_type = leave_type;
        this.leave_reason = leave_reason;
        this.start_date = start_date;
        this.end_date = end_date;
        this.leave_status = leave_status;
        this.afname = afname;
        this.alname = alname;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected ViewStatusData(Parcel in) {
        leave_id = in.readInt();
        user_id = in.readInt();
        leave_request = in.readString();
        leave_type = in.readString();
        leave_reason = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        leave_status = in.readString();
        afname = in.readString();
        alname = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    public static final Creator<ViewStatusData> CREATOR = new Creator<ViewStatusData>() {
        @Override
        public ViewStatusData createFromParcel(Parcel in) {
            return new ViewStatusData(in);
        }

        @Override
        public ViewStatusData[] newArray(int size) {
            return new ViewStatusData[size];
        }
    };

    public String getAfname() {
        return afname;
    }

    public void setAfname(String afname) {
        this.afname = afname;
    }

    public String getAlname() {
        return alname;
    }

    public void setAlname(String alname) {
        this.alname = alname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getLeave_id() {
        return leave_id;
    }

    public void setLeave_id(int leave_id) {
        this.leave_id = leave_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLeave_request() {
        return leave_request;
    }

    public void setLeave_request(String leave_request) {
        this.leave_request = leave_request;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getLeave_reason() {
        return leave_reason;
    }

    public void setLeave_reason(String leave_reason) {
        this.leave_reason = leave_reason;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getLeave_status() {
        return leave_status;
    }

    public void setLeave_status(String leave_status) {
        this.leave_status = leave_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(leave_id);
        dest.writeInt(user_id);
        dest.writeString(leave_request);
        dest.writeString(leave_type);
        dest.writeString(leave_reason);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(leave_status);
        dest.writeString(afname);
        dest.writeString(alname);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}
