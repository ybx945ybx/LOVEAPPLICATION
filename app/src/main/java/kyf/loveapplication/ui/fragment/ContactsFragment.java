package kyf.loveapplication.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kyf.loveapplication.R;
import kyf.loveapplication.adapter.ContactsAdapter;
import kyf.loveapplication.adapter.NewContactsAdapter;
import kyf.loveapplication.data.event.FriendStateChangeEvent;
import kyf.loveapplication.data.model.FriendListResult;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.ui.activity.AddFreindActivity;
import kyf.loveapplication.ui.activity.PenddingAcceptActivity;
import kyf.loveapplication.ui.activity.UserHomePageActivity;
import kyf.loveapplication.ui.view.EasyRecyclerViewSidebar;
import kyf.loveapplication.ui.view.PinnedHeaderListView;
import kyf.loveapplication.utils.UserManager;

/**
 * 通讯录
 * Created by a55 on 2017/11/2.
 */

public class ContactsFragment extends BaseFragment {

    @BindView(R.id.pinnerListView)      PinnedHeaderListView    listView;           // ListView
    @BindView(R.id.section_sidebar)     EasyRecyclerViewSidebar slidebar;           // 侧边栏
    @BindView(R.id.section_floating_tv) TextView                slideFloatingTxt;   // 悬浮提示框

    //    @BindView(R.id.rycv_list) RecyclerView rycvList;                                // 通讯录列表
    private View         headView;
    private LinearLayout llytAdd;
    private LinearLayout llytGroup;
    private LinearLayout llytPending;

    //    private NewContactsAdapter  mAdapter;
    private ArrayList<FriendListResult> mList;

    private ContactsAdapter adapter;  //适配器
    //    private ArrayList<AllBrandOrSellerBean> mData;    //数据
    private int             type;

    //    private ChateListAdapter mAdapter;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);
        initVars();
        initViews();
        initEvent();
        initData();
        return v;
    }

    private void initVars() {
        mList = new ArrayList<>();
    }

    private void initViews() {
        headView = LayoutInflater.from(mActivity).inflate(R.layout.contacts_head, null);
        llytAdd = findViewById(headView, R.id.llyt_add_friend);
        llytGroup = findViewById(headView, R.id.llyt_group);
        llytPending = findViewById(headView, R.id.llyt_pending_friend);

        //        adapter = new ContactsAdapter(mActivity, mList);
        //        listView.setAdapter(adapter);
        //        mAdapter = new NewContactsAdapter(mActivity, mList);
        //        mAdapter.addHeaderView(headView);
        //
        //        rycvList.setLayoutManager(new LinearLayoutManager(mActivity));
        //        rycvList.setAdapter(mAdapter);
    }

    private void initEvent() {
        slidebar.setFloatView(slideFloatingTxt);
        slidebar.setOnTouchSectionListener(new EasyRecyclerViewSidebar.OnTouchSectionListener() {
            @Override
            public void onTouchLetterSection(int sectionIndex, EasyRecyclerViewSidebar.EasySection letterSection) {
                slideFloatingTxt.setText(letterSection.letter);

                //计算正确的位置
                int itemId = (int) adapter.getItemId(sectionIndex, 0);
                listView.setSelection(itemId);
            }
        });

        llytAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFreindActivity.toThisAvtivity(mActivity);
            }
        });

        llytGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/12/17

            }
        });

        llytPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/12/17
                PenddingAcceptActivity.toThisAvtivity(mActivity, UserManager.getInstance().getUserId());
            }
        });

    }

    private void initData() {
        getFriendList();
    }

    private void getFriendList() {
        NetRepository.getInstance().friendList()
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<ArrayList<FriendListResult>>() {
                    @Override
                    public void onSuccess(ArrayList<FriendListResult> friendList) {
                        Logger.d(friendList);
//                        Logger.e(friendList.toString());
                        if (friendList != null && friendList.size() > 0) {
                            ArrayList<EasyRecyclerViewSidebar.EasySection> list = new ArrayList<>();
                            for (FriendListResult bean : friendList) {
                                if (bean.groupName != null)
                                    list.add(new EasyRecyclerViewSidebar.EasySection(bean.groupName));
                            }
                            slidebar.setSections(list);

                            if (adapter == null){
                                listView.addHeaderView(headView);
                                adapter = new ContactsAdapter(mActivity, friendList);
                                listView.setAdapter(adapter);
                            }else {
                                adapter.changeData(friendList);
                            }

                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Subscribe
    public void onFriendStateChange(FriendStateChangeEvent friendStateChangeEvent) {
        getFriendList();
    }
}
