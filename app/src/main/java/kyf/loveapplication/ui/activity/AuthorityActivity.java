package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.data.event.FriendStateChangeEvent;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.utils.ToastUtils;

public class AuthorityActivity extends BaseActivity {


    @BindView(R.id.sb_not_see_me)  SwitchButton mSbWechat;    // 微信
    @BindView(R.id.sb_not_see_her) SwitchButton mSbWeibo;     // 微博
    @BindView(R.id.sb_black)       SwitchButton mSbBlack;        // 拉黑

    @BindView(R.id.rlyt_complain) RelativeLayout rlytComplain;        // QQ
    @BindView(R.id.tv_delete)     TextView       tvDelete;        // QQ

    //    private UserBean userBean;

    private String       userId;
    private String       loginName;
    private List<String> blackNameList;
    private boolean      isBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority);
        ButterKnife.bind(this);

        initVars();
        initViews();
        initEvent();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            loginName = intent.getStringExtra("loginName");
        }

        // 获取黑名单
        blackNameList = EMClient.getInstance().contactManager().getBlackListUsernames();
        Logger.d(blackNameList);

//        new Thread(() -> {
//        try {
////            blackNameList = EMClient.getInstance().contactManager().getBlackListFromServer();
//            blackNameList = EMClient.getInstance().contactManager().getBlackListUsernames();
//            Logger.d(blackNameList);
//        } catch (Exception e) {
//
//        }
//        }).start();
    }

    private void initViews() {

    }

    private void initEvent() {

    }

    private void initData() {

        if (blackNameList != null && blackNameList.size() > 0) {
            for (String name : blackNameList) {
                if (TextUtils.equals(loginName, name)) {
                    isBlack = true;
                    mSbBlack.setChecked(true);
                    break;
                }

            }
        }
    }

    @OnClick({R.id.headerLeftImg, R.id.sb_not_see_me, R.id.sb_not_see_her, R.id.sb_black, R.id.rlyt_complain, R.id.tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLeftImg:
                finish();
                break;
            case R.id.sb_not_see_me:
                break;
            case R.id.sb_not_see_her:
                break;
            case R.id.sb_black:
                try {
                    if (((SwitchButton) v).isChecked()) {
                        EMClient.getInstance().contactManager().removeUserFromBlackList(loginName);
                    } else {
                        //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false，则我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
                        EMClient.getInstance().contactManager().addUserToBlackList(loginName, true);
                    }

                } catch (Exception e) {

                }
                break;
            case R.id.rlyt_complain:
                break;
            case R.id.tv_delete:

                if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(userId))
                    return;
                try {
                    EMClient.getInstance().contactManager().deleteContact(loginName);
                } catch (Exception e) {
                }
                deleteFriend();
                break;
        }
    }

    private void deleteFriend() {
        NetRepository.getInstance().deleteFriend(userId)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast("删除成功");
                        EventBus.getDefault().post(new FriendStateChangeEvent(1));
                        finish();
                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }


    public static void toThisAvtivity(Activity activity, String userId, String loginName) {
        Intent intent = new Intent(activity, AuthorityActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("loginName", loginName);
        activity.startActivity(intent);
    }
}
