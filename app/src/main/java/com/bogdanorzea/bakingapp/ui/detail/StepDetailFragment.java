package com.bogdanorzea.bakingapp.ui.detail;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.data.database.Step;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StepDetailFragment extends Fragment {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_ID = "step_id";
    private static final String PLAYER_PLAY_WHEN_READY = "player_playback_state";
    private static final String PLAYER_CURRENT_POSITION = "player_current_position";
    private static final String PLAYER_CURRENT_WINDOW_INDEX = "player_current_window_index";

    @BindView(R.id.player)
    PlayerView playerView;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.previous_button)
    Button previousButton;
    @BindView(R.id.step_description_text)
    TextView descriptionText;
    @BindView(R.id.step_title_text)
    TextView titleText;

    private ExoPlayer exoPlayer;
    private long playbackPosition = 0;
    private boolean playWhenReady = true;
    private int currentWindowIndex;

    private Step step;

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_item_details, container, false);
        ButterKnife.bind(this, view);

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
                RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
                viewModel.getRecipe().observe(this, recipe -> {
                    if (recipe != null) {
                        step = recipe.steps.get(stepId);

                        if (!TextUtils.isEmpty(step.getVideoURL())) {
                            initializePlayer();
                        } else {
                            Toast.makeText(getContext(), "Could not play the clip", Toast.LENGTH_SHORT).show();
                        }

                        descriptionText.setText(step.getDescription());
                        titleText.setText(step.getShortDescription());

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

        return view;
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
        if (exoPlayer != null) {
            outState.putBoolean(PLAYER_PLAY_WHEN_READY, exoPlayer.getPlayWhenReady());
            outState.putLong(PLAYER_CURRENT_POSITION, exoPlayer.getCurrentPosition());
            outState.putInt(PLAYER_CURRENT_WINDOW_INDEX, exoPlayer.getCurrentWindowIndex());
        }
    }

    interface OnStepNavigationCallback {
        void replaceStepFragment(int stepId);
    }

}
