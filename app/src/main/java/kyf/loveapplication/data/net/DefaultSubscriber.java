package kyf.loveapplication.data.net;

import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import kyf.loveapplication.data.exception.LoveException;
import kyf.loveapplication.utils.ToastUtils;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by a55 on 2017/11/2.
 */

public abstract class DefaultSubscriber<T> extends Subscriber<T> {


    @Override
    public void onCompleted() {
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        Logger.d(e);
        if (e instanceof HttpException || e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {    // 网络问题
            if (onFailed(e)) {
                ToastUtils.showToast("网络请求失败，请检查您的网络设置");
            }
        } else if (e instanceof LoveException) {
            if (onFailed(e)) {
                ToastUtils.showToast(((LoveException) e).msg);
            }
//            switch (((LoveException) e).code) {
//                case 500:
//                    if (onFailed(e)) {
//                        ToastUtils.showToast("服务器异常，请稍后再试");
//                    }
//                    break;
//                // UserToken异常
//                case ReponseCode.CODE_UTK_EMPTY:
//                case ReponseCode.CODE_DECODE_USER_TOKEN_ERROR:
//                case ReponseCode.CODE_USER_TOKEN_EXPIRED:
//                case ReponseCode.CODE_LOGINED_ON_OTHER_DEVICE:
//                    if (!closeTips) {
//                        UserRepository.getInstance().clearUserInfo();
//                        EventBus.getDefault().post(new LoginStateChangeEvent(false));
//                        ActivityCollector.sendExceptionMessage(((HTException) e).code, "");
//                    }
//                    break;
//
//                // DeviceToken异常
//                case ReponseCode.CODE_DECODE_DEVICE_TOKEN_ERROR: {
//                    DeviceRepository.getInstance().clearDeviceToken();
//                    DeviceRepository.getInstance()
//                            .registerDevice()
//                            .subscribe(new DefaultSubscriber<RegisterDeviceResult>() {
//                                @Override
//                                public void onSuccess(RegisterDeviceResult registerDeviceResult) {
//                                    DeviceRepository.getInstance().setDeviceToken(registerDeviceResult.deviceToken);
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                }
//                            });
//                    break;
//                }
//
//                case ReponseCode.CODE_SSO_ALREADY_BINDED:    // 已经绑定第三方账号
//                    onFailed(e);
//                    break;
//                case ReponseCode.CODE_PRODUCT_NOT_FOUND:    // 下架商品
//                case ReponseCode.CODE_INVALIDATE_POST:      // 帖子已失效
//                    EventBus.getDefault().post(new APIErrorEvent(((HTException) e).code, ((HTException) e).msg));
//                    break;
//                //                case 14110:                     // get_activity  数据不存在
//                case 102023:                     // get_activity  数据不存在
//                    break;
//                //                case 102020:                     // 收货地址不存在
//                //                    break;
//                default:
//                    ToastUtils.showToast(((HTException) e).msg);
//                    break;
//            }
        } else {
            // 其他异常
            if (onFailed(e)) {
//                ToastUtils.showToast(e.getMessage());
            }
            e.printStackTrace();
        }

        //        onFailed(e);
        onFinish();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    /**
     * 网络请求 成功的回调
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 网络请求 失败的回调
     *
     * @param e 异常
     * @return hasData
     */
    public boolean onFailed(Throwable e) {
        return true;
    }

    /**
     * 网络请求 结束的回调
     * Note: 无论成功、失败,该方法一定会执行
     * Note: 当被用作Rx map多个网络请求时的观察者时,onSuccess()会执行多次,而onFailed()、onFinish()只执行一次
     */
    public abstract void onFinish();
}
