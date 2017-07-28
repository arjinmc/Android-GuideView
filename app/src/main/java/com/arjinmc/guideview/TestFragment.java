package com.arjinmc.guideview;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Eminem Lu on 27/7/17.
 * Email arjinmc@hotmail.com
 */

public class TestFragment extends Fragment {


    private TextView tv1, tv2, tvHello;
    private LinearLayout ll3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tvHello = (TextView) view.findViewById(R.id.tvHello);
        ll3 = (LinearLayout) view.findViewById(R.id.ll3);

        final GuideView guideView1 = new GuideView.Builder(getActivity())
//                .targetView(tv2)
                .targetView(R.id.ll3)
//                .isShouldClickFocus(false)
//                .isRealClickFocus(true)
                .tipsView(createTextView())
//                .tipsView(LayoutInflater.from(this).inflate(R.layout.layout_linearlayout,null))
//                .tipsView(R.layout.layout_linearlayout)
                .offsetX(0)
                .offsetY(20)
                .layoutGravity(Gravity.BOTTOM)
                .create();
        guideView1.show();


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "click tv1");
//                guideView1.dismiss();
                guideView1.show();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "click tv2");
//                guideView1.show();
                guideView1.dismiss();
            }
        });

    }


    private TextView createTextView() {
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.CYAN);
        textView.setTextSize(30f);
        textView.setText("tips view");
        return textView;
    }
}
