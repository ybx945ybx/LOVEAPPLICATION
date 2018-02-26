package kyf.loveapplication.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kyf.loveapplication.R;
import kyf.loveapplication.adapter.ChateListAdapter;
import kyf.loveapplication.data.model.ChateBean;
import kyf.loveapplication.ui.activity.AddFreindActivity;
import kyf.loveapplication.ui.activity.ChateActivity;
import kyf.loveapplication.utils.FileUtils;
import kyf.loveapplication.utils.Md5FileNameGenerator;
import kyf.loveapplication.utils.ToastUtils;

/**
 * 联聊
 * Created by a55 on 2017/11/2.
 */

public class ChatListFragment extends BaseFragment {

    @BindView(R.id.rycv_chate_list) RecyclerView       rycvChateList;
    @BindView(R.id.swlyt)           SwipeRefreshLayout swlyt;
    @BindView(R.id.ic_search)       ImageView          ivSearch;

    private ChateListAdapter mAdapter;
    private Unbinder         mUnbinder;
    private String           avator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initVars();
        initViews();
        initEvent();
        initData();
        return v;
    }

    private void initVars() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_defaut_avt);
        //        avator = FileUtils.saveBitmap(mActivity, bitmap, new Md5FileNameGenerator().generate("avator"));
        avator = "http://st-prod.b0.upaiyun.com/post/2017/11/19/34jeydl284qt01hqpczv13gplfbskan3";
    }

    private void initViews() {
        rycvChateList.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new ChateListAdapter(mActivity, new ArrayList<>());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String loginName = ((EMConversation)adapter.getData().get(position)).conversationId();
                ChateActivity.toThisAvtivity(mActivity, loginName, "", "");
//                ToastUtils.showToast(mActivity, "去聊天界面");
            }
        });
        rycvChateList.setAdapter(mAdapter);
    }

    private void initEvent() {
        swlyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFreindActivity.toThisAvtivity(mActivity);
            }
        });
    }

    private void initData() {

        ArrayList<EMConversation> chateList = new ArrayList<>();

        //        for (int i = 0; i < 5; i++) {
        //
        //            ChateBean chateBean = new ChateBean();
        //            chateBean.avator = avator;
        //            chateBean.date = "9:30";
        //            chateBean.message = "我明天过来看你" + i;
        //            chateBean.user = "效率特发脑" + i;
        //
        //            chateList.add(chateBean);
        //        }


        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        if (conversations != null) {
            Set<String> keys = conversations.keySet();
            if (keys != null && keys.size() > 0) {
                for (String key : keys) {
                    chateList.add(conversations.get(key));
                }
            }
            mAdapter.setNewData(chateList);
            mAdapter.notifyDataSetChanged();

            swlyt.setRefreshing(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
