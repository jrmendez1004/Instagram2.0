package com.example.instagram20;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("59WliAAxwDAKDGWYcuwINFOQErTYPyMckbZvlJAY")
                .clientKey("cSFevg1rSpmmC36PxVyCbtjobAcu1BpY4DJ4vrZ0")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
