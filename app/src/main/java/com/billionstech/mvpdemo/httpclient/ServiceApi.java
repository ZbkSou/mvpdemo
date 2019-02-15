package com.billionstech.mvpdemo.httpclient;

import com.billionstech.mvpdemo.model.BaseResult;
import com.billionstech.mvpdemo.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:
 */
public interface ServiceApi {
    @POST("Login")
    @FormUrlEncoded
    Call<BaseResult<User>> getUser(@Field("userId") String userName);
}
