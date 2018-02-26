package kyf.loveapplication.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kyf.loveapplication.R;

/**
 * Created by a55 on 2017/11/18.
 */

public class LoveEditText extends RelativeLayout {

    @BindView(R.id.et_content)  EditText  etContent;
    @BindView(R.id.tv_top)      TextView  tvTop;
    @BindView(R.id.tv_left)     TextView  tvLeft;
    @BindView(R.id.tv_right)    TextView  tvRight;
    @BindView(R.id.iv_right)    ImageView ivRight;
    @BindView(R.id.bottom_line) View      bottomLine;


    private onRightTxtClickListener mOnRightTxtClickListener;
    private onRightImgClickListener mOnRightImgClickListener;

    public LoveEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.love_edit_text, this);
        ButterKnife.bind(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoveEditText);

        String topTxt           = ta.getString(R.styleable.LoveEditText_top_txt);
        String leftTxt          = ta.getString(R.styleable.LoveEditText_left_txt);
        String editTxt          = ta.getString(R.styleable.LoveEditText_edit_txt);
        String editHintTxt      = ta.getString(R.styleable.LoveEditText_edit_hit_txt);
        int    editTxtColor     = ta.getColor(R.styleable.LoveEditText_edit_txt_color, Color.parseColor("#333333"));
        int    editHintTxtColor = ta.getColor(R.styleable.LoveEditText_edit_hit_txt_color, Color.parseColor("#333333"));

        String   rightTxt          = ta.getString(R.styleable.LoveEditText_right_txt);
        boolean  rightTxtEnable    = ta.getBoolean(R.styleable.LoveEditText_right_txt_enable, true);
        boolean  bottomLineVisible = ta.getBoolean(R.styleable.LoveEditText_bottom_line_visible, true);
        Drawable rightImg          = ta.getDrawable(R.styleable.LoveEditText_right_img);
        int            inputType        = ta.getInt(R.styleable.LoveEditText_android_inputType, -1);

        int gravity = ta.getInt(R.styleable.LoveEditText_android_gravity, Gravity.LEFT);
        etContent.setGravity(gravity);
        etContent.setTextColor(editTxtColor);
        etContent.setHintTextColor(editHintTxtColor);

        if (!TextUtils.isEmpty(topTxt)) {
            tvTop.setText(topTxt);
            tvTop.setVisibility(VISIBLE);

        } else {
            tvTop.setVisibility(GONE);
        }

        // inputType
        if (inputType >= 0) {
            etContent.setInputType(inputType);
        }

        if (!TextUtils.isEmpty(leftTxt)) {
            tvLeft.setText(leftTxt);
            tvLeft.setVisibility(VISIBLE);

        } else {
            tvLeft.setVisibility(GONE);

        }

        if (!TextUtils.isEmpty(editHintTxt))
            etContent.setHint(editHintTxt);

        if (!TextUtils.isEmpty(editTxt))
            etContent.setText(editTxt);

        if (!TextUtils.isEmpty(rightTxt)) {
            tvRight.setText(rightTxt);
            tvRight.setEnabled(rightTxtEnable);
        }

        bottomLine.setVisibility(bottomLineVisible ? VISIBLE : GONE);

        if (rightImg != null)
            ivRight.setImageDrawable(rightImg);

        ivRight.setOnClickListener(v -> {
            if (mOnRightImgClickListener != null)
                mOnRightImgClickListener.onRightImgClick(v);
        });

        tvRight.setOnClickListener(v -> {
            if (mOnRightTxtClickListener != null)
                mOnRightTxtClickListener.onRightTxtClick(v);
        });


        ta.recycle();
    }

    public void setTopText(String s) {
        tvTop.setText(s);
    }

    public void setEditText(String s) {
        etContent.setText(s);
    }

    public CharSequence getEditText() {
        return etContent.getText();
    }

    public void setTransformationMethod(TransformationMethod method) {
        etContent.setTransformationMethod(method);
    }

    public void setRightText(String s) {
        tvRight.setText(s);
    }

    public void setRightTextEnable(boolean enable) {
        tvRight.setEnabled(enable);
    }

    public boolean getRightTextEnable() {
        return tvRight.isEnabled();
    }

    public void setRightImgSelect(boolean select) {
        ivRight.setSelected(select);
    }

    public boolean getRightImgSelect() {
        return ivRight.isSelected();
    }

    public interface onRightTxtClickListener {
        void onRightTxtClick(View view);
    }

    public void setOnRightTxtClickListener(onRightTxtClickListener onRightTxtClickListener) {
        mOnRightTxtClickListener = onRightTxtClickListener;
    }

    public interface onRightImgClickListener {
        void onRightImgClick(View view);
    }

    public void setOnRightImgClickListener(onRightImgClickListener onRightImgClickListener) {
        mOnRightImgClickListener = onRightImgClickListener;
    }

    public void setFocusable(boolean focuse){
        etContent.setFocusable(focuse);
    }
}
