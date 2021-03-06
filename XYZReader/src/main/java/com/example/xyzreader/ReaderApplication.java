package com.example.xyzreader;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class ReaderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));

        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);

        Picasso.setSingletonInstance(built);
    }
}
