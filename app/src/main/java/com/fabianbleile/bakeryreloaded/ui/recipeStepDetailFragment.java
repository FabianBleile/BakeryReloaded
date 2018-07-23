package com.fabianbleile.bakeryreloaded.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fabianbleile.bakeryreloaded.R;
import com.fabianbleile.bakeryreloaded.Utils.RecipeObject;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A fragment representing a single recipeStep detail screen.
 * This fragment is either contained in a {@link RecipeActivity
 * in two-pane mode (on tablets) or a {@link recipeStepDetailActivity}
 * on handsets.
 */
public class recipeStepDetailFragment extends Fragment  implements ExoPlayer.EventListener{

    private static final String TAG = "recipeStepDetailFrag";
    Context mContext;

    public static final String ARG_STEP_OBJECT = "step";
    public static final String ARG_RECIPE_NAME = "name";
    public static final String ARG_SHORT_DESCRIPT = "name";
    public static final String ARG_LONG_DESCRIPT = "name";

    private static String mShortDescription;
    private static String mDescription;

    TextView tvDescription;
    private SimpleExoPlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private RecipeObject mRecipeObject;
    private RecipeObject.StepObject mStepObject;

    private boolean mFullScreen;
    private Bundle mSaveInstanceState;

    public static int getmStepId() {
        return mStepId;
    }

    public static int mStepId;

    public recipeStepDetailFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ARG_SHORT_DESCRIPT, mShortDescription);
        outState.putString(ARG_LONG_DESCRIPT, mDescription);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "onCreate");

        if (getArguments() != null && getArguments().containsKey(ARG_STEP_OBJECT)) {
            mSaveInstanceState = getArguments();
            mStepObject = mSaveInstanceState.getParcelable(ARG_STEP_OBJECT);
            mStepId = mStepObject.getId();
            mShortDescription = mStepObject.getShortDescription();
            mDescription = mStepObject.getDescription();
        }

        if(savedInstanceState != null){
            Toast.makeText(getContext(), "in saved instance", Toast.LENGTH_SHORT).show();
            mSaveInstanceState = savedInstanceState;
            mDescription = mSaveInstanceState.getString(ARG_LONG_DESCRIPT);
            mShortDescription = mSaveInstanceState.getString(ARG_SHORT_DESCRIPT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");

        mContext = container.getContext();
        View rootView = inflater.inflate(R.layout.recipestep_detail, container, false);

        if (rootView.findViewById(R.id.tv_description) != null) {
            // The coordinator layout will be present only in the
            // small-screen layouts in portrait.
            // If this view is present, then the
            // activity should NOT be in full-screen mode.
            mFullScreen = false;
        } else {
            mFullScreen = true;
        }

        // Initialize the player view.
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.simpleExoPlayerView);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.video_error));

        if(!mFullScreen){
            tvDescription = rootView.findViewById(R.id.tv_description);
            tvDescription.setText(mDescription);
            ConstraintSet set = new ConstraintSet();
            set.constrainHeight(R.id.simpleExoPlayerView, ConstraintSet.MATCH_CONSTRAINT);
            set.constrainWidth(R.id.simpleExoPlayerView, ConstraintSet.MATCH_CONSTRAINT);
        } else {
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(getScreenWidth(), getScreenHeight());
            mPlayerView.setLayoutParams(layoutParams);
        }

        Uri uri = null;
        // Check if step contains a video Url
        if(mStepObject != null && isNetworkAvailable()){
            if(!TextUtils.equals(mStepObject.getVideoUrl(), "")){
                uri = Uri.parse(mStepObject.getVideoUrl()).buildUpon().build();
                if(uri != null){
                    // Initialize the Media Session.
                    initializeMediaSession();
                    // Initialize Player.
                    initializePlayer(uri);
                } else {
                    // show an error image
                    Toast.makeText(mContext, "VideoUrl is not accessable.", Toast.LENGTH_SHORT).show();
                    mPlayerView.setVisibility(View.GONE);
                }
            } else if (!TextUtils.equals(mStepObject.getThumbnailUrl(), "")){
                uri = Uri.parse(mStepObject.getThumbnailUrl()).buildUpon().build();
                if(uri != null){
                    // Initialize the Media Session.
                    initializeMediaSession();
                    // Initialize Player.
                    initializePlayer(uri);
                } else {
                    // show an error image
                    Toast.makeText(mContext, "VideoUrl is not accessable.", Toast.LENGTH_SHORT).show();
                    mPlayerView.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(mContext, "VideoUrl is empty.", Toast.LENGTH_SHORT).show();
                mPlayerView.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(mContext, "Check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = this.getActivity();
        assert activity != null;
        if(!mFullScreen){
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mShortDescription);
            }
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(mContext, TAG);
        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);
        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                //PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());
        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(mContext, "Backery Reloaded");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    mContext, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        releasePlayer();
        if(mMediaSession != null){
            mMediaSession.setActive(false);
        }
    }


    // ExoPlayer Event Listeners
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == com.google.android.exoplayer2.ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == com.google.android.exoplayer2.ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    //----------------------------------------------------------------------------------------------
    // helper methods
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
