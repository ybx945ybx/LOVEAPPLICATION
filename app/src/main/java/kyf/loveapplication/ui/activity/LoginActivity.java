package kyf.loveapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.data.event.LoginStateChangeEvent;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.ui.view.LoveEditText;
import kyf.loveapplication.utils.ToastUtils;
import kyf.loveapplication.utils.UserManager;

/**
 * Created by a55 on 2017/11/7.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_country)        TextView     tvCountry;
    @BindView(R.id.tv_select_country) TextView     tvSelectCountry;
    @BindView(R.id.et_account)        LoveEditText etAccount;
    @BindView(R.id.et_password)       LoveEditText etPassword;
    @BindView(R.id.tv_login)          TextView     tvLogin;
    @BindView(R.id.tv_to_code_login)  TextView     tvToCodeLogin;
    @BindView(R.id.tv_register)       TextView     tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        intVars();
        initView();
        initEvent();
        initData();

    }

    private void intVars() {

    }

    private void initView() {
        etPassword.setRightImgSelect(false);
//        etPassword.setTransformationMethod(etPassword.getRightImgSelect() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());

    }

    private void initEvent() {

        etPassword.setOnRightImgClickListener(view -> {
            etPassword.setRightImgSelect(!etPassword.getRightImgSelect());

            etPassword.setTransformationMethod(etPassword.getRightImgSelect() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            //切换后将EditText光标置于末尾
            CharSequence charSequence = etPassword.getEditText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        });
    }

    private void initData() {

    }

    @OnClick({R.id.tv_register, R.id.tv_to_code_login, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (TextUtils.isEmpty(etAccount.getEditText())) {
                    ToastUtils.showToast(mActivity, "请输入账号");
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getEditText())) {
                    ToastUtils.showToast(mActivity, "请输入密码");
                    return;
                }
                dologin(etAccount.getEditText().toString(), etPassword.getEditText().toString());
                break;
            case R.id.tv_to_code_login:
                LoginMobileActivity.launch(mActivity);
                break;
            case R.id.tv_register:
                RegisterActivity.launch(mActivity);
                break;
        }
    }

    private void dologin(String mobile, String password) {
        NetRepository.getInstance().login(mobile, password)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        Logger.d(userBean);
                        UserManager.getInstance().setUser(userBean);

                        loginEmc(userBean.loginName, password);

                        Toast.makeText(mActivity, "登录成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new LoginStateChangeEvent());
                        finish();
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        LoginDefeatActivity.launch(mActivity);
                        return super.onFailed(e);
                    }
                });
    }

    private void loginEmc(String userName, String password) {
        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                initChateData();
                Logger.d("登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Logger.d(message);
                dismissProgressDialog();
            }
        });
    }

    @Subscribe
    public void onLoginStateEvent(LoginStateChangeEvent event) {
        finish();
    }


    public static void lunach(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);

    }
}
