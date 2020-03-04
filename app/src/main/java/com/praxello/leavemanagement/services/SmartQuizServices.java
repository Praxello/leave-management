package com.praxello.leavemanagement.services;

import com.praxello.leavemanagement.model.CommonResponse;
import com.praxello.leavemanagement.model.login.LoginResponse;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponse;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponseAdmin;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SmartQuizServices {

    @FormUrlEncoded
    @POST("~tailor/smartquiz/leavemanagement/login.php")
    Call<LoginResponse> login(@Field("usrname") String usrname, @Field("uuid") String uuid, @Field("passwrd") String passwrd);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/leavemanagement/apply_leave.php")
    Call<CommonResponse> addLeaveRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/leavemanagement/updateStatus.php")
    Call<ViewStatusResponseAdmin> updateStatus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/leavemanagement/getallleave.php")
    Call<ViewStatusResponse> getAllLeaveDetails(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/leavemanagement/deleteLeave.php")
    Call<CommonResponse> deleteLeaveRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/leavemanagement/update_leave.php")
    Call<ViewStatusResponseAdmin> updateLeave(@FieldMap Map<String, String> params);

}
