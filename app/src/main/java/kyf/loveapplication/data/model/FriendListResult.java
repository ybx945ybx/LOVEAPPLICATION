package kyf.loveapplication.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a55 on 2018/2/16.
 */

public class FriendListResult {

    /**
     * childList : [{"avatar":"http://47.104.84.211/static/resource/headpic/da678e17-2be2-44d5-8686-21e362913ac5.png","loginMobile":"18000000002","loginName":"18000000002","memberId":4727,"nickName":"test002","storeId":238}]
     * groupName : T
     */

    public String              groupName;
    public ArrayList<UserBean> childList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<UserBean> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<UserBean> childList) {
        this.childList = childList;
    }

    //    public static class ChildListBean {
    //        /**
    //         * avatar : http://47.104.84.211/static/resource/headpic/da678e17-2be2-44d5-8686-21e362913ac5.png
    //         * loginMobile : 18000000002
    //         * loginName : 18000000002
    //         * memberId : 4727
    //         * nickName : test002
    //         * storeId : 238
    //         */
    //
    //        p String avatar;
    //        private String loginMobile;
    //        private String loginName;
    //        private int    memberId;
    //        private String nickName;
    //        private int    storeId;
    //
    //        public String getAvatar() {
    //            return avatar;
    //        }
    //
    //        public void setAvatar(String avatar) {
    //            this.avatar = avatar;
    //        }
    //
    //        public String getLoginMobile() {
    //            return loginMobile;
    //        }
    //
    //        public void setLoginMobile(String loginMobile) {
    //            this.loginMobile = loginMobile;
    //        }
    //
    //        public String getLoginName() {
    //            return loginName;
    //        }
    //
    //        public void setLoginName(String loginName) {
    //            this.loginName = loginName;
    //        }
    //
    //        public int getMemberId() {
    //            return memberId;
    //        }
    //
    //        public void setMemberId(int memberId) {
    //            this.memberId = memberId;
    //        }
    //
    //        public String getNickName() {
    //            return nickName;
    //        }
    //
    //        public void setNickName(String nickName) {
    //            this.nickName = nickName;
    //        }
    //
    //        public int getStoreId() {
    //            return storeId;
    //        }
    //
    //        public void setStoreId(int storeId) {
    //            this.storeId = storeId;
    //        }
    //    }
}
