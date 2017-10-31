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
import java.io.FileInputStream;
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

        try {
            try {
                FileInputStream fis = new FileInputStream
                        ("/mnt/external_sd/km1930/resources/box/multiModel/800001/7卢塞恩C10分钟.MP4");
                fis.close();
                fis.close();
                fis.close();
                int i = 0 / 0;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onCreate: 内部catch ");
                throw e;
            } finally {
                Log.e(TAG, "onCreate: 内部finally ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: 外部 ");
        } finally {
            Log.e(TAG, "onCreate: 外部finally ");

        }


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

        final String file = list.get(position);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("播放样式：");
        String[] choice = new String[]{"对比", "刘", "原生"
                , "2视频播放", "3视频播放", "4视频播放"
                , "ExoPlayer"
        };

        builder.setSingleChoiceItems(choice, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        if (which == 3 || which == 4 || which == 5) {
                            intent = new Intent(SplashActivity.this, Main3Activity.class);
                            intent.putExtra("file1", list.get(position));
                            intent.putExtra("file2", list.get((position + 0)%list.size()));
                            intent.putExtra("file3", list.get((position + 0)%list.size()));
                            intent.putExtra("file4", list.get((position + 0)%list.size()));
                            intent.putExtra("type", which);
                        } else if (which==6){
                            intent = new Intent(SplashActivity.this, ExoPlayerActivity.class);
                            intent.putExtra("type", which);
                            intent.putExtra("file1", list.get(position));
                            intent.putExtra("file2", list.get((position + 0)%list.size()));
                        }else{
                            intent = new Intent(SplashActivity.this, Main2Activity.class);
                            intent.putExtra("type", which);
                            intent.putExtra("file", file);
                        }
                        startActivity(intent);
                        dialog.dismiss(); // 让窗口消失
                    }
                }

        );
        builder.create().show();
    }
}
