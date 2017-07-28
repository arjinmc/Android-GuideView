package com.arjinmc.guideview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Eminem Lu on 27/7/17.
 * Email arjinmc@hotmail.com
 */

public class FragmentTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);

        getFragmentManager().beginTransaction().add(R.id.ll_content,new TestFragment()).commit();
    }
}
