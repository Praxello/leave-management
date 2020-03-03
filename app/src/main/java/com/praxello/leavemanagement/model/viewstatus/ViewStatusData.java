package com.praxello.leavemanagement.model.viewstatus;

public class ViewStatusData {
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
}
