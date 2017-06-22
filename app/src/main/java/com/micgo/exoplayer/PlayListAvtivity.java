package com.micgo.exoplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.micgo.R;
import com.micgo.utils.KTVLog;

import java.io.IOException;

/**
 * Created by liuhongtian on 17/6/22.
 */

public class PlayListAvtivity extends AppCompatActivity {

    private static final String TAG = "PlayListAvtivity";

    private Uri firstVideoUri = Uri.parse("http://qiniuuwmp3.changba.com/913447182.mp4");
    private Uri secondVideoUri = Uri.parse("http://lzaiuw.changba.com/userdata/video/832436415.mp4");
//   private Uri thirdVideoUri = Uri.parse("");

    private SimpleExoPlayer player;

    private PLEventListener plEventListener = new PLEventListener();
    private PLStateListener plStateListener = new PLStateListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        setTitle("PlayList");

        preparePlayer();
    }

    private void preparePlayer() {
        MediaSource firstSource = new ExtractorMediaSource(firstVideoUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);
        MediaSource secondSource = new ExtractorMediaSource(secondVideoUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);
//        MediaSource thirdSource = new ExtractorMediaSource(thirdVideoUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(firstSource, secondSource);

        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(new Handler()), new DefaultLoadControl());
        player.addListener(plStateListener);

        SimpleExoPlayerView simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.simple_exo_player_view);

        simpleExoPlayerView.setPlayer(player);
        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, PlayListAvtivity.class);
        return intent;
    }

    private class PLEventListener implements ExtractorMediaSource.EventListener {

        @Override
        public void onLoadError(IOException error) {
            KTVLog.d(TAG, "PLEventListener onLoadError : " + error);
        }
    }

    private class PLStateListener implements ExoPlayer.EventListener {

        @Override
        public void onLoadingChanged(boolean isLoading) {
            KTVLog.d(TAG, "PLStateListener onLoadingChanged : " + isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            KTVLog.d(TAG, "PLStateListener onPlayerStateChanged | playWhenReady : " + playWhenReady + " | playbackState : " + playbackState);
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            KTVLog.d(TAG, "PLStateListener onTimelineChanged");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            KTVLog.d(TAG, "PLStateListener onPlayerError : " + error);
        }

        @Override
        public void onPositionDiscontinuity() {
            KTVLog.d(TAG, "PLStateListener onPositionDiscontinuity");
        }
    }

}
