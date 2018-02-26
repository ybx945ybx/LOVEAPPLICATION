package kyf.loveapplication.data.model;

import java.io.Serializable;

/**
 * Created by a55 on 2018/1/30.
 */

public class UserBean implements Serializable{
//    avatar : "123"           // 头像
//    loginMobile : "Ffff"      // 手机
//    loginName : "Ffff"
//    memberId : 4721           // 用户id
//    nickName : "Gggg"
//    storeId : 238
    public String avatar;
    public String loginMobile;
    public String loginName;
    public String memberId;
    public String nickName;
    public String storeId;

    public String friendId;
    public String status;
    public String verifyContent;
}
