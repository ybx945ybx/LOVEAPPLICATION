package kyf.loveapplication.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kyf.loveapplication.ActivityCollector;
import kyf.loveapplication.R;
import kyf.loveapplication.data.constant.Constant;
import kyf.loveapplication.ui.fragment.BaseFragment;
import kyf.loveapplication.ui.fragment.ChatListFragment;
import kyf.loveapplication.ui.fragment.CommunityFragment;
import kyf.loveapplication.ui.fragment.ContactsFragment;
import kyf.loveapplication.ui.fragment.MeFragment;
import kyf.loveapplication.ui.fragment.ProductRelationFragment;
import kyf.loveapplication.utils.ToastUtils;
import kyf.loveapplication.utils.UserManager;

public class MainActivity extends BaseActivity {

    @BindView(R.id.radioGroup) RadioGroup mRadioGroup;         // 底部RadioGroup

    private ArrayList<BaseFragment> mFragments;                // 保存Fragment的集合
    private ArrayList<String>       mPermissionList;           // 保存Fragment的集合

    private int oldPosition;                                   // 上次checked位置
    private int newPosition;                                   // 最新checked位置
    private long clickTime = 0;                                // 第一次点击的时间

    private EMMessageListener msgListener;
    private int Notification_ID_BASE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initVars();
        initViews();
        initData();
    }

    private void getPermission() {
        mPermissionList = new ArrayList<>();
        mPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        mPermissionList.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < mPermissionList.size(); i++) {
                if (checkSelfPermission(mPermissionList.get(i)) != PackageManager.PERMISSION_GRANTED) {
                    list.add(mPermissionList.get(i));
                }
            }

            switch (list.size()) {
                case 0:
                    break;
                case 1:
                    String[] permisses1 = {list.get(0)};
                    requestPermissions(permisses1, 100);
                    break;
                case 2:
                    String[] permisses2 = {list.get(0), list.get(1)};
                    requestPermissions(permisses2, 100);
                    break;
                case 3:
                    String[] permisses3 = {list.get(0), list.get(1), list.get(2)};
                    requestPermissions(permisses3, 100);
                    break;

            }

        }
    }

    private void initVars() {
        getPermission();
        // 获取黑名单
        new Thread(() -> {
            try {
                EMClient.getInstance().contactManager().getBlackListUsernames();
                //                Logger.d(blackNameList);
            } catch (Exception e) {

            }
        }).start();
        setMessageListener();

    }

    private void initViews() {
        //初始化Fragment
        mFragments = new ArrayList<>(5);
        mFragments.add(new ProductRelationFragment());
        mFragments.add(new CommunityFragment());
        mFragments.add(new ChatListFragment());
        mFragments.add(new ContactsFragment());
        mFragments.add(new MeFragment());
        //初始化标记
        oldPosition = 0;
        newPosition = 0;
        getSupportFragmentManager().beginTransaction().add(R.id.contain_view, mFragments.get(0)).commit();
        for (int i = 0; i < mFragments.size(); i++) {
            mRadioGroup.getChildAt(i).setId(i);
        }
        mRadioGroup.check(0);
        //        oldPosition = 0;
        //        newPosition = 3;
        //        switchFragment();
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == 2 || checkedId == 3 || checkedId == 4) {
                if (!UserManager.getInstance().isLogin()) {
                    mRadioGroup.check(newPosition);
                    LoginActivity.lunach(mActivity);
                    return;
                }
            }

            oldPosition = newPosition;
            newPosition = checkedId;

            switchFragment();
        });

    }

    private void initData() {

    }

    public void switchFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mFragments.get(oldPosition));
        BaseFragment fragment = mFragments.get(newPosition);
        if (fragment.isAdded()) {
            ft.show(fragment);
            if (newPosition == 2 || newPosition == 3 || newPosition == 4) {
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.lunach(mActivity);
                }
            }
        } else {
            ft.add(R.id.contain_view, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (System.currentTimeMillis() - clickTime > 2000) {
            Toast.makeText(this, "再点一次退出Love", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public static void toThisAvtivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private void sendNotify(List<EMMessage> messages) {

        NotificationManager        nm      = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // 消息内容
        EMMessage item = messages.get(0);
        if (item.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody message = (EMTextMessageBody) item.getBody();
        builder.setContentText(message.getMessage());
        }

        PendingIntent contentIntent = getPendingIntent(item.getFrom());//PendingIntent.getActivity(this, 100,  i,  PendingIntent.FLAG_UPDATE_CURRENT);
        if (contentIntent != null) {
            builder.setContentIntent(contentIntent);
        }
        builder.setAutoCancel(true);
        Bitmap btm = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        builder.setLargeIcon(btm);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("联信");

        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setTicker("New message");
        //        if(BaseApplication.canVibrite){
        //            builder.setVibrate(new long[]{1000, 500, 1000, 500});
        //        }
        builder.setWhen(System.currentTimeMillis());
        Notification nf = builder.build();

        Notification_ID_BASE += 1;
        nm.notify("联信", Notification_ID_BASE, nf);

    }

    private PendingIntent getPendingIntent(String loginName ) {
        Intent        intent = null;
        PendingIntent pd     = null;

        intent = new Intent(getApplicationContext(), ChateActivity.class);
        intent.putExtra("userId", loginName);
        intent.putExtra("chateName", loginName);
        intent.putExtra("chateAvator", loginName);
        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
        pd = PendingIntent.getActivity(this, 207, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pd;
    }

    private void setMessageListener() {
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息

                Logger.d("收到消息");

                // 正在聊天界面
                if (ActivityCollector.getTopActivity() instanceof ChateActivity) {
                    if (((ChateActivity) ActivityCollector.getTopActivity()).loginName.equals(messages.get(0).getFrom())) {
                        // 当前正在进行的会话
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ChateActivity) ActivityCollector.getTopActivity()).mList.addAll(messages);
                                ((ChateActivity) ActivityCollector.getTopActivity()).mAdapter.notifyDataSetChanged();
                                ((ChateActivity) ActivityCollector.getTopActivity()).rycvMessage.smoothScrollToPosition(((ChateActivity) ActivityCollector.getTopActivity()).mList.size() - 1);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                ToastUtils.showToast("收到新消息");
                                sendNotify(messages);
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtils.showToast("收到新消息");
                            sendNotify(messages);
                        }
                    });
                }


            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                Logger.d("收到偷穿消息");

            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                Logger.d("收到已读回执");

            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
                Logger.d("收到已送达回执");

            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
                Logger.d("消息被撤回");

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                Logger.d("消息状态变动");

            }
        };


        EMClient.getInstance().chatManager().addMessageListener(msgListener);

        //记得在不需要的时候移除listener，如在activity的onDestroy()时
        //        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    protected void onDestroy() {
        //记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);

        super.onDestroy();
    }
}
