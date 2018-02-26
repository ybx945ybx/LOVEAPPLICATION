package kyf.loveapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kyf.loveapplication.R;
import kyf.loveapplication.data.model.FriendListResult;
import kyf.loveapplication.data.model.UserBean;
import kyf.loveapplication.ui.activity.UserHomePageActivity;
import kyf.loveapplication.ui.view.LoveImageView;
import kyf.loveapplication.utils.LoveUtils;

/**
 * Created by 55haitao on 2016/11/9.
 */

public class ContactsAdapter extends SectionedBaseAdapter {


    private ArrayList<FriendListResult> datas;

    private Activity mActivity;

    private LayoutInflater inflater;

    private View.OnClickListener mItemListener;

    private View.OnClickListener mStarListener;

    public ContactsAdapter(Activity activity, ArrayList<FriendListResult> datas) {
        this.mActivity = activity;
        this.datas = datas;
        this.inflater = LayoutInflater.from(activity);
        initListener();
    }

    private void initListener() {

        mStarListener = v -> {

            //            if (!HaiUtils.needLogin(mActivity)) {
            //                GetFollowBrandStoreResult.DataBean item = (GetFollowBrandStoreResult.DataBean) v.getTag();
            //
            //                ProductRepository.getInstance()
            //                        .followBrandSeller(mIsBrand ? PageType.BRAND : PageType.SELLER, item.nameen)
            //                        .subscribe(new DefaultSubscriber<CommonDataBean>() {
            //                            @Override
            //                            public void onSuccess(CommonDataBean commonDataBean) {
            //                                if (!HaiUtils.needLogin(mActivity)) {
            //                                    item.is_followed = !item.is_followed;
            //                                    if (item.is_followed)
            //                                        ToastUtils.showToast(mActivity, "关注成功");
            //                                    notifyDataSetChanged();
            //                                }
            //                            }
            //
            //                            @Override
            //                            public void onFinish() {
            //
            //                            }
            //                        });
            //            }
        };

        mItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHomePageActivity.toThisAvtivity(mActivity, (String) v.getTag());
            }
        };

    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        long result = 0;
        result += section;
        for (int i = 0; i < section; i++) {
            result += getCountForSection(i);
        }
        result += position;
        return result;
    }

    @Override
    public int getSectionCount() {
        return LoveUtils.getSize(datas);
    }

    @Override
    public int getCountForSection(int section) {
        return datas.get(section).childList.size();
        //        return datas.get(section).values.size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        UserBean item = datas.get(section).childList.get(position);

        LVBaseAdaptrer.LVHolder holder = null;
        //
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chate_item, parent, false);
            holder = new LVBaseAdaptrer.LVHolder(convertView);

//                                holder.getItemView().setOnClickListener(mItemListener);
            //                    holder.getView(R.id.concernBtn).setOnClickListener(mStarListener);

            convertView.setTag(holder);
        } else {
            holder = (LVBaseAdaptrer.LVHolder) convertView.getTag();
        }

//        holder.getItemView().setTag(R.id.tag_for_img, item.memberId);

        ((LoveImageView)holder.getView(R.id.iv_avator)).setImageURI(item.avatar);
        holder.setText(R.id.tv_user, item.nickName);

        holder.setVisibile(R.id.tv_message, false);
        holder.setVisibile(R.id.tv_date, false);
        holder.setVisibile(R.id.view_line_big, false);
        holder.setVisibile(R.id.view_line_small, true);

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHomePageActivity.toThisAvtivity(mActivity, item.memberId);

            }
        });

//        String title = item.nameen;
//        if (mIsBrand && !TextUtils.equals(item.namecn, item.nameen)) {
//            title = item.nameen + "/" + item.namecn;
//        }
//
//        holder.setText(R.id.brandNameTxt, title);
//        CheckedTextView checkedTextView = holder.getView(R.id.concernBtn);
//        checkedTextView.setTag(item);
//
//        checkedTextView.setChecked(item.is_followed);
//
//        if (TextUtils.isEmpty(item.logo1)) {
//            holder.setVisibile(R.id.smallCoverTex, true);
//            holder.setVisibile(R.id.smallCoverImg, false);
//
//            String logoName = "";
//            if (!TextUtils.isEmpty(item.nameen)) {
//                logoName = item.nameen;
//            } else if (!TextUtils.isEmpty(item.namecn)) {
//                logoName = item.namecn;
//            } else {
//                logoName = item.name;
//            }
//
//            HaiUtils.setBrandOrSellerImg(holder.getView(R.id.smallCoverTex), logoName, 2);
//
//        } else {
//            holder.setVisibile(R.id.smallCoverTex, false);
//            holder.setVisibile(R.id.smallCoverImg, true);
//
//            UPaiYunLoadManager.loadImage(mActivity, item.logo1, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, holder.getView(R.id.smallCoverImg));
//        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_contacts_header, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.headerTxt)).setText(datas.get(section).groupName);

        return convertView;
    }

    public void changeData(ArrayList<FriendListResult> datas) {
        if (datas == null) {
            return;
        }
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

}
