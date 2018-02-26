package kyf.loveapplication.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import kyf.loveapplication.R;

/**
 * Created by a55 on 2017/10/31.
 */

public class LoveTextView extends AppCompatTextView {
    private static Typeface sRegular;
    private static Typeface sLight;
    private static Typeface sGotham;

    private final static String Regular = "0";
    private final static String Light   = "1";
    private final static String Gotham  = "2";

    public LoveTextView(Context context) {
        super(context);
        useTypeFace(context, null);
    }

    public LoveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        useTypeFace(context, attrs);
    }

    private void useTypeFace(Context context, AttributeSet attrs) {
        TypedArray t = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoveTextView, R.attr.expandTypeface, 0);

        String value = t.getString(R.styleable.LoveTextView_expandTypeface);
        if (TextUtils.isEmpty(value)) {
            setTypeface(sRegular);
        } else if (value.equals(Regular)) {
            setTypeface(sRegular);
        } else if (value.equals(Light)) {
            setTypeface(sLight);
        } else if (value.equals(Gotham)) {
            setTypeface(sGotham);
        }
        t.recycle();
    }

    public static void init(Context context) {
        if (sRegular == null) {
            sRegular = Typeface.createFromAsset(context.getAssets(), "fonts/FZLTH_R_GB.TTF");
        }
        if (sLight == null) {
            sLight = Typeface.createFromAsset(context.getAssets(), "fonts/FZLTH_L_GB.TTF");
        }
        if (sGotham == null) {
            sGotham = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book.ttf");
        }
    }
}
