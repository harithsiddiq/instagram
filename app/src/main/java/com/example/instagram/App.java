package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9fut4JCGvBsnOViXMF6CWkgCW477ueCxxRnfUHHu")
                // if desired
                .clientKey("J2Z9lSxFqTJfF2oF88TNjXjHyndcW4sTNiHRbivp")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
