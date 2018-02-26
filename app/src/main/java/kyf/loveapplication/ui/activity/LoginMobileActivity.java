package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.data.event.LoginStateChangeEvent;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.ui.view.LoveEditText;
import kyf.loveapplication.ui.view.LoveImageView;
import kyf.loveapplication.utils.ToastUtils;
import kyf.loveapplication.utils.UserManager;

/**
 * 短信验证码登录
 * Created by a55 on 2017/11/18.
 */

public class LoginMobileActivity extends BaseActivity {

    @BindView(R.id.iv_head)       LoveImageView ivHead;
    @BindView(R.id.et_phone)      LoveEditText  etPhone;
    @BindView(R.id.et_code)       LoveEditText  etCode;
    @BindView(R.id.tv_login)      TextView      tvLogin;

    private CountDownTimer mCountDownTimer;         // 发送验证码倒计时
    private boolean        isOnCount;               // 是否在倒计时

    /**
     * 跳转到当前页
     *
     * @param context context
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginMobileActivity.class);
        ((Activity) context).startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        ButterKnife.bind(this);

        intVars();
        initView();
        initEvent();
        initData();

    }

    private void intVars() {

    }

    private void initView() {

    }

    private void initEvent() {

        etPhone.setOnRightTxtClickListener(view -> {
            sendSmsCode();
        });
    }

    private void initData() {

    }

    /**
     * 发送验证码
     */
    private void sendSmsCode() {
        if (TextUtils.isEmpty(etPhone.getEditText())) {
            ToastUtils.showToast(mActivity, "请输入手机号码");
            return;
        }

        NetRepository.getInstance().sendLoginCode(etPhone.getEditText().toString().trim())
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast("短信验证码已发送至" + etPhone.getEditText().toString().trim());
                        startCountDown();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 发送验证码倒计时
     */
    private void startCountDown() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new TimerCountDown(90 * 1000, 1000);
        }
        mCountDownTimer.start();
        isOnCount = true;
    }


    class TimerCountDown extends CountDownTimer {

        public TimerCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            etPhone.setRightTextEnable(false);
            etPhone.setRightText("重新获取" + millisUntilFinished / 1000 + "s");

        }

        @Override
        public void onFinish() {
            isOnCount = false;
            etPhone.setRightTextEnable(true);
            etPhone.setRightText("获取验证码");
        }
    }

    @OnClick({R.id.headerLeftImg, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
            case R.id.tv_login:
                if (TextUtils.isEmpty(etPhone.getEditText())) {
                    ToastUtils.showToast(mActivity, "请先输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(etCode.getEditText())) {
                    ToastUtils.showToast(mActivity, "请先输入验证码");
                    return;
                }
//                ToastUtils.showToast(mActivity, "登录");

                dologin(etPhone.getEditText().toString().trim(), etCode.getEditText().toString().trim());

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}
