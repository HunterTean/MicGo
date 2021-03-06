package com.micgo.exoplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.micgo.exoplayer.studio.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.micgo.R;
import com.micgo.utils.KTVLog;
import com.micgo.utils.KTVUtility;

import java.io.File;
import java.io.IOException;

/**
 * Created by liuhongtian on 17/6/22.
 */

public class PlayListAvtivity extends AppCompatActivity {

    private static final String TAG = "PlayListAvtivity";

    private Uri firstUri = Uri.parse("http://qiniuuwmp3.changba.com/913447182.mp4");
    private Uri mp3Uri = Uri.parse("http://qiniuuwmp3.changba.com/913673283.mp3");
    private Uri secondUri = Uri.parse("http://lzaiuw.changba.com/userdata/video/832436415.mp4");
    private Uri thirdUri = Uri.parse("http://qiniuuwmp3.changba.com/913447182.mp4");

    private Uri mp4NoUri = Uri.parse("http://ktv200.vps.changbaops.com/links/no.mp4");
    private Uri mp4Uri = Uri.parse("http://jinshanuwmp3.changba.com/946326764.mp4");

    private Uri aacUri = Uri.parse("http://qiniuuwmp3.changba.com/948304870.mp3"); // aac

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
        MediaSource firstSource = new ExtractorMediaSource(firstUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);
        MediaSource mp3Source = new ExtractorMediaSource(mp3Uri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);
        MediaSource thirdSource = new ExtractorMediaSource(thirdUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);

        MediaSource mp4Source = new ExtractorMediaSource(mp4Uri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);
        MediaSource aacSource = new ExtractorMediaSource(aacUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);

        MediaSource mp4NoSource = new ExtractorMediaSource(mp4NoUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);
        Uri mp4LocalNoUri = Uri.fromFile(KTVUtility.getMGFile("no_online.mp4"));

        MediaSource mp4LocalNoSource = new ExtractorMediaSource(mp4LocalNoUri, new DefaultDataSourceFactory(this, "changab"), new DefaultExtractorsFactory(), new Handler(), plEventListener);

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(firstSource, thirdSource);

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
