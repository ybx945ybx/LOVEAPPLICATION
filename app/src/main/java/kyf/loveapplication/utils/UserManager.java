package kyf.loveapplication.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import kyf.loveapplication.LoveApplication;
import kyf.loveapplication.data.constant.LoveStringConstants;
import kyf.loveapplication.data.model.UserBean;

/**
 * Created by a55 on 2018/1/31.
 */

public class UserManager {
    private static UserManager sInstance;
    private        UserBean    userObject;

    public static synchronized UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    private UserManager() {
    }

    /**
     * 设置用户
     *
     * @param userObject
     */
    public void setUser(UserBean userObject) {
        if (null == userObject)
            return;
        this.userObject = userObject;
        SPUtils.put(LoveApplication.getContext(), LoveStringConstants.USER, new Gson().toJson(userObject));
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserBean getUser() {
        if (null != this.userObject)
            return this.userObject;
        String strUser = (String) SPUtils.get(LoveApplication.getContext(), LoveStringConstants.USER, "");
        if (TextUtils.isEmpty(strUser))
            return null;
        this.userObject = new Gson().fromJson(strUser, UserBean.class);
        return this.userObject;
    }


    /**
     * 获取用户ID
     *
     * @return
     */
    public String getUserId() {
        if (null != getUser())
            return this.userObject.memberId;
        return "";
    }

    /**
     * 获取用户头像
     *
     * @return
     */
    public String getUserAvator() {
        if (null != getUser())
            return this.userObject.avatar;
        return "";
    }

    /**
     * 获取用户名称
     *
     * @return
     */
    public String getUserName() {
        if (null != getUser())
            return this.userObject.loginName;
        return "";
    }

//    /**
//     * 获取用户名称
//     *
//     * @return
//     */
//    public String getNickname() {
//        if (null != getUser())
//            return this.userObject.nickname;
//        return "";
//    }

    /**
     * 清空用户信息
     */
    public void clearUser() {
        this.userObject = null;
        SPUtils.put(LoveApplication.getContext(), LoveStringConstants.USER, "");
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return null != getUser();
    }

}
