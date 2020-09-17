package com.bignerdranch.android.youtubedataapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.youtubedataapp.video.VideoItem;

import java.util.ArrayList;
import java.util.List;

import database.VideoBaseHelper;
import database.VideoCursorWrapper;
import database.VideoDbSchema.VideoEntry;

public class DataLab {

    private static DataLab sDataLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DataLab get(Context context) {
        if (sDataLab == null) {
            sDataLab = new DataLab(context);
        }
        return sDataLab;
    }

    private DataLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new VideoBaseHelper(mContext).getWritableDatabase();
    }

    public List<VideoItem> getVideos() {
        List<VideoItem> videos = new ArrayList<>();
        VideoCursorWrapper cursor = queryVideos(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                videos.add(cursor.getVideo());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return videos;
    }

    public VideoItem getVideoById(long id){
        VideoCursorWrapper cursor = queryVideos(VideoEntry._ID + " = ?", new String[]{String.valueOf(id)});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getVideo();
        } finally {
            cursor.close();
        }
    }

    public void addVideo(VideoItem v) {
        ContentValues values = getContentValues(v);
        mDatabase.insert(VideoEntry.TABLE_NAME, null, values);
    }

    public void updateVideo(VideoItem v){
        long id = v.getId();
        ContentValues values = getContentValues(v);
        mDatabase.update(VideoEntry.TABLE_NAME, values, VideoEntry._ID + " = ?", new String[] {String.valueOf(id)});
    }

    public void removeVideo(long id){
        mDatabase.delete(VideoEntry.TABLE_NAME, VideoEntry._ID + "=" + id, null);
    }

    private static ContentValues getContentValues(VideoItem video) {
        ContentValues values = new ContentValues();
        values.put(VideoEntry.COLUMN_LINK, video.getLink());
        values.put(VideoEntry.COLUMN_TITLE, video.getTitle());
        values.put(VideoEntry.COLUMN_LIKES, video.getLikesCount());
        values.put(VideoEntry.COLUMN_GOAL, video.getGoal());
        values.put(VideoEntry.COLUMN_THUMBNAIL, video.getThumbnailUrl());
        return values;
    }

    private VideoCursorWrapper queryVideos(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                VideoEntry.TABLE_NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new VideoCursorWrapper(cursor);
    }

}
