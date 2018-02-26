package kyf.loveapplication.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kyf.loveapplication.R;

/**
 * 物联
 * Created by a55 on 2017/11/2.
 */

public class ProductRelationFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_frame_layout, container, false);

        getChildFragmentManager().beginTransaction().add(R.id.view_container, WebFragment.initFragment("http://47.104.84.211:8082/index.htm/")).commit();

        return view;
    }
}
