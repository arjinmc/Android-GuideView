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
                .isShouldClickFocus(false)
//                .isRealClickFocus(true)
                .tipsView(createTextView())
//                .tipsView(R.layout.layout_linearlayout)
                .offsetX(0)
                .offsetY(20)
                .shape(GuideView.SHAPE_RECTANGLE)
                .radian(10)
                .roundRectOffset(30)
                .layoutGravity(Gravity.BOTTOM)
//                .onDismissListener(new GuideView.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        Log.e("setOnDismissListener","dismiss");
//                    }
//                })
//                .bgColor(Color.parseColor("#b0239823"))
                .create();

//        guideView1.setOnDismissListener(new GuideView.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Log.e("setOnDismissListener","dismiss");
//            }
//        });

        guideView1.show();


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
        textView.setText("tips view");
        return textView;
    }
}
