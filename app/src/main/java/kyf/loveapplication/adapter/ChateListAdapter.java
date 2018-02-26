package kyf.loveapplication.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import kyf.loveapplication.R;
import kyf.loveapplication.data.model.ChateBean;
import kyf.loveapplication.data.model.SearchResult;
import kyf.loveapplication.data.net.DefaultSubscriber;
import kyf.loveapplication.data.repository.NetRepository;
import kyf.loveapplication.ui.view.LoveImageView;
import kyf.loveapplication.utils.LoveUtils;

/**
 * 会话列表
 * Created by a55 on 2017/11/19.
 */

public class ChateListAdapter extends BaseQuickAdapter<EMConversation, BaseViewHolder> {
    private Activity mActivity;

    public ChateListAdapter(Activity activity, ArrayList<EMConversation> data) {
        super(R.layout.chate_item, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, EMConversation item) {
        //        Logger.d(item.avator);
        List<EMMessage> messages = item.getAllMessages();
        EMMessage       message  = messages.get(0);
        String time = LoveUtils.formatLatelyTime(String.valueOf(message.getMsgTime()));
        helper.setText(R.id.tv_date, time);
        NetRepository.getInstance().findMember(message.getUserName())
                .subscribe(new DefaultSubscriber<SearchResult>() {
                    @Override
                    public void onSuccess(SearchResult searchResult) {
                        helper.setText(R.id.tv_user, searchResult.storeMember.nickName);
                        ((LoveImageView) helper.getView(R.id.iv_avator)).setImageURI(searchResult.storeMember.avatar);
                    }

                    @Override
                    public void onFinish() {

                    }
                });
        if (message.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            helper.setText(R.id.tv_message, body.getMessage());

            //            ((LoveImageView) helper.getView(R.id.iv_avator)).setImageURI(item.avator);
//            helper.setText(R.id.tv_user, message.getUserName());
//            helper.setText(R.id.tv_date, LoveUtils.formatLatelyTime(String.valueOf(message.getMsgTime())));
//            helper.setText(R.id.tv_date, String.valueOf(message.getMsgTime()));
        }else if (message.getBody() instanceof EMImageMessageBody){
            helper.setText(R.id.tv_message, "图片");

        }

    }
}
