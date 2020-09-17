package com.bignerdranch.android.youtubedataapp;

import android.content.Context;

import com.bignerdranch.android.youtubedataapp.model.DataLab;
import com.bignerdranch.android.youtubedataapp.video.VideoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YouTubeAsync {

    public static final String API_KEY = "AIzaSyD3YuiTOc8Wj6nLsq6Lq-C_MZJe_JXmkP0";

    private String getUrlString(String urlSpec) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlSpec).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return "";
        }
    }

    public VideoItem getVideoByLink(String s) throws IOException {
        String id = getIdFromUrl(s);
        String queryStats = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=" + id + "&key=" + API_KEY;
        String queryInfo = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + id + "&key=" + API_KEY;
        VideoItem videoItem = new VideoItem(s);
        try {
            JSONObject jsonBody = new JSONObject(getUrlString(queryStats));
            parseItemLikes(videoItem, jsonBody);
            jsonBody = new JSONObject(getUrlString(queryInfo));
            parseItemTitle(videoItem, jsonBody);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return videoItem;
    }

    private String getIdFromUrl(String s) {
        String link = s;
        link = link.substring(link.lastIndexOf("v=") + 2);
//      link = link.substring(0, link.indexOf("&t"));
        return link;
    }

    private void parseItemLikes(VideoItem item, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray items = jsonBody.getJSONArray("items");
        JSONObject statistics = items.getJSONObject(0).getJSONObject("statistics");
        item.setLikesCount(statistics.getInt("likeCount"));
    }

    private void parseItemTitle(VideoItem item, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray items = jsonBody.getJSONArray("items");
        JSONObject videoJsonObject = items.getJSONObject(0).getJSONObject("snippet");
        item.setTitle(videoJsonObject.getString("title"));
        videoJsonObject = videoJsonObject.getJSONObject("thumbnails").getJSONObject("standard");
        item.setThumbnailUrl(videoJsonObject.getString("url"));
    }

    public void updateVideos(Context context) {
        DataLab dataLab = DataLab.get(context);
        List<VideoItem> items = dataLab.getVideos();
        for (VideoItem item : items) {
            try {
                VideoItem newItem = getVideoByLink(item.getLink());
                item.updateItem(newItem);
                dataLab.updateVideo(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
