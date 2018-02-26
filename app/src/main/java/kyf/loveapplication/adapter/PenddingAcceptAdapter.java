package kyf.loveapplication.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

import kyf.loveapplication.R;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.ui.view.LoveImageView;

/**
 * Created by a55 on 2018/2/19.
 */

public class PenddingAcceptAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    private Activity      mActivity;
    private OnAcceptClick mOnAcceptClick;

    public PenddingAcceptAdapter(Activity mActivity, ArrayList<UserBean> data) {
        super(R.layout.chate_item, data);
        this.mActivity = mActivity;
    }

    public interface OnAcceptClick {
        void OnAccept(String friendId);
    }

    public void setOnAcceptClick(OnAcceptClick mOnAcceptClick) {
        this.mOnAcceptClick = mOnAcceptClick;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        ((LoveImageView) helper.getView(R.id.iv_avator)).setImageURI(item.avatar);
        helper.setText(R.id.tv_user, item.nickName);

        helper.setVisible(R.id.tv_message, false);
        //        helper.setVisible(R.id.tv_date, false);
        TextView tvAccept = helper.getView(R.id.tv_date);
        //        =1 则是 没有通过 显示为接受按钮   =2 则是已经通过  显示为已添加
        tvAccept.setEnabled("1".equals(item.status));
        tvAccept.setText("1".equals(item.status) ? "接受" : "已添加");

        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 统一好友请求
                    EMClient.getInstance().contactManager().acceptInvitation(item.loginName);
                    item.status = "2";
                    notifyDataSetChanged();
                    if (mOnAcceptClick != null) {
                        mOnAcceptClick.OnAccept(item.memberId);
                    }
                    //                    tvAccept.setEnabled(false);
                } catch (Exception e) {
                }
            }
        });

        helper.setVisible(R.id.view_line_big, false);
        helper.setVisible(R.id.view_line_small, true);
    }
}
