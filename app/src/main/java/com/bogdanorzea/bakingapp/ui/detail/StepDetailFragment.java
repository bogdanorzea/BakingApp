package com.bogdanorzea.bakingapp.ui.detail;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.bakingapp.BakingApp;
import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.data.database.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public class StepDetailFragment extends Fragment {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_ID = "step_id";
    private static final String PLAYER_PLAY_WHEN_READY = "player_playback_state";
    private static final String PLAYER_CURRENT_POSITION = "player_current_position";
    private static final String PLAYER_CURRENT_WINDOW_INDEX = "player_current_window_index";
    private static final String STEP_MEDIA_SESSION = "STEP_MEDIA_SESSION";
    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private long playbackPosition = 0;
    private boolean playWhenReady = true;
    private int currentWindowIndex;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private Player.EventListener playerEventListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
            } else if (playbackState == Player.STATE_READY) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
            }

            mediaSession.setPlaybackState(stateBuilder.build());
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };
    private int recipeId;
    private int stepId;
    private RecipeViewModel viewModel;
    private Step step;

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_item_details, container, false);
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

            if (recipeId != -1 && stepId != -1) {
                Timber.d("Loaded step #%s from recipe #%d", stepId, recipeId);
                RecipeViewModelFactory factory =
                        InjectorUtils.provideDetailViewModelFactory(getContext(), recipeId);
                viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
                viewModel.getRecipe().observe(this, recipe -> {
                    if (recipe != null) {
                        step = recipe.steps.get(stepId);

                        if (!TextUtils.isEmpty(step.getVideoURL())) {
                            initializePlayer();
                        } else {
                            Toast.makeText(getContext(), "Could not play the clip", Toast.LENGTH_SHORT).show();
                        }

                        ((TextView) view.findViewById(R.id.step_description_text))
                                .setText(step.getDescription());

                        ((TextView) view.findViewById(R.id.step_title_text))
                                .setText(step.getShortDescription());

                        Button nextButton = view.findViewById(R.id.next_button);
                        if (stepId < recipe.steps.size() - 1) {
                            nextButton.setOnClickListener(v -> {
                                Activity activity = getActivity();
                                if (activity != null) {
                                    try {
                                        ((OnStepNavigationCallback) activity).replaceStepFragment(stepId + 1);
                                    } catch (ClassCastException e) {
                                        Timber.e("Class does not implement OnStepNavigationCallback interface");
                                    }
                                }
                            });
                        } else {
                            nextButton.setEnabled(false);
                        }

                        Button previousButton = view.findViewById(R.id.previous_button);
                        if (0 < stepId) {
                            previousButton.setOnClickListener(v -> {
                                Activity activity = getActivity();
                                if (activity != null) {
                                    try {
                                        ((OnStepNavigationCallback) activity).replaceStepFragment(stepId - 1);
                                    } catch (ClassCastException e) {
                                        Timber.e("Class does not implement OnStepNavigationCallback interface");
                                    }
                                }
                            });
                        } else {
                            previousButton.setEnabled(false);
                        }
                    }
                });
            } else {
                Timber.e("Invalid fragment arguments");
            }
        }

        createMediaSession();

        return view;
    }

    private void createMediaSession() {
        Context context = getContext();
        if (context == null) return;

        mediaSession = new MediaSessionCompat(context, STEP_MEDIA_SESSION);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new StepSessionCallback());
    }

    private void initializePlayer() {
        if (step == null) return;

        String videoUrl = step.getVideoURL();
        if (TextUtils.isEmpty(videoUrl)) {
            Timber.e("Step is missing a video");
            return;
        }

        Timber.d("Going to run %s", videoUrl);
        Uri mp4VideoUri = Uri.parse(videoUrl);
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

            exoPlayer.addListener(playerEventListener);
            exoPlayer.prepare(videoSource);
        }

        exoPlayer.seekTo(currentWindowIndex, playbackPosition);
        exoPlayer.setPlayWhenReady(playWhenReady);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
        if (mediaSession != null) mediaSession.setActive(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
        if (mediaSession != null) mediaSession.setActive(false);
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
        if (exoPlayer != null) {
            outState.putBoolean(PLAYER_PLAY_WHEN_READY, exoPlayer.getPlayWhenReady());
            outState.putLong(PLAYER_CURRENT_POSITION, exoPlayer.getCurrentPosition());
            outState.putInt(PLAYER_CURRENT_WINDOW_INDEX, exoPlayer.getCurrentWindowIndex());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaSession != null) mediaSession.release();
        RefWatcher refWatcher = BakingApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    interface OnStepNavigationCallback {
        void replaceStepFragment(int stepId);
    }

    private class StepSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }
}
