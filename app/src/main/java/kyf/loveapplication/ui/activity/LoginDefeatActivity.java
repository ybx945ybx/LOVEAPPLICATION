package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;

/**
 * 登录失败
 * Created by a55 on 2017/11/18.
 */

public class LoginDefeatActivity extends BaseActivity {

    @BindView(R.id.tv_pwd_error) TextView tvPwdError;
    @BindView(R.id.tv_law_error) TextView tvLawError;
    @BindView(R.id.tv_find)      TextView tvFind;
    @BindView(R.id.tv_ice_day)   TextView tvIceDay;

    /**
     * 跳转到当前页
     *
     * @param context context
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginDefeatActivity.class);
        ((Activity) context).startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_defeat);
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

    }

    private void initData() {
        tvPwdError.setSelected(true);
        tvLawError.setSelected(false);
        tvIceDay.setText("20");
    }


    @OnClick({R.id.headerLeftImg, R.id.tv_find})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
            case R.id.tv_find:
                findPwd();
                break;
        }
    }

    /**
     * 找回密码
     */
    private void findPwd() {
        FindPassWordActivity.launch(mActivity);
    }

}
