package kyf.loveapplication.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import kyf.loveapplication.LoveApplication;

/**
 * Created by a55 on 2017/11/2.
 */

public class ToastUtils {
    private static Toast sToast;

    public static void showToast(Context context, String content) {
        if (TextUtils.isEmpty(content))
            return;
        if (sToast == null) {
            sToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
        }

        sToast.show();
    }

    public static void showToast(String content) {
        if (TextUtils.isEmpty(content))
            return;
        if (sToast == null) {
            sToast = Toast.makeText(LoveApplication.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
        }

        sToast.show();
    }
}
