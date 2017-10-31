package com.vrmediademo.vrmediademo.fragment;

import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.ut.vrmedia.VRPlayer;
import com.ut.vrmedia.camera.VROrthogonalCamera;
import com.ut.vrmedia.view.VRSurfaceView;
import com.ut.vrmedia.view.VRTextureView;
import com.vrmediademo.vrmediademo.R;

import java.io.IOException;

/**
 * Created by djf on 2017/5/4.
 */

public class CompareFragment extends Fragment implements TextureView.SurfaceTextureListener,
        VRPlayer.OnPreparedListener, VRPlayer.OnCompletionListener {


    private static String TAG = CompareFragment.class.getSimpleName();

    //    private VRPerspectiveCamera mCamera;
    private VROrthogonalCamera mCamera;
    private VRPlayer mVRMedia;
    private VRTextureView mTextureView;
    private Surface mTextureSurface;
    private Button mChangeSpeedButton;
    private float mPlaybackRate;
    String filepath;
    private Handler mHandler = new Handler();
    private int mMillionSecond = 0;

    private Runnable runnable = new Runnable() {
        public void run() {
            if (mDirection == 0) {
                mCurrentAngel += 0.5f;
                if (mCurrentAngel >= 5.0f) {
                    mCurrentAngel = 5.0f;
                    mDirection = 1;
                }
            } else {
                mCurrentAngel -= 0.5f;
                if (mCurrentAngel <= -5.0f) {
                    mCurrentAngel = -5.0f;
                    mDirection = 0;
                }
            }
//            mCamera.yaw(mCurrentAngel);

            mHandler.postDelayed(this, mMillionSecond);     //postDelayed(this,1000)
            // 方法安排一个Runnable对象到主线程队列中
        }
    };
    private VideoView startVideo;
    private View mRootView;
    private TextView time1;
    private TextView time2;

    public void startTimer(final int millionSecond) {
        mMillionSecond = millionSecond;
        mHandler.postDelayed(runnable, mMillionSecond);
    }

    public void stopTimer() {
        mHandler.removeCallbacks(runnable);
    }

    private float mCurrentAngel = 0;
    private int mDirection = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        mRootView = inflater.inflate(R.layout.fragment_compare, container, false);
        mTextureView = (VRTextureView) mRootView.findViewById(R.id.videoSurfaceView);
        TextView mVideoViewTextView = (TextView) mRootView.findViewById(R.id.VideoView);
        TextView mVRPlayerTextView = (TextView) mRootView.findViewById(R.id.VRPlayer);
        final TextView rate = (TextView) mRootView.findViewById(R.id.rate);
        time1 = (TextView) mRootView.findViewById(R.id.time1);
        time2 = (TextView) mRootView.findViewById(R.id.time2);
        TextView up = (TextView) mRootView.findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVRMedia != null && mVRMedia.isPlaying()) {
                    mPlaybackRate += 1;
                    mVRMedia.setPlaybackRate(mPlaybackRate);
                    mVRMedia.start();
                    rate.setText("" + mPlaybackRate);
                }
            }
        });
        TextView down = (TextView) mRootView.findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVRMedia != null && mVRMedia.isPlaying()) {
                    mPlaybackRate -= 1;
                    mVRMedia.setPlaybackRate(mPlaybackRate);
                    mVRMedia.start();
                    rate.setText("" + mPlaybackRate);
                }
            }
        });
        TextView start = (TextView) mRootView.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVRMedia != null && !mVRMedia.isPlaying()) {
                    mPlaybackRate = 1;
                    mVRMedia.setPlaybackRate(mPlaybackRate);
                    rate.setText("" + mPlaybackRate);
                    mVRMedia.start();
                }
            }
        });
        TextView pause = (TextView) mRootView.findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVRMedia != null && mVRMedia.isPlaying()) {
                    mVRMedia.pause();
                }
            }
        });

        mTextureView.setSurfaceTextureListener(this);
        filepath = getArguments().getString("file");

        mVRMedia = new VRPlayer(getActivity());
        mVRMedia.setOnPreparedListener(this);
        mVRMedia.setOnCompletionListener(this);
        mPlaybackRate = 1.0f;

        startVideo = (VideoView) mRootView.findViewById(R.id.vv);
        startVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                startVideo.start();
            }
        });
        startVideo.setVideoURI(Uri.parse(filepath));

        TextView pause2 = (TextView) mRootView.findViewById(R.id.pause2);
        pause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startVideo != null && startVideo.isPlaying())
                    startVideo.pause();
            }
        });
        TextView start2 = (TextView) mRootView.findViewById(R.id.start2);
        start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startVideo != null && !startVideo.isPlaying())
                    startVideo.start();
            }
        });


        return mRootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");

    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause() before super");
        super.onPause();
        if (mVRMedia != null) {
            mVRMedia.pause();
        }
        Log.i(TAG, "onPause()  after super");
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop() before super");
        super.onStop();
        Log.i(TAG, "onStop() after super");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy before super");

        if (mVRMedia != null) {
            mVRMedia.stop();
            mVRMedia.release();
            mVRMedia = null;
        }
        super.onDestroy();
        Log.i(TAG, "onDestroy after super");

    }

    public void onCompletion(VRPlayer paramMediaPlayer) {
        Log.d(TAG, "onCompletion");
//        //退出播放
//        stopPlayer();
        if (mVRMedia != null) {
            mVRMedia.stop();
            mVRMedia.release();
            mVRMedia = null;
        }

    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
    }

    public void onPrepared(VRPlayer paramMediaPlayer) {
        Log.d(TAG, "onPrepared");

//        mCamera = new VRPerspectiveCamera(mSurfaceView.getWidth(), mSurfaceView.getHeight());
//        mCamera.init(45.0f, (float)mSurfaceView.getWidth() / (float)mSurfaceView.getHeight(), 0
// .5f, 1500.0f);

//        mCamera = new VROrthogonalCamera(-mSurfaceView.getWidth() / 2, mSurfaceView.getWidth()
// / 2, - mSurfaceView.getHeight() / 2, mSurfaceView.getHeight() / 2, 0.5f, 1500f);
//
//        mVRMedia.setCamera(mCamera);

//        mVRMedia.setSurface(mSurfaceView.getHolder().getSurface());
        mVRMedia.start();
//        启动旋转定时器
//        startTimer(1000);
    }

    public void onVRSurfaceViewFlingLeft(VRSurfaceView surfaceView) {
//        mCamera.rotate(-10.0f);
    }

    public void onVRSurfaceViewFlingRight(VRSurfaceView surfaceView) {
//        mCamera.rotate(10.0f);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mTextureSurface = new Surface(surface);


        try {
            mVRMedia.setDataSource(filepath, null);
            mVRMedia.setSurface(mTextureSurface);
            mVRMedia.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        time1.setText(mVRMedia.getCurrentPosition()+"");
        time2.setText(startVideo.getCurrentPosition()+"");
    }

}
