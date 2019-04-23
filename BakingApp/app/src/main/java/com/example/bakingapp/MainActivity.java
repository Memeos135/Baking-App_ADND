package com.example.bakingapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ESPRESSO TESTING
        if(getIntent().getStringExtra("test") != null){
            final TextView textView = findViewById(R.id.textView);
            textView.setText(getIntent().getStringExtra("test"));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView.setText("Espresso");
                }
            });
        }

        context = this;

        if(savedInstanceState == null) {

            readJSON();

        }

    }

    public void fillCards(ArrayList<String> data){

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(context, data);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        if(recyclerView.getTag() != null){
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        recyclerView.setAdapter(recyclerAdapter);
    }

    public void readJSON(){

        data = new ArrayList<>();

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
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                data.add(jsonObject.getString("name"));
                            }

                            // call this at the end of reading the JSONOBJECT
                            fillCards(data);

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

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("myArrayList", data);
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        data = savedInstanceState.getStringArrayList("myArrayList");

        reSetupRecycler(data);
    }

    public void reSetupRecycler(ArrayList<String> data){
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(context, data);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        if(recyclerView.getTag() != null){
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        recyclerView.setAdapter(recyclerAdapter);
    }
}
