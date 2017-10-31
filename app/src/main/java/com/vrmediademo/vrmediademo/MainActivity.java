package com.vrmediademo.vrmediademo;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.ut.vrmedia.VRPlayer;
import com.ut.vrmedia.camera.VROrthogonalCamera;
import com.ut.vrmedia.view.VRSurfaceView;
import com.ut.vrmedia.view.VRTextureView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TextureView
        .SurfaceTextureListener, VRPlayer.OnPreparedListener, View.OnClickListener, VRPlayer
        .OnCompletionListener {

    private static String TAG = MainActivity.class.getSimpleName();

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

    public void startTimer(final int millionSecond) {
        mMillionSecond = millionSecond;
        mHandler.postDelayed(runnable, mMillionSecond);
    }

    public void stopTimer() {
        mHandler.removeCallbacks(runnable);
    }

    private float mCurrentAngel = 0;
    private int mDirection = 0;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filepath = getIntent().getStringExtra("file");

        mTextureView = (VRTextureView) this.findViewById(R.id.videoSurfaceView);
        mTextureView.setSurfaceTextureListener(this);

        mVRMedia = new VRPlayer(this);

//        Map<String, String> params = new HashMap<>();
//        params.put(VRPlayer.CURVED_SURFACE_WIDTH, "1920.0");
//        params.put(VRPlayer.CURVED_SURFACE_EXPONENT, "0.0001");
//
//        params.put(VRPlayer.CURVED_SURFACE_HEIGHT, "780");
//        params.put(VRPlayer.CURVED_SURFACE_WIDTH_SEGS, "1");
//        params.put(VRPlayer.CURVED_SURFACE_HEIGHT_SEGS, "1");
//
//        mVRMedia.setVideoRenderMode(VRPlayer.VIDEO_RENDER_MODE_ORIGIN, params);
//
//        mVRMedia.setRenderTargetPosition(new float[]{0.f, 0.0f, -800.0f});

        startVideo = (VideoView) findViewById(R.id.vv);
        startVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                startVideo.start();
            }
        });
        startVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        startVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });


        mVRMedia.setOnPreparedListener(this);
        mVRMedia.setOnCompletionListener(this);

//        mChangeSpeedButton = (Button)this.findViewById(R.id.changeSpeedButton);
//        mChangeSpeedButton.setOnClickListener(this);
//
//        MediaCodec mediaCodec;
//
//        MediaFormat format = new MediaFormat();
//        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities
// .COLOR_FormatRawBayer8bit);

        mPlaybackRate = 1.0f;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause() before super");
        super.onPause();
        if (mVRMedia != null) {
            mVRMedia.pause();
        }
        Log.i(TAG, "onPause()  after super");
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop() before super");
        super.onStop();
        Log.i(TAG, "onStop() after super");
    }

    @Override
    protected void onDestroy() {
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

        //销毁当前播放窗口
        this.finish();
    }

//    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//
//        try {
////            mVRMedia.setDataSource("/mnt/sdcard/Movies/1.mp4", /*"bin/mesh/screen.objx",*/
// null);
////            mVRMedia.setDataSource("/mnt/sdcard/Movies/MonkeyRun.mov", null);
////            mVRMedia.setScreenAssetPath("bin/mesh/screen.objx", new float[]{0.f, 0.0f, -10
// .0f}, new float[]{0.f, 0.0f, 0.f});
//
////            mVRMedia.setVideoRenderMode(VRPlayer.VIDEO_RENDER_MODE_CURVED_SURFACE);
////            mVRMedia.setDataSource("/storage/emulated/0/test.mp4", null);
//
////            mVRMedia.setDataSource("/mnt/usb_storage/USB_DISK2/1/C0002.mp4", null);
//
//            mVRMedia.setDataSource("/mnt/sdcard/Movies/test.mp4", null);
//
//            mVRMedia.setSurface(mSurfaceView.getHolder().getSurface());
//
//            Map<String, String> params = new HashMap<>();
//            params.put(VRPlayer.CURVED_SURFACE_WIDTH, "2600.0");
//            params.put(VRPlayer.CURVED_SURFACE_EXPONENT, "0.0001");
//
//            params.put(VRPlayer.CURVED_SURFACE_HEIGHT, "1080");
//            params.put(VRPlayer.CURVED_SURFACE_WIDTH_SEGS, "1");
//            params.put(VRPlayer.CURVED_SURFACE_HEIGHT_SEGS, "1");
//
//            mVRMedia.setVideoRenderMode(VRPlayer.VIDEO_RENDER_MODE_ORIGIN, params);
//
//            mVRMedia.setRenderTargetPosition(new float[]{0.f, 0.0f, -800.0f});
//
////            SurfaceTexture surfaceTexture = new SurfaceTexture();
////            surfaceTexture.setOnFrameAvailableListener(this, null);
//
//            mVRMedia.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
    }

    public void onClick(View v) {
        mPlaybackRate += 1.0f;

        if (mPlaybackRate == 100.0f) {
            mPlaybackRate = 1.0f;
        }

//        mVRMedia.setPlaybackRate(mPlaybackRate);
        mVRMedia.pause();
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
        startTimer(1000);
    }

    public void onVRSurfaceViewFlingLeft(VRSurfaceView surfaceView) {
//        mCamera.rotate(-10.0f);
    }

    public void onVRSurfaceViewFlingRight(VRSurfaceView surfaceView) {
//        mCamera.rotate(10.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mTextureSurface = new Surface(surface);


        try {
//            mVRMedia.setDataSource("/mnt/sdcard/Movies/1.mp4", /*"bin/mesh/screen.objx",*/ null);
//            mVRMedia.setDataSource("/mnt/sdcard/Movies/MonkeyRun.mov", null);
//            mVRMedia.setScreenAssetPath("bin/mesh/screen.objx", new float[]{0.f, 0.0f, -10.0f},
// new float[]{0.f, 0.0f, 0.f});

//            mVRMedia.setVideoRenderMode(VRPlayer.VIDEO_RENDER_MODE_CURVED_SURFACE);
//            mVRMedia.setDataSource("/storage/emulated/0/test.mp4", null);

//            mVRMedia.setDataSource("/mnt/usb_storage/USB_DISK2/1/C0002.mp4", null);

            //准备资源
            mVRMedia.setDataSource(filepath, null);
            startVideo.setVideoURI(Uri.parse(filepath));

            mVRMedia.setSurface(mTextureSurface);

//            mVRMedia.setSurface(mSurfaceView.getHolder().getSurface());
//
//            Map<String, String> params = new HashMap<>();
//            params.put(VRPlayer.CURVED_SURFACE_WIDTH, "2600.0");
//            params.put(VRPlayer.CURVED_SURFACE_EXPONENT, "0.0001");
//
//            params.put(VRPlayer.CURVED_SURFACE_HEIGHT, "1080");
//            params.put(VRPlayer.CURVED_SURFACE_WIDTH_SEGS, "1");
//            params.put(VRPlayer.CURVED_SURFACE_HEIGHT_SEGS, "1");
//
//            mVRMedia.setVideoRenderMode(VRPlayer.VIDEO_RENDER_MODE_ORIGIN, params);
//
//            mVRMedia.setRenderTargetPosition(new float[]{0.f, 0.0f, -800.0f});
//
//            SurfaceTexture surfaceTexture = new SurfaceTexture();
//            surfaceTexture.setOnFrameAvailableListener(this, null);

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

    }

}
