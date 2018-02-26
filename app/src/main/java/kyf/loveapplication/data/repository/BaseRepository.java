package kyf.loveapplication.data.repository;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.widget.Toast;

import kyf.loveapplication.data.exception.LoveException;
import kyf.loveapplication.data.net.ApiModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by a55 on 2017/11/2.
 */

public class BaseRepository {

    protected <T> Observable<T> transform(Observable<ApiModel<T>> observable) {
        return this.transform(observable, null, false);
    }

    protected <T> Observable<T> transform(Observable<ApiModel<T>> observable, Context context, boolean needSystemTime) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ApiModel<T>, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(ApiModel<T> result) {
                        if (result == null) {
                            return Observable.error(new NetworkErrorException());
                        } else {
                            if (result.isSuccess) { // 成功
                                //                                if (needSystemTime) {
                                //                                    if (result.stat != null) {
                                //                                        Toast.makeText(context, result.stat.toString() + "解析的stat", Toast.LENGTH_SHORT).show();
                                //                                        Toast.makeText(context, result.stat.systime + "解析的", Toast.LENGTH_SHORT).show();
                                //                                        Toast.makeText(context, result.stat.cid + "解析的cid", Toast.LENGTH_SHORT).show();
                                //                                        //                                    SPUtils.put(context, "systemTime", result.stat.systime);
                                //
                                //                                        //                                    HaiApplication.productSystemTime = result.stat.systime;
                                //                                    }
                                //                                }
                                return Observable.just(result.data);
                            } else { // 错误
                                return Observable.error(LoveException.createLoveException(0, result.description));
                            }
                        }
                    }
                });
    }
}
