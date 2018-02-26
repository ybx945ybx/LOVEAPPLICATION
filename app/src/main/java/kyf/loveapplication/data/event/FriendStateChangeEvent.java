package kyf.loveapplication.data.event;

/**
 * Created by a55 on 2018/2/12.
 */

public class FriendStateChangeEvent {
    public int state;    //  0是加好友1是删除好友2是拉黑3是收到好友请求

    public FriendStateChangeEvent(int state){
        this.state = state;
    }
}
