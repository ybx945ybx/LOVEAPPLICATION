package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kyf.loveapplication.R;

public class AddPhoneFriendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_friend);
    }

    public static void toThisAvtivity(Activity activity) {
        Intent intent = new Intent(activity, AddPhoneFriendActivity.class);
        activity.startActivity(intent);
    }
}
