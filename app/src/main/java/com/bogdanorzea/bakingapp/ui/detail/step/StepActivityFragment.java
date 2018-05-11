package com.bogdanorzea.bakingapp.ui.detail.step;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bogdanorzea.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepActivityFragment extends Fragment {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_ID = "step_id";

    private static final String PLAYER_PLAY_WHEN_READY = "player_playback_state";
    private static final String PLAYER_CURRENT_POSITION = "player_current_position";
    private static final String PLAYER_CURRENT_WINDOW_INDEX = "player_current_window_index";
    private ExoPlayer exoPlayer;
    private long playbackPosition = 0;
    private boolean playWhenReady = true;
    private PlayerView playerView;
    private int currentWindowIndex;

    public StepActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        playerView = view.findViewById(R.id.player);

        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY);
            playbackPosition = savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
            currentWindowIndex = savedInstanceState.getInt(PLAYER_CURRENT_WINDOW_INDEX);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            int recipeId = bundle.getInt(RECIPE_ID, -1);
            int stepId = bundle.getInt(STEP_ID, -1);

            Toast.makeText(getContext(), recipeId + " & " + stepId, Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void initializePlayer() {
        Uri mp4VideoUri = Uri.parse("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
        Context context = getContext();

        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            playerView.setPlayer(exoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "BakingApp"),
                    new DefaultBandwidthMeter());

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mp4VideoUri);

            exoPlayer.prepare(videoSource);
        }

        exoPlayer.seekTo(currentWindowIndex, playbackPosition);
        exoPlayer.setPlayWhenReady(playWhenReady);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if (exoPlayer == null) return;

        playWhenReady = exoPlayer.getPlayWhenReady();
        playbackPosition = exoPlayer.getCurrentPosition();
        currentWindowIndex = exoPlayer.getCurrentWindowIndex();

        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAYER_PLAY_WHEN_READY, exoPlayer.getPlayWhenReady());
        outState.putLong(PLAYER_CURRENT_POSITION, exoPlayer.getCurrentPosition());
        outState.putInt(PLAYER_CURRENT_WINDOW_INDEX, exoPlayer.getCurrentWindowIndex());
    }
}
