package com.billionstech.mvpdemo.user;

import android.widget.TextView;

import com.billionstech.mvpdemo.R;
import com.billionstech.mvpdemo.base.BaseMvpActivity;
import com.billionstech.mvpdemo.inject.InjectPresenter;
import com.billionstech.mvpdemo.model.User;
import com.billionstech.mvpdemo.mvp.user.UserInfoContract;
import com.billionstech.mvpdemo.mvp.user.UserInfoPresenter;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:
 */
public class MainActivity extends BaseMvpActivity implements UserInfoContract.UserInfoView {
    //多个Presenter通过注入代替直接 new ，这里的缺点是没有持有接口，违反了依赖倒置

    @InjectPresenter
    private UserInfoPresenter mUserInfoPresenter;
    private TextView mTextView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
        mTextView = findViewById(R.id.tv);

    }

    /**
     * 在这里去请求数据
     */
    @Override
    protected void initData() {
        mUserInfoPresenter.getUserInfo("token");
    }

    /**
     * 显示一个加载的进度条
     */
    @Override
    public void onLoading() {

    }

    /**
     * 请求数据成功回调该方法
     *
     * @param user
     */
    @Override
    public void onSuccess(User user) {
        mTextView.setText(user.getUserName());
    }

    /**
     * 请求数据失败回调该方法
     */
    @Override
    public void onError(int code, String errorMessage) {

    }
}
