package com.vrmediademo.vrmediademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by liuenbao on 5/9/16.
 */
public class LoadActivity extends Activity {

    private Button mButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mButton = (Button)this.findViewById(R.id.start_player_activity);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 新建一个Intent对象 */
                Intent intent = new Intent();
                intent.putExtra("name","LeiPei");
                /* 指定intent要启动的类 */
                intent.setClass(LoadActivity.this, MainActivity.class);
                 /* 启动一个新的Activity */
                LoadActivity.this.startActivity(intent);
            }
        });
    }
}
