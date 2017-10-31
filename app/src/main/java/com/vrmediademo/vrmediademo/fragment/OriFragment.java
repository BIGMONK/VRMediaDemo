package com.vrmediademo.vrmediademo.fragment;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.vrmediademo.vrmediademo.R;

/**
 * Created by djf on 2017/5/4.
 */

public class OriFragment extends Fragment implements View.OnClickListener {

    private View mRootView;
    private String filepath;
    private VideoView startVideo;
    private static final String TAG = "OriFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_ori, container, false);
        filepath = getArguments().getString("file");
        startVideo = (VideoView) mRootView.findViewById(R.id.vv);
        startVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "onPrepared: ");
                startVideo.start();
            }
        });
        startVideo.setVideoURI(Uri.parse(filepath));
        startVideo.setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        startVideo.start();
    }
}
