package kyf.loveapplication.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import kyf.loveapplication.R;

/**
 * Created by a55 on 2017/10/31.
 */

public class LoveProgressDialog extends Dialog {
    private String   mMsg;
    private TextView mTvMsg;

    public LoveProgressDialog(Context context) {
        this(context, R.style.ProgressDialog, null);
    }

    public LoveProgressDialog(Context context, int themeResId, String message) {
        this(context, themeResId, message, false);
    }

    public LoveProgressDialog(Context context, int themeResId, String message, boolean cancelable) {
        super(context, themeResId);

        this.mMsg = message;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_progress_dialog);

        mTvMsg = (LoveTextView) findViewById(R.id.tv_msg);
        setMessage(mMsg);
    }


    /**
     * 设置Dialog显示文字
     */
    public void setMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            mTvMsg.setText(msg);
        }
    }


}
