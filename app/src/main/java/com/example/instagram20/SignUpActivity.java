package com.example.instagram20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPw;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPw = findViewById(R.id.etConfirmPw);
        btnSignUp = findViewById(R.id.btnCreate);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPassword.getText().toString().compareTo(etConfirmPw.getText().toString()) == 0){
                    ParseUser user = new ParseUser();
                    user.setUsername(etUsername.getText().toString());
                    user.setPassword(etPassword.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                Toast.makeText(SignUpActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else {
                    Toast.makeText(SignUpActivity.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}