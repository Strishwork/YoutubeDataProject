package com.bignerdranch.android.youtubedataapp.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.youtubedataapp.R;

public class VideoActivity extends AppCompatActivity implements VideoFragment.Callbacks{

    private static final String EXTRA_VIDEO_ID = "video_id";

    VideoFragment videoFragment;

    public static Intent newIntent(Context packageContext, long videoId) {
        Intent intent = new Intent(packageContext, VideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long id = getIntent().getLongExtra(EXTRA_VIDEO_ID, -1);

        videoFragment = VideoFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, videoFragment).commit();
    }

    @Override
    public void onVideoItemUpdated(VideoItem videoItem) {

    }
}
