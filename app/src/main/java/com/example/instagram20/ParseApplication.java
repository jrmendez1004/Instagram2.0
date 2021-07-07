package com.example.instagram20;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("59WliAAxwDAKDGWYcuwINFOQErTYPyMckbZvlJAY")
                .clientKey("cSFevg1rSpmmC36PxVyCbtjobAcu1BpY4DJ4vrZ0")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
