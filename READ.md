解决问题

1. Model 获取到数据之前，退出了 Activity，此时由于 Activity 被 Presenter 引用，而 Presenter 正在进行耗时操作，会导致 Activity 的对象无法被回收，造成了内存泄漏

通过在 BaseActivity 中重写onDestroy 来解绑presenter

```
  @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑Presenter
        presenter.detach();
    }
```

```
presenter
  /**
     * 解绑View
     */
    public void detach() {
        mView = null;
        mModel = null;
    }
```

这个时候带来的问题就是 Model 获取到数据之后调用 mView接口方法会空指针，所以做出优化

可以通过判断 mview 是否为空来解决，通过学习 retrofit的源码之后发现可以类似 ServiceMethod的创建使用动态代理来优化

```
public class BasePresenter<V extends BaseView, M extends BaseModel> {
    private V mView;
    private M mModel;

    /**
     * 绑定View
     *
     * @param view
     */
    public void attach(final V view) {
        //动态代理
        mView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //在View层显示数据之前用户可能退出了View层的页面，会在Activity的onDestroy()方法中会把mView置为null
                //由于View层都是接口，这里采用了动态代理，如果在View层显示数据之前用户可能退出了View层的页面，返回null的话，onSuccess()方法不会执行
                if (mView == null) {
                    return null;
                }
                //每次调用View层接口的方法，都会执行这里
                return method.invoke(view, args);
            }
        });
        //用反射动态创建Model
        Type[] params = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        try {

            mModel = (M) ((Class) params[1]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解绑View
     */
    public void detach() {
        mView = null;
        mModel = null;
    }

    public V getView() {
        return mView;
    }

    public M getModel() {
        return mModel;
    }
}

```

2. 单 View 多 Presenter 的优化

通过反射创建Presenter ，类似功能在 [学习的 demo 项目中使用](https://github.com/ZbkSou/pipijoke) 模仿 xutils 来动态注入控件省去 findbyid

```
 @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenters = new ArrayList<>();
        //1 Activity ? ViewGroupMvpProxy ? Fragment?抽离 工具类抽出去，或者把每个代码copy
        //注入Presenter 通过反射(或者Dagger)
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
```