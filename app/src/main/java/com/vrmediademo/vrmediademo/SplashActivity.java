package com.vrmediademo.vrmediademo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vrmediademo.vrmediademo.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "SplashActivity";
    private ArrayList<String> list;
    private ListViewAdapter adapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

//        int i = 1 / 0;

        ListView listView = (ListView) findViewById(R.id.listview);
        list = new ArrayList<>();
        adapter = new ListViewAdapter(list, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        mProgressDialog = new ProgressDialog(SplashActivity.this);
        mProgressDialog.setMessage("视频查找中……");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        Log.d(TAG, "onCreate: 遍历文件夹开始" + System.currentTimeMillis());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> diskPath = FileUtils.getDiskPath();
                for (String s : diskPath) {
                    File file = new File(s);
                    if (file.exists()) {
                        list.addAll(FileUtils.getFilePath(file));
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        Toast.makeText(SplashActivity.this, "发现视频：" + list.size(), Toast
                                .LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "onCreate: 遍历文件夹结束" + System.currentTimeMillis());

                    }
                });
            }
        }).start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("播放样式：");
        String[] choice = new String[]{"VRMedia VS VideoView", "VRMedia", "VideoView"
                , "2 VRMedia", "3 VRMedia", "4 VRMedia"
                , "ExoPlayer", "ExoPlayerAA", "ExoPlayerAB","ExoPlayerAAAA", "ExoPlayerABCD"
        };

        builder.setSingleChoiceItems(choice, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        if (which == 3 || which == 4 || which == 5) {
                            intent = new Intent(SplashActivity.this, Main3Activity.class);
                            intent.putExtra("file1", list.get(position));
                            intent.putExtra("file2", list.get((position + 0) % list.size()));
                            intent.putExtra("file3", list.get((position + 0) % list.size()));
                            intent.putExtra("file4", list.get((position + 0) % list.size()));
                            intent.putExtra("type", which);
                        } else if (which == 6 || which == 7 || which == 8||which==9||which==10) {
                            intent = new Intent(SplashActivity.this, ExoPlayerActivity.class);
                            intent.putExtra("type", which);
                            intent.putExtra("file1", list.get(position));
                            int off = 0;
                            if (which == 8||which==10) {
                                off = 1;
                            }
                            intent.putExtra("file2", list.get((position + off) % list.size()));
                            intent.putExtra("file3", list.get((position + off+off) % list.size()));
                            intent.putExtra("file4", list.get((position + off+ off+ off) % list.size()));
                        } else {
                            intent = new Intent(SplashActivity.this, Main2Activity.class);
                            intent.putExtra("type", which);
                            intent.putExtra("file", list.get(position));
                        }
                        startActivity(intent);
                        dialog.dismiss(); // 让窗口消失
                    }
                }

        );
        builder.create().show();
    }
}
