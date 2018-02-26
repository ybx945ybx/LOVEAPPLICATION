package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.data.model.SearchResult;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.ui.view.LoveImageView;
import kyf.loveapplication.utils.ToastUtils;

/**
 * 添加新朋友
 */
public class AddFreindActivity extends BaseActivity {

    @BindView(R.id.tv_add_friend) TextView tvAddPhoneFriend;         // 底部RadioGroup
    @BindView(R.id.tv_add_record) TextView tvAddRecord;              // 底部RadioGroup

    @BindView(R.id.et_search) EditText etSearch;                     // 底部RadioGroup
    @BindView(R.id.tv_search) TextView tvSearch;                     // 底部RadioGroup

    @BindView(R.id.rlyt_search_result) RelativeLayout rlytSearchResult;         // 底部RadioGroup
    @BindView(R.id.iv_head)            LoveImageView  ivHead;         // 底部RadioGroup
    @BindView(R.id.tv_name)            TextView       tvName;         // 底部RadioGroup
    @BindView(R.id.tv_sign)            TextView       tvsign;         // 底部RadioGroup
    @BindView(R.id.iv_gender)          ImageView      ivGender;       // 底部RadioGroup
    @BindView(R.id.et_content)         EditText       etContent;      // 底部RadioGroup
    @BindView(R.id.tv_send_message)    TextView       tvSend;         // 底部RadioGroup
    @BindView(R.id.tv_add)             TextView       tvAdd;          // 底部RadioGroup

    @BindView(R.id.tv_search_no_result) TextView tvSearchNoResult;         // 底部RadioGroup

    private UserBean friendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_freind);

        ButterKnife.bind(this);
        //        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initEvent();
        initData();
    }

    private void initVars() {
//        try {
//            EMClient.getInstance().contactManager().acceptInvitation("18000000001");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        friendList();
        friendInfo = new UserBean();
    }

    private void friendList() {
        new Thread(() -> {
            try {
                List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                if (usernames != null && usernames.size() > 0) {

                }
                Logger.d(usernames);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void initViews() {

    }

    private void initEvent() {

//        ivHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserHomePageActivity.toThisAvtivity(mActivity, friendInfo.memberId);
//
//            }
//        });
    }

    private void initData() {

    }

    @OnClick({R.id.headerLeftImg, R.id.tv_add_friend, R.id.tv_add_record, R.id.tv_search, R.id.tv_send_message, R.id.tv_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
            case R.id.tv_add_friend:
                AddPhoneFriendActivity.toThisAvtivity(mActivity);
                break;
            case R.id.tv_add_record:
                AddRecorderActivity.toThisAvtivity(mActivity);
                break;
            case R.id.tv_search:
                if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
                    ToastUtils.showToast("请输入搜索内容");
                    return;
                }
                doSearch();
                break;
            case R.id.tv_send_message:
//                ChatActivity.launch(mActivity, friendInfo.loginName);
//                if(conversation.isGroup()){
//                    if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
//                        // it's group chat
//                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
//                    }else{
//                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
//                    }
//
//                }
//                Intent intent = new Intent(this, ChatActivity.class);
//                // it's single chat
//                intent.putExtra("userId", friendInfo.loginName);
//                startActivity(intent);
                break;
            case R.id.tv_add:
                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    ToastUtils.showToast("请输入验证消息");
                    return;
                }
                //参数为要添加的好友的username和添加理由
                try {
                    EMClient.getInstance().contactManager().addContact(etSearch.getText().toString().trim(), etContent.getText().toString().trim());
                } catch (Exception e) {

                }
                addFriendVerify();
                break;
        }
    }

    private void addFriendVerify() {
        NetRepository.getInstance().addFriendVerify(friendInfo.memberId, etContent.getText().toString().trim())
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast("发送成功");
//                        EventBus.getDefault().post(new FriendStateChangeEvent(0));

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 搜索好友
     */
    private void doSearch() {
        NetRepository.getInstance().findMember(etSearch.getText().toString().trim())
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<SearchResult>() {
                    @Override
                    public void onSuccess(SearchResult userBean) {
                        Logger.d(userBean);
                        friendInfo = userBean.storeMember;
                        ivHead.setImageURI(friendInfo.avatar);
                        tvName.setText(friendInfo.nickName);
                        etContent.setText("");

                        tvSend.setVisibility(userBean.isFriend ? View.VISIBLE : View.GONE);
                        tvAdd.setVisibility(userBean.isFriend ? View.GONE : View.VISIBLE);

                        rlytSearchResult.setVisibility(View.VISIBLE);
                        tvSearchNoResult.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        rlytSearchResult.setVisibility(View.GONE);
                        tvSearchNoResult.setVisibility(View.VISIBLE);
                        return super.onFailed(e);
                    }
                });
    }

    public static void toThisAvtivity(Activity activity) {
        Intent intent = new Intent(activity, AddFreindActivity.class);
        activity.startActivity(intent);
    }
}
