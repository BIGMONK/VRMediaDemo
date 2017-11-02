package com.vrmediademo.vrmediademo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vrmediademo.vrmediademo.fragment.LiuFragment;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "Main3Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (getIntent().getIntExtra("type", 0)) {
            case 5://四个视频
                String filepath4 = getIntent().getStringExtra("file4");
                Bundle bundle4 = new Bundle();
                bundle4.putString("file", filepath4);
                bundle4.putFloat("speed", 2);
                Fragment f4 = new LiuFragment();
                f4.setArguments(bundle4);
                transaction.replace(R.id.fragment4, f4);
            case 4://三个视频
                String filepath3 = getIntent().getStringExtra("file3");
                Bundle bundle3 = new Bundle();
                bundle3.putString("file", filepath3);
                bundle3.putFloat("speed", 1.5f);
                Fragment f3 = new LiuFragment();
                f3.setArguments(bundle3);
                transaction.replace(R.id.fragment3, f3);
            case 3://两个视频
                String filepath1 = getIntent().getStringExtra("file1");
                Bundle bundle1 = new Bundle();
                bundle1.putString("file", filepath1);
                bundle1.putFloat("speed", 0.5f);
                String filepath2 = getIntent().getStringExtra("file2");
                Bundle bundle2 = new Bundle();
                bundle2.putString("file", filepath2);
                bundle2.putFloat("speed", 1);
                Fragment f1 = new LiuFragment();
                Fragment f2 = new LiuFragment();
                f1.setArguments(bundle1);
                f2.setArguments(bundle2);
                transaction.replace(R.id.fragment1, f1);
                transaction.replace(R.id.fragment2, f2);
                break;
        }
        transaction.commit();
    }
}
