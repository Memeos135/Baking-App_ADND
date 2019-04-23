package com.example.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StepDetails extends AppCompatActivity implements ExoPlayer.EventListener {

    StepSetter stepSetter;
    TextView desc;
    boolean flag = false;
    SimpleExoPlayer player;
    String path;
    long progress = 0;
    int parent_position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_details);

        if(savedInstanceState == null && getIntent().getStringExtra("test") == null) {

            stepSetter = getIntent().getParcelableExtra("stepSetter");
            parent_position = getIntent().getIntExtra("parent_position", 0);

            desc = findViewById(R.id.desc);
            desc.setText(stepSetter.getDescription());

            checkVideoUrl();

        }

    }

    public void processButtons(View v){
        if(v.getTag().equals("backButton")){
            // GO PREVIOUS
            fetchPreviousStep();
            progress = 0;
        }else{
            // GO NEXT
            fetchNextStep();
            progress = 0;
        }
    }

    public void checkVideoUrl(){
        if(!stepSetter.getVideoURL().equals("")){
            // DO VIDEO
            if(player != null){
                player.release();
            }
            playerSetup();
        }else{
            Toast.makeText(this, "Could not load video.", Toast.LENGTH_SHORT).show();
            flag = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("desc", desc.getText().toString());
        outState.putParcelable("stepSetter", stepSetter);
        outState.putInt("parent_position", parent_position);

        progress = player.getCurrentPosition();
        outState.putLong("progress", progress);

        if(flag) {
            outState.putBoolean("flag", true);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        desc = findViewById(R.id.desc);
        desc.setText(savedInstanceState.getString("desc"));
        stepSetter = savedInstanceState.getParcelable("stepSetter");
        progress = savedInstanceState.getLong("progress");
        parent_position = savedInstanceState.getInt("parent_position");

        flag = savedInstanceState.getBoolean("flag");
        if(flag){
            Toast.makeText(this, "Could not load video.", Toast.LENGTH_SHORT).show();
        }else{
            playerSetup();
        }

    }

    public void fetchNextStep(){
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

            // Prepare the Request based on the json being read. In this case, we are reading a json that is structured to be an array of jsonObjects
            // Hence, we are using a JsonArrayRequest
            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // display response
                            try {

                                    JSONObject jsonObject = response.getJSONObject(parent_position);
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("steps");

                                if(Integer.parseInt(stepSetter.getId()) < jsonArray1.length()-1) {

                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(Integer.parseInt(stepSetter.getId()) + 1);

                                    String id = jsonObject1.getString("id");
                                    String short_description = jsonObject1.getString("shortDescription");
                                    String description = jsonObject1.getString("description");
                                    String videoURL = jsonObject1.getString("videoURL");

                                    stepSetter.setDescription(description);
                                    stepSetter.setId(id);
                                    stepSetter.setShort_description(short_description);
                                    stepSetter.setVideoURL(videoURL);

                                    updateUI(stepSetter);
                                    player.release();
                                    playerSetup();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }
            );

            // add it to the RequestQueue
            queue.add(getRequest);
        }

    public void fetchPreviousStep(){

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

            // Prepare the Request based on the json being read. In this case, we are reading a json that is structured to be an array of jsonObjects
            // Hence, we are using a JsonArrayRequest
            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // display response
                            try {

                                if(Integer.parseInt(stepSetter.getId()) > 0) {

                                    JSONObject jsonObject = response.getJSONObject(parent_position);
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("steps");

                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(Integer.parseInt(stepSetter.getId()) - 1);

                                    String id = jsonObject1.getString("id");
                                    String short_description = jsonObject1.getString("shortDescription");
                                    String description = jsonObject1.getString("description");
                                    String videoURL = jsonObject1.getString("videoURL");

                                    stepSetter.setDescription(description);
                                    stepSetter.setId(id);
                                    stepSetter.setShort_description(short_description);
                                    stepSetter.setVideoURL(videoURL);

                                    updateUI(stepSetter);
                                    player.release();
                                    playerSetup();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }
            );

            // add it to the RequestQueue
            queue.add(getRequest);
        }

        public void updateUI(StepSetter stepSetter){
        desc.setText(stepSetter.getDescription());
        }

    public void playerSetup(){
        path = stepSetter.getVideoURL();

        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();
        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        SimpleExoPlayerView playerView = (SimpleExoPlayerView) findViewById(R.id.exoPlayer);
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(path),
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        player.addListener(this);
        player.prepare(videoSource);
        playerView.requestFocus();
        player.setPlayWhenReady(true); // to play video when ready. Use false to pause a video

        if(progress != 0){
            player.seekTo(progress);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
        }
    }

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
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                //You can use progress dialog to show user that video is preparing or buffering so please wait
                break;
            case ExoPlayer.STATE_IDLE:
                //idle state
                break;
            case ExoPlayer.STATE_READY:
                // dismiss your dialog here because our video is ready to play now
                break;
            case ExoPlayer.STATE_ENDED:
                // do your processing after ending of video
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // show user that something went wrong. I am showing dialog but you can use your way
        Toast.makeText(this, "Could not load video.", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onStop() {
        super.onStop();
        if (!flag && getIntent().getStringExtra("test") == null) {
            player.release();   //it is important to release a player
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
