package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.adapter.PenddingAcceptAdapter;
import kyf.loveapplication.data.event.FriendStateChangeEvent;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.model.FriendVerifyListResult;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.utils.ToastUtils;

/**
 * 待验证好友列表
 */
public class PenddingAcceptActivity extends BaseActivity {

    @BindView(R.id.content_view) RecyclerView rycvList;         // 列表

    private PenddingAcceptAdapter mAdapter;
    private ArrayList<UserBean>   mList;

    private String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendding_accept);
        ButterKnife.bind(this);

        initVars();
        initViews();
        initEvents();
        initdata();

    }

    private void initVars() {
        mList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            memberId = intent.getStringExtra("memberId");
        }
    }

    private void initViews() {
        rycvList.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new PenddingAcceptAdapter(mActivity, mList);
        rycvList.setAdapter(mAdapter);
    }

    private void initEvents() {
        mAdapter.setOnAcceptClick(new PenddingAcceptAdapter.OnAcceptClick() {
            @Override
            public void OnAccept(String friendId) {
                addFriend(friendId);
            }
        });
    }

    private void initdata() {
        NetRepository.getInstance().friendVerifyList()
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<FriendVerifyListResult>() {
                    @Override
                    public void onSuccess(FriendVerifyListResult friendVerifyListResult) {
                        if (friendVerifyListResult != null && friendVerifyListResult.detailList != null &&
                                friendVerifyListResult.detailList.size() > 0 &&
                                friendVerifyListResult.verifyList != null &&
                                friendVerifyListResult.verifyList.size() > 0) {
                            for (int i = 0; i < friendVerifyListResult.verifyList.size(); i++) {
                                friendVerifyListResult.detailList.get(i).friendId = friendVerifyListResult.verifyList.get(i).friendId;
                                friendVerifyListResult.detailList.get(i).status = friendVerifyListResult.verifyList.get(i).status;
                                friendVerifyListResult.detailList.get(i).verifyContent = friendVerifyListResult.verifyList.get(i).verifyContent;
                            }

                            mAdapter.setNewData(friendVerifyListResult.detailList);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @OnClick({R.id.headerLeftImg})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;

        }
    }

    private void addFriend(String friendId) {
        NetRepository.getInstance().addFriend(friendId)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showToast("添加成功");
                        EventBus.getDefault().post(new FriendStateChangeEvent(0));

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    public static void toThisAvtivity(Activity activity, String memberId) {
        Intent intent = new Intent(activity, PenddingAcceptActivity.class);
        intent.putExtra("memberId", memberId);
        activity.startActivity(intent);
    }
}
