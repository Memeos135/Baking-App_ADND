package com.example.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity implements ExoPlayer.EventListener, DescriptionFragment.OnStepClickListener{

    Context context;
    ArrayList<IngredientSetter> ingredients;
    ArrayList<StepSetter> steps;
    int position = 0;
    boolean tabletFlag = false;
    String path = "";
    boolean flag = false;
    SimpleExoPlayer player;
    long progress = 0;
    int id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        context = this;

        if(constraintLayout.getTag() != null){

            tabletFlag = true;

            if(getIntent().getStringExtra("recipe_number") == null){

                readRecipeIngredients();
                position = Integer.parseInt(getIntent().getStringExtra("position"));

            }else{

                if(savedInstanceState == null) {
                    readRecipeIngredients();
                    position = Integer.parseInt(getIntent().getStringExtra("recipe_number"));
                }
            }

        }else {

            if (getIntent().getStringExtra("test") == null) {

                if(savedInstanceState == null) {
                    readRecipeIngredients();
                    position = Integer.parseInt(getIntent().getStringExtra("position"));
                }
            }
        }
    }

    public void readRecipeIngredients(){
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        // Prepare the Request based on the json being read. In this case, we are reading a json that is structured to be an array of jsonObjects
        // Hence, we are using a JsonArrayRequest
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        try {

                            JSONObject jsonObject = response.getJSONObject(position);
                            JSONArray jsonArray = jsonObject.getJSONArray("ingredients");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String measure = jsonObject1.getString("measure");
                                String quantity = jsonObject1.getString("quantity");
                                String ingredient = jsonObject1.getString("ingredient");

                                ingredients.add(new IngredientSetter(ingredient, quantity, measure));

                            }

                            JSONArray jsonArray1 = jsonObject.getJSONArray("steps");

                            for(int j = 0; j < jsonArray1.length(); j++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                String id = "";

                                // There is an issue with the ids of Cheesecake Recipe in the JSON provided. After the 6th id, it jumps an additional
                                // step to make the id = 8. So, I am decrementing it by one.
                                if(Integer.parseInt(jsonObject1.getString("id")) > 6 && position == 2){
                                    id = String.valueOf(Integer.parseInt(jsonObject1.getString("id")) - 1);
                                }else {
                                    id = jsonObject1.getString("id");
                                }
                                String short_description = jsonObject1.getString("shortDescription");
                                String description = jsonObject1.getString("description");
                                String videoURL = jsonObject1.getString("videoURL");

                                steps.add(new StepSetter(short_description, description, videoURL, id));
                            }

                            // call this at the end of reading the JSONOBJECT
                            fillIngredients(ingredients, steps);

                            if(tabletFlag) {
                                TextView textView = findViewById(R.id.desc);
                                textView.setText(steps.get(0).getDescription());
                                checkVideoUrl(0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);

    }

    public void fillIngredients(ArrayList<IngredientSetter> ingredients, ArrayList<StepSetter> steps){

        IngredientsRecyclerAdapter recyclerAdapter = new IngredientsRecyclerAdapter(context, ingredients);
        RecyclerView recyclerView = findViewById(R.id.ingredientsRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(recyclerAdapter);

        // DO FRAGMENTS
        setupFragments(steps);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("myArrayList", ingredients);
        outState.putParcelableArrayList("MyStepsArrayList", steps);
        outState.putInt("position", position);
        outState.putBoolean("tabletFlag", tabletFlag);
        outState.putBoolean("flag", flag);
        outState.putInt("id", id);

        if(tabletFlag) {
            progress = player.getCurrentPosition();
            outState.putLong("progress", progress);
        }
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ingredients = savedInstanceState.getParcelableArrayList("myArrayList");
        steps = savedInstanceState.getParcelableArrayList("MyStepsArrayList");
        position = savedInstanceState.getInt("position");
        tabletFlag = savedInstanceState.getBoolean("tabletFlag");
        progress = savedInstanceState.getLong("progress");
        flag = savedInstanceState.getBoolean("flag");
        id = savedInstanceState.getInt("id");

        if(tabletFlag) {

            TextView textView = findViewById(R.id.desc);
            textView.setText(steps.get(id).getDescription());

        }
            reSetupIngredients(ingredients, steps);
    }

    public void reSetupIngredients(ArrayList<IngredientSetter> ingredients, ArrayList<StepSetter> steps){
        IngredientsRecyclerAdapter recyclerAdapter = new IngredientsRecyclerAdapter(context, ingredients);
        RecyclerView recyclerView = findViewById(R.id.ingredientsRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(recyclerAdapter);
    }

    public void setupFragments(ArrayList<StepSetter> steps){
        for(int i = 0; i < steps.size(); i++) {
            DescriptionFragment descriptionFragment = new DescriptionFragment(steps.get(i), position, tabletFlag);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.container, descriptionFragment)
                    .commit();
        }

    }

    public void checkVideoUrl(int id){
        if(!steps.get(id).getVideoURL().equals("")){
            // DO VIDEO
            playerSetup(id);
        }else{
            Toast.makeText(this, "Could not load video.", Toast.LENGTH_SHORT).show();
            flag = true;
        }
    }

    public void playerSetup(int id){
        path = steps.get(id).getVideoURL();
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
        if (!flag && tabletFlag) {
            player.release();   //it is important to release a player
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStepClicked(int position) {
        progress = 0;
        id = position;

        player.release();
        playerSetup(id);

        TextView textView = findViewById(R.id.desc);
        textView.setText(steps.get(id).getDescription());
    }
}
