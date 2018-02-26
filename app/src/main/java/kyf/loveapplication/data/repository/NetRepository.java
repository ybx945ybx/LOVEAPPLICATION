package kyf.loveapplication.data.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import kyf.loveapplication.data.model.FriendListResult;
import kyf.loveapplication.data.model.SearchResult;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.data.model.FriendVerifyListResult;
import kyf.loveapplication.data.net.RetrofitFactory;
import kyf.loveapplication.data.net.api.NetService;
import kyf.loveapplication.utils.UserManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by a55 on 2018/1/30.
 */

public class NetRepository extends BaseRepository {
    private static volatile NetRepository instance;
    private final           NetService    mHomeService;

    private NetRepository() {
        mHomeService = RetrofitFactory.createService(NetService.class);
    }

    public static NetRepository getInstance() {
        if (instance == null) {
            synchronized (NetRepository.class) {
                if (instance == null) {
                    instance = new NetRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 注册
     * avatar头像
     * mobile手机号
     * nickname昵称
     * province 省
     * city 市
     * district 区
     * password 密码
     * smsCode  验证码
     */
    public Observable<UserBean> register(File avatar, String mobile, String nickname, String province, String city, String district, String password, String smsCode) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mobile", mobile)
                .addFormDataPart("nickname", nickname)
                .addFormDataPart("province", province)
                .addFormDataPart("city", city)
                .addFormDataPart("district", district)
                .addFormDataPart("password", password)
                .addFormDataPart("smsCode", smsCode)
                .addFormDataPart("avatar", avatar.getName(), RequestBody.create(MediaType.parse("image/*"), avatar))
                .build();
        return transform(mHomeService.register(requestBody));
    }

    //    public Observable<UserBean> register(String avatar, String mobile, String nickname, String province, String city, String district, String password) {
    //        TreeMap<String, Object> map = new TreeMap<>();
    //        map.put("avatar", "123");
    //        map.put("mobile", mobile);
    //        map.put("nickname", nickname);
    //        map.put("province", province);
    //        map.put("city", city);
    //        map.put("district", district);
    //        map.put("password", password);
    //
    //        return transform(mHomeService.register(map));
    //    }

    /**
     * 注册获取验证码
     * mobile password
     */
    public Observable<UserBean> sendCode(String mobile) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("mobile", mobile);
        return transform(mHomeService.sendCode(map));
    }


    /**
     * 登录获取验证码
     * mobile password
     */
    public Observable<UserBean> sendLoginCode(String mobile) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("mobile", mobile);
        return transform(mHomeService.sendLoginCode(map));
    }

    /**
     * 登录
     * mobile password
     */
    public Observable<UserBean> login(String mobile, String password) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("mobile", mobile);
        map.put("password", password);
        return transform(mHomeService.login(map));
    }

    /**
     * 验证码登录
     * mobile password
     */
    public Observable<UserBean> loginBySMSCode(String mobile, String smsCode) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("mobile", mobile);
        map.put("smsCode", smsCode);
        return transform(mHomeService.loginBySMSCode(map));
    }

    /**
     * 查找好友
     *
     * @param friendLoginName
     * @return
     */
    public Observable<SearchResult> findMember(String friendLoginName) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("friendLoginName", friendLoginName);
        addUserId(map);
        return transform(mHomeService.findMember(map));
    }

    /**
     * 添加好友验证信息
     *
     * @param friendId
     * @return
     */
    public Observable<UserBean> addFriendVerify(String friendId, String verifyContent) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("friendId", friendId);
        map.put("verifyContent", verifyContent);
        addUserId(map);
        return transform(mHomeService.addFriendVerify(map));
    }

    /**
     * 添加好友
     *
     * @param friendId
     * @return
     */
    public Observable<UserBean> addFriend(String friendId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("friendId", friendId);
        addUserId(map);
        return transform(mHomeService.addFriend(map));
    }

    /**
     * 好友列表
     *
     * @return
     */
    public Observable<ArrayList<FriendListResult>> friendList() {
        TreeMap<String, Object> map = new TreeMap<>();
        addUserId(map);
        return transform(mHomeService.friendList(map));
    }

    /**
     * 好友详情
     * @param friendId
     * @return
     */
    public Observable<SearchResult> friendDetail(String friendId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("friendId", friendId);
        addUserId(map);
        return transform(mHomeService.friendDetail(map));
    }

    /**
     * 删除好友
     * @param friendId
     * @return
     */
    public Observable<UserBean> deleteFriend(String friendId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("friendId", friendId);
        addUserId(map);
        return transform(mHomeService.deleteFriend(map));
    }

    /**
     * 好友验证信息列表
     *
     * @return
     */
    public Observable<FriendVerifyListResult> friendVerifyList() {
        TreeMap<String, Object> map = new TreeMap<>();
        addUserId(map);
        return transform(mHomeService.friendVerifyList(map));
    }

    private void addUserId(TreeMap<String, Object> map) {
        map.put("userId", UserManager.getInstance().getUserId());
    }

}
