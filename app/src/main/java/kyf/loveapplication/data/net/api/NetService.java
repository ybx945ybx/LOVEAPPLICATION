package kyf.loveapplication.data.net.api;

import java.util.ArrayList;
import java.util.Map;

import kyf.loveapplication.data.model.FriendListResult;
import kyf.loveapplication.data.model.SearchResult;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.model.FriendVerifyListResult;
import kyf.loveapplication.data.net.ApiModel;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by a55 on 2018/1/30.
 */

public interface NetService {
    /**
     * 注册
     */
    @POST("member/register.json")
    Observable<ApiModel<UserBean>> register(@Body RequestBody Body);
    //    Observable<ApiModel<UserBean>> register(@QueryMap Map<String, Object> body);

    /**
     * 注册获取验证码
     */
    @GET("member/sendCode.json")
    Observable<ApiModel<UserBean>> sendCode(@QueryMap Map<String, Object> body);

    /**
     * 登录
     */
    @GET("member/login.json")
    Observable<ApiModel<UserBean>> login(@QueryMap Map<String, Object> body);

    /**
     * 验证码登录
     */
    @GET("member/loginBySMSCode.json")
    Observable<ApiModel<UserBean>> loginBySMSCode(@QueryMap Map<String, Object> body);

    /**
     * 登录获取短信验证码
     */
    @GET("member/sendLoginCode.json")
    Observable<ApiModel<UserBean>> sendLoginCode(@QueryMap Map<String, Object> body);

    /**
     * 查找好友
     */
    @GET("member/findMember.json")
    Observable<ApiModel<SearchResult>> findMember(@QueryMap Map<String, Object> body);

    /**
     * 添加好友验证信息
     */
    @GET("member/addFriendVerify.json")
    Observable<ApiModel<UserBean>> addFriendVerify(@QueryMap Map<String, Object> body);

    /**
     * 添加好友
     */
    @GET("member/addFriend.json")
    Observable<ApiModel<UserBean>> addFriend(@QueryMap Map<String, Object> body);

    /**
     * 删除好友
     */
    @GET("member/deleteFriend.json")
    Observable<ApiModel<UserBean>> deleteFriend(@QueryMap Map<String, Object> body);

    /**
     * 删除好友
     */
    @GET("member/friendVerifyList.json")
    Observable<ApiModel<FriendVerifyListResult>> friendVerifyList(@QueryMap Map<String, Object> body);

    /**
     * 好友列表
     */
    @GET("member/friendList.json")
    Observable<ApiModel<ArrayList<FriendListResult>>> friendList(@QueryMap Map<String, Object> body);

    /**
     * 好友详情
     */
    @GET("member/friendDetail.json")
    Observable<ApiModel<SearchResult>> friendDetail(@QueryMap Map<String, Object> body);

}
