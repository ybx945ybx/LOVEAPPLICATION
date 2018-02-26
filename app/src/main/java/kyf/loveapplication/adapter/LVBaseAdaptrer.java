package kyf.loveapplication.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kyf.loveapplication.R;

/**
 * Created by 55haitao on 2016/11/8.
 */

public abstract class LVBaseAdaptrer<T> extends BaseAdapter {

    protected ArrayList<T> mDatas;

    protected Context mContext;

    private int mResId;

    public LVBaseAdaptrer(Context context, ArrayList<T> datas, int resId) {
        mContext = context;
        mDatas = new ArrayList<T>(datas);
        mResId = resId;
    }

    public ArrayList<T> getmDatas() {
        return mDatas;
    }

    public void setmDatas(ArrayList<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LVHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResId, parent, false);
            holder = new LVHolder(convertView);
            convertView.setTag(R.id.tag_for_item, holder);
        } else {
            holder = (LVHolder) convertView.getTag(R.id.tag_for_item);
        }
        convertView.setTag(R.id.tag_for_position, position);
        bindView(holder, mDatas.get(position));

        return convertView;
    }

    public void addDatas(ArrayList<T> datas) {
        if (datas != null) {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void changeDatas(ArrayList<T> datas) {
        if (datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public abstract void bindView(LVHolder holder, T t);

    public static class LVHolder {
        public  HashMap<Integer, View> mViewMaps;
        private View                   mItemView;

        public LVHolder(View itemView) {
            mViewMaps = new HashMap<>();
            this.mItemView = itemView;
        }

        /**
         * 代替findViewById
         *
         * @param id
         * @param <T>
         * @return
         */
        public <T extends View> T getView(int id) {
            View view = mViewMaps.get(id);
            if (view == null) {
                view = mItemView.findViewById(id);
                mViewMaps.put(id, view);
            }
            return (T) view;
        }

        public View setCheckStatus(int id, boolean b) {
            View view = getView(id);
            ((Checkable) view).setChecked(b);
            return view;
        }

        public void setText(int id, String txt) {
            ((TextView) getView(id)).setText(txt);
        }

        public void setVisibile(int id, boolean visible) {
            (getView(id)).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }

        public <T extends View> T getItemView() {
            return (T) mItemView;
        }

        public int getPosition() {
            return (int) mItemView.getTag(R.id.tag_for_position);
        }
    }

}
