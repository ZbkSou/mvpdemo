package com.billionstech.mvpdemo.mvp.user;

import android.support.annotation.NonNull;

import com.billionstech.mvpdemo.base.BasePresenter;
import com.billionstech.mvpdemo.httpclient.Constant;
import com.billionstech.mvpdemo.model.BaseResult;
import com.billionstech.mvpdemo.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:
 */
public class UserInfoPresenter extends BasePresenter<UserInfoContract.UserInfoView, UserInfoModel> implements UserInfoContract.UserInfoPresenter {
    @Override
    public void getUserInfo(String userName) {
        getView().onLoading();
        getModel().getUserInfo(userName).enqueue(new Callback<BaseResult<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<User>> call, @NonNull Response<BaseResult<User>> response) {
                BaseResult<User> userBaseResult = response.body();
                if (response.isSuccessful() && userBaseResult != null) {
                    if (userBaseResult.getCode() == Constant.SUCCESS_CODE && userBaseResult.getData() != null) {
                        getView().onSuccess(userBaseResult.getData());
                    } else {
                        getView().onError(userBaseResult.getCode(), userBaseResult.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<User>> call, @NonNull Throwable t) {
                getView().onError(Constant.SERVER_EXCEPTION, t.getMessage());
            }
        });
    }
}
