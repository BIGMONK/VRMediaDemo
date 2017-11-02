package com.vrmediademo.vrmediademo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.orhanobut.logger.Logger;
import com.vrmediademo.vrmediademo.R;

/**
 * Created by djf on 2017/10/31.
 */

public class ExoPlayerFragment extends Fragment implements View.OnClickListener, ExoPlayer
        .EventListener, SimpleExoPlayer.VideoListener, VideoRendererEventListener
         {
    private static final String TAG = "ExoPlayerFragment";
    private View mRootView;
    private SimpleExoPlayerView player_view;
    private TextView debug_text_view,filename_textview;
    private LinearLayout controls_root;
    private Button retry_button, increase_button, decrease_button, start_button;
    private TextView speed_textview;
    private String filepath;
    private Context mContext;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private boolean inErrorState;
    private PlaybackParameters parameters;
    private DebugTextViewHelper debugViewHelper;
    private int resumeWindow;
    private long resumePosition;
    private boolean shouldAutoPlay;
    float speed = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        filepath = "file://" + getArguments().getString("file");
        speed = getArguments().getFloat("speed");
        Logger.t(TAG).d("视频资源：" + filepath + " speed=" + speed);
        clearResumePosition();
        shouldAutoPlay = true;
    }

    private void initializePlayer() {
        boolean needNewPlayer = player == null;
        if (needNewPlayer) {
            // 1. Create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
//            player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
//            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mContext)
//                    , trackSelector);
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mContext)
                    , trackSelector,new DefaultLoadControl(new DefaultAllocator(true,4*C.DEFAULT_BUFFER_SEGMENT_SIZE)));

            player.addListener(this);
            player.setVideoListener(this);
            player.setVideoDebugListener(this);
            parameters = new PlaybackParameters(speed, 1);
            player.setPlaybackParameters(parameters);
            player_view.setPlayer(player);
            player_view.setControllerShowTimeoutMs(0);//设置控制栏超时时间
            debugViewHelper = new DebugTextViewHelper(player, debug_text_view);
            debugViewHelper.start();
        }
        // Measures bandwidth during playback. Can be null if not required.
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
//                Util.getUserAgent(mContext, "yourApplicationName"), bandwidthMeter);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "yourApplicationName"), null);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(filepath),
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            player.seekTo(resumeWindow, resumePosition);
        }
        player.setPlayWhenReady(shouldAutoPlay);
        player.prepare(videoSource);
        inErrorState = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_exoplayer, container, false);
        initView(mRootView);

        speed_textview.setText(speed + "");

        return mRootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.t(TAG).d("onDestroy");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        } else {
            showToast("无权限");
        }
    }


    @Override
    public void onClick(View v) {
        if (v == retry_button) {
            initializePlayer();
        } else
            switch (v.getId()) {
                case R.id.decrease_button:
                    if (speed > 0) {
                        speed -= 0.1f;
                        speed_textview.setText(speed + "");
                        parameters = new PlaybackParameters(speed, 1);
                        player.setPlaybackParameters(parameters);
                    } else {
                        showToast("已是最小速度");
                    }
                    break;
                case R.id.increase_button:
                    if (speed < 3) {
                        speed += 0.1f;
                        speed_textview.setText(speed + "");
                        parameters = new PlaybackParameters(speed, 1);
                        player.setPlaybackParameters(parameters);
                    } else {
                        showToast("已是最大速度");
                    }
                    break;
                case R.id.start_button:
                    player.setPlayWhenReady(!player.getPlayWhenReady());
                    break;
            }
    }


    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initView(View mRootView) {
        player_view = (SimpleExoPlayerView) mRootView.findViewById(R.id.player_view);
        filename_textview = (TextView) mRootView.findViewById(R.id.filename_textview);
        filename_textview.setText(filepath);
        debug_text_view = (TextView) mRootView.findViewById(R.id.debug_text_view);
        debug_text_view.setOnClickListener(this);
        retry_button = (Button) mRootView.findViewById(R.id.retry_button);
        retry_button.setOnClickListener(this);
        controls_root = (LinearLayout) mRootView.findViewById(R.id.controls_root);
        controls_root.setOnClickListener(this);
        increase_button = (Button) mRootView.findViewById(R.id.increase_button);
        increase_button.setOnClickListener(this);
        speed_textview = (TextView) mRootView.findViewById(R.id.speed_textview);
        speed_textview.setOnClickListener(this);
        decrease_button = (Button) mRootView.findViewById(R.id.decrease_button);
        decrease_button.setOnClickListener(this);
        start_button = (Button) mRootView.findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
    }

    /*    播放器监听     */
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // Do nothing.
        Logger.t(TAG).d("onTimelineChanged timeline.getWindowCount=" + timeline.getWindowCount()
                + "  timeline.getPeriodCount=" + timeline.getPeriodCount()
                + ((manifest == null) ? "manifest=null" : "  manifest=" + manifest.toString())
                + " player  getDuration=" + player.getDuration()
                + " getAudioSessionId=" + player.getAudioSessionId()
                + ((player.getAudioFormat() == null) ? "getAudioFormat=null"
                : " getAudioFormat=" + player.getAudioFormat().toString())
                + " getCurrentWindowIndex=" + player.getCurrentWindowIndex()
                + " getCurrentPeriodIndex=" + player.getCurrentPeriodIndex()
        );
    }

    TrackGroupArray lastSeenTrackGroupArray = null;

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        if (trackGroups != lastSeenTrackGroupArray) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector
                    .getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                        == MappingTrackSelector.MappedTrackInfo
                        .RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    Toast.makeText(mContext, "不支持的视频格式", Toast.LENGTH_LONG);
                }
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                        == MappingTrackSelector.MappedTrackInfo
                        .RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    Toast.makeText(mContext, "不支持的音频格式", Toast.LENGTH_LONG);
                }
            }
            lastSeenTrackGroupArray = trackGroups;
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Logger.t(TAG).d("onLoadingChanged isLoading=" + isLoading);
    }

    /**
     * 播放状态监听
     *
     * @param playWhenReady 是否在播放
     * @param playbackState 播放器状态
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String state = "";
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                state = "STATE_BUFFERING";
                break;
            case ExoPlayer.STATE_ENDED:
                state = "STATE_ENDED";
                break;
            case ExoPlayer.STATE_IDLE:
                state = "STATE_IDLE";
                break;
            case ExoPlayer.STATE_READY:
                state = "STATE_READY";
                start_button.setText(playWhenReady ? "暂停" : "播放");
                Logger.t(TAG).d("视频格式：" + player.getVideoFormat().toString());

                break;
        }
        Logger.t(TAG).d("onPlayerStateChanged playWhenReady=" + playWhenReady +
                "  playbackState=" + state + "   ");
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        Logger.t(TAG).d("onPlayerError ExoPlaybackException=" + e.toString());
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil
                            .DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            Toast.makeText(mContext, errorString, Toast.LENGTH_LONG);
        }
        inErrorState = true;


        if (isBehindLiveWindow(e)) {
            clearResumePosition();
            initializePlayer();
        } else {
            updateResumePosition();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        Logger.t(TAG).d("onPositionDiscontinuity inErrorState=" + inErrorState);
        if (inErrorState) {
            // This will only occur if the user has performed a seek whilst in the error state.
            // Update the
            // resume position so that if the user then retries, playback will resume from the
            // position to
            // which they seeked.
            updateResumePosition();
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Logger.t(TAG).d("onPlaybackParametersChanged playbackParameters=" + playbackParameters
                .toString());
    }
    /*    播放器监听     */


    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = Math.max(0, player.getCurrentPosition());
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private void releasePlayer() {
        if (player != null) {
            debugViewHelper.stop();
            debugViewHelper = null;
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
        }
    }


    /* setVideoListener*/

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float
            pixelWidthHeightRatio) {
        Logger.t(TAG).d("setVideoListener onVideoSizeChanged =" + width + " " + height + " " +
                unappliedRotationDegrees + " " + pixelWidthHeightRatio);
    }

    @Override
    public void onRenderedFirstFrame() {
        Logger.t(TAG).d("setVideoListener onRenderedFirstFrame");
    }

    /* setVideoDebugListener*/
    @Override
    public void onVideoEnabled(DecoderCounters counters) {
        Logger.t(TAG).d("setVideoDebugListener onVideoEnabled");
    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long
            initializationDurationMs) {
        Logger.t(TAG).d("setVideoDebugListener  onVideoDecoderInitialized decoderName:"
                +decoderName
        +" initializedTimestampMs："+initializedTimestampMs
        +" initializationDurationMs："+initializationDurationMs        );

        showToast(decoderName);

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {
        Logger.t(TAG).d("setVideoDebugListener onVideoInputFormatChanged format:"+format.toString());

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
        Logger.t(TAG).d("setVideoDebugListener onDroppedFrames count:"+count+"  elapsedMs:"+elapsedMs);

    }


    @Override
    public void onRenderedFirstFrame(Surface surface) {
        Logger.t(TAG).d("setVideoDebugListener onRenderedFirstFrame");

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        Logger.t(TAG).d("setVideoDebugListener onVideoDisabled");
    }

}
