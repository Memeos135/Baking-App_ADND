package com.example.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WidgetService extends IntentService {
    Context context;
    static String responseS = "";

    public WidgetService() {
        super("RemoteIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = this;
        if ("np".equals(intent.getAction())) {

            readING(0, context);

        }else if("br".equals(intent.getAction())){

            readING(1, context);

        }else if("yc".equals(intent.getAction())){

            readING(2, context);

        }else{

            readING(3, context);

        }
    }

    public void readING(final int parent_position, final Context context){

        RequestQueue queue = Volley.newRequestQueue(context);

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

                            JSONObject jsonObject = response.getJSONObject(parent_position);
                            JSONArray jsonArray = jsonObject.getJSONArray("ingredients");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String ingredient = jsonObject1.getString("ingredient");

                                responseS += "# " + ingredient + "\n";

                            }

                            setupIngredientText(context);

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

    public void setupIngredientText(Context context){

        Intent brIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        brIntent.putExtra("response", responseS);
        sendBroadcast(brIntent);
        responseS = "";
    }
}
