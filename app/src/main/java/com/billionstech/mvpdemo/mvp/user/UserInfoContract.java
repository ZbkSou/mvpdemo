package com.billionstech.mvpdemo.mvp.user;

import com.billionstech.mvpdemo.base.BaseView;
import com.billionstech.mvpdemo.model.User;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:
 */
public class UserInfoContract {

    //View层
    public interface UserInfoView extends BaseView {

        void onSuccess(User user);

        void onError(int code, String errorMessage);

    }

    interface UserInfoPresenter {
        void getUserInfo(String userName);
    }

}
