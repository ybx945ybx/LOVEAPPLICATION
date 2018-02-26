package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

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
import kyf.loveapplication.utils.AddressUtils;
import kyf.loveapplication.utils.ImageUtil;
import kyf.loveapplication.utils.ToastUtils;
import kyf.loveapplication.utils.UserManager;

import static kyf.loveapplication.utils.AddressUtils.cityList;
import static kyf.loveapplication.utils.AddressUtils.districtList;
import static kyf.loveapplication.utils.AddressUtils.provinceList;

/**
 * 注册
 * Created by a55 on 2017/11/18.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.iv_head)        LoveImageView ivHead;
    @BindView(R.id.iv_second_head) LoveImageView ivSecondHead;

    @BindView(R.id.et_name)        LoveEditText etName;
    @BindView(R.id.et_second_name) LoveEditText etSecondName;
    //    @BindView(R.id.et_province)    LoveEditText etProvice;
    @BindView(R.id.tv_area)        TextView     etProvice;
    @BindView(R.id.et_phone)       LoveEditText etPhone;
    @BindView(R.id.et_code)        LoveEditText etCode;
    @BindView(R.id.et_password)    LoveEditText etPassword;

    private static final int REQUEST_CODE_CHOOSE = 100;

    private String mAvatarBase64 = "";
    private String mAvatar       = "";

    private CountDownTimer mCountDownTimer;         // 发送验证码倒计时
    private boolean        isOnCount;               // 是否在倒计时

    private String mProvince;
    private String mCity;
    private String mDisctrict;

    /**
     * 跳转到当前页
     *
     * @param context context
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        ((Activity) context).startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        intVars();
        initView();
        initEvent();
        initData();

    }

    private void intVars() {
        initAddressData();
    }

    private void initView() {
    }

    private void initEvent() {
        etPhone.setOnRightTxtClickListener(new LoveEditText.onRightTxtClickListener() {
            @Override
            public void onRightTxtClick(View view) {
                sendSmsCode();
            }
        });
    }

    /**
     * 发送验证码
     */
    private void sendSmsCode() {
        if (TextUtils.isEmpty(etPhone.getEditText())) {
            ToastUtils.showToast(mActivity, "请输入手机号码");
            return;
        }

        NetRepository.getInstance().sendCode(etPhone.getEditText().toString().trim())
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

    private void initData() {

    }

    @OnClick({R.id.tv_register, R.id.rlyt_head, R.id.llyt_aggre, R.id.llyt_province})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlyt_head:
                selectPic();
                break;
            case R.id.llyt_aggre:
                WebActivity.luanch(mActivity);
                break;
            case R.id.llyt_province:
                // 显示选择框
                if (provinceList.size() > 0 && cityList.size() > 0 && districtList.size() > 0) {
                    OptionsPickerView picker = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
                        mProvince = provinceList.get(options1);
                        mCity = cityList.get(options1).get(options2);
                        mDisctrict = options3 >= 0 ? districtList.get(options1).get(options2).get(options3) : "";
                        etProvice.setText(mProvince + " " + mCity + " " + mDisctrict);
                    }).build();
                    picker.setPicker(provinceList, cityList, districtList);
                    picker.show();
                }
                break;
            case R.id.tv_register:
                if (TextUtils.isEmpty(mAvatarBase64)) {
                    ToastUtils.showToast(mActivity, "请选择头像");
                    return;
                }
                if (TextUtils.isEmpty(etName.getEditText())) {
                    ToastUtils.showToast(mActivity, "请输入昵称");
                    return;
                }
                if (TextUtils.isEmpty(mProvince)) {
                    ToastUtils.showToast(mActivity, "请选择地区");
                    return;
                }
                if (TextUtils.isEmpty(etPhone.getEditText())) {
                    ToastUtils.showToast(mActivity, "请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(etCode.getEditText())) {
                    ToastUtils.showToast(mActivity, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getEditText())) {
                    ToastUtils.showToast(mActivity, "请输入密码");
                    return;
                }
                register();
                break;
        }
    }

    private void register() {
        try {
            File file = null;
            if (!TextUtils.isEmpty(mAvatar)) {
                file = new File(mAvatar);
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
            doRegister(file);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "jpeg保存失败");
        }

    }

    private void doRegister(File file) {
        NetRepository.getInstance()
                .register(file, etPhone.getEditText().toString(),
                        //                .register(mAvatarBase64, etPhone.getEditText().toString(),
                        etName.getEditText().toString(),
                        mProvince,
                        mCity,
                        mDisctrict,
                        etPassword.getEditText().toString().trim(),
                        etCode.getEditText().toString().trim())
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        Logger.d(userBean);
                        UserManager.getInstance().setUser(userBean);

                        loginEmc(userBean.loginName, etPassword.getEditText().toString());

                        Toast.makeText(mActivity, "注册成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new LoginStateChangeEvent());
                        finish();
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        ToastUtils.showToast(e.getMessage());

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

    /**
     * 选择头像
     */
    private void selectPic() {
        Matisse.from(RegisterActivity.this)
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .maxSelectable(1)
                //                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    /**
     * 获取头像Base64编码
     *
     * @param avatar 头像本地url
     * @return 头像Base64编码
     */
    private String getUserAvatarBase64(String avatar) {
        Bitmap bmAvatar = ImageUtil.getSmallBitmap(avatar, 400, 400);
        return ImageUtil.compressImageBase64(bmAvatar);
    }

    /**
     * 初始化地区数据
     */
    private void initAddressData() {
        try {
            AddressUtils.parseFromJson(mActivity);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    //    private void registerEmc(String userName, String password) {
    //        new Thread(() -> {
    //            try {
    //                //注册失败会抛出HyphenateException
    //                EMClient.getInstance().createAccount(userName, password);//同步方法
    //                EventBus.getDefault().post(new LoginStateChangeEvent());
    //                MainActivity.toThisAvtivity(mActivity);
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //        }).start();
    //    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
            Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));

            if (Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size() > 0) {
                mAvatar = Matisse.obtainPathResult(data).get(0);
                mAvatarBase64 = getUserAvatarBase64(Matisse.obtainPathResult(data).get(0));
                ivHead.setImageURI(mAvatar.contains("file:") ? mAvatar : "file:///" + mAvatar);
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}
