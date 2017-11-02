package com.vrmediademo.vrmediademo.fragment;

import android.app.Fragment;
import android.graphics.SurfaceTexture;
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

import com.orhanobut.logger.Logger;
import com.ut.vrmedia.VRPlayer;
import com.ut.vrmedia.camera.VROrthogonalCamera;
import com.ut.vrmedia.view.VRSurfaceView;
import com.ut.vrmedia.view.VRTextureView;
import com.vrmediademo.vrmediademo.R;

import java.io.IOException;

/**
 * Created by djf on 2017/5/4.
 */

public class LiuFragment extends Fragment implements TextureView.SurfaceTextureListener,
        VRPlayer.OnPreparedListener, VRPlayer.OnCompletionListener {
    private static String TAG = LiuFragment.class.getSimpleName();
    //    private VRPerspectiveCamera mCamera;
    private VROrthogonalCamera mCamera;
    private VRPlayer mVRMedia;
    private VRTextureView mTextureView;
    private Surface mTextureSurface;
    private Button mChangeSpeedButton;
    private float mPlaybackRate;
    private TextView positionTextview,speedTextview;
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
    private View mRootView;

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
        mRootView = inflater.inflate(R.layout.fragment_liu, container, false);
        mTextureView = (VRTextureView) mRootView.findViewById(R.id.videoSurfaceView);
        positionTextview = (TextView) mRootView.findViewById(R.id.position_textview);
        speedTextview = (TextView) mRootView.findViewById(R.id.speed_textview);
        mTextureView.setSurfaceTextureListener(this);
        filepath = getArguments().getString("file");
        mVRMedia = new VRPlayer(getActivity());
        mVRMedia.setOnPreparedListener(this);
        mVRMedia.setOnCompletionListener(this);
        mPlaybackRate = getArguments().getFloat("speed",1);
        speedTextview.setText("播放文件："+filepath+"  播放速度："+mPlaybackRate);
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
        mVRMedia.setPlaybackRate(mPlaybackRate);
        mVRMedia.start();
//        启动旋转定时器
        startTimer(1000);
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
        Logger.t(TAG).d("onSurfaceTextureUpdated");
        if (positionTextview != null && mVRMedia != null) {
            positionTextview.setText("播放进度：" + mVRMedia.getCurrentPosition());
        }
    }

}
