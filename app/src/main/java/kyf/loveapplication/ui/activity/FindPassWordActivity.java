package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.ui.view.LoveEditText;
import kyf.loveapplication.utils.ToastUtils;

public class FindPassWordActivity extends BaseActivity {

    @BindView(R.id.et_phone) LoveEditText etPhone;
    @BindView(R.id.et_wxi)   LoveEditText etWxi;
    @BindView(R.id.tv_send)  TextView     tvSend;

    /**
     * 跳转到当前页
     *
     * @param context context
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, FindPassWordActivity.class);
        ((Activity) context).startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word);
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

    }

    @OnClick({R.id.tv_send, R.id.headerLeftImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
            case R.id.tv_send:
                ToastUtils.showToast(mActivity, "发送");
                break;
        }
    }
}
