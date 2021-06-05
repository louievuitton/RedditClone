package com.example.redditclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.redditclone.models.Listing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView header;
    private RecyclerView listingRecView;
    private ListingRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listingRecView = findViewById(R.id.listingRecView);
        header = findViewById(R.id.header);

        fetchListingsData();
    }

    private void fetchListingsData() {
        String url = "https://www.reddit.com/r/nba/hot.json";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Listing> listings = new ArrayList<>();
                    JSONArray res = response.getJSONObject("data").getJSONArray("children");
                    for (int i = 0; i < res.length(); i++) {
                        JSONObject obj = (JSONObject) res.get(i);
                        Listing listing = new Listing();
                        listing.setId(obj.getJSONObject("data").getString("name"));
                        listing.setTitle(obj.getJSONObject("data").getString("title"));
                        if (obj.getJSONObject("data").getString("thumbnail").equals("self")) {
                            listing.setThumbnail("data:image/png;base64,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");
                        }
                        else {
                            listing.setThumbnail(obj.getJSONObject("data").getString("thumbnail"));
                        }
                        listings.add(listing);
                    }

                    adapter = new ListingRecViewAdapter(MainActivity.this);
                    listingRecView.setAdapter(adapter);
                    listingRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    adapter.setListings(listings);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}