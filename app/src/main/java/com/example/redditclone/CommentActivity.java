package com.example.redditclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Listing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private TextView header;
    private RecyclerView commentRecView;
    private String listingId;
    private CommentRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        if (intent != null) {
            listingId = intent.getStringExtra("listingId");
        }

        header = findViewById(R.id.header);
        commentRecView = findViewById(R.id.commentRecView);

        fetchCommentsData();
    }

    private void fetchCommentsData() {
        String url = "https://www.reddit.com/r/nba/comments.json?article=" + listingId + "&sort=top";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Comment> comments = new ArrayList<>();
                    JSONArray res = response.getJSONObject("data").getJSONArray("children");
                    for (int i = 0; i < res.length(); i++) {
                        JSONObject obj = (JSONObject) res.get(i);
                        Comment comment = new Comment();
                        comment.setId("t1_" + obj.getJSONObject("data").getString("id"));
                        comment.setAuthor(obj.getJSONObject("data").getString("author"));
                        comment.setBody(obj.getJSONObject("data").getString("body"));
                        comments.add(comment);
                    }

                    adapter = new CommentRecViewAdapter(CommentActivity.this);
                    commentRecView.setAdapter(adapter);
                    commentRecView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                    adapter.setComments(comments);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}