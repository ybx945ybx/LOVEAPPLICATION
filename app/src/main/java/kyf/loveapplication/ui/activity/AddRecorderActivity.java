package kyf.loveapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kyf.loveapplication.R;

public class AddRecorderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recorder);

        findViewById(R.id.llyt_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHomePageActivity.toThisAvtivity(mActivity, "447");
            }
        });
    }

    public static void toThisAvtivity(Activity activity) {
        Intent intent = new Intent(activity, AddRecorderActivity.class);
        activity.startActivity(intent);
    }
}
