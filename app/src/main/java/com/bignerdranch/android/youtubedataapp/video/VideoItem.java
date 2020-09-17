package com.bignerdranch.android.youtubedataapp.video;

public class VideoItem {

    private String mTitle;
    private long mId;
    private String mLink;
    private String mThumbnailUrl;
    private int mLikesCount;
    private int mGoal;

    public VideoItem(String url){
        mLink = url;
        setGoal(0);
    }

    public String getLink() {
        return mLink;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public void setLikesCount(int likesCount) {
        mLikesCount = likesCount;
    }

    public int getGoal() {
        return mGoal;
    }

    public void setGoal(int goal) {
        mGoal = goal;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public void updateItem(VideoItem newItem){
        mLikesCount = newItem.getLikesCount();
        mTitle = newItem.getTitle();
        mThumbnailUrl = newItem.getThumbnailUrl();
    }
}
