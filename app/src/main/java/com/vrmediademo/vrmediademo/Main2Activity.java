package com.vrmediademo.vrmediademo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.vrmediademo.vrmediademo.fragment.FragmentFactory;

public class Main2Activity extends AppCompatActivity {

    private String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        filepath = getIntent().getStringExtra("file");


        Bundle bundle=new Bundle();
        bundle.putString("file",filepath);
        Fragment compareFragment=new FragmentFactory().creatFragment(getIntent().getIntExtra("type",0));
        compareFragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment, compareFragment);
        transaction.commit();


    }
}
