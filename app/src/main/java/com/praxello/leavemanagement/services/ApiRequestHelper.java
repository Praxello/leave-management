package com.praxello.leavemanagement.services;

import android.text.Html;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.ConfigUrl;
import com.praxello.leavemanagement.model.CommonResponse;
import com.praxello.leavemanagement.model.login.LoginResponse;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponse;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponseAdmin;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestHelper {

    public interface OnRequestComplete {
        void onSuccess(Object object);

        void onFailure(String apiResponse);
    }

    private static ApiRequestHelper instance;
    private SmartQuiz application;
    private SmartQuizServices SmartQuizServices;
    static Gson gson;


    public static synchronized ApiRequestHelper init(SmartQuiz application) {
        if (null == instance) {
            instance = new ApiRequestHelper();
            instance.setApplication(application);
            gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                    .setExclusionStrategies(new ExclusionStrategy() {
//                        @Override
//                        public boolean shouldSkipField(FieldAttributes f) {
//                            return f.getDeclaringClass().equals(RealmObject.class);
//                        }
//
//                        @Override
//                        public boolean shouldSkipClass(Class<?> clazz) {
//                            return false;
//                        }
//                    })
                    .create();
            instance.createRestAdapter();
        }
        return instance;
    }

    public void login(String username,String uuid,String password, final OnRequestComplete onRequestComplete) {
        Call<LoginResponse> call = SmartQuizServices.login(username,uuid,password);
        call_api_login(onRequestComplete, call);
    }

    public void getAllLeaveDetails(String user_id, final OnRequestComplete onRequestComplete) {
        Call<ViewStatusResponse> call = SmartQuizServices.getAllLeaveDetails(user_id);
        call_api_all_leave_details(onRequestComplete, call);
    }

    public void addLeaveRequest(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CommonResponse> call = SmartQuizServices.addLeaveRequest(params);
        call_api_add_leave_request(onRequestComplete, call);
    }

    public void deleteLeaveRequest(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CommonResponse> call = SmartQuizServices.deleteLeaveRequest(params);
        call_api_add_leave_request(onRequestComplete, call);
    }

    public void updateStatus(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<ViewStatusResponseAdmin> call = SmartQuizServices.updateStatus(params);
        call_api_update_leave_details(onRequestComplete, call);
    }

    public void updateLeave(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<ViewStatusResponseAdmin> call = SmartQuizServices.updateLeave(params);
        call_api_update_leave_details(onRequestComplete, call);
    }

    private void call_api_update_leave_details(final OnRequestComplete onRequestComplete, Call<ViewStatusResponseAdmin> call) {
        call.enqueue(new Callback<ViewStatusResponseAdmin>() {
            @Override
            public void onResponse(Call<ViewStatusResponseAdmin> call, Response<ViewStatusResponseAdmin> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewStatusResponseAdmin> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }
    private void call_api_all_leave_details(final OnRequestComplete onRequestComplete, Call<ViewStatusResponse> call) {
        call.enqueue(new Callback<ViewStatusResponse>() {
            @Override
            public void onResponse(Call<ViewStatusResponse> call, Response<ViewStatusResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewStatusResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }


    private void call_api_add_leave_request(final OnRequestComplete onRequestComplete, Call<CommonResponse> call) {
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }


    private void call_api_login(final OnRequestComplete onRequestComplete, Call<LoginResponse> call) {
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void handle_fail_response(Throwable t, OnRequestComplete onRequestComplete) {
        if (t.getMessage() != null) {
            if (t.getMessage().contains("Unable to resolve host")) {
                onRequestComplete.onFailure(AllKeys.NO_INTERNET_AVAILABLE);
            } else {
                onRequestComplete.onFailure(Html.fromHtml(t.getLocalizedMessage()) + "");
            }
        } else
            onRequestComplete.onFailure(AllKeys.UNPROPER_RESPONSE);
    }

    private static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    /**
     * REST Adapter Configuration
     */
    private void createRestAdapter() {
//        ObjectMapper objectMapper = new ObjectMapper();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
// add your other interceptors …

// add logging as last interceptor
        httpClient.interceptors().add(logging);
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(ConfigUrl.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.client(httpClient.build()).build();
        SmartQuizServices = retrofit.create(SmartQuizServices.class);
    }

    /**
     * End REST Adapter Configuration
     */

    public SmartQuiz getApplication() {
        return application;
    }

    public void setApplication(SmartQuiz application) {
        this.application = application;
    }

}
