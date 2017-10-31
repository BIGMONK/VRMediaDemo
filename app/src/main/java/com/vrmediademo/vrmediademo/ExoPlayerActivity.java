package com.vrmediademo.vrmediademo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.vrmediademo.vrmediademo.fragment.FragmentFactory;

public class ExoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exo_player);

        String filepath1 = getIntent().getStringExtra("file1");
        String filepath2 = getIntent().getStringExtra("file2");

        Fragment compareFragment1 = new FragmentFactory().creatFragment(getIntent().getIntExtra
                ("type", 0));

        Fragment compareFragment2 = new FragmentFactory().creatFragment(getIntent().getIntExtra
                ("type", 0));

        Bundle bundle = new Bundle();
        bundle.putString("file", filepath1);
        bundle.putFloat("speed", 1);
        compareFragment1.setArguments(bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putString("file", filepath2);
        bundle2.putFloat("speed", 2);
        compareFragment2.setArguments(bundle2);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.replace(R.id.fragment1, compareFragment1);
        transaction.replace(R.id.fragment2, compareFragment2);
        transaction.commit();

    }
}
