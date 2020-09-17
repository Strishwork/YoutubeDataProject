package com.bignerdranch.android.youtubedataapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bignerdranch.android.youtubedataapp.video.VideoActivity;
import com.bignerdranch.android.youtubedataapp.video.VideoFragment;
import com.bignerdranch.android.youtubedataapp.video.VideoItem;

public class MainActivity extends AppCompatActivity implements ListFragment.Callbacks, VideoFragment.Callbacks {

    ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);

        listFragment = ListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
    }

    @Override
    public void onVideoSelected(VideoItem videoItem) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent i = VideoActivity.newIntent(this, videoItem.getId());
            startActivity(i);
        } else {
            Fragment newDetail = VideoFragment.newInstance(videoItem.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();
        }
    }

    @Override
    public void onVideoItemUpdated(VideoItem videoItem) {
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
