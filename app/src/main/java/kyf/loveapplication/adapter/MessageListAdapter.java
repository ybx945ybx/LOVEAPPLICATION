package kyf.loveapplication.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.adapter.message.EMAImageMessageBody;

import java.util.ArrayList;
import java.util.List;

import kyf.loveapplication.R;
import kyf.loveapplication.data.constant.Constant;
import kyf.loveapplication.data.model.ChateBean;
import kyf.loveapplication.ui.view.LoveImageView;
import kyf.loveapplication.utils.LoveUtils;
import kyf.loveapplication.utils.UserManager;

/**
 * Created by a55 on 2018/2/12.
 */

public class MessageListAdapter extends BaseQuickAdapter<EMMessage, BaseViewHolder> {

    private Activity mActivity;
    private String   chateAvator;
    private String   meAvator;

    public MessageListAdapter(Activity mActivity, ArrayList<EMMessage> data, String chateAvator) {
        super(data);
        this.mActivity = mActivity;
        this.chateAvator = chateAvator;
        meAvator = UserManager.getInstance().getUserAvator();

        setMultiTypeDelegate(new MultiTypeDelegate<EMMessage>() {
            @Override
            protected int getItemType(EMMessage bean) {
                int type = 0;
                if (bean.getBody() instanceof EMTextMessageBody) {
                    type = Constant.MESSAGE_TYPE_TXT;
                }else if (bean.getBody() instanceof EMImageMessageBody){
                    type = Constant.MESSAGE_TYPE_IMG;
                }else if (bean.getBody() instanceof EMImageMessageBody){
                    type = Constant.MESSAGE_TYPE_CALL;
                }else if (bean.getBody() instanceof EMImageMessageBody){
                    type = Constant.MESSAGE_TYPE_VEDIO;
                }
                return type;
            }
        });

        getMultiTypeDelegate()
                // 文本
                .registerItemType(Constant.MESSAGE_TYPE_TXT, R.layout.message_item)
                // 图片
                .registerItemType(Constant.MESSAGE_TYPE_IMG, R.layout.message_img_item)
                // 视屏
                .registerItemType(Constant.MESSAGE_TYPE_CALL, R.layout.message_call_item)
                // 语音
                .registerItemType(Constant.MESSAGE_TYPE_VEDIO, R.layout.message_vedio_item);

    }

    @Override
    protected void convert(BaseViewHolder helper, EMMessage item) {
        switch (helper.getItemViewType()) {
            case Constant.MESSAGE_TYPE_TXT:
                setTxtView(helper, item);
                break;
            case Constant.MESSAGE_TYPE_IMG:
                setImgView(helper, item);
                break;
        }

    }


    public void setChateAvator(String chateAvator) {
        this.chateAvator = chateAvator;
    }

    /**
     * 图片消息
     * @param helper
     * @param item
     */
    private void setImgView(BaseViewHolder helper, EMMessage item) {
        EMImageMessageBody message = (EMImageMessageBody) item.getBody();
        helper.setText(R.id.tvTime, LoveUtils.formatLatelyTime(String.valueOf(item.getMsgTime())));
        if (item.direct() == EMMessage.Direct.RECEIVE) {  //  接收消息
            helper.getView(R.id.leftLayout).setVisibility(View.VISIBLE);
            helper.getView(R.id.rightLayout).setVisibility(View.GONE);
            ((LoveImageView) helper.getView(R.id.leftContent)).setImageURI(message.getRemoteUrl());
            ((LoveImageView) helper.getView(R.id.leftImage)).setImageURI(chateAvator);
        } else {                     //   发送消息
            helper.getView(R.id.leftLayout).setVisibility(View.GONE);
            helper.getView(R.id.rightLayout).setVisibility(View.VISIBLE);
            ((LoveImageView) helper.getView(R.id.rightContent)).setImageURI(message.getRemoteUrl());
            ((LoveImageView) helper.getView(R.id.rightImage)).setImageURI(meAvator);

        }
    }

    /**
     * 文本消息
     * @param helper
     * @param item
     */
    private void setTxtView(BaseViewHolder helper, EMMessage item) {
        EMTextMessageBody message = (EMTextMessageBody) item.getBody();
        String time = LoveUtils.formatLatelyTime(String.valueOf(item.getMsgTime()));
        helper.setText(R.id.tvTime, time);

        if (item.direct() == EMMessage.Direct.RECEIVE) {  //  接收消息
            helper.getView(R.id.leftLayout).setVisibility(View.VISIBLE);
            helper.getView(R.id.rightLayout).setVisibility(View.GONE);
            helper.setText(R.id.leftContent, message.getMessage());
            ((LoveImageView) helper.getView(R.id.leftImage)).setImageURI(chateAvator);
        } else {                     //   发送消息
            helper.getView(R.id.leftLayout).setVisibility(View.GONE);
            helper.getView(R.id.rightLayout).setVisibility(View.VISIBLE);
            helper.setText(R.id.rightContent, message.getMessage());
            ((LoveImageView) helper.getView(R.id.rightImage)).setImageURI(meAvator);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
