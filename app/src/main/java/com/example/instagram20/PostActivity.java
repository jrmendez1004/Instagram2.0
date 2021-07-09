package com.example.instagram20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    private Button btnPost;
    private Button btnTakePic;
    private EditText etDescription;
    private ImageView ivPreview;
    public static final int REQUEST_CODE =  25;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    public static final String TAG = "PostActivity";
    public static final int RESIZED_WIDTH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnPost = findViewById(R.id.btnPost);
        btnTakePic = findViewById(R.id.btnTakePic);
        etDescription = findViewById(R.id.etDescription);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);


        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if(description.isEmpty()) {
                    Toast.makeText(PostActivity.this, "Description can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivPreview.getDrawable() == null){
                    Toast.makeText(PostActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, RESIZED_WIDTH);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            File resizedFile = getPhotoFileUri(photoFileName + "_resized");
            try {
                resizedFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(resizedFile);
                fos.write(bytes.toByteArray());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ivPreview.setImageBitmap(resizedBitmap);
        }
        else
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdir()) {
        }

        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null)
                    Toast.makeText(PostActivity.this, "error while saving", Toast.LENGTH_SHORT).show();
                Toast.makeText(PostActivity.this, "save successful", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                ivPreview.setImageResource(0);
                ivPreview.setVisibility(View.GONE);
            }
        });
    }
}