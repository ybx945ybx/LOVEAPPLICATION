package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kyf.loveapplication.R;
import kyf.loveapplication.adapter.MessageListAdapter;
import kyf.loveapplication.data.constant.Constant;
import kyf.loveapplication.data.event.ChateTakePhotoEvent;
import kyf.loveapplication.data.model.SearchResult;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.ui.view.LoveImageView;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_GROUP;

/**
 * 聊天界面
 */
public class ChateActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivLeft;
    private TextView  tvTitle;
    private ImageView ivRight;

    public RecyclerView         rycvMessage;
    public MessageListAdapter   mAdapter;
    public ArrayList<EMMessage> mList;

    public  EditText  etInput;
    private TextView  tvSend;
    private ImageView ivExt;

    private LinearLayout llytExt;
    private TextView     tvCapter;
    private TextView     tvPhoto;
    private TextView     tvCall;
    private TextView     tvVedio;

    private String chateName;
    public  String loginName;
    private String chateAvator;

    private static final int REQUEST_CODE_CHOOSE = 100;
    //    private static final int REQUEST_CODE_CHOOSE = 100;
    //    private static final int REQUEST_CODE_CHOOSE = 100;

    private int              chatType;
    private EaseChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initEvents();
        initData();
    }

    private void initVars() {

        mList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            chateName = intent.getStringExtra("chateName");
            loginName = intent.getStringExtra("userId");
            chateAvator = intent.getStringExtra("chateAvator");
            chatType = intent.getIntExtra("chatType", 1);
        }
    }

    private void initViews() {
        ivLeft = (ImageView) findViewById(R.id.headerLeftImg);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight = (ImageView) findViewById(R.id.headerRightImg);

        rycvMessage = (RecyclerView) findViewById(R.id.rycv_message_list);
        rycvMessage.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new MessageListAdapter(mActivity, mList, chateAvator);
        rycvMessage.setAdapter(mAdapter);

        etInput = (EditText) findViewById(R.id.et_input);
        tvSend = (TextView) findViewById(R.id.tv_send);
        ivExt = (ImageView) findViewById(R.id.iv_ext);

        llytExt = (LinearLayout) findViewById(R.id.llyt_ext);
        tvCapter = (TextView) findViewById(R.id.tv_capter);
        tvPhoto = (TextView) findViewById(R.id.tv_photo);
        tvCall = (TextView) findViewById(R.id.tv_call);
        tvVedio = (TextView) findViewById(R.id.tv_vedio);

        //get user id or group id
        //        loginName = getIntent().getExtras().getString("userId");
        //        //use EaseChatFratFragment
        //        chatFragment = new EaseChatFragment();
        //        //pass parameters to chat fragment
        //        chatFragment.setArguments(getIntent().getExtras());
        //        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    private void initEvents() {
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        ivExt.setOnClickListener(this);

        tvPhoto.setOnClickListener(this);
        tvCapter.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        tvVedio.setOnClickListener(this);

        rycvMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 收起键盘
                //                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //                imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);

            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tvSend.setVisibility(View.VISIBLE);
                    llytExt.setVisibility(View.GONE);
                    ivExt.setVisibility(View.GONE);
                } else {
                    tvSend.setVisibility(View.GONE);
                    ivExt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initData() {

        if (TextUtils.isEmpty(chateName)) {
            NetRepository.getInstance().findMember(loginName)
                    .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                    .subscribe(new DefaultSubscriber<SearchResult>() {
                        @Override
                        public void onSuccess(SearchResult searchResult) {
                            chateName = searchResult.storeMember.nickName;
                            chateAvator = searchResult.storeMember.avatar;
                            mAdapter.setChateAvator(chateAvator);
                            getMessageList();
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        } else {
            getMessageList();
        }

    }

    private void getMessageList() {
        //获取聊天记录
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(loginName);
        //获取此会话的所有消息
        if (conversation != null) {
            List<EMMessage> messages = conversation.getAllMessages();

            mList.addAll(messages);
            mAdapter.notifyDataSetChanged();
        }
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        //        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

        tvTitle.setText(chateName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLeftImg:
                onBackPressed();
                break;
            case R.id.headerRightImg:

                break;
            case R.id.iv_ext:
                hidImm();
                llytExt.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_send:      //  文本消息
                sendMessage();
                break;
            case R.id.tv_photo:     //  相册
                Matisse.from(ChateActivity.this)
                        .choose(MimeType.ofAll(), false)
                        .countable(true)
                        .maxSelectable(1)
                        //                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        //                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
            case R.id.tv_capter:    //  相机拍照
                CaptureActivity.launch(mActivity);
                break;
            case R.id.tv_call:      //  语音通话
                break;
            case R.id.tv_vedio:     //  视屏通话
                break;
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        if (TextUtils.isEmpty(etInput.getText().toString().trim()))
            return;

        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(etInput.getText().toString(), loginName);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        etInput.setText("");
        etInput.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 收起键盘
                hidImm();

            }
        }, 200);
        ivExt.setVisibility(View.VISIBLE);
        tvSend.setVisibility(View.GONE);

        mList.add(message);
        mAdapter.notifyDataSetChanged();
        rycvMessage.smoothScrollToPosition(mList.size() - 1);
    }

    private void hidImm() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);

    }

    private void sendImgMessage(String imagePath) {
        //        发送图片消息
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, loginName);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);

        EMClient.getInstance().chatManager().sendMessage(message);

        mList.add(message);
        mAdapter.notifyDataSetChanged();
    }

    public static void toThisAvtivity(Activity activity, String loginName, String chateName, String chateAvator) {
        Intent intent = new Intent(activity, ChateActivity.class);
        intent.putExtra("userId", loginName);
        intent.putExtra("chateName", chateName);
        intent.putExtra("chateAvator", chateAvator);
        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);

        activity.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {  //  相册选择照片
            Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
            Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));

            if (Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size() > 0) {
                String photo = Matisse.obtainPathResult(data).get(0);
                //                sendImgMessage(photo.contains("file:") ? photo : "file:///" + photo);
                sendImgMessage(photo);
            }
        }
    }

    @Subscribe
    public void onTakePhotoChange(ChateTakePhotoEvent event) {
        sendImgMessage(event.photoPath);
    }

    public void AcceptMessage(List<EMMessage> messages) {
        mList.addAll(messages);
        mAdapter.notifyDataSetChanged();
    }
}
