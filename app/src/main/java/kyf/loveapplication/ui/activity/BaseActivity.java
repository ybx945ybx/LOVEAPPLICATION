package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import kyf.loveapplication.ActivityCollector;
import kyf.loveapplication.data.constant.LoveStringConstants;
import kyf.loveapplication.data.event.FriendStateChangeEvent;
import kyf.loveapplication.data.exception.LoveException;
import kyf.loveapplication.ui.view.LoveProgressDialog;
import kyf.loveapplication.ui.view.MultipleStatusView;
import kyf.loveapplication.utils.SPUtils;
import kyf.loveapplication.utils.ToastUtils;
import retrofit2.adapter.rxjava.HttpException;

/**
 * activity基类
 * Created by a55 on 2017/10/31.
 */

public class BaseActivity extends RxAppCompatActivity {

    public String TAG = getClass().getSimpleName();
    protected Activity           mActivity;
    private   LoveProgressDialog mHaiProgressDialog;
    protected int PAGE_SIZE = 20;
    protected boolean mHasLoad;                         // 是否加载过一次

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
//                case HaiConstants.ReponseCode.CODE_UTK_EMPTY:
//                case HaiConstants.ReponseCode.CODE_DECODE_USER_TOKEN_ERROR:
//                case HaiConstants.ReponseCode.CODE_USER_TOKEN_EXPIRED:
//                case HaiConstants.ReponseCode.CODE_LOGINED_ON_OTHER_DEVICE:
//                    UserRepository.getInstance().clearUserInfo();
//                    EventBus.getDefault().post(new LoginStateChangeEvent(false));
//                    showNeedLoginDialog(msg.arg1);
//                    break;
//                case HaiConstants.ReponseCode.CODE_DECODE_DEVICE_TOKEN_ERROR:       //DeviceToken异常
//                    DeviceRepository.getInstance().clearDeviceToken();
//                    DeviceRepository.getInstance()
//                            .registerDevice()
//                            .subscribe(new DefaultSubscriber<RegisterDeviceResult>() {
//                                @Override
//                                public void onSuccess(RegisterDeviceResult registerDeviceResult) {
//                                    DeviceRepository.getInstance().setDeviceToken(registerDeviceResult.deviceToken);
//                                    SPUtils.put(mActivity, "_dtk", registerDeviceResult.deviceToken);
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                }
//                            });
//                    break;
//                case 500:
//                case 102023:
//                    break;
//                case 14000:
//                    // 下架商品
//                    EventBus.getDefault().post(new APIErrorEvent(14000, (String) msg.obj));
//                    break;
//                case 102054:
//                    ToastUtils.showToast(mActivity, "该商品每次限购一件");
//                    break;
//                case 110:
//                    // 兑换优惠券时候，兑换码过期
//                    EventBus.getDefault().post(new APIErrorEvent(110, (String) msg.obj));
//                    break;
//                default:
//                    String str = (String) msg.obj;
//                    if (!TextUtils.isEmpty(str)) {
//                        ToastUtils.showToast(BaseActivity.this, str);
//                    }
//                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        ActivityCollector.add(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remove(this);
        if (EventBus.getDefault().isRegistered(this)) {                  //防止内存泄漏
            EventBus.getDefault().unregister(this);
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void showProgressDialog() {
        showProgressDialog(false);
    }

    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(null, cancelable);
    }

    public void showProgressDialog(String message) {
        showProgressDialog(message, false);
    }

    /**
     * 显示ProgressDialog
     *
     * @param message    显示文字
     * @param cancelable 是否可取消
     */
    public void showProgressDialog(String message, boolean cancelable) {
        if (mHaiProgressDialog == null) {
            mHaiProgressDialog = new LoveProgressDialog(this);
            mHaiProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (!mHaiProgressDialog.isShowing()) {
            mHaiProgressDialog.show();
        }
        mHaiProgressDialog.setMessage(message);
        mHaiProgressDialog.setCancelable(cancelable);
    }


    /**
     * 隐藏ProgressDialog
     */
    public void dismissProgressDialog() {
        if (mHaiProgressDialog == null) return;

        if (mHaiProgressDialog.isShowing()) {
            mHaiProgressDialog.setMessage("正在加载...");
            mHaiProgressDialog.dismiss();
        }
    }

    /**
     * 通用显示错误页
     *
     * @param msv     MultipleStatusView
     * @param e       异常
     * @param hasData 是否加载过数据
     */
    public void showFailView(MultipleStatusView msv, Throwable e, boolean hasData) {
        if ((e instanceof HttpException || e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException)
                && !hasData) {    // 网络问题
            msv.showNoNetwork();
        } else if (e instanceof LoveException && !hasData) {
            msv.showError();
        } else if (!hasData) {
            msv.showError();
        }
    }


    public void initChateData() {
        try {
            EMClient.getInstance().groupManager().loadAllGroups();
            EMClient.getInstance().chatManager().loadAllConversations();
            EMClient.getInstance().contactManager().getBlackListFromServer();

        }catch (Exception e){

        }

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

//            @Override
//            public void onContactAgreed(String username) {
//                //好友请求被同意
//            }
//
//            @Override
//            public void onContactRefused(String username) {
//                //好友请求被拒绝
//            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                ToastUtils.showToast("收到好友邀请");
                Logger.d("username = " + username);
                Logger.d("reason = " + reason);
                EventBus.getDefault().post(new FriendStateChangeEvent(3));
//                SPUtils.put(mActivity, LoveStringConstants.PENDDING_ACCEPT,);
            }

            @Override
            public void onFriendRequestAccepted(String username) {

            }

            @Override
            public void onFriendRequestDeclined(String username) {

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
            }
        });
    }
}
