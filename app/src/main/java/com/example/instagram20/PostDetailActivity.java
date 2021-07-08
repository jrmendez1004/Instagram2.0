package com.example.instagram20;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvDetail;
    ImageView ivPost;
    TextView tvTime;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvDetail = (TextView) findViewById(R.id.tvDescription);
        ivPost = (ImageView) findViewById(R.id.ivImage);
        tvTime = (TextView) findViewById(R.id.tvTime);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvUsername.setText(post.getUser().getUsername());
        tvDetail.setText(post.getDescription());
        tvTime.setText(Post.calculateTimeAgo(post.getCreatedAt()));
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPost);
        }
    }

}