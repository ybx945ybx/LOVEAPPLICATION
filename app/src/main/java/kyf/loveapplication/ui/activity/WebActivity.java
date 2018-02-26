package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import kyf.loveapplication.R;

/**
 * Created by a55 on 2018/2/10.
 */

public class WebActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
    }

    public static void luanch(Activity activity) {
        Intent intent = new Intent(activity, WebActivity.class);
        activity.startActivity(intent);
    }
}
