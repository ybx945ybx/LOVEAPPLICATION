package kyf.loveapplication.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import kyf.loveapplication.R;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.ui.view.LoveImageView;

/**
 * Created by a55 on 2018/2/12.
 */

public class NewContactsAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
    private Activity mActivity;

    public NewContactsAdapter(Activity mActivity, ArrayList<UserBean> data) {
        super(R.layout.chate_item, data);
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        ((LoveImageView)helper.getView(R.id.iv_avator)).setImageURI(item.avatar);
        helper.setText(R.id.tv_user, item.nickName);

        helper.setVisible(R.id.tv_message, false);
        helper.setVisible(R.id.tv_date, false);
        helper.setVisible(R.id.view_line_big, false);
        helper.setVisible(R.id.view_line_small, true);
//        helper.setText(R.id.tv_date, item.date);
//        helper.setText(R.id.tv_message, item.message);
    }
}
