package com.example.instagram20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static String TAG = "MainActivity";
    static int LOAD_LIMIT = 3;
    RecyclerView rvPosts;

    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbarTop);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(resize(getResources().getDrawable(R.drawable.nav_logo_whiteout)));

        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });

        queryPosts();
    }

    private void loadNextDataFromApi(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(LOAD_LIMIT);
        query.setSkip(LOAD_LIMIT * page);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null)
                    return;
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(LOAD_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null)
                    return;
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onLogout(MenuItem menuItem) {
        finish();
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insta_top, menu);
        return true;
    }

    public void postPicture(MenuItem menuItem) {
        Intent i = new Intent(this, PostActivity.class);
        startActivity(i);
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 150, 50, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
}