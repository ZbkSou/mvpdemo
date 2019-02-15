package com.billionstech.mvpdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.billionstech.mvpdemo.inject.InjectPresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * User: bkzhou
 * Date: 2019/2/14
 * Description:只针对于 mvp 的 Activity 封装对于其他的再继承其他的BaseActivity来完成
 */
public abstract class BaseMvpActivity extends AppCompatActivity implements BaseView {

    private List<BasePresenter> mPresenters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenters = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                //创建注入
                Class<? extends BasePresenter> presenterClazz = null;
                try {
                    //获取这个类
                    presenterClazz = ( Class<? extends BasePresenter> ) field.getType();
                } catch (Exception e) {
                    // 乱七八糟一些注入
                    throw new RuntimeException("not support inject presenter" + field.getType());
                }
                try {
                    //创建BasePresenter对象
                    BasePresenter basePresenter = presenterClazz.newInstance();
                    //attach
                    basePresenter.attach(this);
                    mPresenters.add(basePresenter);
                    field.setAccessible(true);
                    field.set(this, basePresenter);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        initView();
        initData();
    }


    protected abstract void initView();

    protected abstract void initData();

    public abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑Presenter
        for (BasePresenter presenter : mPresenters) {
            presenter.detach();
        }
    }
}
