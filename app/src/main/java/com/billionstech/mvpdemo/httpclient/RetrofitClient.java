package com.billionstech.mvpdemo.httpclient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:
 */
public class RetrofitClient {
    private static final ServiceApi mServiceApi;
    //直接用代理拦截返回固定数据测试
    private static final String BASE_URL = "http://192.168.10.92:3996/";

    static {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        //创建一个ServiceApi实例对象
        mServiceApi = retrofit.create(ServiceApi.class);
    }

    public static ServiceApi getServiceApi() {
        return mServiceApi;
    }
}
