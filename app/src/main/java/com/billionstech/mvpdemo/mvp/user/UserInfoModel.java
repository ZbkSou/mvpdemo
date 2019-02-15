package com.billionstech.mvpdemo.mvp.user;

import com.billionstech.mvpdemo.base.BaseModel;
import com.billionstech.mvpdemo.httpclient.RetrofitClient;
import com.billionstech.mvpdemo.model.BaseResult;
import com.billionstech.mvpdemo.model.User;

import retrofit2.Call;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:
 */
public class UserInfoModel extends BaseModel  {

    public Call<BaseResult<User>> getUserInfo(String userName) {
        return RetrofitClient.getServiceApi().getUser(userName);
    }
}