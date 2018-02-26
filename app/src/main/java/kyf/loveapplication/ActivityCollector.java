package kyf.loveapplication;

import android.os.Handler;
import android.os.Message;

import java.util.Stack;

import kyf.loveapplication.ui.activity.BaseActivity;

/**
 * Created by a55 on 2017/10/31.
 */

public class ActivityCollector {
    /**
     * 异常处理的Handler,用于发送消息
     */
    private static Stack<BaseActivity> stack = new Stack<BaseActivity>();

    /**
     * 注册Activity
     */
    public static void add(BaseActivity activity) {
        if (activity != null) {
            stack.push(activity);
        }
    }

    /**
     * 反注册Handler的引用,防止内存泄漏
     */
    public static void remove(BaseActivity handler) {
        stack.remove(handler);
    }

    public static void sendExceptionMessage(int code, String msg) {
        Handler topHandler = stack.peek().getHandler();
        Message message    = Message.obtain();
        message.arg1 = code;
        message.obj = msg;
        message.what = 0;    //标记,用于取消Message
        topHandler.sendMessage(message);
    }

    public static BaseActivity getTopActivity() {
        return stack.peek();
    }

    public static BaseActivity getSecondActivity() {
        if (stack.size() > 1) {
            return stack.get(stack.size() - 2);
        } else {
            return null;
        }
        //        return stack.peek();
    }

    public static boolean contains(Class cls) {

        boolean result = false;

        for (BaseActivity activity : stack) {
            if (activity.getClass() == cls) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static void finishAll() {
        for (BaseActivity activity : stack) {
            //            stack.remove(baseActivity);
            //                baseActivity.onBackPressed();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        stack.clear();
    }
}
