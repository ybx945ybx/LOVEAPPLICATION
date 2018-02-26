package kyf.loveapplication.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.ActivityCollector;
import kyf.loveapplication.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by 董猛 on 16/7/7.
 */

/**
 * 自定义View,为Activity/Fragment提供头部
 * <p>
 * 使用{@link #setHeadClickListener(OnHeadViewClickListener)}设置监听事件
 * </p>
 * <p/>
 * <p>
 * 使用{@link #setHeadTitle(String)}手动更改title内容
 * </p>
 * <p/>
 * <p>
 * xml属性的使用,参考{@link #init(Context, AttributeSet)}
 * </p>
 */

public class DynamicHeaderView extends RelativeLayout {

    @BindView(R.id.headerLeftImg)
    ImageView headerLeftImg;
    @BindView(R.id.headTitleTxt)
    TextView  headTitleTxt;
    @BindView(R.id.headerRightImg)
    ImageView headerRightImg;
    @BindView(R.id.headRightTxt)
    TextView  headerRightTxt;

    private OnHeadViewClickListener mListener;
    private OnClickListener         mExtraListener;

    public DynamicHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.layout_for_activity_header, this);

        ButterKnife.bind(this);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.DynamicHeaderView);

        Drawable leftD = t.getDrawable(R.styleable.DynamicHeaderView_headerLeftImg);
        if (leftD != null) {
            headerLeftImg.setImageDrawable(leftD);
        }

        Drawable d = t.getDrawable(R.styleable.DynamicHeaderView_headerRightImg);

        if (d != null) {
            headerRightImg.setImageDrawable(d);
            headerRightTxt.setVisibility(GONE);
            headerRightImg.setVisibility(VISIBLE);
        }

        String rightTxt = t.getString(R.styleable.DynamicHeaderView_headerRightTxt);
        if (!TextUtils.isEmpty(rightTxt)) {
            headerRightTxt.setText(rightTxt);
            headerRightTxt.setVisibility(VISIBLE);
            headerRightImg.setVisibility(GONE);
        }

        String titleString = t.getString(R.styleable.DynamicHeaderView_headerTitleTxt);
        headTitleTxt.setText(titleString);

        // 设置右边字体颜色
        int rightColor = t.getColor(R.styleable.DynamicHeaderView_headerRightTxtColor, Color.GRAY);
        headerRightTxt.setTextColor(rightColor);

        t.recycle();
    }

    @OnClick({R.id.headerLeftImg, R.id.headerRightImg, R.id.headRightTxt})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.headerLeftImg:
                if (mExtraListener == null) {
                    ActivityCollector.getTopActivity().onBackPressed();
                } else {
                    mExtraListener.onClick(view);
                }
                break;
            case R.id.headerRightImg:
            case R.id.headRightTxt:
                if (mListener != null) {
                    mListener.onRightImgClick();
                }
                break;
        }
    }

    /**
     * 隐藏左边按钮
     */
    public void hideLeftButton() {
        headerLeftImg.setVisibility(GONE);
    }

    /**
     * 显示左边按钮
     */
    public void showLeftButton() {
        headerLeftImg.setVisibility(VISIBLE);
    }

    /**
     * 手动设置Activity Title 字符串
     *
     * @param title
     */
    public void setHeadTitle(String title) {
        Observable.just(title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> headTitleTxt.setText(title));
    }

    public void setRightText(String text) {
        headerRightImg.setVisibility(View.GONE);
        headerRightTxt.setVisibility(View.VISIBLE);

        headerRightTxt.setText(text);
    }

    public String getRightText() {
        return headerRightTxt.getVisibility() == VISIBLE ? headerRightTxt.getText().toString() : "";
    }

    /**
     * 设置右边字体颜色
     */
    public void setRighTextColor(int color) {
        headerRightTxt.setTextColor(color);
    }

    public void setHeaderRightImg(int resId) {
        headerRightImg.setVisibility(View.VISIBLE);
        headerRightTxt.setVisibility(View.GONE);

        headerRightImg.setImageResource(resId);
    }

    public void setHeaderRightHidden() {

        headerRightImg.setVisibility(GONE);
        headerRightTxt.setVisibility(GONE);

    }

    public void setHeaderLeftHidden(boolean hidden) {

        if (hidden) {
            headerLeftImg.setVisibility(GONE);
        } else {
            headerLeftImg.setVisibility(VISIBLE);
        }
    }

    public void setDividerLineVisibility(int visibility) {
        findViewById(R.id.lineDivider).setVisibility(visibility);
    }

    public void setHeadClickListener(OnHeadViewClickListener mListener) {
        this.mListener = mListener;
    }

    public void setHeaderExtraListener(View.OnClickListener listener) {
        mExtraListener = listener;
    }

    public static interface OnHeadViewClickListener {
        //        void onLeftImgClick();

        void onRightImgClick();
    }
}
