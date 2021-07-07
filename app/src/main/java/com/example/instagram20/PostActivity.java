package com.example.instagram20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PostActivity extends AppCompatActivity {
    private Button btnPost;
    private Button btnTakePic;
    private EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnPost = findViewById(R.id.btnPost);
        btnTakePic = findViewById(R.id.btnTakePic);
        etDescription = findViewById(R.id.etDescription);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if(description.isEmpty()) {
                    Toast.makeText(PostActivity.this, "Description can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser);
            }
        });
    }

    private void savePost(String description, ParseUser currentUser) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        //post.setImage();
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null)
                    Toast.makeText(PostActivity.this, "error while saving", Toast.LENGTH_SHORT).show();
                Toast.makeText(PostActivity.this, "save successful", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
            }
        });
    }
}