package com.example.instagram20;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.net.Inet4Address;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvTime;

        private Button ivLike;
        private ImageView ivComment;
        private Button ivSave;
        private ImageView ivSend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);

            ivLike = itemView.findViewById(R.id.ivNotLiked);
            ivComment = itemView.findViewById(R.id.ivComment);
            ivSave = itemView.findViewById(R.id.ivSave);
            ivSend = itemView.findViewById(R.id.ivSend);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvTime.setText(Post.calculateTimeAgo(post.getCreatedAt()));

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            itemView.findViewById(R.id.ivImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(context, PostDetailActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });

            //Change heart to be liked
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();
                    if(ivLike.getBackground().equals(res.getDrawable(R.drawable.ufi_heart_active))) {
                        Drawable drawable = res.getDrawable(R.drawable.ufi_heart);
                        ivLike.setBackground(drawable);
                    }
                    else {
                        Drawable drawable = res.getDrawable(R.drawable.ufi_heart_active);
                        ivLike.setBackground(drawable);
                    }
                }
            });

            //change save Image
            ivSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();
                    if(ivSave.getBackground().equals(res.getDrawable(R.drawable.ufi_save_active))) {
                        Drawable drawable = res.getDrawable(R.drawable.ufi_save_icon);
                        ivSave.setBackground(drawable);
                    }
                    else {
                        Drawable drawable = res.getDrawable(R.drawable.ufi_save_active);
                        ivSave.setBackground(drawable);
                    }
                }
            });

        }
    }
}
