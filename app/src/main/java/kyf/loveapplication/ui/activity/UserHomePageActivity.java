package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.data.event.FriendStateChangeEvent;
import kyf.loveapplication.data.model.SearchResult;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.ui.view.LoveImageView;
import kyf.loveapplication.utils.ToastUtils;

public class UserHomePageActivity extends BaseActivity {

    //    @BindView(R.id.tv_add_friend) TextView tvAddPhoneFriend;         // 底部RadioGroup
    //    @BindView(R.id.tv_add_record) TextView tvAddRecord;         // 底部RadioGroup
    //
    //    @BindView(R.id.et_search) EditText etSearch;         // 底部RadioGroup
    //    @BindView(R.id.tv_search) TextView tvSearch;         // 底部RadioGroup
    //
    //    @BindView(R.id.rlyt_search_result) RelativeLayout rlytSearchResult;         // 底部RadioGroup
    @BindView(R.id.iv_head) LoveImageView ivHead;         // 底部RadioGroup
    @BindView(R.id.tv_name) TextView      tvName;         // 底部RadioGroup
    //    @BindView(R.id.tv_sign)   TextView      tvSign;         // 底部RadioGroup
    //    @BindView(R.id.tv_source) TextView      tvSource;         // 底部RadioGroup
    //
    //    @BindView(R.id.rycv_cirle) RecyclerView rycvCirle;         // 朋友圈

    //    @BindView(R.id.iv_gender)          ImageView      ivGender;         // 底部RadioGroup
    //    @BindView(R.id.et_content)         EditText       etContent;         // 底部RadioGroup
    //    @BindView(R.id.tv_send_message)    TextView       tvSend;         // 底部RadioGroup
    //    @BindView(R.id.tv_add)             TextView       tvAdd;         // 底部RadioGroup

    //    @BindView(R.id.tv_search_no_result) TextView tvSearchNoResult;         // 底部RadioGroup


    private String   userId;
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initEvent();
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        }
    }

    private void initViews() {

    }

    private void initEvent() {

    }

    /**
     * 获取好友详情
     */
    private void initData() {
        NetRepository.getInstance().friendDetail(userId)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<SearchResult>() {
                    @Override
                    public void onSuccess(SearchResult searchResult) {
                        userBean = searchResult.storeMember;

                        ivHead.setImageURI(userBean.avatar);
                        tvName.setText(userBean.nickName);

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @OnClick({R.id.headerLeftImg, R.id.tv_right, R.id.tv_more, R.id.tv_send_message, R.id.tv_modif, R.id.tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
            case R.id.tv_right:
                AuthorityActivity.toThisAvtivity(mActivity, userId, userBean.loginName);
                break;
            case R.id.tv_more:
                break;
            case R.id.tv_send_message:
                if (userBean == null)
                    return;

                ChateActivity.toThisAvtivity(mActivity, userBean.loginName, userBean.nickName, userBean.avatar);
                //                ChatActivity.launch(mActivity, userBean.loginName);

                //                Intent intent = new Intent(this, ChatActivity.class);
                //                // it's single chat
                //                intent.putExtra("userId", userBean.loginName);
                //                startActivity(intent);
                break;
            case R.id.tv_modif:
                break;
            //            case R.id.tv_delete:
            //
            //                if (userBean == null)
            //                    return;
            //                //                if ()
            //                try {
            //                    EMClient.getInstance().contactManager().deleteContact(userBean.loginName);
            //                } catch (Exception e) {
            //                }
            //                deleteFriend();
            //                break;
        }
    }

    /**
     * 删除好友
     */
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

    public static void toThisAvtivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, UserHomePageActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
    }

    @Subscribe
    public void onFriendStateChange(FriendStateChangeEvent friendStateChangeEvent) {
        finish();
    }
}
