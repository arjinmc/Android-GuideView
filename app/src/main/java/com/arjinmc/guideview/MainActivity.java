package com.arjinmc.guideview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv1, tv2, tvHello;
    private LinearLayout ll3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tvHello = (TextView) findViewById(R.id.tvHello);
        ll3 = (LinearLayout) findViewById(R.id.ll3);

        final GuideView guideView1 = new GuideView.Builder(this)
                .targetView(tv2)
//                .targetView(R.id.ll3)
//                .isShouldClickFocus(false)
//                .isRealClickFocus(true)
//                .tipsView(createTextView())
                .tipsView(R.layout.layout_linearlayout)
                .offsetX(0)
                .offsetY(0)
                .shape(GuideView.SHAPE_RECTANGLE)
//                .shape(GuideView.SHAPE_OVAL)
                .radian(10)
                .roundRectOffset(10)
//                .layoutGravity(Gravity.TOP)
                .layoutGravity(Gravity.BOTTOM)
//                .layoutGravity(Gravity.RIGHT)
//                .layoutGravity(Gravity.LEFT)
//                .onDismissListener(new GuideView.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        Log.e("setOnDismissListener","dismiss");
//                    }
//                })
//                .bgColor(Color.parseColor("#b0239823"))
                .blurRadius(5)
                .create();

//        guideView1.setOnDismissListener(new GuideView.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Log.e("setOnDismissListener","dismiss");
//            }
//        });

//        guideView1.show();


        //use GuideViewQueue
//        GuideViewQueue guideViewQueue = GuideViewQueue.getInstance();
//        guideViewQueue.add(new GuideView.Builder(this).targetView(tv1).tipsView(createTextView())
//                .shape(GuideView.SHAPE_OVAL).create());
//        guideViewQueue.add(new GuideView.Builder(this).targetView(tv2).tipsView(R.layout.layout_linearlayout)
//                .layoutGravity(Gravity.BOTTOM).create());
//        guideViewQueue.add(new GuideView.Builder(this).targetView(ll3).tipsView(createTextView())
//                .shape(GuideView.SHAPE_RECTANGLE).radian(10).roundRectOffset(10).create());
//        guideViewQueue.setOnFinallyDismissListener(new GuideViewQueue.OnFinallyDismissListener() {
//            @Override
//            public void onDismiss() {
//                Log.e("GuideViewQueue", "finally dimiss");
//            }
//        });
//        guideViewQueue.start();


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "click tv1");
                guideView1.show();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "click tv2");
                guideView1.dismiss();
            }
        });

    }


    private TextView createTextView() {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.CYAN);
        textView.setTextSize(30f);
        textView.setBackgroundColor(Color.RED);
        textView.setText("tips view");
        return textView;
    }
}
