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
        int which = getIntent().getIntExtra("type", 0);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (which == 6) {
            setContentView(R.layout.activity_exo_player);
        } else if (which == 7 || which == 8) {
            setContentView(R.layout.activity_exo_player2);
        } else {
            setContentView(R.layout.activity_exo_player4);
        }
        switch (which) {
            case 9://AAAA
                String filepathA3 = getIntent().getStringExtra("file1");
                Fragment compareFragmentA3 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra                                ("type", 0));
                Bundle bundleA3 = new Bundle();
                bundleA3.putString("file", filepathA3);
                bundleA3.putFloat("speed", 0.5f);
                compareFragmentA3.setArguments(bundleA3);
                transaction.replace(R.id.fragment3, compareFragmentA3);

                String filepathA4 = getIntent().getStringExtra("file1");
                Fragment compareFragmentA4 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra
                                ("type", 0));
                Bundle bundleA4 = new Bundle();
                bundleA4.putString("file", filepathA4);
                bundleA4.putFloat("speed", 2);
                compareFragmentA4.setArguments(bundleA4);
                transaction.replace(R.id.fragment4, compareFragmentA4);
            case 7://AA
                String filepathA2 = getIntent().getStringExtra("file1");
                Fragment compareFragmentA2 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra
                                ("type", 0));
                Bundle bundleA1 = new Bundle();
                bundleA1.putString("file", filepathA2);
                bundleA1.putFloat("speed", 1.5f);
                compareFragmentA2.setArguments(bundleA1);
                transaction.replace(R.id.fragment2, compareFragmentA2);
            case 6://A
                String filepath1 = getIntent().getStringExtra("file1");
                Fragment compareFragment1 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra
                                ("type", 0));
                Bundle bundle = new Bundle();
                bundle.putString("file", filepath1);
                bundle.putFloat("speed", 1);
                compareFragment1.setArguments(bundle);
                transaction.replace(R.id.fragment1, compareFragment1);
                break;
        }

        switch (which) {
            case 10://ABCD
                String filepath4 = getIntent().getStringExtra("file4");
                Fragment compareFragment4 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra
                                ("type", 0));
                Bundle bundle4 = new Bundle();
                bundle4.putString("file", filepath4);
                bundle4.putFloat("speed", 2);
                compareFragment4.setArguments(bundle4);
                transaction.replace(R.id.fragment4, compareFragment4);

                String filepath3 = getIntent().getStringExtra("file3");
                Fragment compareFragment3 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra
                                ("type", 0));
                Bundle bundle3 = new Bundle();
                bundle3.putString("file", filepath3);
                bundle3.putFloat("speed", 0.5f);
                compareFragment3.setArguments(bundle3);
                transaction.replace(R.id.fragment3, compareFragment3);

            case 8://AB
                String filepath2 = getIntent().getStringExtra("file2");
                Fragment compareFragment2 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra
                                ("type", 0));
                Bundle bundle2 = new Bundle();
                bundle2.putString("file", filepath2);
                bundle2.putFloat("speed", 1);
                compareFragment2.setArguments(bundle2);
                transaction.replace(R.id.fragment2, compareFragment2);

                String filepath1 = getIntent().getStringExtra("file1");
                Fragment compareFragment1 = new FragmentFactory().creatFragment(getIntent()
                        .getIntExtra
                                ("type", 0));
                Bundle bundle = new Bundle();
                bundle.putString("file", filepath1);
                bundle.putFloat("speed", 1.5f);
                compareFragment1.setArguments(bundle);
                transaction.replace(R.id.fragment1, compareFragment1);
                break;
        }


        transaction.commit();

    }
}
